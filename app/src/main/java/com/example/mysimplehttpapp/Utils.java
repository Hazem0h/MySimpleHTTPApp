package com.example.mysimplehttpapp;


import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//this class is used for making the code in the main class readable
//similar to utils.py in python to make the code polished
public class Utils {

    static void bindingLogic(EarthQuake earthQuake, MyAdapter.ViewHolder holder, Activity myActivity){

        //this is a utility method to bind data to the UI
        long date = earthQuake.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM DD, yyyy\n hh:mm a");
        String formattedDate = dateFormat.format(new Date(date));
        holder.dateTextView.setText(formattedDate);

        /////// String stuff
        String [] locationAndDistance = earthQuake.getDistance_loc().split("of ");
        if(locationAndDistance.length == 1){
            holder.locTextView.setText(locationAndDistance[0]);
            holder.distTetView.setText("");
            holder.distTetView.setVisibility(View.GONE);
        }else{
            holder.distTetView.setText(locationAndDistance[0]+"of");
            holder.locTextView.setText(locationAndDistance[1]);
        }

        //decimal stuff
        double mag = earthQuake.getMagnitude();
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
    }

    static String fetchResponse(String urlString){
        //1-get the URL object
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException exception) {
            Log.e("EXCEPTION URL", "String can't be converted to URL");
            return null;
        }

        //2-Attempt the HTTP request
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() != 200){
                return null;
            }
        } catch (IOException exception) {
            Log.e("EXCEPTION IOEXCEPTION", "while attempting to open connection");
            return null;
        }

        //3-Input stream and buffer reader
        try {
            InputStream inStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

            //4- Reading
            StringBuilder builder = new StringBuilder();
            String line = bufferedReader.readLine();
            while(line != null){
                builder.append(line);
                line = bufferedReader.readLine();
            }
            inStream.close();
            return builder.toString();

        } catch (IOException exception) {
            Log.e("EXCEPTION", "error while getting input stream from open connection");
            return null;
        }finally {
            connection.disconnect();
        }
    }

    static void postExecuteCode(String jsonResponse,
                                ArrayList<EarthQuake> earthQuakeArrayList,
                                MyAdapter rvAdapter,
                                Activity myActivity)
    {
        //we will parse the JSON into the eartchquake list
        JSONArray responseArray;
        try {
            JSONObject root = new JSONObject(jsonResponse);
            responseArray = root.getJSONArray("features");

            for (int i = 0; i<responseArray.length(); i++){
                JSONObject currentEQ = responseArray.getJSONObject(i).getJSONObject("properties");
                earthQuakeArrayList.add(new EarthQuake(
                        currentEQ.getString("place"),
                        currentEQ.getLong("time"),
                        currentEQ.getDouble("mag"),
                        currentEQ.getString("url")));
            }

            rvAdapter.notifyDataSetChanged();
            //to tell the rv that our data source was modified

        }catch (JSONException exception){
            Toast.makeText(myActivity, "PARSing failed", Toast.LENGTH_SHORT).show();
        }
    }
}
