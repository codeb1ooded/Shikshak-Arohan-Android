package com.igdtuw.technotwisters.sih_android.OtherFiles;

public class GPSService {
    public GPSService() {
    }


  /*  public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {

            AlarmManager alarmMgr;
            PendingIntent alarmIntent;

            alarmMgr = (AlarmManager) GPSService.this.getSystemService(Context.ALARM_SERVICE);
            Intent intent1 = new Intent(GPSService.this, NotificationReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(GPSService.this, 0, intent1, 0);
            gps.showSettingsAlert();
        }
        return flags;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }*/
}
