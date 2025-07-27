package com.wifimapper.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "wifi_networks")
public class WifiNetwork {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "ssid")
    private String ssid;
    
    @ColumnInfo(name = "bssid")
    private String bssid;
    
    @ColumnInfo(name = "signal_strength")
    private int signalStrength;
    
    @ColumnInfo(name = "frequency")
    private int frequency;
    
    @ColumnInfo(name = "security_type")
    private String securityType;
    
    @ColumnInfo(name = "latitude")
    private double latitude;
    
    @ColumnInfo(name = "longitude")
    private double longitude;
    
    @ColumnInfo(name = "timestamp")
    private long timestamp;
    
    @ColumnInfo(name = "is_known")
    private boolean isKnown;
    
    @ColumnInfo(name = "detection_count")
    private int detectionCount;

    // Constructors
    public WifiNetwork() {}
    
    public WifiNetwork(String ssid, String bssid, int signalStrength, int frequency, 
                      String securityType, double latitude, double longitude) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.signalStrength = signalStrength;
        this.frequency = frequency;
        this.securityType = securityType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = System.currentTimeMillis();
        this.isKnown = false;
        this.detectionCount = 1;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getSsid() { return ssid; }
    public void setSsid(String ssid) { this.ssid = ssid; }
    
    public String getBssid() { return bssid; }
    public void setBssid(String bssid) { this.bssid = bssid; }
    
    public int getSignalStrength() { return signalStrength; }
    public void setSignalStrength(int signalStrength) { this.signalStrength = signalStrength; }
    
    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }
    
    public String getSecurityType() { return securityType; }
    public void setSecurityType(String securityType) { this.securityType = securityType; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public boolean isKnown() { return isKnown; }
    public void setKnown(boolean known) { isKnown = known; }
    
    public int getDetectionCount() { return detectionCount; }
    public void setDetectionCount(int detectionCount) { this.detectionCount = detectionCount; }
    
    public void incrementDetectionCount() {
        this.detectionCount++;
    }
}
