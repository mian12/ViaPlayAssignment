package com.example.best.viaplayassignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.best.viaplayassignment.Commmon.Common;
import com.example.best.viaplayassignment.MainActivity;
import com.example.best.viaplayassignment.R;
import com.example.best.viaplayassignment.model.ViaplaySection;
import com.example.best.viaplayassignment.roomDb.MyAppDatabase;
import com.example.best.viaplayassignment.roomDb.Sections;
import com.example.best.viaplayassignment.volly.MySingleton;
import com.example.best.viaplayassignment.volly.NavigateActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.MyViewHolder> {


    public Context context;
    List<ViaplaySection> viaplaySectionArrayList;



    public SectionAdapter(Context context, List<ViaplaySection> viaplaySectionArrayList) {
        this.context = context;
        this.viaplaySectionArrayList = viaplaySectionArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_section, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {

            final int position = i;

                myViewHolder.title.setText(viaplaySectionArrayList.get(i).getTitle());
                myViewHolder.name.setText(viaplaySectionArrayList.get(i).getName());

                myViewHolder.row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, NavigateActivity.class);
                        intent.putExtra("webUrl", viaplaySectionArrayList.get(position).getHref());
                        intent.putExtra("title", viaplaySectionArrayList.get(position).getTitle());
                        intent.putExtra("name", viaplaySectionArrayList.get(position).getName());
                        intent.putExtra("position", position+"");


                        context.startActivity(intent);
                    }
                });

        } catch (Exception e) {
            Log.e("err", e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return viaplaySectionArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, name;
        public View row;

        MyViewHolder(View view) {
            super(view);
            row = view;

            title = view.findViewById(R.id.title);
            name = view.findViewById(R.id.name);

        }
    }




}
