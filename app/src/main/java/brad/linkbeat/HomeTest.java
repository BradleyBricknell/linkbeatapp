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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.MainThread;
import android.util.Base64;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Display;
import android.os.Environment;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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

import com.acrcloud.rec.sdk.ACRCloudClient;
import com.acrcloud.rec.sdk.ACRCloudConfig;
import com.acrcloud.rec.sdk.IACRCloudListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class HomeTest extends Activity {

    private String _GET = "GET";
    private String BOUNDARYSTR = "*****2015.03.30.acrcloud.rec.copyright." + System.currentTimeMillis() + "*****";
    private String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";
    private String ENDBOUNDARY = "--" + BOUNDARYSTR + "--\r\n\r\n";
    private String _THREADSTRING = "";
    final String contentType = "application/x-www-form-urlenoded";


    private String _ACRRESPONSE = "{\"status\":{\"msg\":\"Success\",\"code\":0,\"version\":\"1.0\"},\"metadata\":{\"music\":[{\"external_ids\":{\"isrc\":\"QMUY41500080\",\"upc\":\"653738300326\"},\"album\":{\"name\":\"Lean On (Remixes), Vol.2\"},\"play_offset_ms\":14080,\"duration_ms\":\"225000\",\"external_metadata\":{\"omusic\":{\"album\":{\"name\":\"Peace Is The Mission 和平任務\",\"id\":1231350},\"artists\":[{\"name\":\"AC/DC\",\"id\":27252}],\"track\":{\"name\":\"Lean On (feat. MØ &amp; DJ Snake)\",\"id\":1231350004}},\"deezer\":{\"album\":{\"id\":11145928},\"artists\":[{\"id\":282118}],\"track\":{\"id\":\"106904402\"}}},\"acrid\":\"7dd84cfe6c7a4822abbebc18c480b5cb\",\"title\":\"Lean On (feat. MØ & DJ Snake) [J Balvin & Farruko Remix]\",\"artists\":[{\"name\":\"AC/DC\"}]},{\"external_ids\":{\"isrc\":\"QMUY41500008\",\"upc\":\"653738275129\"},\"play_offset_ms\":14280,\"external_metadata\":{\"omusic\":{\"album\":{\"name\":\"Peace Is The Mission 和平任務\",\"id\":1231350},\"artists\":[{\"name\":\"ac\",\"id\":27252}],\"track\":{\"name\":\"Lean On (feat. MØ &amp; DJ Snake)\",\"id\":1231350004}},\"spotify\":{\"album\":{\"id\":\"56k0jdcAe2CBpCOsD1HE0A\"},\"artists\":[{\"id\":\"738wLrAtLtCtFOLvQBXOXp\"},{\"id\":\"0bdfiayQAKewqEvaU6rXCv\"},{\"id\":\"540vIaP2JwjQb9dm3aArA4\"}],\"track\":{\"id\":\"4KcVVhAaHxqtX2ANt4b3tc\"}},\"itunes\":{\"album\":{\"id\":975442615},\"artists\":[{\"id\":315761934}],\"track\":{\"id\":975443020}},\"deezer\":{\"album\":{\"id\":9751262},\"artists\":[{\"id\":7595506}],\"genres\":[{\"id\":106}],\"track\":{\"id\":95859598}}},\"label\":\"Mad Decent\",\"release_date\":\"2015-03-02\",\"title\":\"Lean On\",\"duration_ms\":\"176561\",\"album\":{\"name\":\"Lean On\"},\"acrid\":\"ded792bc75a2c6758edf9d2503327792\",\"genres\":[{\"name\":\"Electro\"}],\"artists\":[{\"name\":\"Major Lazer feat. MØ & DJ Snake\"}]}],\"timestamp_utc\":\"2015-12-14 15:17:37\"},\"result_type\":0}\n" +
            "12-14 15:17:37.188   ";

    //REAL ONE - NOW CHANGING "artists" to test other artist pages
    //"{\"status\":{\"msg\":\"Success\",\"code\":0,\"version\":\"1.0\"},\"metadata\":{\"music\":[{\"external_ids\":{\"isrc\":\"QMUY41500080\",\"upc\":\"653738300326\"},\"album\":{\"name\":\"Lean On (Remixes), Vol.2\"},\"play_offset_ms\":14080,\"duration_ms\":\"225000\",\"external_metadata\":{\"omusic\":{\"album\":{\"name\":\"Peace Is The Mission 和平任務\",\"id\":1231350},\"artists\":[{\"name\":\"Major Lazer\",\"id\":27252}],\"track\":{\"name\":\"Lean On (feat. MØ &amp; DJ Snake)\",\"id\":1231350004}},\"deezer\":{\"album\":{\"id\":11145928},\"artists\":[{\"id\":282118}],\"track\":{\"id\":\"106904402\"}}},\"acrid\":\"7dd84cfe6c7a4822abbebc18c480b5cb\",\"title\":\"Lean On (feat. MØ & DJ Snake) [J Balvin & Farruko Remix]\",\"artists\":[{\"name\":\"Major Lazer\"}]},{\"external_ids\":{\"isrc\":\"QMUY41500008\",\"upc\":\"653738275129\"},\"play_offset_ms\":14280,\"external_metadata\":{\"omusic\":{\"album\":{\"name\":\"Peace Is The Mission 和平任務\",\"id\":1231350},\"artists\":[{\"name\":\"Major Lazer\",\"id\":27252}],\"track\":{\"name\":\"Lean On (feat. MØ &amp; DJ Snake)\",\"id\":1231350004}},\"spotify\":{\"album\":{\"id\":\"56k0jdcAe2CBpCOsD1HE0A\"},\"artists\":[{\"id\":\"738wLrAtLtCtFOLvQBXOXp\"},{\"id\":\"0bdfiayQAKewqEvaU6rXCv\"},{\"id\":\"540vIaP2JwjQb9dm3aArA4\"}],\"track\":{\"id\":\"4KcVVhAaHxqtX2ANt4b3tc\"}},\"itunes\":{\"album\":{\"id\":975442615},\"artists\":[{\"id\":315761934}],\"track\":{\"id\":975443020}},\"deezer\":{\"album\":{\"id\":9751262},\"artists\":[{\"id\":7595506}],\"genres\":[{\"id\":106}],\"track\":{\"id\":95859598}}},\"label\":\"Mad Decent\",\"release_date\":\"2015-03-02\",\"title\":\"Lean On\",\"duration_ms\":\"176561\",\"album\":{\"name\":\"Lean On\"},\"acrid\":\"ded792bc75a2c6758edf9d2503327792\",\"genres\":[{\"name\":\"Electro\"}],\"artists\":[{\"name\":\"Major Lazer feat. MØ & DJ Snake\"}]}],\"timestamp_utc\":\"2015-12-14 15:17:37\"},\"result_type\":0}\n" +
    //    "12-14 15:17:37.188   ";

    //From Stackoverflow for encoding and decoding bitmaps for easy bitmap manipuaton
    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

    }

    //
    public void start(View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] artistInfo = parseACRCloudResponseOfArtistString(_ACRRESPONSE);

                getBandsInTown(artistInfo[1]);
                setUpProfile(artistInfo);

                // COMMENTED OUT ACRCLOUD REQUESTS TO SAVE USING ALL THE REQUESTS......use _ACRRESPONSE constant for testing purposes

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
//                        getBandsInTown(parseACRCloudResponseOfArtistString(s));
//                    }
//
//                    @Override
//                    public void onVolumeChanged(double v) {
//
//                    }
//                };
//                cc.initWithConfig(config);
//                cc.startRecognize();
            }
        });
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

        artist = artist.replaceAll("\\s", "");
        artist = artist.replaceAll("/", "");

        artistArr[1] = artist;
        return artistArr;
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

    public String httpReq(String method, String httpUrl, String contentType) {
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


    public void getBandsInTown(final String artistName) {
        final String artist = artistName;
        Log.e("artistName", artistName);
        final String bandsInTownArtistUrl = "http://api.bandsintown.com/artists/" + artist + ".json?api_version=2.0&app_id=linkbeat";
        final String bandsInTownEventsUrl = "http://api.bandsintown.com/artists/" + artist + "/events.json?api_version=2.0&app_id=linkbeat";
        Log.e("bandsInTownArtistUrl", bandsInTownArtistUrl);
        Log.e("bandsInTownEventsUrl", bandsInTownEventsUrl);
        final String contentType = "application/x-www-form-urlenoded";

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response = httpReq(_GET, bandsInTownEventsUrl, contentType);
                Log.e("artistResponse", response);
                final String imageThumbNail;
                if (response.length() < 3) {
                    Log.e("Nope", "no current events");
                    String backupResponse = httpReq(_GET, bandsInTownArtistUrl, contentType);
                    //   imageThumbNail = extractArtistThumbNail(backupResponse);

                } else {
                    //   imageThumbNail = extractEventsThumbNail(response);
                    //         Log.e("itisimageThumbNail", imageThumbNail);
                }
                //  try {
                //       final URL imageUrl = new URL(imageThumbNail);
                //        try {
                //   InputStream is = imageUrl.openConnection().getInputStream();
//                        final Bitmap bitMap = BitmapFactory.decodeStream(is);
//                        final ImageView artistLogo = (ImageView) findViewById(R.id.artistLogo);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //      artistLogo.setImageBitmap(bitMap);
                        TextView output = (TextView) findViewById(R.id.output);
                        output.setText(artistName + " touring data = ");
                        output.append(extractEventDateTime(response));
                        output.append(extractEventsLocation(response));
                        output.append(extractEventsTicketUrl(response));
                        output.append(extractEventsTicketStatus(response));

                    }
                });
