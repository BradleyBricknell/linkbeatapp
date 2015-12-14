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
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.acrcloud.rec.sdk.ACRCloudClient;
//import com.acrcloud.rec.sdk.ACRCloudConfig;
//import com.acrcloud.rec.sdk.IACRCloudListener;

import org.json.JSONObject;

public class HomeTest extends Activity{

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


    private String _ACRRESPONSE = "{\"status\":{\"msg\":\"Success\",\"code\":0,\"version\":\"1.0\"},\"metadata\":{\"music\":[{\"external_ids\":{\"isrc\":\"QMUY41500080\",\"upc\":\"653738300326\"},\"album\":{\"name\":\"Lean On (Remixes), Vol.2\"},\"play_offset_ms\":14080,\"duration_ms\":\"225000\",\"external_metadata\":{\"omusic\":{\"album\":{\"name\":\"Peace Is The Mission 和平任務\",\"id\":1231350},\"artists\":[{\"name\":\"Major Lazer\",\"id\":27252}],\"track\":{\"name\":\"Lean On (feat. MØ &amp; DJ Snake)\",\"id\":1231350004}},\"deezer\":{\"album\":{\"id\":11145928},\"artists\":[{\"id\":282118}],\"track\":{\"id\":\"106904402\"}}},\"acrid\":\"7dd84cfe6c7a4822abbebc18c480b5cb\",\"title\":\"Lean On (feat. MØ & DJ Snake) [J Balvin & Farruko Remix]\",\"artists\":[{\"name\":\"Major Lazer\"}]},{\"external_ids\":{\"isrc\":\"QMUY41500008\",\"upc\":\"653738275129\"},\"play_offset_ms\":14280,\"external_metadata\":{\"omusic\":{\"album\":{\"name\":\"Peace Is The Mission 和平任務\",\"id\":1231350},\"artists\":[{\"name\":\"Major Lazer\",\"id\":27252}],\"track\":{\"name\":\"Lean On (feat. MØ &amp; DJ Snake)\",\"id\":1231350004}},\"spotify\":{\"album\":{\"id\":\"56k0jdcAe2CBpCOsD1HE0A\"},\"artists\":[{\"id\":\"738wLrAtLtCtFOLvQBXOXp\"},{\"id\":\"0bdfiayQAKewqEvaU6rXCv\"},{\"id\":\"540vIaP2JwjQb9dm3aArA4\"}],\"track\":{\"id\":\"4KcVVhAaHxqtX2ANt4b3tc\"}},\"itunes\":{\"album\":{\"id\":975442615},\"artists\":[{\"id\":315761934}],\"track\":{\"id\":975443020}},\"deezer\":{\"album\":{\"id\":9751262},\"artists\":[{\"id\":7595506}],\"genres\":[{\"id\":106}],\"track\":{\"id\":95859598}}},\"label\":\"Mad Decent\",\"release_date\":\"2015-03-02\",\"title\":\"Lean On\",\"duration_ms\":\"176561\",\"album\":{\"name\":\"Lean On\"},\"acrid\":\"ded792bc75a2c6758edf9d2503327792\",\"genres\":[{\"name\":\"Electro\"}],\"artists\":[{\"name\":\"Major Lazer feat. MØ & DJ Snake\"}]}],\"timestamp_utc\":\"2015-12-14 15:17:37\"},\"result_type\":0}\n" +
            "12-14 15:17:37.188   ";
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

//
    public void start() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("ARTIST ISSSSSSS: ", parseACRCloudResponseOfArtistString(_ACRRESPONSE));
                // COMMENTED OUT ACRCLOUD REQUESTS TO SAVE USING ALL THE REQUESTS......use _ACRRESPONSE constant for testing purposes
//
//                String accessKey = "83cdb4671a18926e305e55430a0a3564";
//                String accessSecret = "OPqSf8SuBSsypqg4Pu7eFJF0KrfyjRa04nAIqNsW";
//                Map<String, String> postParams = new HashMap<>();
//                postParams.put("host", "ap-southeast-1.api.acrcloud.com");
//                postParams.put("access_key", accessKey);
//                postParams.put("access_secret", accessSecret);
//                postParams.put("debug", "false");
//                postParams.put("timeout", "10");
//
//                final ACRCloudClient cc = new ACRCloudClient();
//                Log.d("RECOGNISE", "SAD JIASIODJIKOAWSDJOSJDIAO");
//
//
//                ACRCloudConfig config = new ACRCloudConfig();
//                config.accessKey = accessKey;
//                config.accessSecret = accessSecret;
//                config.context = getApplicationContext();
//                config.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE;
//                config.requestTimeout = 5000;
//                config.host = "ap-southeast-1.api.acrcloud.com";
//                config.acrcloudListener = new IACRCloudListener() {
//                    @Override
//                    public void onResult(String s) {
//                        Toast.makeText(getBaseContext(), "Result", Toast.LENGTH_SHORT).show();
//                        Log.d("RESULT", s);
//                        cc.release();
//                        parseArtistString(s);
//
//                    }
//
//                    @Override
//                    public void onVolumeChanged(double v) {
//                        Log.e("VOLUME CHANGED", "VOLUME CHANGED");
//                    }
//                };
//                cc.initWithConfig(config);
//                cc.startRecognize();
            }
        });
    }

        private String parseACRCloudResponseOfArtistString(String ARCCloudJSON){
            String artist = "";
            try{
                JSONObject artistParent = new JSONObject(ARCCloudJSON);
                try {
                    JSONObject obj = artistParent.getJSONObject("metadata");
                    Object art =  obj.getString("music");

                    JSONArray music = new JSONArray(art.toString());
                    JSONObject a = music.getJSONObject(0);
                    Object objArt = a.getString("artists");

                    JSONArray artistJSONArray = new JSONArray(objArt.toString());
                    JSONObject artistNameObj = artistJSONArray.getJSONObject(0);
                    Object artistName = artistNameObj.getString("name");
                    artist = artistName.toString();

                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            return artist;

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
