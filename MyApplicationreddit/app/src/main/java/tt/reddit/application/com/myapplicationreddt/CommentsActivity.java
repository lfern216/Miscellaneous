package tt.reddit.application.com.myapplicationreddt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        setupImageLoader();
        init_Post();
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
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar_post);

        title.setText(postTitle);
        author.setText(postAuthor);
        DateUpdated.setText(postDateUpdated);

        displayImage(postThumbnail,thumbnail,progressBar);

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
}
