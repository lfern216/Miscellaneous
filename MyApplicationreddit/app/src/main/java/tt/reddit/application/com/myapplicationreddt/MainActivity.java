package tt.reddit.application.com.myapplicationreddt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
//import android.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button btnRefresh;
    private EditText editFeedName;
    String currentFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        editFeedName = (EditText) findViewById(R.id.editTextFeed);

        setupToolbar();

        initialization();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedName = editFeedName.getText().toString();

                if(feedName!=null){
                    currentFeed = feedName;

                    initialization();
                }else{

                    initialization();
                }
            }
        });
    }

    private void setupToolbar(){
        Toolbar mainToolBar = (Toolbar)findViewById(R.id.toolbar_main);

        setSupportActionBar(mainToolBar);
        mainToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                switch (item.getItemId()){
                    case R.id.nav_login:
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                }


                return false;
            }
        });
    }

    public void initialization(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/r/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedApi feedApi = retrofit.create(FeedApi.class);

        Call<Feed> call = feedApi.getfeed(currentFeed);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.d("MainActivity", "onresponse: feed " + response.body().toString());
                Log.d("MainActivity", "onresponse: server " + response.toString());

                List<Entry> entries = response.body().getEntries();
                final ArrayList<Post> postList = new ArrayList<Post>();

                Log.d("MainActivity", "author1 " + entries.get(0).getAuthor());
                Log.d("MainActivity", "updated1 " + entries.get(0).getUpdated());
                Log.d("MainActivity", "title1 " + entries.get(0).getTitle());

                System.out.println("entries content: "+ entries.get(0).getContent().toString());

                for(int i=0;i<entries.size();i++) {

                    String identifier = entries.get(i).getContent();
                    int counter = 0;
                    int temp = 0;

                    while (temp != -1) {

                        temp = identifier.indexOf("<a href=", temp);
                        if (temp != -1) {
                            ++counter;
                            ++temp;
                        }
                    }

                    List<String> res = new ArrayList<String>();

                    String[] splitter = identifier.split("<a href=" + "\"");

                    for (int s = 1; s < splitter.length; s++) {
                        String temp2 = splitter[s];
                        int index = temp2.indexOf("\"");
                        temp2 = temp2.substring(0, index);
                        Log.e("MainActivity", "extracted insie loop: " + temp2);
                        res.add(temp2);
                    }

                    String[] splitter2 = identifier.split("<img src=" + "\"");
                    for (int w = 1; w < splitter2.length; w++) {
                        String temp2 = splitter2[w];
                        int index = temp2.indexOf("\"");
                        temp2 = temp2.substring(0, index);
                        res.add(temp2);
                    }

                    int last = res.size();
                    System.out.println("last: " + last);

                    for (int k = 0; k < res.size(); k++) {

                        System.out.println("entries co res_z: " + res.get(k));

                    }

                    try {
                        String f = res.get(res.size() - 1);
                        System.out.println("entries co :res part2: " + f);
                        //res.add(f);

                    } catch (IndexOutOfBoundsException e) {
                        res.add(null);
                        Log.e("MainActivity", "onresponse indexoutofbounds: " + e.getMessage());
                    } catch (NullPointerException e) {
                        res.add(null);
                        Log.e("MainActivity", "onresponse nullpointer: " + e.getMessage());
                    }

                    postList.add(new Post(entries.get(i).getTitle(), "By: "+entries.get(i).getAuthor().getName().substring(3), "Posted: " + entries.get(i).getUpdated(), res.get(0), res.get(res.size() - 1)));
                }


                for(int i=0;i<postList.size();i++){
                    System.out.println(i + "postlist_title_z: " + postList.get(i).getTitle());
                    System.out.println(i +  "postlist_author_z: " + postList.get(i).getAuthor());
                    System.out.println(i +  "postlist_dateupdated_z: " + postList.get(i).getDate_updated());
                    System.out.println(i +  "postlist_posturl_z: " + postList.get(i).getPostURL());
                    System.out.println(i +  "postlist_thumbnail_z: " + postList.get(i).getThumbnailURL());
                }


                ListView listView = (ListView) findViewById(R.id.listview);
                CustomListAdapter customAdapter = new CustomListAdapter(MainActivity.this,R.layout.card_layout,postList);
                listView.setAdapter(customAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this,CommentsActivity.class);
                        intent.putExtra("post_url",postList.get(position).getPostURL());
                        intent.putExtra("post_thumbnail",postList.get(position).getThumbnailURL());
                        intent.putExtra("post_title",postList.get(position).getTitle());
                        intent.putExtra("post_author",postList.get(position).getAuthor());
                        intent.putExtra("post_updated",postList.get(position).getDate_updated());
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e("MainActivity", "onfailure: Unable to retrieve RSS: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return true;
    }


}
