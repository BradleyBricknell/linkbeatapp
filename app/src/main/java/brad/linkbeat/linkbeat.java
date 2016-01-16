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


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.graphics.Point;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;

import android.os.Bundle;
import android.view.Display;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Bitmap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class linkbeat extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
    }

    final private String _GET = "GET";
    final String contentType = "application/x-www-form-urlenoded";
    Bitmap tourDateIcon;


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


    //
    public void start(View v) {
        String[] artistInfo = parseACRCloudResponseOfArtistString(_ACRRESPONSE);
        TextView textView = (TextView) findViewById(R.id.artistTitle);
        textView.setText(artistInfo[0]);
        retrieveWikiData(artistInfo[0]);
        getBandsInTown(artistInfo[1]);


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
                Log.d("makeTourDatesTable", "ADDING ROWS");
                TableLayout tl = (TableLayout) findViewById(R.id.tourDatesTable);
                TableRow row;
                if (tourDateCount < 1) {
                    row = new TableRow(context);
                    TextView noEventMessage = new TextView(context);
                    noEventMessage.setText("No Events Available");
                    row.addView(noEventMessage);
                    tl.addView(row);
                } else {
                    for (int counter = 0; counter < tourDateCount; counter++) {
                        row = new TableRow(context);
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
                        Log.d("adding rows", Integer.toString(counter));
                        Log.e("Thread.activeCount", Integer.toString(Thread.activeCount()));
                    }


                }
            }
        });

    }

    public int getTourDatesCount(String response) {
        int tourDateCount = 0;
        try {
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

                //  try {
                //       final URL imageUrl = new URL(imageThumbNail);
                //        try {
                //   InputStream is = imageUrl.openConnection().getInputStream();
//                        final Bitmap bitMap = BitmapFactory.decodeStream(is);
//                        final ImageView artistLogo = (ImageView) findView(R.id.artistLogo);

                //      artistLogo.setImageBitmap(bitMap);
                Log.d("datetime", extractEventDateTime(response, 0));
                Log.d("location", extractEventsLocation(response, 0));
                Log.d("ticketUrl", extractEventsTicketUrl(response, 0));
                Log.d("ticketStatus", extractEventsTicketStatus(response, 0));

//                    } catch (IOException e) {
//                        Log.d("HTTP: ", e.toString());
//                    }

//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
            }

        };
        bandsInTownThread.start();
    }

    private void setUpProfile(final String[] wikiInfo) {
        Thread setupThread = new Thread() {
            @Override
            public void run() {
                Log.e("profile", "profile");
                Display display = getWindowManager().getDefaultDisplay();
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

    private String extractEventsThumbNail(String response, int index) {
        String imageThumbNail = "";
        try {
            JSONArray thumb = new JSONArray(response);
            JSONObject thumbUrlObj = thumb.getJSONObject(index);
            Object tn = thumbUrlObj.getString("artists");

            JSONArray thumbChild = new JSONArray(tn.toString());
            JSONObject thumbUrlObjChild1 = thumbChild.getJSONObject(index);
            Object urlOjb = thumbUrlObjChild1.getString("thumb_url");
            imageThumbNail = urlOjb.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageThumbNail;
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
        final String[] wikiData = new String[2];

        final String wikiAPITitlesUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&titles=";
        final String wikiAPIImagesUrlHead = "https://en.wikipedia.org/w/api.php?action=query&pageids=";
        final String wikiAPIImagesUrlTail = "&prop=pageimages&format=json&pilimit=1&pithumbsize=300";

        //Set ProfileUrl
        Thread wikiThread = new Thread() {
            @Override
            public void run() {
                String pageId = parsePageIds(httpReq(_GET, wikiAPITitlesUrl + artistName, contentType));
                wikiData[0] = getWikiThumbUrl(httpReq(_GET, wikiAPIImagesUrlHead + pageId + wikiAPIImagesUrlTail, contentType), pageId);
                // Log.d("profileUrl", profileUrl);

                Log.e("wikiData[0]", wikiData[0]);
                setUpProfile(wikiData);
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
        Log.d("sourceString", sourceString);
        return sourceString;
    }

    private String getArtistExtract(String wikiResponse) {
        String artistExtract = "";

        return artistExtract;
    }

//    private String getThreadString() {
//        Log.d("getThreadString", "s" + _THREADSTRING + "e");
//        return _THREADSTRING;
//    }
//
//    private void setThreadString(String newString) {
//        _THREADSTRING = newString;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "release");
    }
}
