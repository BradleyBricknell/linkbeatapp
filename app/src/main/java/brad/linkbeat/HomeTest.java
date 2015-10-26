package brad.linkbeat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.acrcloud.rec.sdk.ACRCloudClient;
import com.acrcloud.rec.sdk.ACRCloudConfig;
import com.acrcloud.rec.sdk.IACRCloudListener;
import org.w3c.dom.Text;

public class HomeTest extends Activity{

    private ACRCloudClient mClient;
    private ACRCloudConfig mConfig;
    private IACRCloudListener listener;



    private boolean mProcessing = false;
    private boolean initState = false;

   // private String mResult;

    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        path = Environment.getExternalStorageDirectory().toString()
                + "/acrcloud/model";

        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }

        Button startBtn = (Button) findViewById(R.id.clicky);

        startBtn.setText(getResources().getString(R.string.app_name));
       //Button cancelBtn = (Button) findViewById(R.id.button);
        //cancelBtn.setText(getResources().getString(R.string.app_name));
        findViewById(R.id.bandsInTownButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getBandsInTown();
            }
        });
        findViewById(R.id.clicky).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                start();
            }
        });

        /*findViewById(R.id.button).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        cancel();
                    }
                });
    */
    }


    public void start() {
        TextView output;
       // mVolume.setText("");
        //mResult.setText("");
        if(!this.initState) {
            this.mConfig = new ACRCloudConfig();
            this.mConfig.acrcloudListener = listener;
            this.mConfig.context = this;
            this.mConfig.host = "ap-southeast-1.api.acrcloud.com";
            this.mConfig.accessKey = "6733b9cf07c2a41c";
            this.mConfig.accessSecret = "80b30d6b777e747266ef3d9709a4be17";
            this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE;
            this.mClient = new ACRCloudClient();

                this.initState = this.mClient.initWithConfig(this.mConfig);

            if (!this.initState) {
                Toast.makeText(this, "init error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
       // this.cancel();

        if (!mProcessing) {
            mProcessing = true;
            output = (TextView) findViewById(R.id.output);
            output.append("processing");
            if (this.mClient == null || !this.mClient.startRecognize()) {
                mProcessing = false;
              //  mResult.setText("start error!");
            }
        }

    }

    protected void cancel() {
        if (mProcessing && this.mClient != null) {
            this.mClient.stop();
         //   tv_time.setText("");
          //  mResult.setText("");
        }
        mProcessing = false;
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

        if (this.mClient != null) {
            this.mClient.stop();
            mProcessing = false;


        }
    }

    public void onVolumeChanged(double volume) {
      //  mVolume.setText(getResources().getString(R.string.app_name) + volume);
    }



    public void getBandsInTown(){
        new Thread(){
            public void run(){
                EditText artistField =  (EditText) findViewById(R.id.artist);
                Editable artist = artistField.getText();
                try {
                    URL url = null;
                    String response = null;
                    //String params = "api_version=2.0&app_id=linkbeat";
                    try {
                        url = new URL("http://api.bandsintown.com/artists/" + artist.toString().trim() + ".json?api_version=2.0&app_id=linkbeat");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlenoded");
                    connection.setRequestMethod("GET");
                    OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
                    //request.write(params);
                    request.flush();
                    request.close();
                    String line = "";


                    InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);

                    final StringBuilder sb = new StringBuilder();

                    while((line = reader.readLine()) != null){
                        sb.append(line + "/n");
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String response = sb.toString();
                            Log.println(1,null,response);
                            TextView output = (TextView) findViewById(R.id.artist);
                            output.setText(response);
                        }
                    });

                    isr.close();
                    reader.close();
                }catch(IOException e){
                    Log.e("HTTP GET: ", e.toString());
                }
            }
        }.start();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity", "release");
        if (this.mClient != null) {
            this.mClient.release();
            this.initState = false;
            this.mClient = null;
        }
    }
}
