package com.igdtuw.technotwisters.sih_android.OtherFiles;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class P2PTracker {

    private ArrayList<String> mRecordedResults = new ArrayList<>();
    private int mWaitingTime = 120000;

    public P2PTracker() {

    }


    public P2PTracker(int waitingTime) {
        this.mWaitingTime = waitingTime;
    }

    public void validateAddresses(Context context, final List<String> addresses, final ScannerValidation scannerValidation) {
//        startScan(new Scanner() {
//            @Override
//            public void scanningComplete(List<String> res) {
//                boolean output = false;
//                for (String address : addresses) {
//                    if (res.contains(address)) {
//                        output = true;
//                        break;
//                    }
//                }
//                scannerValidation.validationComplete(output);
//            }
//        });
        startAllScan(context, new Scanner() {
            @Override
            public void scanningComplete(List<String> listOfAddress) {
                boolean output = false;
                for (String address : addresses) {
                    if (listOfAddress.contains(address)) {
                        output = true;
                        break;
                    }
                }
                scannerValidation.validationComplete(output);
            }
        });

    }

    public String getBluetoothName(String id) {
        BluetoothAdapter bla = BluetoothAdapter.getDefaultAdapter();
        if (!bla.isEnabled()) {
            bla.enable();
        }
        return bla.getRemoteDevice(id).getName();
    }

    public void startAllScan(final Context context, final Scanner sc) {
        BluetoothAdapter bla = BluetoothAdapter.getDefaultAdapter();
        if (!bla.isEnabled()) {
            bla.enable();
        }
        Toast.makeText(context, "Bluetooth Devices Fetching started!!", Toast.LENGTH_LONG).show();
        bla.startDiscovery();
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    if (!mRecordedResults.contains(device.getAddress())) {
                        System.out.println(device.getAddress());
                        mRecordedResults.add(device.getAddress());
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);

        new CountDownTimer(60000, 30000) {

            @Override
            public void onTick(long l) {
                System.out.println("On Tick");
            }

            @Override
            public void onFinish() {
                System.out.println("On Finish");
                context.unregisterReceiver(mReceiver);
                sc.scanningComplete(mRecordedResults);
            }
        }.start();
    }


    public void startScan(final Scanner sc) {
        BluetoothAdapter bla = BluetoothAdapter.getDefaultAdapter();
        if (!bla.isEnabled()) {
            bla.enable();
        }
        bla.startDiscovery();
        final BluetoothLeScanner bleScanner = bla.getBluetoothLeScanner();
        final ScanCallback scanCallback = new ScanCallback() {

            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                System.out.println(result.getDevice().getAddress());
                if (!mRecordedResults.contains(result.getDevice().getAddress())) {
                    mRecordedResults.add(result.getDevice().getAddress());
                }
            }
        };

        if (bleScanner != null) {
            bleScanner.startScan(scanCallback);
            new CountDownTimer(mWaitingTime, (long) (0.5 * mWaitingTime)) {

                @Override
                public void onTick(long l) {
                    System.out.println("On Tick");
                }

                @Override
                public void onFinish() {
                    System.out.println("On Finish");
                    bleScanner.stopScan(scanCallback);
                    sc.scanningComplete(mRecordedResults);
                }
            }.start();
        }
    }

    public static String getBluetoothAddress() {
        if (Build.VERSION_CODES.M > VERSION.SDK_INT) {
            BluetoothAdapter bla = BluetoothAdapter.getDefaultAdapter();
            if (!bla.isEnabled()) {
                bla.enable();
            }
            return bla.getAddress();
        } else {
            String output = "";
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        output = "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    output = res1.toString();
                }
            } catch (Exception ex) {
            }
            return output;
        }
    }

    public String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    public interface Scanner {
        void scanningComplete(List<String> listOfAddress);
    }

    public interface ScannerValidation {
        void validationComplete(boolean output);
    }

}
