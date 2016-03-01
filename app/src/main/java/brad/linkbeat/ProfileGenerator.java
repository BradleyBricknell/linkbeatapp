package brad.linkbeat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.graphics.Point;

import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Base64;

import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.widget.Toast;


import com.acrcloud.rec.sdk.ACRCloudClient;
import com.acrcloud.rec.sdk.ACRCloudConfig;
import com.acrcloud.rec.sdk.IACRCloudListener;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class ProfileGenerator extends Activity {
    final private String _GET = "GET";
    final String contentType = "application/x-www-form-urlenoded";
    Bitmap tourDateIcon;
    private String pageId;
    private String[] artistInfo;
    Display display;
    isVerified is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d("FacebookSdk", String.valueOf(FacebookSdk.isInitialized()));
        Log.d("PROFILEGENERATRED", "STARTED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        Bundle b = getIntent().getExtras();
        artistInfo = b.getStringArray("artistInfo");
        is = new isVerified();
        is.ArtistVerifiedJsonResponse(artistInfo[1]);
        display = getWindowManager().getDefaultDisplay();
        buildProfile();
    }

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


    public void buildProfile() {
        TextView textView = (TextView) findViewById(R.id.artistTitle);
        textView.setText(artistInfo[0]);
        retrieveWikiData(artistInfo[0]);
        getBandsInTown(artistInfo[1]);
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
            isr.close();
            reader.close();
        } catch (IOException e) {
        }
        return sb.toString();
    }


    public void setBitmapForTourDates() {
        Thread getBuyImageThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL imageUrl = new URL("http://i.imgur.com/ATl96GN.png");
                    try {
                        InputStream is = imageUrl.openConnection().getInputStream();
                        final Bitmap bitMap = BitmapFactory.decodeStream(is);
                        String encodedImage = encodeTobase64(bitMap);
                        final byte[] imageAsBytes = Base64.decode(encodedImage, 0);
                        Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                        tourDateIcon = b;
                    } catch (IOException e) {
                        Log.d("HTTP: ", e.toString());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

        };
        getBuyImageThread.start();
    }


    public void makeTourDatesTable(final String response, final int tourDateCount, final Context context) {
        setBitmapForTourDates();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ScrollView scrollView = (ScrollView) findViewById(R.id.tableScrollView);
                Point res = new Point();
                display.getSize(res);
                scrollView.getLayoutParams().height = res.y / 4;

                Log.d("makeTourDatesTable", "ADDING ROWS");
                TableLayout tl = (TableLayout) findViewById(R.id.tourDatesTable);
                tl.removeAllViews();
                TableRow row;

                if (tourDateCount == 0) {
                    row = new TableRow(context);
                    TextView noEventMessage = new TextView(context);
                    noEventMessage.setText("Sorry! Not Currently Touring!");
                    row.addView(noEventMessage);
                    tl.addView(row);
                } else {
                    for (int counter = 0; counter < tourDateCount; counter++) {
                        row = new TableRow(context);
                        row.setMinimumHeight(100);
                        final ImageView button = new ImageButton(context);
                        final String ticketUrl = extractEventsTicketUrl(response, counter);
                        button.setImageBitmap(tourDateIcon);
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                intent.setData(Uri.parse(ticketUrl));
                                startActivity(intent);
                            }
                        });
                        TextView dateTime = new TextView(context);
                        dateTime.setText(extractEventDateTime(response, counter));
                        TextView location = new TextView(context);
                        location.setText(extractEventsLocation(response, counter));
                        dateTime.setTextColor(Color.BLACK);
                        location.setTextColor(Color.BLACK);
                        row.addView(button);
                        row.addView(dateTime);
                        row.addView(location);
                        tl.addView(row, counter);
                    }
                }
            }
        });
    }

    public int getTourDatesCount(String response) {
        int tourDateCount = 0;
        try {
            Log.e("getTourDatesResponse", ":" + response);
            if (response.equals("[]")) {
                return 0;
            }
            JSONArray responseAsArray = new JSONArray(response);
            tourDateCount = responseAsArray.length();
            Log.e("tourDateCount", Integer.toString(tourDateCount));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tourDateCount;
    }

    public void getBandsInTown(final String artistName) {
        final String artist = artistName;
        Log.d("artistName", artistName);
        final String bandsInTownEventsUrl = "http://api.bandsintown.com/artists/" + artist + "/events.json?api_version=2.0&app_id=linkbeat";
        Log.d("bandsInTownEventsUrl", bandsInTownEventsUrl);
        final String contentType = "application/x-www-form-urlenoded";

        Thread bandsInTownThread = new Thread() {
            @Override
            public void run() {
                final String response = httpReq(_GET, bandsInTownEventsUrl, contentType);
                Log.d("artistResponse", response);
                makeTourDatesTable(response, getTourDatesCount(response), getBaseContext());

                Log.d("datetime", extractEventDateTime(response, 0));
                Log.d("location", extractEventsLocation(response, 0));
                Log.d("ticketUrl", extractEventsTicketUrl(response, 0));
                Log.d("ticketStatus", extractEventsTicketStatus(response, 0));

            }

        };
        bandsInTownThread.start();
    }

    private void addWikiElements(final String[] wikiInfo) {
        Thread setupThread = new Thread() {
            @Override
            public void run() {

                Point res = new Point();
                display.getSize(res);
                final int bitmapWidth = res.y / 4;
                final int bitmapHeight = res.x / 4;
                try {
                    URL imageUrl = new URL(wikiInfo[0]);
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
                                TextView extractView = (TextView) findViewById(R.id.extractView);
                                extractView.setText(wikiInfo[1]);
                            }
                        });
                    } catch (IOException e) {
                        Log.d("HTTP: ", e.toString());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        };
        setupThread.start();
    }

    private String extractEventDateTime(String response, int index) {
        String dateTime = "";
        try {
            JSONArray dt = new JSONArray(response);
            JSONObject dateTimeObj = dt.getJSONObject(index);
            dateTime = dateTimeObj.getString("datetime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dateTime;
    }


    private String extractEventsTicketUrl(String response, int index) {
        String ticketUrl = "";
        try {
            JSONArray tu = new JSONArray(response);
            JSONObject ticketUrlObj = tu.getJSONObject(index);
            ticketUrl = ticketUrlObj.getString("ticket_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ticketUrl;
    }

    private String extractEventsTicketStatus(String response, int index) {
        String ticketStatus = "";
        try {
            JSONArray ts = new JSONArray(response);
            JSONObject ticketStatusObj = ts.getJSONObject(index);
            ticketStatus = ticketStatusObj.getString("ticket_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ticketStatus;
    }

    private String extractEventsLocation(String response, int index) {
        String location = "";
        try {
            JSONArray loc = new JSONArray(response);
            JSONObject locationObj = loc.getJSONObject(index);
            location = locationObj.getString("formatted_location");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return location;
    }

    //invokes setupProfile parsing in wikiData[]
    // [0] wiki thumburl
    // [1] wiki extract

    private void retrieveWikiData(final String artistName) {
        final String wikiArtistName = artistName.replace(" ", "_");
        final String[] wikiData = new String[2];

        final String wikiAPITitlesUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&titles=";
        final String wikiAPIImagesUrlHead = "https://en.wikipedia.org/w/api.php?action=query&pageids=";
        final String wikiAPIImagesUrlTail = "&prop=pageimages&format=json&pilimit=1&pithumbsize=300";
        final String wikiAPIExtractUrl = "&prop=extracts&exintro=&explaintext=";
        Log.e("ARTISTNAME", wikiArtistName);
        //Set ProfileUrl
        Thread wikiThread = new Thread() {
            @Override
            public void run() {

                pageId = parsePageIds(httpReq(_GET, wikiAPITitlesUrl + wikiArtistName, contentType));
                wikiData[0] = getThumbUrl(httpReq(_GET, wikiAPIImagesUrlHead + pageId + wikiAPIImagesUrlTail, contentType), pageId);
                // Log.d("profileUrl", profileUrl);
                wikiData[1] = getArtistExtract(httpReq(_GET, wikiAPITitlesUrl + wikiArtistName + wikiAPIExtractUrl, contentType));
                wikiData[1] = wikiData[1].substring(0, wikiData[1].indexOf("\n") + 1);
                Log.e("wikiData[1]", wikiAPITitlesUrl + wikiArtistName + wikiAPIExtractUrl);
                Log.e("wikiData[1]", wikiData[1]);
                addWikiElements(wikiData);

            }
        };
        wikiThread.start();


        /// /return profileUrl;
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

    private String getThumbUrl(String wikiResponse, String pageId) {
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
        Log.d("sourceString", sourceString);
        return sourceString;
    }

    private String getArtistExtract(String wikiResponse) {
        String artistExtract = "";
        try {
            JSONObject wr = new JSONObject(wikiResponse);
            JSONObject wikiJSONObj = wr.getJSONObject("query");
            String pages = wikiJSONObj.getString("pages");
            JSONObject pageIdValue = new JSONObject(pages);
            JSONObject pageIds = pageIdValue.getJSONObject(pageId);
            artistExtract = pageIds.getString("extract");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artistExtract;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "release");
    }
}
