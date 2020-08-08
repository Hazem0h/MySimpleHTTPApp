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

        //// Handling date
        long date = earthQuakeArrayList.get(position).getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM DD, yyyy\n hh:mm a");
        String formattedDate = dateFormat.format(new Date(date));
        holder.dateTextView.setText(formattedDate);

        /////// String stuff
        String [] locationAndDistance = earthQuakeArrayList.get(position).getDistance_loc().split("of ");
        if(locationAndDistance.length == 1){
            holder.locTextView.setText(locationAndDistance[0]);
            holder.distTetView.setText("");
            holder.distTetView.setVisibility(View.GONE);
        }else{
            holder.distTetView.setText(locationAndDistance[0]+"of");
            holder.locTextView.setText(locationAndDistance[1]);
        }

        //decimal stuff
        double mag = earthQuakeArrayList.get(position).getMagnitude();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String formattedMag = decimalFormat.format(mag);
        holder.magTextView.setText(formattedMag);

        ///color stuff
        GradientDrawable gradientDrawable = (GradientDrawable) holder.magTextView.getBackground();
        int color;
        switch((int) mag){
            case 1:
                color = R.color.magnitude1;
                break;
            case 2:
                color = R.color.magnitude2;
                break;
            case 3:
                color = R.color.magnitude3;
                break;
            case 4:
                color = R.color.magnitude4;
                break;
            case 5:
                color = R.color.magnitude5;
                break;
            case 6:
                color = R.color.magnitude6;
                break;
            case 7:
                color = R.color.magnitude7;
                break;
            case 8:
                color = R.color.magnitude8;
                break;
            case 9:
                color = R.color.magnitude9;
                break;
            case 10:
                color = R.color.magnitude10plus;
                break;
            default:
                color = R.color.magnitude10plus;
        }
        gradientDrawable.setColor(myActivity.getResources().getColor(color));

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
