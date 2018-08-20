package com.example.nsrin.tirupatibalaji;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;


public class CommonListingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String mParam1;
    public String mParam2;
    String articles[]={"PHP", "mysql","javascript","android","ionic","rightnow","c-sharp","html","css","interesting-facts"};


    private OnFragmentInteractionListener mListener;

    public CommonListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //This is an inner class to add our listview.
    class CustomAdapter extends  BaseAdapter{

        @Override
        public int getCount(){
            return articles.length;
        }

        @Override
        public Object getItem(int i){
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view=getLayoutInflater().inflate(R.layout.custom_list_view,null);

            ImageView list_view_img=view.findViewById(R.id.list_view_image);
            TextView list_view_title= view.findViewById(R.id.list_view_title);
            TextView list_view_description=view.findViewById(R.id.list_view_description);

            list_view_title.setText(articles[position]);
            list_view_description.setText(articles[position]);

            return view;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_common_listing, container, false);
//      ArrayAdapter ListViewAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,articles);
        CustomAdapter custom_adapter=new CustomAdapter();
        ListView listView = (ListView) view.findViewById(R.id.article_list);
        listView.setAdapter(custom_adapter);

        return view;

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
} //Class ends here.


