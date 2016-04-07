package brad.linkbeat;

import android.app.Application;

import android.os.Bundle;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.facebook.FacebookSdk;

import junit.framework.*;

import brad.linkbeat.LinkBeatHome;
import brad.linkbeat.ProfileGenerator;


public class ApplicationTest extends TestCase {
    protected LinkBeatHome linkBeatHome;
    protected ProfileGenerator profileGenerator;
    protected isVerified isVerified;

    protected String badResponse;
    protected String goodResponse;
    protected String bandsInTownQuery;
    protected String bandsInTownResponse;
    protected String artistName;
    protected String verifiedId;

    // Not protected so facebook graph response can assign the id to this value for testing
    static String facebookPageId = "";

    protected void setUp() {

        linkBeatHome = new LinkBeatHome();
        profileGenerator = new ProfileGenerator();

        isVerified = new isVerified();


        artistName = "The 1975";
        verifiedId = "365648163464988";

        goodResponse = "{\"status\":{\"msg\":\"Success\",\"code\":0,\"version\":\"1.0\"},\"service_type\":0,\"metadata\":{\"music\":[{\"external_ids\":{\"isrc\":\"GBK3W1000207\",\"upc\":\"602537671243\"},\"play_offset_ms\":3300,\"external_metadata\":{\"spotify\":{\"album\":{\"id\":\"0mkOUedmYlOzCC4tOm2v0c\"},\"artists\":[{\"id\":\"3mIj9lX2MWuHmhNCA7LSCW\"}],\"track\":{\"id\":\"5Y1X9CVW6JRWqFm1cCzvwp\"}},\"deezer\":{\"album\":{\"id\":7633582},\"artists\":[{\"id\":3583591}],\"genres\":[{\"id\":85}],\"track\":{\"id\":76908423}}},\"label\":\"Interscope\",\"release_date\":\"2014-04-15\",\"title\":\"Chocolate\",\"duration_ms\":\"315357\",\"album\":{\"name\":\"The 1975\"},\"acrid\":\"6e758b01a0885a19c4db5919d7452ec4\",\"genres\":[{\"name\":\"Alternative\"}],\"artists\":[{\"name\":\"The 1975\"}]}],\"timestamp_utc\":\"2016-02-24 22:09:06\"},\"result_type\":0}";
        badResponse = "{\"status\": {\"code\":1001, \"msg\":\"NoResult\", \"version\":\"1.0\", } }";

        bandsInTownQuery = "http://api.bandsintown.com/artists/the%201975/events.json?api_version=2.0&app_id=linkbeat";
        bandsInTownResponse = profileGenerator.httpReq("GET", bandsInTownQuery, "\"application/x-www-form-urlenoded\"");

    }


    protected void runTest() throws Throwable {
        super.runTest();
    }

    @SmallTest
    public void testBandsInTown() {
        // Test the bands in town service by checking the response
        assertTrue(bandsInTownResponse.contains("The 1975"));

        // Test essential properties of the response exist
        // check property facebook_tour_dates_url exists
        assertTrue(bandsInTownResponse.contains("facebook_tour_dates_url"));

        // check property formatted_location  exists
        assertTrue(bandsInTownResponse.contains("formatted_location"));

        // check property datetime exists
        assertTrue(bandsInTownResponse.contains("datetime"));

        // check property ticket_status exists
        assertTrue(bandsInTownResponse.contains("ticket_url"));

        // check the number of touring events is greater than 0
        assertTrue(profileGenerator.getTourDatesCount(bandsInTownResponse) > 0);


    }

    public void testWiki() {
        //check the output from retrieveWikiData
        Log.d("retrieveWikiData", profileGenerator.retrieveWikiData(artistName)[0]);
    }

    @SmallTest
    public void testSocialMedia() {
    //Test Twitter intent properties

    }

    public void testFacebookOAuth() {
        // Test the Id getting verified
     //   assertEquals("365648163464988", isVerified.getId());

    }

    @SmallTest
    public void testACRCloud() {
        // Test a bad response
        assertEquals("Bad Response", linkBeatHome.ACRCloudProcedure(badResponse));
        Log.d("Test a bad response", linkBeatHome.ACRCloudProcedure(badResponse));

        // Test a good response
        assertEquals( "The 1975", linkBeatHome.ACRCloudProcedure(goodResponse));
        Log.d("Test a good response", linkBeatHome.ACRCloudProcedure(goodResponse));

    }


    public static void setFacebookPageId(String pageId) {
        facebookPageId = pageId;
        Log.d("SET FB  TEST ", facebookPageId);

    }


}