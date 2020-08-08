package com.example.mysimplehttpapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<EarthQuake> earthQuakeArrayList;
    private Activity myActivity;

    public MyAdapter(ArrayList<EarthQuake> data, Activity hostActivity){
        earthQuakeArrayList = data;
        myActivity = hostActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(myActivity.getLayoutInflater().inflate(
                R.layout.list_item, parent, false)
                , myActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        //1-Binding the data to the UI
        EarthQuake earthQuake = earthQuakeArrayList.get(position);
        Utils.bindingLogic(earthQuake, holder, myActivity);

        /////The click listener
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri earthquakeUri = Uri.parse(earthQuakeArrayList.get(position).getEarthquakeSpecificURL());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                myActivity.startActivity(websiteIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return earthQuakeArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTextView;
        TextView magTextView;
        TextView distTetView;
        TextView locTextView;
        FrameLayout frameLayout;
        Context myContext;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.date_text_view);
            magTextView = (TextView) itemView.findViewById(R.id.magnitude_text_view);
            distTetView = (TextView) itemView.findViewById(R.id.distance_text_view);
            locTextView = (TextView) itemView.findViewById(R.id.location_text_view);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frame_view);
            myContext = context;
        }
    }
}
