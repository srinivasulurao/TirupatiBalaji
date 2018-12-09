package com.example.nsrin.tirupatibalaji;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ViewArticle extends AppCompatActivity {

    public String nid;
    ProgressBar pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        //Toolbar title will be set after fetching the node body.
        Intent intent = getIntent();
        this.nid = intent.getExtras().getString("nid");
        new NodeFetcher().execute("http://srinivasulurao.com/node/"+nid+"/?_format=json");
    }

    class node_image_downloader extends  AsyncTask<String,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(String... params) {
            String imageURL = params[0];
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                android.util.Log.d("logcat_error",e.getMessage());
                e.printStackTrace();
            }
            return bitmap;

        }

        protected void onPostExecute(Bitmap result){
            super.onPostExecute(result);
            //ImageView article_img=findViewById(R.id.article_image);
            //article_img.setImageBitmap(result);
        }
    }

    class NodeFetcher extends AsyncTask<String,String,String>{


        protected void preExecute(){

        }

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

            try{
                JSONObject reader=new JSONObject(article);

                JSONArray na=reader.getJSONArray("field_image");
                JSONObject nvd=na.getJSONObject(0);
                String ai=nvd.getString("url");

                JSONArray nid_array=reader.getJSONArray("body");
                JSONObject nid_value_object=nid_array.getJSONObject(0);
                String body="<img style='width:100% !important;' src='"+ai.toString()+"'>"+nid_value_object.getString("value");

                WebView wv=findViewById(R.id.article_body);
                wv.loadData(body,"text/html; charset=utf-8", "utf-8");

               //new node_image_downloader().execute(ai);

               JSONArray na2=reader.getJSONArray("title");
                JSONObject nvd2=na2.getJSONObject(0);
                String article_title=nvd2.getString("value");

                Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(mActionBarToolbar);
                getSupportActionBar().setTitle(article_title);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);

                mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        finish();
                    }
                });
                

            }
            catch (Exception e){

            }

        }
    } //Async task ends here.
}
