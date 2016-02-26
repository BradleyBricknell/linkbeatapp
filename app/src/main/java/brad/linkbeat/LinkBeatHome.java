package brad.linkbeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 24/02/2016.
 */
public class LinkBeatHome extends Activity{

    private String _ACRRESPONSE = "{\"status\":{\"msg\":\"Success\",\"code\":0,\"version\":\"1.0\"},\"service_type\":0,\"metadata\":{\"music\":[{\"external_ids\":{\"isrc\":\"GBK3W1000207\",\"upc\":\"602537671243\"},\"play_offset_ms\":3300,\"external_metadata\":{\"spotify\":{\"album\":{\"id\":\"0mkOUedmYlOzCC4tOm2v0c\"},\"artists\":[{\"id\":\"3mIj9lX2MWuHmhNCA7LSCW\"}],\"track\":{\"id\":\"5Y1X9CVW6JRWqFm1cCzvwp\"}},\"deezer\":{\"album\":{\"id\":7633582},\"artists\":[{\"id\":3583591}],\"genres\":[{\"id\":85}],\"track\":{\"id\":76908423}}},\"label\":\"Interscope\",\"release_date\":\"2014-04-15\",\"title\":\"Chocolate\",\"duration_ms\":\"315357\",\"album\":{\"name\":\"The 1975\"},\"acrid\":\"6e758b01a0885a19c4db5919d7452ec4\",\"genres\":[{\"name\":\"Alternative\"}],\"artists\":[{\"name\":\"The 1975\"}]}],\"timestamp_utc\":\"2016-02-24 22:09:06\"},\"result_type\":0}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }


    public void start(View v) {
        String[] artistInfo = parseACRCloudResponseOfArtistString(_ACRRESPONSE);
        Intent profileGen  = new Intent(this, ProfileGenerator.class);
        Bundle b = new Bundle();
        b.putStringArray("artistInfo", artistInfo);
        profileGen.putExtras(b);
        startActivity(profileGen);
        finish();
        // COMMENTED OUT ACRCLOUD REQUESTS TO SAVE USING ALL THE REQUESTS......use _ACRRESPONSE constant for testing purposes
//
//        String accessKey = "83cdb4671a18926e305e55430a0a3564";
//        String accessSecret = "OPqSf8SuBSsypqg4Pu7eFJF0KrfyjRa04nAIqNsW";
//        Map<String, String> postParams = new HashMap<>();
//        postParams.put("host", "ap-southeast-1.api.acrcloud.com");
//        postParams.put("access_key", accessKey);
//        postParams.put("access_secret", accessSecret);
//        postParams.put("debug", "false");
//        postParams.put("timeout", "10");
//
//        final ACRCloudClient cc = new ACRCloudClient();
//
//        ACRCloudConfig config = new ACRCloudConfig();
//        config.accessKey = accessKey;
//        config.accessSecret = accessSecret;
//        config.context = getApplicationContext();
//        config.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE;
//        config.requestTimeout = 5000;
//        config.host = "ap-southeast-1.api.acrcloud.com";
//        config.acrcloudListener = new IACRCloudListener() {
//            @Override
//            public void onResult(String s) {
//                Toast.makeText(getBaseContext(), "Result", Toast.LENGTH_SHORT).show();
//                Log.d("RESULT", s);
//                cc.release();
//                String[] artistInfo = parseACRCloudResponseOfArtistString(s);
//                TextView textView = (TextView) findViewById(R.id.artistTitle);
//                textView.setText(artistInfo[0]);
//                retrieveWikiData(artistInfo[0]);
//                getBandsInTown(artistInfo[1]);
//
//            }
//
//            @Override
//            public void onVolumeChanged(double v) {
//
//            }
//        };
//        cc.initWithConfig(config);
//        cc.startRecognize();


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

}
