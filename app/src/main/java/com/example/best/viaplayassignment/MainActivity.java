package com.example.best.viaplayassignment;

import android.arch.persistence.room.Room;
import android.os.StrictMode;
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
import com.example.best.viaplayassignment.model.ViaPlayResponse;
import com.example.best.viaplayassignment.model.ViaplaySection;
import com.example.best.viaplayassignment.remote.IViaPlayApi;
import com.example.best.viaplayassignment.roomDb.MyAppDatabase;
import com.example.best.viaplayassignment.roomDb.Sections;
import com.example.best.viaplayassignment.volly.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private IViaPlayApi iViaPlayApi;
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


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // initilize dialog indicator
        dialog = new SpotsDialog(MainActivity.this);
        dialog.setCancelable(false);

        // initialize recylerview
        recyclerView = findViewById(R.id.recyclerview);

        //setting the  gridlayout for recyclerview
        layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);


        //init room database
        myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "ViaPlay").allowMainThreadQueries().build();


        //   iViaPlayApi = Common.getSections();


        if (Common.isConnectedToInternet(MainActivity.this)) {

            viaplaySectionArrayList = new ArrayList<>();

            sendRequest();

        } else {


            Toast.makeText(this, "Offline Mode", Toast.LENGTH_SHORT).show();
            try {


                List<Sections> offLinelist = myAppDatabase.myDao().getSections();


                viaplaySectionArrayList = new ArrayList<>();

                for (int i = 0; i < offLinelist.size(); i++) {
                    ViaplaySection viaplaySection = new ViaplaySection();

                    viaplaySection.setId(offLinelist.get(i).getSectionId());
                    viaplaySection.setTitle(offLinelist.get(i).getSectionTitle());
                    viaplaySection.setHref(offLinelist.get(i).getSectionHref());
                    viaplaySection.setName(offLinelist.get(i).getSectionName());

                    viaplaySectionArrayList.add(viaplaySection);

                }

                SectionAdapter adapter = new SectionAdapter(this, viaplaySectionArrayList);
                recyclerView.setAdapter(adapter);


            } catch (Exception e) {
                e.getMessage();
            }
        }

    }

    private void sendRequest() {
        dialog.show();


        String url = "https://content.viaplay.se/androiddash-se";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dialog.dismiss();
                        Log.e("res", response);

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


                            SectionAdapter adapter = new SectionAdapter(MainActivity.this, viaplaySectionArrayList);
                            recyclerView.setAdapter(adapter);

                            myAppDatabase.myDao().clearSections();

                            for (int i = 0; i < viaplaySectionArrayList.size(); i++) {
                                Sections sections = new Sections();

                                sections.setSectionId(viaplaySectionArrayList.get(i).getId());
                                sections.setSectionTitle(viaplaySectionArrayList.get(i).getTitle());
                                sections.setSectionName(viaplaySectionArrayList.get(i).getName());
                                sections.setSectionHref(viaplaySectionArrayList.get(i).getHref());

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


        MySingleton.getInstance().addToReqQueue(postRequest);


    }


    public void sendRequestHref(final Sections sections, String url) {


        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        sections.setSectionHrefOffline(response);

                        myAppDatabase.myDao().addToSections(sections);


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


        MySingleton.getInstance().addToReqQueue(postRequest);

    }

    public void retroRequest() {
        iViaPlayApi.getViaPlayResponse().enqueue(new Callback<ViaPlayResponse>() {
            @Override
            public void onResponse(Call<ViaPlayResponse> call, Response<ViaPlayResponse> response) {
                ViaPlayResponse viaPlayResponse = response.body();

                dialog.dismiss();
            }


            @Override
            public void onFailure(Call<ViaPlayResponse> call, Throwable t) {

                dialog.dismiss();

                Log.e("error", t.toString());
            }
        });

    }


}
