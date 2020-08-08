package com.example.mysimplehttpapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<EarthQuake> earthQuakeArrayList = new ArrayList<>();
    MyAdapter rvAdapter; // is needed as global for postExecute method
    String query = "https://earthquake.usgs.gov/fdsnws/event/1/query?f" +
            "ormat=geojson&starttime=2014-01-01&endtime=2014-01-02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        rvAdapter = new MyAdapter(earthQuakeArrayList, this);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //////////// The Async thread initialization

        EarthQuakeAsync earthQuakeAsync = new EarthQuakeAsync();
        earthQuakeAsync.execute(query);
    }

    class EarthQuakeAsync extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urlString) {

            //1-get the URL object
            URL url;
            try {
                url = new URL(urlString[0]);
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

        @Override
        protected void onPostExecute(String jsonResponse) {

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
                Toast.makeText(MainActivity.this, "PARSing failed", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(jsonResponse);
        }
    }




    @Override
    protected void onDestroy() {
        earthQuakeArrayList.clear();
        super.onDestroy();
    }
}