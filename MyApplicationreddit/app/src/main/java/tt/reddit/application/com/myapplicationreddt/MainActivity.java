package tt.reddit.application.com.myapplicationreddt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/r/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedApi feedApi = retrofit.create(FeedApi.class);

        Call<Feed> call = feedApi.getfeed();

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.d("MainActivity", "onresponse: feed " + response.body().toString());
                Log.d("MainActivity", "onresponse: server " + response.toString());
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e("MainActivity", "onfailure: Unable to retrieve RSS: " + t.getMessage());
            }
        });








































    }
}
