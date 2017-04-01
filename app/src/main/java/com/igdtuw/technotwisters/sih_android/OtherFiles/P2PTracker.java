package com.igdtuw.technotwisters.sih_android.OtherFiles;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class P2PTracker {

    private HashMap<String, ScanResult> mRecordedResults = new HashMap<>();

    public void validateAddresses(final List<String> addresses, final ScannerValidation scannerValidation) {
        startScan(new Scanner() {
            @Override
            public void scanningComplete() {
                boolean output = false;
                List<String> res = new ArrayList<>();
                for (String key : mRecordedResults.keySet()) {
                    res.add(key);
                }
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
        BluetoothAdapter bla = BluetoothAdapter.getDefaultAdapter();
        if (!bla.isEnabled()) {
            bla.enable();
        }
        System.out.println(bla.getAddress());
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

            new CountDownTimer(60 * 1000, 30 * 1000) {

                @Override
                public void onTick(long l) {
                    System.out.println("On Tick");
                }

                @Override
                public void onFinish() {
                    System.out.println("On Finish");
                    bleScanner.stopScan(scanCallback);
                    for (String key : mRecordedResults.keySet()) {
                        System.out.println(mRecordedResults.get(key).toString());
                    }
                    sc.scanningComplete();
                }
            }.start();
        }
    }

    public static String getBluetoothAddress() {
        BluetoothAdapter bla = BluetoothAdapter.getDefaultAdapter();
        if (!bla.isEnabled()) {
            bla.enable();
        }
        return bla.getAddress();
    }

    public interface Scanner {
        void scanningComplete();
    }

    public interface ScannerValidation {
        void validationComplete(boolean output);
    }

}
