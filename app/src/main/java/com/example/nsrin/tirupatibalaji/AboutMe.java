package com.example.nsrin.tirupatibalaji;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
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

public class AboutMe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
            }
        });

        new NodeAboutMe().execute(getString(R.string.server)+"/node/3/?_format=json");
    }

        //Start the sync task.

        class NodeAboutMe extends AsyncTask<String, String,String>{

            @Override
            protected String doInBackground(String... params) {

                HttpURLConnection httpURLConnection=null;
                try {
                    URL url = new URL(params[0]);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                    httpURLConnection.setRequestProperty("Accept", "*/*");
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer access_token= new StringBuffer();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        access_token.append(line);
                    }
                    return access_token.toString();
                } catch (MalformedURLException ex){
                    android.util.Log.d("logcat_error",ex.getMessage());
                    ex.printStackTrace();
                }
                catch (IOException ex){
                    android.util.Log.d("logcat_error",ex.toString());
                    ex.printStackTrace();
                }finally {
                    if(httpURLConnection !=null)
                        httpURLConnection.disconnect();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String article) {
                super.onPostExecute(article);
                //Now do your stuff.
                try {
                    JSONObject jo=new JSONObject(article);
                    JSONArray ja=jo.getJSONArray("body");
                    JSONObject ja_body_object=ja.getJSONObject(0);
                    String body=ja_body_object.getString("value");

                    WebView webView=findViewById(R.id.about_me_web);
                    webView.loadData(body,"text/html; charset=utf-8", "utf-8");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }//Asynctask ends here.



}//Class ends here.
