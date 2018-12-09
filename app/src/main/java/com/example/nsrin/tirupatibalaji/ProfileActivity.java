package com.example.nsrin.tirupatibalaji;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity implements CommonListingFragment.OnFragmentInteractionListener{

    private DrawerLayout pdl;
    private ActionBarDrawerToggle drawerToggle;
    FragmentStatePagerAdapter pager_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pdl=findViewById(R.id.profile_drawer_layout);
        drawerToggle=new ActionBarDrawerToggle(this,pdl,R.string.open,R.string.close); //Four parameters to be added.
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //#################################################
        //Do something when  Navigation item is selected
        //#################################################
        NavigationView navigationView=findViewById(R.id.navigation_drawer_container);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ndmi=item.getItemId();
                if(ndmi==R.id.logout){
                    Intent il=new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(il);
                }
                if(ndmi==R.id.contact_me){
                    Intent ic=new Intent(ProfileActivity.this, ContactMe.class);
                    startActivity(ic);
                }

                if(ndmi==R.id.about_me){
                    Intent iam=new Intent(ProfileActivity.this,AboutMe.class);
                    startActivity(iam);
                }
                return false;
            }
        });
        //##################################################
        //Now do the setting for tabs.
        //##################################################
        TabLayout tab_layout=findViewById(R.id.tab_layout);

        tab_layout.addTab(tab_layout.newTab().setText("PHP"));
        tab_layout.addTab(tab_layout.newTab().setText("MySQL"));
        tab_layout.addTab(tab_layout.newTab().setText("JavaScript"));
        tab_layout.addTab(tab_layout.newTab().setText("Android"));
        tab_layout.addTab(tab_layout.newTab().setText("Ionic"));
        tab_layout.addTab(tab_layout.newTab().setText("RightNow"));
        tab_layout.addTab(tab_layout.newTab().setText("C#"));
        tab_layout.addTab(tab_layout.newTab().setText("HTML"));
        tab_layout.addTab(tab_layout.newTab().setText("CSS"));
        tab_layout.addTab(tab_layout.newTab().setText("Interesting Facts"));
        tab_layout.setTabGravity(TabLayout.MODE_SCROLLABLE);

        ViewPager view_pager= findViewById(R.id.tab_pager);
        pager_adapter= new com.example.nsrin.tirupatibalaji.PagerAdapter(getSupportFragmentManager(),tab_layout.getTabCount());
        view_pager.setAdapter(pager_adapter);

        view_pager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewPager view_pager= findViewById(R.id.tab_pager);
                view_pager.setCurrentItem(tab.getPosition());  //Fantastic, this works like a charm, thanks god.
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
