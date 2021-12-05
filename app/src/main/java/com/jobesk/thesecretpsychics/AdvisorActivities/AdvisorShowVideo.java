package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.R;

public class
AdvisorShowVideo extends AppCompatActivity {
    private ImageView back_img;
    private TextView toolbar_title;
    private WebView webview;
    private String targeturl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_show_video);


        Bundle extras = getIntent().getExtras();
        targeturl=  extras.getString("videoLink");




        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);
//        toolbar_title.setText(get);


        webview = (WebView) findViewById(R.id.webview01);
        webview.setWebViewClient(new myWebClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(targeturl);

    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            view.loadUrl(url);
            return true;

        }
    }

}
