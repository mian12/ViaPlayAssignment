package com.example.best.viaplayassignment.volly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.best.viaplayassignment.R;

public class NavigateActivity extends AppCompatActivity {

    public WebView webView;
    public TextView textViewTitle, textViewName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);


        String url = getIntent().getExtras().getString("webUrl");
        String title = getIntent().getExtras().getString("title");
        String name = getIntent().getExtras().getString("name");

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewName = findViewById(R.id.textViewName);

        webView = findViewById(R.id.webView);


        try {
            textViewTitle.setText(title);
            textViewName.setText(name);

            webView.loadUrl(url);

        } catch (Exception e) {
            e.getMessage();
        }

//        webView.javaScriptEnabled = true
//        webView.webChromeClient= WebChromeClient()
//        webView.webViewClient=object :WebViewClient(){
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                alertDailog.dismiss();
//            }
//
//        }


    }
}
