package tt.reddit.application.com.myapplicationreddt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        final TextView textV = (TextView)findViewById(R.id.webview_textview);
        WebView webV = (WebView) findViewById(R.id.webview);
        final ProgressBar progressB = (ProgressBar) findViewById(R.id.webview_ProgressBar);

        Intent i = getIntent();
        String url = i.getStringExtra("url");
        webV.getSettings().setJavaScriptEnabled(true);
        webV.loadUrl(url);

        //hide the progress bar once the web page is viewing
        webV.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                progressB.setVisibility(View.GONE);
                textV.setText("");
            }
        });


    }
}