//                    } catch (IOException e) {
//                        Log.e("HTTP: ", e.toString());
//                    }

//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
            }

        }).start();
    }

    private void setUpProfile(final String[] artistInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("setUp", retrieveWikiData(artistInfo[0])[1]);
                String[] wikiData = retrieveWikiData(artistInfo[0]);

                final String trueThumbUrl = wikiData[1];
                Display display = getWindowManager().getDefaultDisplay();
                Point res = new Point();
                display.getSize(res);
                final int bitmapWidth = res.y / 7;
                final int bitmapHeight = res.x / 7;
                Log.e("trueThumbUrl", wikiData[1]);

                Log.e("THIS ONE IS RUNNING", wikiData[1]);
                try {
                    URL imageUrl = new URL(wikiData[1]);
                    try {
                        InputStream is = imageUrl.openConnection().getInputStream();
                        final Bitmap bitMap = BitmapFactory.decodeStream(is);

                        final ImageView artistLogo = (ImageView) findViewById(R.id.artistLogo);

                        String encodedImage = encodeTobase64(bitMap);
                        final byte[] imageAsBytes = Base64.decode(encodedImage, 0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                                artistLogo.setImageBitmap(Bitmap.createScaledBitmap(b, bitmapWidth, bitmapHeight, false));
                            }
                        });
                    } catch (IOException e) {
                        Log.e("HTTP: ", e.toString());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            // getBandsInTown(artistInfo[1]);

        }).start();
    }


    private String extractArtistThumbNail(String response) {
        String imageThumbNail = "";
        try {
            JSONObject thumb = new JSONObject(response);
            imageThumbNail = thumb.getString("thumb_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageThumbNail;
    }

    private String extractEventsThumbNail(String response) {
        String imageThumbNail = "";
        try {
            JSONArray thumb = new JSONArray(response);
            JSONObject thumbUrlObj = thumb.getJSONObject(0);
            Object tn = thumbUrlObj.getString("artists");

            JSONArray thumbChild = new JSONArray(tn.toString());
            JSONObject thumbUrlObjChild1 = thumbChild.getJSONObject(0);
            Object urlOjb = thumbUrlObjChild1.getString("thumb_url");
            imageThumbNail = urlOjb.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageThumbNail;
    }


    private String extractEventDateTime(String response) {
        String dateTime = "";
        try {
            JSONArray dt = new JSONArray(response);
            JSONObject dateTimeObj = dt.getJSONObject(0);
            dateTime = dateTimeObj.getString("datetime");
            Log.e("dateTime", dateTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    private String extractEventsTicketUrl(String response) {
        String ticketUrl = "";
        try {
            JSONArray tu = new JSONArray(response);
            JSONObject ticketUrlObj = tu.getJSONObject(0);
            ticketUrl = ticketUrlObj.getString("ticket_url");
            Log.e("ticketUrl", ticketUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ticketUrl;
    }

    private String extractEventsTicketStatus(String response) {
        String ticketStatus = "";
        try {
            JSONArray ts = new JSONArray(response);
            JSONObject ticketStatusObj = ts.getJSONObject(0);
            ticketStatus = ticketStatusObj.getString("ticket_status");
            Log.e("ticketStatus", ticketStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ticketStatus;
    }

    private String extractEventsLocation(String response) {
        String location = "";
        try {
            JSONArray loc = new JSONArray(response);
            JSONObject locationObj = loc.getJSONObject(0);
            location = locationObj.getString("formatted_location");
            Log.e("location", location);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return location;
    }

    //wiki data consists of
    //[0] - extract about artist
    //[1] - url for thumbnail
    private String[] retrieveWikiData(final String artistName) {
        final String[] wikiData = new String[2];

        final String wikiAPITitlesUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&titles=";
        final String wikiAPIImagesUrlHead = "https://en.wikipedia.org/w/api.php?action=query&pageids=";
        final String wikiAPIImagesUrlTail = "&prop=pageimages&format=json&pilimit=1&pithumbsize=300";

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String pageId = parsePageIds(httpReq(_GET, wikiAPITitlesUrl + artistName, contentType));
                setThreadString(getWikiThumbUrl(httpReq(_GET, wikiAPIImagesUrlHead + pageId + wikiAPIImagesUrlTail, contentType), pageId));
                Log.e("getThreadString", wikiAPIImagesUrlHead + pageId + wikiAPIImagesUrlTail);
            }
        }).start();
        wikiData[1] = getThreadString();
        Log.e("artistData[1]", wikiData[1]);
        return wikiData;
    }

    private String parsePageIds(String wikiResponse) {
        String pageIds = "";
        try {
            JSONObject wr = new JSONObject(wikiResponse);
            JSONObject wikiJSONObj = wr.getJSONObject("query");
            Object pages = wikiJSONObj.getString("pages");
            JSONObject pageIdsObj = new JSONObject(pages.toString());
            JSONArray ob = pageIdsObj.names();
            pageIds = ob.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pageIds;
    }

    private String getWikiThumbUrl(String wikiResponse, String pageId) {
        String sourceString = "";
        try {
            JSONObject wr = new JSONObject(wikiResponse);
            JSONObject wikiJSONObj = wr.getJSONObject("query");
            String pages = wikiJSONObj.getString("pages");
            JSONObject pageIdValue = new JSONObject(pages);
            JSONObject pageIds = pageIdValue.getJSONObject(pageId);
            JSONObject thumbnail = pageIds.getJSONObject("thumbnail");
            ;
            Object source = thumbnail.getString("source");
            sourceString = source.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("getWikiThumbUrl3", sourceString);
        return sourceString;
    }

    private String getArtistExtract(String wikiResponse) {
        String artistExtract = "";

        return artistExtract;
    }

    private String getThreadString() {
        return _THREADSTRING;
    }

    private void setThreadString(String newString) {
        _THREADSTRING = newString;
        Log.e("SETTING STRING: ", newString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity", "release");
    }
}
