package com.igdtuw.technotwisters.sih_android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.igdtuw.technotwisters.sih_android.api.ApiClient;
import com.igdtuw.technotwisters.sih_android.constants.SharedPreferencesStrings;
import com.igdtuw.technotwisters.sih_android.fragments.Dashboard_HomeFragment;
import com.igdtuw.technotwisters.sih_android.fragments.Dashboard_NotificationFragment;
import com.igdtuw.technotwisters.sih_android.fragments.Dashboard_SettingFragment;
import com.igdtuw.technotwisters.sih_android.fragments.Dashboard_ToDoFragment;
import com.igdtuw.technotwisters.sih_android.OtherFiles.CircleTransform;
import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.model.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, SharedPreferencesStrings {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;

    SharedPreferences sharedPreferences;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-dashboard_toolbar_menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav dashboard_toolbar_menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_TODO = "TODO";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    Call<Result> logoutUser;

    String username, accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(SP_USER_USERNAME, null);
        accessToken = sharedPreferences.getString(SP_USER_ACCESS_TOKEN, null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // load nav dashboard_toolbar_menu header data
        // loadNavHeader();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            Dashboard_HomeFragment homeFragment = new Dashboard_HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, homeFragment).commit();
        }
    }

    /***
     * Load navigation dashboard_toolbar_menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("Ravi Tamada");
        txtWebsite.setText("www.androidhive.info");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            Dashboard_HomeFragment homeFragment = new Dashboard_HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, homeFragment).commit();
        }
        else if(id == R.id.nav_today_attendance){

        }
        else if(id == R.id.nav_track_day){

        }
        else if(id == R.id.nav_track_week){

        }
        else if(id == R.id.nav_track_month){

        }
        else if(id == R.id.nav_track_year){

        }
        else if(id == R.id.nav_to_do){
            Dashboard_ToDoFragment toDoFragment = new Dashboard_ToDoFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, toDoFragment).commit();
        }
        else if(id == R.id.nav_notifications){
            Dashboard_NotificationFragment notificationFragment = new Dashboard_NotificationFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, notificationFragment).commit();
        }
        else if(id == R.id.nav_settings){
            Dashboard_SettingFragment settingFragment = new Dashboard_SettingFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, settingFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
            builder.setTitle ("Confirm");
            builder.setMessage ("Are you sure you want to logout?");
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_confirm_logout, null);
            builder.setView(v);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    logoutUser = ApiClient.getInterface().logoutUser(username, accessToken);
                    logoutUser.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            if (response.isSuccessful()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(SP_USER_TOKEN_GRANTED, false);
                                editor.commit();
                                editor.putString(SP_USER_ACCESS_TOKEN, "N/A");
                                editor.commit();
                                editor.putString(SP_USER_USERNAME, "N/A");
                                editor.commit();
                                editor.putString(SP_NAME, "N/A");
                                editor.commit();
                                Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                ActivityCompat.finishAffinity(DashboardActivity.this);
                            } else {
                                Toast.makeText(DashboardActivity.this, "Logout failed: "+response.errorBody(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Toast.makeText(DashboardActivity.this, "Logout failed: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}