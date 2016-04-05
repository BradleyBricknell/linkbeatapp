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

    private String _ACRRESPONSE = "{\"status\":{\"msg\":\"Success\",\"code\":0,\"version\":\"1.0\"},\"service_type\":0,\"metadata\":{\"music\":[{\"external_ids\":{\"isrc\":\"GBK3W1000207\",\"upc\":\"602537671243\"},\"play_offset_ms\":3300,\"external_metadata\":{\"spotify\":{\"album\":{\"id\":\"0mkOUedmYlOzCC4tOm2v0c\"},\"artists\":[{\"id\":\"3mIj9lX2MWuHmhNCA7LSCW\"}],\"track\":{\"id\":\"5Y1X9CVW6JRWqFm1cCzvwp\"}},\"deezer\":{\"album\":{\"id\":7633582},\"artists\":[{\"id\":3583591}],\"genres\":[{\"id\":85}],\"track\":{\"id\":76908423}}},\"label\":\"Interscope\",\"release_date\":\"2014-04-15\",\"title\":\"Chocolate\",\"duration_ms\":\"315357\",\"album\":{\"name\":\"The 1975\"},\"acrid\":\"6e758b01a0885a19c4db5919d7452ec4\",\"genres\":[{\"name\":\"Alternative\"}],\"artists\":[{\"name\":\"The 1975\"}]}],\"timestamp_utc\":\"2016-02-24 22:09:06\"},\"result_type\":0}";
    private String _ACRUNKNOWNRESPONSE = "{\"status\":{\"msg\":\"Success\",\"code\":0,\"version\":\"1.0\"},\"service_type\":0,\"metadata\":{\"music\":[{\"external_ids\":{\"isrc\":\"USAT29902060\",\"upc\":\"075678144363\"},\"play_offset_ms\":155020,\"release_date\":\"1975-06-24\",\"label\":\"Atlantic Records\",\"external_metadata\":{\"itunes\":{\"album\":{\"id\":342386143},\"artists\":[{\"id\":554526}],\"track\":{\"id\":342386519}},\"deezer\":{\"album\":{\"id\":436203},\"artists\":[{\"id\":1890}],\"genres\":[{\"id\":165}],\"track\":{\"id\":4737427}},\"youtube\":{\"vid\":\"fNo5JVG98GY\"},\"spotify\":{\"album\":{\"id\":\"1Nyr1yLuCndptRRMZ0wmlP\"},\"artists\":[{\"id\":\"3rRmDmzPcAFwcUDvG5gBqO\"}],\"track\":{\"id\":\"76mmOTUgeJYYbljEaDSQS6\"}}},\"title\":\"My Special Prayer\",\"duration_ms\":\"182866\",\"genres\":[{\"name\":\"R&B\\/Soul\\/Funk\"}],\"acrid\":\"db2f545ae5727c8de3af9ebf506b7106\",\"album\":{\"name\":\"The Best Of Percy Sledge\"},\"artists\":[{\"name\":\"Percy Sledge\"}]}],\"timestamp_utc\":\"2016-04-03 16:58:57\"},\"result_type\":0}";
    private String _ACRBADRESPONSE = "{\"status\": {\"code\":1001, \"msg\":\"NoResult\", \"version\":\"1.0\", } }";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }


    public void start(View v) {
        setContentView(R.layout.layout);

//        Log.d("bad response?", String.valueOf(_ACRBADRESPONSE.contains("msg\":\"Success\"")));
//        Log.d("GOOD response?", String.valueOf(_ACRRESPONSE.contains("msg\":\"Success\"")));
//        Log.d("RESULT", _ACRBADRESPONSE);
//        if (_ACRRESPONSE.contains("msg\":\"Success\"")) {
//            String[] artistInfo = parseACRCloudResponseOfArtistString(_ACRRESPONSE);
//            Intent profileGen = new Intent(getBaseContext(), ProfileGenerator.class);
//            Bundle b = new Bundle();
//            b.putStringArray("artistInfo", artistInfo);
//            profileGen.putExtras(b);
//            startActivity(profileGen);
//            overridePendingTransition(0, 0);
//            finish();
//        } else {
//            restartApp();
//            Toast.makeText(this, R.string.unableToIdentify, Toast.LENGTH_LONG).show();
//        }

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
                }else{
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
}
