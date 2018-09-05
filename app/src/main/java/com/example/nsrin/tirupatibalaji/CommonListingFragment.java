package com.example.nsrin.tirupatibalaji;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CommonListingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String mParam1;
    public int mParam2=0;
    String articles[]={"php", "mysql","javascript","android","ionic","rightnow","c-sharp","html","css","interesting-facts"};
    public View fragment_view;
    ArrayList ImageList;
    ArrayList DescriptionList;
    ArrayList TitleList;
    ArrayList ArticleListID;

    private OnFragmentInteractionListener mListener;

    public CommonListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    class ImageDownloader extends AsyncTask <String, Void, Bitmap>{

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

        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            ImageList.add(result); //Adding the bitmap to the arrayList.
            mParam2++;

            if(mParam2==TitleList.size()) {
                CustomAdapter custom_adapter = new CustomAdapter();
                ListView listView = fragment_view.findViewById(R.id.article_list);
                listView.setAdapter(custom_adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getActivity(),"Item Clicked"+id,Toast.LENGTH_LONG).show();
                        // go to different intent.
                        Intent intent = new Intent(fragment_view.getContext(),ViewArticle.class);
                        //intent.putExtra("nid", id);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private class ArticlesFetcher extends  AsyncTask<String, String, String> { //Minimum Three parameters are required.

        //Just methods are required in AsyncTask, such a fantastic thing in java.

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
        protected void onPostExecute(String articles) {
            super.onPostExecute(articles);

            ImageList=new ArrayList<Bitmap>(); //This is going to be list of Bitmaps.
            DescriptionList=new ArrayList<String>();
            TitleList=new ArrayList<String>();
            ArticleListID=new ArrayList<Integer>();

            //Now build the title, image and description array.

            try {
                JSONArray reader=new JSONArray(articles);
                for(int i=0;i<reader.length();i++){
                    JSONObject elements=reader.getJSONObject(i);

                    JSONArray nid_array=elements.getJSONArray("nid");
                    JSONObject nid_value_object=nid_array.getJSONObject(0);
                    String nid_val=nid_value_object.getString("value");

                    ArticleListID.add(Integer.valueOf(nid_val));


                    JSONArray title_array=elements.getJSONArray("title");
                    JSONObject element_value_object=title_array.getJSONObject(0);
                    String element_val=element_value_object.getString("value");

                    TitleList.add(element_val);

                    JSONArray description_array=elements.getJSONArray("body");
                    JSONObject description_value_object=description_array.getJSONObject(0);
                    String description_val=description_value_object.getString("value");

                    DescriptionList.add(description_val);

                    JSONArray image_array=elements.getJSONArray("field_image");
                    JSONObject image_value_object=image_array.getJSONObject(0);
                    String image_val=image_value_object.getString("url");

                    //ImageList.add(image_val); //We have to create a bitMap result for this image.
                    new ImageDownloader().execute(image_val); //calling the asynctask to finish the stuff.


                }
            } catch (JSONException e) {

                e.printStackTrace();
            }

//            CustomAdapter custom_adapter=new CustomAdapter();
//            ListView listView = fragment_view.findViewById(R.id.article_list);
//            listView.setAdapter(custom_adapter);


        } //Post Execute ends here.
    }



    //This is an inner class to add our listview.
    class CustomAdapter extends  BaseAdapter{

        @Override
        public int getCount(){
            return TitleList.size();
        }

        @Override
        public Object getItem(int i){
            return null;
        }

        @Override
        public long getItemId(int position) {
            Integer nid= (Integer) ArticleListID.get(position);
            return nid;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //View view=getLayoutInflater().inflate(R.layout.custom_list_view,null);
            View view=convertView;
            if(view==null){
                view=getLayoutInflater().inflate(R.layout.custom_list_view,null);
            }

            ImageView list_view_img=view.findViewById(R.id.list_view_image);
            TextView list_view_title= view.findViewById(R.id.list_view_title);
            TextView list_view_description=view.findViewById(R.id.list_view_description);

            String list_title = (String) TitleList.get(position);
            String list_description = (String) DescriptionList.get(position);

            //#####################################################
            //Finally setting the listview with the content.#######
            //######################################################

            list_view_img.setImageBitmap((Bitmap)ImageList.get(position));
            list_view_title.setText(Html.fromHtml(list_title));
            list_view_description.setText(Html.fromHtml(list_description.substring(0, 50)).toString() + "...");


            return view;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_common_listing, container, false);
       this.fragment_view=view;
       new ArticlesFetcher().execute(getString(R.string.mobile_api)+"/php");
       return view;

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
} //Class ends here.


