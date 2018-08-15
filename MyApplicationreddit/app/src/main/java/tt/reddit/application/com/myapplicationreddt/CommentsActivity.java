package tt.reddit.application.com.myapplicationreddt;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by lazaro on 8/5/18.
 */

public class CommentsActivity extends AppCompatActivity{

    private static String postURL;
    private static String postThumbnail;
    private static String postTitle;
    private static String postAuthor;
    private static String postDateUpdated;
    private int defaultImage;
    private String currentFeedurl;
    private ListView list_comments;

    ArrayList<Comment> commentList = new ArrayList<>();
    ProgressBar comment_ProgressBar;
    TextView comment_editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        comment_ProgressBar = (ProgressBar)findViewById(R.id.progressBar_post);
        comment_ProgressBar.setVisibility(View.VISIBLE);
        comment_editText = (TextView) findViewById(R.id.text_comment);
        setupImageLoader();
        init_Post();
        list_comments = (ListView)findViewById(R.id.listView_comments);

        setupToolbar();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.reddit.com/r/").addConverterFactory(SimpleXmlConverterFactory.create()).build();

        FeedApi feed = retrofit.create(FeedApi.class);
        Call<Feed> call = feed.getfeed(currentFeedurl);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {

                //Log.d("MainActivity", "onresponse: server " + response.toString());

                List<Entry> entries = response.body().getEntries();

                for(int i=1;i<entries.size();i++){

                    String identifier = entries.get(i).getContent();
                    //System.out.println("rezz2_content " + identifier);

                    int g = identifier.indexOf("<div class=\"md\"><p>");
                    int g2 = ++g;
                    int h = identifier.indexOf("</p>",g2);

                    String[] splitter2 = entries.get(i).getContent().split("<div class=\"md\"><p>");

                    if(splitter2.length > 1) {

                        int finalSplitter = splitter2[1].indexOf("</p> </div><!-- SC_ON -->");
                        String current = "";

                        if(finalSplitter == -1){
                            current = splitter2[1];
                        }else {
                            current = splitter2[1].substring(0,finalSplitter);
                        }

                        try{

                            commentList.add(new Comment(entries.get(i).getAuthor().getName().toString(),
                                    current,entries.get(i).getId().toString(),entries.get(i).getUpdated().toString()));

                        }catch(IndexOutOfBoundsException e){
                            commentList.add(new Comment("error reading","none","none","none"));
                            Log.e("CommentsActivity","onResponse: IndexOutOfBoundsException"+e.getMessage());
                        }catch (NullPointerException e){
                            commentList.add(new Comment("error reading",
                                    "none",entries.get(i).getId().toString(),entries.get(i).getUpdated().toString()));

                            Log.e("CommentsActivity","onResponse: NullPointerException"+e.getMessage());
                        }

                        CommentsListAdapter adapter = new CommentsListAdapter(CommentsActivity.this,R.layout.comments_layout,commentList);
                        list_comments.setAdapter(adapter);


                        list_comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                getUserComment();
                            }
                        });


                        comment_ProgressBar.setVisibility(View.GONE);
                        comment_editText.setText("");

                    }else{
                        commentList.add(new Comment("error reading","none","none","none"));
                    }
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e("CommentsActivity", "onfailure: Unable to retrieve RSS: " + t.getMessage());
            }
        });
    }

    private void init_Post(){

        Intent receiver = getIntent();
        postURL = receiver.getStringExtra("post_url");
        postThumbnail = receiver.getStringExtra("post_thumbnail");
        postTitle = receiver.getStringExtra("post_title");
        postAuthor = receiver.getStringExtra("post_author");
        postDateUpdated = receiver.getStringExtra("post_updated");

        TextView title = (TextView)findViewById(R.id.postTitle);
        TextView author = (TextView)findViewById(R.id.postAuthor);
        TextView DateUpdated = (TextView)findViewById(R.id.postUpdated);
        ImageView thumbnail = (ImageView)findViewById(R.id.postThumbnail);
        Button reply = (Button) findViewById(R.id.replyButton);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar_comments);

        title.setText(postTitle);
        author.setText(postAuthor);
        DateUpdated.setText(postDateUpdated);

        displayImage(postThumbnail,thumbnail,progressBar);

        try{
            String[] splitter = postURL.split("https://www.reddit.com/r/");
            currentFeedurl = splitter[1];

        }catch(ArrayIndexOutOfBoundsException e){
            Log.e("CommentsActivity","ArrayIndexOutOfBoundsException: " + e.getMessage());
        }


        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getUserComment();
            }
        });


        //opening url in web view
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CommentsActivity.this,WebActivity.class);
                intent.putExtra("url",postURL);
                startActivity(intent);
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
                        Intent intent = new Intent(CommentsActivity.this,LoginActivity.class);
                        startActivity(intent);
                }


                return false;
            }
        });
    }

    private void getUserComment(){

        final Dialog dialog = new Dialog(CommentsActivity.this);
        dialog.setTitle("Dialog Box");
        dialog.setContentView(R.layout.comments_dialogbox);

        //it occupies only 75% of the width and height of the screen
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.75);

        dialog.getWindow().setLayout(width,height);
        dialog.show();

    }

    public void displayImage(String imageURL, ImageView imageView, final ProgressBar progressbar){

        //create the imageloader object
        ImageLoader imageLoader = ImageLoader.getInstance();

        //create display options
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        //download and display image from url
        imageLoader.displayImage(imageURL, imageView, options , new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressbar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressbar.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressbar.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressbar.setVisibility(View.GONE);
            }
        });

    }

    private void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                CommentsActivity.this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        defaultImage = CommentsActivity.this.getResources().getIdentifier("@drawable/no_image",null,CommentsActivity.this.getPackageName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return true;
    }
}
