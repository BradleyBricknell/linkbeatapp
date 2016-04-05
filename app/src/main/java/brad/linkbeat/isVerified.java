package brad.linkbeat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Brad on 27/02/2016.
 */
public class isVerified extends Activity {

    public void ArtistVerifiedJsonResponse(String artist) {
        final AccessToken token = new AccessToken("1033456563340806|dBiRL2nxNZeYalf0LPOmDDzmnl0", "1033456563340806", "852507748103932", null, null, null, null, null);
        final String artistName = artist.replace("%20", "");
        final Bundle bundleWithParams = new Bundle();
        bundleWithParams.putString("fields", "is_verified");
        Log.d(" AccessToken.getCur", AccessToken.ACCESS_TOKEN_KEY);
        GraphRequest graphRequest = new GraphRequest(
                token,
                "/" + artistName,
                bundleWithParams,
                HttpMethod.GET,
                new GraphRequest.Callback() {

                    public void onCompleted(GraphResponse response) {
                        try {
                            if (!response.toString().contains("Cannot query users by their username")) {
                                ProfileGenerator.setfacebookPageId(response.getJSONObject().getString("id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

        );
        graphRequest.executeAsync();
    }



}
