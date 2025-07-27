package com.wifimapper.app.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.wifimapper.app.database.AppDatabase;
import com.wifimapper.app.models.WifiNetwork;

import java.util.List;

public class WiFiScanService extends Service {
    private static final String TAG = "WiFiScanService";
    private WifiManager wifiManager;
    private AppDatabase database;
    private boolean isScanning = false;
    
    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                processScanResults();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        database = AppDatabase.getInstance(this);
        
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startScanning();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiScanReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startScanning() {
        if (!isScanning) {
            isScanning = true;
            new Thread(() -> {
                while (isScanning) {
                    wifiManager.startScan();
                    try {
                        Thread.sleep(10000); // Scan every 10 seconds
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }).start();
        }
    }

    private void processScanResults() {
        List<ScanResult> results = wifiManager.getScanResults();
        
        new Thread(() -> {
            for (ScanResult result : results) {
                WifiNetwork existing = database.wifiNetworkDao().getNetworkByBssid(result.BSSID);
                
                if (existing != null) {
                    existing.setSignalStrength(result.level);
                    existing.incrementDetectionCount();
                    existing.setKnown(true);
                    database.wifiNetworkDao().update(existing);
                } else {
                    WifiNetwork network = new WifiNetwork(
                            result.SSID,
                            result.BSSID,
                            result.level,
                            result.frequency,
                            result.capabilities,
                            0.0, // Latitude will be updated by LocationService
                            0.0  // Longitude will be updated by LocationService
                    );
                    database.wifiNetworkDao().insert(network);
                }
            }
        }).start();
    }
}
