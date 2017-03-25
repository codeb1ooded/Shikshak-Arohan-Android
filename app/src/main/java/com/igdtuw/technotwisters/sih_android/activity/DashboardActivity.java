package com.igdtuw.technotwisters.sih_android.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.igdtuw.technotwisters.sih_android.OtherFiles.GPSService;
import com.igdtuw.technotwisters.sih_android.OtherFiles.GPSTracker;
import com.igdtuw.technotwisters.sih_android.OtherFiles.NotificationReceiver;
import com.igdtuw.technotwisters.sih_android.api.ApiClient;
import com.igdtuw.technotwisters.sih_android.constants.SharedPreferencesStrings;
import com.igdtuw.technotwisters.sih_android.fragments.Dashboard_HomeFragment;
import com.igdtuw.technotwisters.sih_android.fragments.Dashboard_NotificationFragment;
import com.igdtuw.technotwisters.sih_android.fragments.Dashboard_SettingFragment;
import com.igdtuw.technotwisters.sih_android.fragments.Dashboard_ToDoFragment;
import com.igdtuw.technotwisters.sih_android.OtherFiles.CircleTransform;
import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.fragments.Dashboard_TrackFragment;
import com.igdtuw.technotwisters.sih_android.model.Result;

import java.util.Calendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferencesStrings {
    int mark;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-dashboard_toolbar_menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav dashboard_toolbar_menu item
    public static int navItemIndex = 0;

    Call<Result> logoutUser;

    String username, accessToken;
    boolean schoolAdded;

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
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString(SP_USER_USERNAME, null);
        accessToken = sharedPreferences.getString(SP_USER_ACCESS_TOKEN, null);
        schoolAdded = sharedPreferences.getBoolean(SP_SCHOOL_ADDED, false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //***********NOTIFICATION GENERATOR************
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager) DashboardActivity.this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(DashboardActivity.this, NotificationReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(DashboardActivity.this, 0, intent, 0);

// Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 12);
        calendar.set(Calendar.SECOND, 00);
        // Intent intent = new Intent(DashboardActivity.this, NotificationReceiver.class);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
        //*********END OF THE CODE************

       /* Intent intent1 = new Intent(DashboardActivity.this,NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DashboardActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) DashboardActivity.this.getSystemService(DashboardActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
*/

        //*************** For tracking location randomly
        Thread myThread = null;

        Runnable myRunnableThread = new CountDownRunner();
        myThread= new Thread(myRunnableThread);
        myThread.start();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            Dashboard_HomeFragment homeFragment = new Dashboard_HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, homeFragment).commit();
        }


    }
    public void doWork() {

        runOnUiThread(new Runnable() {
            public void run() {
                try{

                    Calendar c = Calendar.getInstance();
                    int seconds = c.get(Calendar.SECOND);
                    int minutes = c.get(Calendar.MINUTE);
                    int hours = c.get(Calendar.HOUR_OF_DAY);


                    String curTime = hours + ":" + minutes + ":" + seconds;

                    GPSTracker gps = new GPSTracker(DashboardActivity.this);



                }catch (Exception e) {}
            }

        });


    }


    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    long names[] = { 30 ,20 , 40, 60};
                    Random Dice = new Random();
                    int n = Dice.nextInt(names.length);

                    Thread.sleep(n*60*1000); // Pause of random tme in  Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
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
        if (id == R.id.nav_home) {
            Dashboard_HomeFragment homeFragment = new Dashboard_HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, homeFragment).commit();
        } else if (id == R.id.nav_today_attendance) {
            if (schoolAdded) {
                // TODO: first check if user is within the time period to mark attendance
                onCreateDialogSingleChoice().show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("You aren't allowed this action!");
                builder.setMessage("Click ok to add school first");
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_confirm_logout, null);
                builder.setView(v);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(DashboardActivity.this, AddSchoolActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        } else if (id == R.id.nav_track_attendance) {
            if (schoolAdded) {
                Dashboard_TrackFragment trackFragment = new Dashboard_TrackFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, trackFragment).commit();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("You aren't allowed this action!");
                builder.setMessage("Click ok to add school first");
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_confirm_logout, null);
                builder.setView(v);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(DashboardActivity.this, AddSchoolActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        } else if (id == R.id.nav_to_do) {
            Dashboard_ToDoFragment toDoFragment = new Dashboard_ToDoFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, toDoFragment).commit();
        } else if (id == R.id.nav_notifications) {
            Dashboard_NotificationFragment notificationFragment = new Dashboard_NotificationFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, notificationFragment).commit();
        } else if (id == R.id.nav_settings) {
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
        if(id==R.id.action_profile){
            Intent i = new Intent();
            i.setClass(DashboardActivity.this, ProfileChangeActivity.class);
            startActivity(i);
        }

        else if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure you want to logout?");
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
                                Toast.makeText(DashboardActivity.this, "Logout failed: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Toast.makeText(DashboardActivity.this, "Logout failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    public Dialog onCreateDialogSingleChoice() {
        mark = 1;
        String present = "Present";
        String absent = "Absent";
        String holiday = "Holiday";
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        CharSequence[] array = {present, absent, holiday};
        builder.setTitle("Mark your Attendance");

        builder.setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {     // present
                    mark = 0;
                    /*LocationManager locationManager = (LocationManager) DashboardActivity.this.getSystemService(Context.LOCATION_SERVICE);

                    LocationListener locationListener = new LocationListener() {
                        public void onLocationChanged(Location location) {
                            location.getLatitude();
                            location.getLongitude();
                            location.getAccuracy();
                            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                            Date date = new Date();
                            Log.i("DashboardActivity", sdf.format(date));
                        }

                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        public void onProviderEnabled(String provider) {
                        }

                        public void onProviderDisabled(String provider) {
                        }
                    };
                    if (ActivityCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);*/
                } else if (which == 1) {
                    // absent
                    mark = 1;
                } else {           // holiday
                    mark = 2;
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mark == 0) {

                    GPSTracker gps = new GPSTracker(DashboardActivity.this);
                    if (gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }


                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }

    public void startService(View view) {
        startService(new Intent(getBaseContext(), GPSService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), GPSService.class));
    }

}
