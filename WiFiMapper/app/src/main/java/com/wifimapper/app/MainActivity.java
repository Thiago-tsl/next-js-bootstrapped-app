package com.wifimapper.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.wifimapper.app.services.WiFiScanService;
import com.wifimapper.app.services.LocationService;
import com.wifimapper.app.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private MainViewModel viewModel;
    
    private TextView totalNetworksText;
    private TextView knownNetworksText;
    private TextView newNetworksText;
    private Button startScanButton;
    private Button viewMapButton;
    private Button viewListButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupViewModel();
        checkPermissions();
    }

    private void initializeViews() {
        totalNetworksText = findViewById(R.id.total_networks_text);
        knownNetworksText = findViewById(R.id.known_networks_text);
        newNetworksText = findViewById(R.id.new_networks_text);
        startScanButton = findViewById(R.id.start_scan_button);
        viewMapButton = findViewById(R.id.view_map_button);
        viewListButton = findViewById(R.id.view_list_button);
        settingsButton = findViewById(R.id.settings_button);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        
        viewModel.getTotalNetworks().observe(this, count -> {
            totalNetworksText.setText(String.valueOf(count));
        });
        
        viewModel.getKnownNetworks().observe(this, count -> {
            knownNetworksText.setText(String.valueOf(count));
        });
        
        viewModel.getNewNetworks().observe(this, count -> {
            newNetworksText.setText(String.valueOf(count));
        });
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    public void onStartScanClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, WiFiScanService.class));
            startService(new Intent(this, LocationService.class));
        }
    }

    public void onViewMapClick(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void onViewListClick(View view) {
        Intent intent = new Intent(this, NetworkListActivity.class);
        startActivity(intent);
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
