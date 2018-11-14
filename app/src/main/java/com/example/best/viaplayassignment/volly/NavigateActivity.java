package com.example.best.viaplayassignment.volly;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.best.viaplayassignment.Commmon.Common;
import com.example.best.viaplayassignment.MainActivity;
import com.example.best.viaplayassignment.R;
import com.example.best.viaplayassignment.roomDb.Sections;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import dmax.dialog.SpotsDialog;

public class NavigateActivity extends AppCompatActivity {

    // public WebView webView;
    public TextView textViewTitle, textViewName, textViewOffLine;
    public TextView textViewTitle2, textViewDescription;

    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);


        // getting intent from section adapter
        String url = getIntent().getExtras().getString("webUrl");
        String title1 = getIntent().getExtras().getString("title1");
        String name = getIntent().getExtras().getString("name");
        String position = getIntent().getExtras().getString("position");

        // set title and back button on toolbar
        getSupportActionBar().setTitle(title1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // initilize  progress dialog indicator
        dialog = new SpotsDialog(NavigateActivity.this);
        dialog.setCancelable(false);

        // intialize xml views
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewName = findViewById(R.id.textViewName);
        textViewTitle2 = findViewById(R.id.title2);
        textViewDescription = findViewById(R.id.description);

        try {


            // check net is connected or not
            if (Common.isConnectedToInternet(NavigateActivity.this)) {
                try {
                    textViewTitle.setText(title1);
                    textViewName.setText(name);

                    //getting title and description against url
                    sendRequest(url);


                } catch (Exception e) {
                    e.getMessage();
                }
            } else {

                textViewTitle.setText(title1);
                textViewName.setText(name);


                // getting list from room databse
                List<Sections> offLinelist = MainActivity.myAppDatabase.myDao().getSections();

                String offlineMode = offLinelist.get(Integer.parseInt(position)).getSectionHrefOffline();
                String title2 = offLinelist.get(Integer.parseInt(position)).getTitle();
                String description = offLinelist.get(Integer.parseInt(position)).getDescription();

                textViewTitle2.setText(title2);
                textViewDescription.setText(description);

                textViewOffLine.setText(offlineMode);
            }

        } catch (Exception e) {
            e.getMessage();
        }


    }


    public void sendRequest(String url) {

        dialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String title = jsonObject.getString("title");
                            String description = jsonObject.getString("description");

                            textViewTitle2.setText(title);
                            textViewDescription.setText(description);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.e("error", error.getMessage() + "");
                Toast.makeText(NavigateActivity.this,
                        "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        // add string request into queue
        MySingleton.getInstance().addToReqQueue(stringRequest);

    }
}
