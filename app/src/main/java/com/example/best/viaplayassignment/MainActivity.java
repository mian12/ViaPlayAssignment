package com.example.best.viaplayassignment;

import android.arch.persistence.room.Room;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.best.viaplayassignment.Commmon.Common;
import com.example.best.viaplayassignment.adapter.SectionAdapter;
import com.example.best.viaplayassignment.model.ViaplaySection;
import com.example.best.viaplayassignment.roomDb.MyAppDatabase;
import com.example.best.viaplayassignment.roomDb.Sections;
import com.example.best.viaplayassignment.volly.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SpotsDialog dialog;
    public SwipeRefreshLayout swipeRefreshLayout;
    List<ViaplaySection> viaplaySectionArrayList = null;
    public static MyAppDatabase myAppDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // initilize  progress dialog indicator
        dialog = new SpotsDialog(MainActivity.this);
        dialog.setCancelable(false);

        // initialize recylerview
        recyclerView = findViewById(R.id.recyclerview);

        //setting the  gridlayout for recyclerview
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);


        //init room database
        myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "ViaPlay")
                .allowMainThreadQueries().build();


        // pull to refresh initialize view
        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Common.isConnectedToInternet(MainActivity.this)) {

                    // create list
                    viaplaySectionArrayList = new ArrayList<>();
                    // fecthing the data from server
                    sendRequest();
                    swipeRefreshLayout.setRefreshing(false);

                } else {

                    // Toast.makeText(MainActivity.this, "Offline Mode", Toast.LENGTH_SHORT).show();
                    offlineMode();
                    swipeRefreshLayout.setRefreshing(false);
                }


            }
        });


        // check net is connected or not
        if (Common.isConnectedToInternet(MainActivity.this)) {

            //create list
            viaplaySectionArrayList = new ArrayList<>();
            //fecthing the data from server
            sendRequest();

        } else {
            //Toast.makeText(this, "Offline Mode", Toast.LENGTH_SHORT).show();
            offlineMode();

        }

    }


    public void offlineMode() {

        try {

            // getting the data from local database
            List<Sections> offLinelist = myAppDatabase.myDao().getSections();

            viaplaySectionArrayList = new ArrayList<>();
            //now inserted local data into List
            for (int i = 0; i < offLinelist.size(); i++) {
                ViaplaySection viaplaySection = new ViaplaySection();

                viaplaySection.setId(offLinelist.get(i).getSectionId());
                viaplaySection.setTitle(offLinelist.get(i).getSectionTitle());
                viaplaySection.setHref(offLinelist.get(i).getSectionHref());
                viaplaySection.setName(offLinelist.get(i).getSectionName());

                viaplaySectionArrayList.add(viaplaySection);

            }
            // now call the adapter to show data on recyclerview
            SectionAdapter adapter = new SectionAdapter(this, viaplaySectionArrayList);
            recyclerView.setAdapter(adapter);


        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void sendRequest() {
        dialog.show();


        String url = "https://content.viaplay.se/androiddash-se";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject linksJson = jsonObject.getJSONObject("_links");
                            JSONArray jsonArray = linksJson.getJSONArray("viaplay:sections");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject innerJson = jsonArray.getJSONObject(i);
                                ViaplaySection viaplaySection = new ViaplaySection();
                                String string = innerJson.getString("href");
                                String[] href = string.split("\\{");
                                viaplaySection.setId(innerJson.getString("id"));
                                viaplaySection.setTitle(innerJson.getString("title"));
                                viaplaySection.setHref(href[0]);
                                viaplaySection.setType(innerJson.getString("type"));
                                viaplaySection.setName(innerJson.getString("name"));
                                viaplaySection.setTemplated(innerJson.getBoolean("templated"));

                                viaplaySectionArrayList.add(viaplaySection);


                            }


                            // now call the adapter to show data on recyclerview
                            SectionAdapter adapter = new SectionAdapter(MainActivity.this, viaplaySectionArrayList);
                            recyclerView.setAdapter(adapter);

                            // clear the local database so that we can prevent from duplication
                            myAppDatabase.myDao().clearSections();

                            for (int i = 0; i < viaplaySectionArrayList.size(); i++) {
                                Sections sections = new Sections();

                                sections.setSectionId(viaplaySectionArrayList.get(i).getId());
                                sections.setSectionTitle(viaplaySectionArrayList.get(i).getTitle());
                                sections.setSectionName(viaplaySectionArrayList.get(i).getName());
                                sections.setSectionHref(viaplaySectionArrayList.get(i).getHref());

                                //call  another reqeuest to fetch the title and discription
                                sendRequestHref(sections, viaplaySectionArrayList.get(i).getHref());


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.e("error", error.getMessage() + "");
                Toast.makeText(MainActivity.this,
                        "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        // add string request into queue
        MySingleton.getInstance().addToReqQueue(stringRequest);


    }


    public void sendRequestHref(final Sections sections, String url) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String title = jsonObject.getString("title");
                            String description = jsonObject.getString("description");

                            sections.setSectionHrefOffline(response);
                            sections.setTitle(title);
                            sections.setDescription(description);

                            //save into room database
                            myAppDatabase.myDao().addToSections(sections);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.e("error", error.getMessage() + "");
                Toast.makeText(MainActivity.this,
                        "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        // add string request into queue
        MySingleton.getInstance().addToReqQueue(stringRequest);

    }


}
