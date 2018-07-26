package tt.reddit.application.com.myapplicationreddt;

/**
 * Created by lazaro on 7/26/18.
 */

import retrofit2.Call;
import retrofit2.http.GET;

public interface FeedApi {

    String BASE_URL = "https://www.reddit.com/r/"; //this is the part that doesn't change (static)

    @GET("earthporn/.rss")
    Call<Feed> getfeed();
}
