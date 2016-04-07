package brad.linkbeat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.acrcloud.rec.sdk.ACRCloudClient;
import com.acrcloud.rec.sdk.ACRCloudConfig;
import com.acrcloud.rec.sdk.IACRCloudListener;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 24/02/2016.
 */
public class LinkBeatHome extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }


    public void start(View v) {
        setContentView(R.layout.layout);

        //        COMMENTED OUT ACRCLOUD REQUESTS TO SAVE USING ALL THE REQUESTS......use _ACRRESPONSE constant for testing purposes
//
        String accessKey = "83cdb4671a18926e305e55430a0a3564";
        String accessSecret = "OPqSf8SuBSsypqg4Pu7eFJF0KrfyjRa04nAIqNsW";
        Map<String, String> postParams = new HashMap<>();
        postParams.put("host", "ap-southeast-1.api.acrcloud.com");
        postParams.put("access_key", accessKey);
        postParams.put("access_secret", accessSecret);
        postParams.put("debug", "false");
        postParams.put("timeout", "10");

        final ACRCloudClient cc = new ACRCloudClient();

        ACRCloudConfig config = new ACRCloudConfig();
        config.accessKey = accessKey;
        config.accessSecret = accessSecret;
        config.context = getApplicationContext();
        config.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE;
        config.requestTimeout = 5000;
        config.host = "ap-southeast-1.api.acrcloud.com";
        config.acrcloudListener = new IACRCloudListener() {
            @Override
            public void onResult(String s) {
                if (s.contains("msg\":\"Success\"")) {
                    cc.release();
                    Log.d("RESULT", s);
                    String[] artistInfo = parseACRCloudResponseOfArtistString(s);
                    Intent profileGen = new Intent(getBaseContext(), ProfileGenerator.class);
                    Bundle b = new Bundle();
                    b.putStringArray("artistInfo", artistInfo);
                    profileGen.putExtras(b);
                    startActivity(profileGen);
                    overridePendingTransition(0, 0);
                    finish();
                } else {
                    cc.release();
                    restartApp();
                    Toast.makeText(getApplicationContext(), R.string.unableToIdentify, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onVolumeChanged(double v) {
            }
        };
        cc.initWithConfig(config);
        cc.startRecognize();


    }


    //[0] - unformatted artist name E.g AC/DC
    //[1] - fortmatted artist name E.g ACDC
    private String[] parseACRCloudResponseOfArtistString(String ARCCloudJSON) {
        String[] artistArr = new String[2];
        String artist = "";
        try {
            JSONObject artistParent = new JSONObject(ARCCloudJSON);
            try {
                JSONObject obj = artistParent.getJSONObject("metadata");
                Object art = obj.getString("music");

                JSONArray music = new JSONArray(art.toString());
                JSONObject a = music.getJSONObject(0);
                Object objArt = a.getString("artists");

                JSONArray artistJSONArray = new JSONArray(objArt.toString());
                JSONObject artistNameObj = artistJSONArray.getJSONObject(0);
                Object artistName = artistNameObj.getString("name");
                artist = artistName.toString();

            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        artistArr[0] = artist;

        artist = artist.replaceAll("\\s", "%20");
        artist = artist.replaceAll("/", "");

        artistArr[1] = artist;
        return artistArr;
    }

    public void restartApp() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public String ACRCloudProcedure(String response) {
        String[] artistInfo;
        Log.d("bad response?", String.valueOf(response.contains("msg\":\"Success\"")));
        Log.d("GOOD response?", String.valueOf(response.contains("msg\":\"Success\"")));
        Log.d("RESULT", response);
        if (response.contains("msg\":\"Success\"")) {

            //[0] - unformatted artist name E.g AC/DC
            //[1] - fortmatted artist name E.g ACDC
            artistInfo = parseACRCloudResponseOfArtistString(response);

            return artistInfo[0];
        } else{
            return "Bad Response";
        }


    }


}
