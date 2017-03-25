package com.igdtuw.technotwisters.sih_android.activity;


import android.content.Intent;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.fragments.Dashboard_HomeFragment;
import com.igdtuw.technotwisters.sih_android.fragments.Main_AboutUs_Fragment;
import com.igdtuw.technotwisters.sih_android.fragments.Main_Faqs_Fragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button login;
    TextView t_photo,t_rti,t_feedback,t_sitemap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        t_photo=(TextView)findViewById(R.id.photo_gallery);
        t_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent();
                i.setClass(MainActivity.this,PhotoGalleryActivity.class);
                startActivity(i);
            }
        });
        t_rti=(TextView)findViewById(R.id.rti);
        t_rti.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent();
                i.setClass(MainActivity.this,RtiActivity.class);
                startActivity(i);
            }
        });
        t_feedback=(TextView)findViewById(R.id.feedback);
        t_feedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent();
                i.setClass(MainActivity.this,FeedbackActivity.class);
                startActivity(i);
            }
        });
        t_sitemap=(TextView)findViewById(R.id.sitemap);
        t_sitemap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent();
                i.setClass(MainActivity.this,SitemapActivity.class);
                startActivity(i);
            }
        });
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the dashboard_toolbar_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

            Intent i = new Intent();
            i.setClass(MainActivity.this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.about_us) {
            Dashboard_HomeFragment about1Fragment = new Dashboard_HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, about1Fragment).commit();

        } else if (id == R.id.faqs) {
            Main_Faqs_Fragment aboutFragment = new Main_Faqs_Fragment();
             getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, aboutFragment).commit();

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
