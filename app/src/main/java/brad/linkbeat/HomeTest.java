package brad.linkbeat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HomeTest extends Activity {

    private FileInputStream fis;
    private ByteArrayOutputStream bos;

    private String path = "";

    private String fileName = Environment.getExternalStorageDirectory().toString()
            + "/acrcloud/model/linkBeatRecording.3gp";

    private String _POST = "POST";
    private String _GET = "GET";
    private Map _PLACEHOLDERMAP = new HashMap<>();
    private String BOUNDARYSTR = "*****2015.03.30.acrcloud.rec.copyright." + System.currentTimeMillis() + "*****";
    private String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";
    private String ENDBOUNDARY = "--" + BOUNDARYSTR + "--\r\n\r\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        path = Environment.getExternalStorageDirectory().toString()
                + "/acrcloud/model";

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = path + "/linkBeatRecording";
        Button startBtn = (Button) findViewById(R.id.clicky);

        startBtn.setText(getResources().getString(R.string.app_name));
        //Button cancelBtn = (Button) findViewById(R.id.button);
        //cancelBtn.setText(getResources().getString(R.string.app_name));
        findViewById(R.id.bandsInTownButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.e("bands", "bands");
                getBandsInTown();


            }
        });
        findViewById(R.id.clicky).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                start();
            }
        });

    }
    private String encodeBase64(byte[] bstr) {
        return new String(Base64.encode(bstr, Base64.NO_WRAP));
    }

//    private String encryptByHMACSHA1(byte[] data, byte[] key) {
//        try {
//            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
//            Mac mac = Mac.getInstance("HmacSHA1");
//            mac.init(signingKey);
//            byte[] rawHmac = mac.doFinal(data);
//            return encodeBase64(rawHmac);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    public void start() {
        File toDelete = new File(fileName);
        boolean deleted = toDelete.delete();
        Log.e("deleted", "" + deleted);
        final MediaRecorder mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            TimerTask myTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mRecorder.stop();

                    mRecorder.release();
                    File packet = new File(fileName);
                    Log.e("Recorded", String.valueOf(packet.length()));
                    try {
                        fis = new FileInputStream(packet);
                        bos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        try {
                            while ((fis.read(buffer)) != -1) {
                                bos.write(buffer);
                            }
                        } catch (IOException e) {
                            Log.e("I/O ERROR ", e.getLocalizedMessage());
                        }
                    } catch (FileNotFoundException e) {
                        Log.e("FIle not found", e.getLocalizedMessage());
                    }
                    Log.e("Bytes Array", bos.toString());
                    getDataFromFingerPrint(bos.toByteArray());
                }
            };
            mRecorder.prepare();
            mRecorder.start();
            Log.e("Started Recording", this.fileName);

            new Timer().schedule(myTimerTask, 8000);
        } catch (IOException e) {
            Log.e("prepare failed", e.getLocalizedMessage());
        }
    }
        private String getDataFromFingerPrint(byte[] fingerPrint){
            String accessKey = "83cdb4671a18926e305e55430a0a3564";
            String accessSecret = "OPqSf8SuBSsypqg4Pu7eFJF0KrfyjRa04nAIqNsW";

            Map<String, Object> postParams = new HashMap<>();
            postParams.put("host", "ap-southeast-1.api.acrcloud.com");
            postParams.put("access_key", accessKey);
            postParams.put("access_secret", accessSecret);
            postParams.put("debug", false);
            postParams.put("timeout", 10);

            ACRCloudRecognizer re = new ACRCloudRecognizer(postParams);
            Log.e("@Params", postParams.toString());
              String response  = re.recognizeByFileBuffer(fingerPrint, fingerPrint.length, 0);
            System.out.println(response);
            Log.e("ACRCloud:", response);
            return response;
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResult(String result) {
        TextView output = (TextView) findViewById(R.id.output);
        String oldRes = (String) output.getText();
        output.setText(oldRes + "\n" + result);

    }

    public void onVolumeChanged(double volume) {
        //  mVolume.setText(getResources().getString(R.string.app_name) + volume);
    }

    public String httpReq(String method, String httpUrl, String contentType, Map<String, Object> requestParams) {
        String stringKeyHeader = BOUNDARY +
                "Content-Disposition: form-data; name=\"%s\"" +
                "\r\n\r\n%s\r\n";
        String filePartHeader = BOUNDARY +
                "Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\n" +
                "Content-Type: application/octet-stream\r\n\r\n";
        OutputStreamWriter request;
        BufferedReader isr;
        Log.e("method", method);
        final StringBuilder sb = new StringBuilder();
        try {
            URL url = null;
            try {
                url = new URL(httpUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Content-Type", contentType);
            request = new OutputStreamWriter(connection.getOutputStream());
            ByteArrayOutputStream postBufferStream = new ByteArrayOutputStream();
            if(requestParams.size() > 0) {
                for (String key : requestParams.keySet()) {
                    Object value = requestParams.get(key);
                    if(value instanceof String ||value instanceof Integer){
                        postBufferStream.write(String.format(stringKeyHeader, key, value).getBytes());
                    } else if(value instanceof byte[]){
                        postBufferStream.write(String.format(filePartHeader, key, key).getBytes());
                        postBufferStream.write((byte[])value);
                        postBufferStream.write("\r\n".getBytes());
                    }
                }
                postBufferStream.write(ENDBOUNDARY.getBytes());
                request.write(postBufferStream.toByteArray().toString());
            }
            request.flush();
            request.close();

            isr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = isr.readLine()) != null) {
                sb.append(line);
            }
            BufferedReader reader = new BufferedReader(isr);

            Log.e("code", connection.getContent().toString());
            isr.close();
            reader.close();
        } catch (IOException e) {
            Log.e("HTTP GET: ", e.toString());
        }
        return sb.toString();
    }


    public void getBandsInTown() {
        new Thread() {
            public void run() {
                EditText artistField = (EditText) findViewById(R.id.artist);
                Editable artist = artistField.getText();
                String bandsInTownUrl = "http://api.bandsintown.com/artists/" + artist.toString().trim() + ".json?api_version=2.0&app_id=linkbeat";
                String contentType = "application/x-www-form-urlenoded";
                try {
                    final String response = httpReq(_GET, bandsInTownUrl, contentType, _PLACEHOLDERMAP);
                    Log.e("response", response);
                    URL imageUrl = new URL("https://s.zkcdn.net/Advertisers/0903335e9cdf4cba877df66c9ef33c4c.png");
                    InputStream is = imageUrl.openConnection().getInputStream();
                    final Bitmap bitMap = BitmapFactory.decodeStream(is);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.println(1, null, response);
                            TextView output = (TextView) findViewById(R.id.output);
                            output.setText(response);
                            ImageView artistLogo = (ImageView) findViewById(R.id.artistLogo);
                            artistLogo.setImageBitmap(bitMap);
                        }
                    });
                } catch (IOException e) {
                    Log.e("HTTP: ", e.toString());
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity", "release");
    }
}
