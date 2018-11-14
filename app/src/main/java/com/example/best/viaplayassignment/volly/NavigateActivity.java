package com.example.best.viaplayassignment.volly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.best.viaplayassignment.Commmon.Common;
import com.example.best.viaplayassignment.MainActivity;
import com.example.best.viaplayassignment.R;
import com.example.best.viaplayassignment.roomDb.MyAppDatabase;
import com.example.best.viaplayassignment.roomDb.Sections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NavigateActivity extends AppCompatActivity {

    public WebView webView;
    public TextView textViewTitle, textViewName, textViewOffLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);


        String url = getIntent().getExtras().getString("webUrl");
        String title = getIntent().getExtras().getString("title");
        String name = getIntent().getExtras().getString("name");

        String position = getIntent().getExtras().getString("position");


        textViewTitle = findViewById(R.id.textViewTitle);
        textViewName = findViewById(R.id.textViewName);
        textViewOffLine = findViewById(R.id.offlineTextView);

        webView = findViewById(R.id.webView);


        try {


            if (Common.isConnectedToInternet(NavigateActivity.this)) {
                try {
                    textViewTitle.setText(title);
                    textViewName.setText(name);

                    webView.loadUrl(url);


                } catch (Exception e) {
                    e.getMessage();
                }
            } else {

                textViewTitle.setText(title);
                textViewName.setText(name);

                List<Sections> offLinelist = MainActivity.myAppDatabase.myDao().getSections();
                String offlineMode = offLinelist.get(Integer.parseInt(position)).getSectionHrefOffline();
                textViewOffLine.setText(offlineMode);
            }

        } catch (Exception e) {
            e.getMessage();
        }


    }


    private void sendRequest(String url, final String secId) {


        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.e("res", response);

                        MainActivity.myAppDatabase.myDao().updateSection(response, secId);


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("error", error.getMessage() + "");
                Toast.makeText(NavigateActivity.this,
                        "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


        MySingleton.getInstance().addToReqQueue(postRequest);


    }
}
