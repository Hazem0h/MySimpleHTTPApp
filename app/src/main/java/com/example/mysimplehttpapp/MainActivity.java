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

    private ArrayList<EarthQuake> earthQuakeArrayList = new ArrayList<>();
    private MyAdapter rvAdapter; // is needed as global for postExecute method
    private String query = "https://earthquake.usgs.gov/fdsnws/event/1/query?f" +
            "ormat=geojson&starttime=2014-01-01&endtime=2014-01-02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        setUpRV(rv);

        //////////// The Async thread initialization
        EarthQuakeAsync earthQuakeAsync = new EarthQuakeAsync();
        earthQuakeAsync.execute(query);
    }

    private void setUpRV(RecyclerView rv){
        rvAdapter = new MyAdapter(earthQuakeArrayList, this);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onDestroy() {
        earthQuakeArrayList.clear();
        rvAdapter = null;
        super.onDestroy();
    }

    class EarthQuakeAsync extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urlString) {
            return Utils.fetchResponse(urlString[0]);
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            Utils.postExecuteCode(jsonResponse, earthQuakeArrayList, rvAdapter, MainActivity.this);
            super.onPostExecute(jsonResponse);
        }
    }
}