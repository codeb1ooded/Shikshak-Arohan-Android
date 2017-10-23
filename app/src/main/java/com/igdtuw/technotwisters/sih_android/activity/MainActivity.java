package com.igdtuw.technotwisters.sih_android.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.activity.static_activities.AboutUs;
import com.igdtuw.technotwisters.sih_android.activity.static_activities.FeedbackActivity;
import com.igdtuw.technotwisters.sih_android.activity.static_activities.PhotoGalleryActivity;
import com.igdtuw.technotwisters.sih_android.activity.static_activities.PrivacyPolicy;
import com.igdtuw.technotwisters.sih_android.activity.static_activities.RtiActivity;
import com.igdtuw.technotwisters.sih_android.activity.static_activities.SitemapActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button login;
    TextView t_photo,t_rti,t_feedback,t_sitemap;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        initViews();
        initClickListener();

    }

    private void initViews(){
        login = (Button)findViewById(R.id.login);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        t_photo=(TextView)findViewById(R.id.photo_gallery);
        t_rti=(TextView)findViewById(R.id.rti);
        t_feedback=(TextView)findViewById(R.id.feedback);
        t_sitemap=(TextView)findViewById(R.id.sitemap);
    }

    private void initClickListener(){
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        t_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent();
                i.setClass(MainActivity.this,PhotoGalleryActivity.class);
                startActivity(i);
            }
        });
        t_rti.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent();
                i.setClass(MainActivity.this,RtiActivity.class);
                startActivity(i);
            }
        });
        t_feedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent();
                i.setClass(MainActivity.this,FeedbackActivity.class);
                startActivity(i);
            }
        });
        t_sitemap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent();
                i.setClass(MainActivity.this,SitemapActivity.class);
                startActivity(i);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            /*Intent i = new Intent();
            i.setClass(MainActivity.this, MainActivity.class);
            startActivity(i);*/
        } else if (id == R.id.about_us) {
            Intent i = new Intent();
            i.setClass(MainActivity.this, AboutUs.class);
            startActivity(i);
        } else if (id == R.id.faqs) {

        }  else if (id == R.id.privacy) {
            Intent i = new Intent();
            i.setClass(MainActivity.this, PrivacyPolicy.class);
            startActivity(i);
        } else if (id == R.id.contact_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
      
        return true;
    }

}
