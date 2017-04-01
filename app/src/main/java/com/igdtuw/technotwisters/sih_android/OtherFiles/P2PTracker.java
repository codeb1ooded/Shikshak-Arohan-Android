package com.igdtuw.technotwisters.sih_android.OtherFiles;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.CountDownTimer;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class P2PTracker {

    private HashMap<String, ScanResult> mRecordedResults = new HashMap<>();
    private int mWaitingTime = 60000;

    public P2PTracker() {

    }


    public P2PTracker(int waitingTime) {
        this.mWaitingTime = waitingTime;
    }

    public void validateAddresses(final List<String> addresses, final ScannerValidation scannerValidation) {
        startScan(new Scanner() {
            @Override
            public void scanningComplete(List<String> res) {
                boolean output = false;
                for (String address : addresses) {
                    if (res.contains(address)) {
                        output = true;
                        break;
                    }
                }
                scannerValidation.validationComplete(output);
            }
        });

    }

    public void startScan(final Scanner sc) {
        System.out.println(getBluetoothAddress());
        BluetoothAdapter bla = BluetoothAdapter.getDefaultAdapter();
        if (!bla.isEnabled()) {
            bla.enable();
        }
        final BluetoothLeScanner bleScanner = bla.getBluetoothLeScanner();
        final ScanCallback scanCallback = new ScanCallback() {

            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                if (!mRecordedResults.keySet().contains(result.getDevice().getAddress())) {
                    System.out.println("On Finish");
                    mRecordedResults.put(result.getDevice().getAddress(), result);
                }
            }
        };

        if (bleScanner != null) {
            bleScanner.startScan(scanCallback);
            new CountDownTimer(mWaitingTime, (int) (0.5 * mWaitingTime)) {

                @Override
                public void onTick(long l) {
                    System.out.println("On Tick");
                }

                @Override
                public void onFinish() {
                    System.out.println("On Finish");
                    bleScanner.stopScan(scanCallback);
                    List<String> addresses = new ArrayList<String>();
                    for (String key : mRecordedResults.keySet()) {
                        System.out.println(mRecordedResults.get(key).toString());
                        addresses.add(key);
                    }
                    sc.scanningComplete(addresses);
                }
            }.start();
        }
    }

    public static String getBluetoothAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public interface Scanner {
        void scanningComplete(List<String> listOfAddress);
    }

    public interface ScannerValidation {
        void validationComplete(boolean output);
    }

}
