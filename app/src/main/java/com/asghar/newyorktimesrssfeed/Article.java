package com.asghar.newyorktimesrssfeed;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Article extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        News news = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            news = (News) extras.getSerializable(Global.NEWS_KEY);
            TextView title = findViewById(R.id.article_title);
            WebView webView = findViewById(R.id.article_webview);
            ProgressBar progressBar = findViewById(R.id.progressBar);

            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.setVisibility(webView.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progressBar.setVisibility(View.GONE);
                    view.setVisibility(WebView.VISIBLE);
                    super.onPageFinished(view, url);
                }
            });
            title.setText(news.getTitle());

            webView.loadUrl(news.getLink());

        }
    }
}