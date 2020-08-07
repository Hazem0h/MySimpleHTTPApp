package com.example.mysimplehttpapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
        rvAdapter = new MyAdapter();
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
                            currentEQ.getDouble("mag")));
                }

                rvAdapter.notifyDataSetChanged();
                //to tell the rv that our data source was modified

            }catch (JSONException exception){
                Toast.makeText(MainActivity.this, "PARSing failed", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(jsonResponse);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<ViewHolder>{
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.list_item,parent,false),
                    MainActivity.this);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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
            gradientDrawable.setColor(getResources().getColor(color));
        }

        @Override
        public int getItemCount() {
            return earthQuakeArrayList.size();
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTextView;
        TextView magTextView;
        TextView distTetView;
        TextView locTextView;
        Context myContext;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.date_text_view);
            magTextView = (TextView) itemView.findViewById(R.id.magnitude_text_view);
            distTetView = (TextView) itemView.findViewById(R.id.distance_text_view);
            locTextView = (TextView) itemView.findViewById(R.id.location_text_view);
            myContext = context;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(myContext, "Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}