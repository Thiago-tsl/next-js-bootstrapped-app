package com.wifimapper.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wifimapper.app.models.WifiNetwork;

import java.util.List;

@Dao
public interface WifiNetworkDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(WifiNetwork network);
    
    @Update
    int update(WifiNetwork network);
    
    @Delete
    int delete(WifiNetwork network);
    
    @Query("SELECT * FROM wifi_networks ORDER BY timestamp DESC")
    LiveData<List<WifiNetwork>> getAllNetworks();
    
    @Query("SELECT * FROM wifi_networks WHERE bssid = :bssid LIMIT 1")
    WifiNetwork getNetworkByBssid(String bssid);
    
    @Query("SELECT * FROM wifi_networks WHERE is_known = 1 ORDER BY detection_count DESC")
    LiveData<List<WifiNetwork>> getKnownNetworks();
    
    @Query("SELECT * FROM wifi_networks WHERE is_known = 0 ORDER BY timestamp DESC")
    LiveData<List<WifiNetwork>> getNewNetworks();
    
    @Query("SELECT COUNT(*) FROM wifi_networks")
    LiveData<Integer> getNetworkCount();
    
    @Query("SELECT COUNT(*) FROM wifi_networks WHERE is_known = 1")
    LiveData<Integer> getKnownNetworkCount();
    
    @Query("DELETE FROM wifi_networks")
    void deleteAll();
    
    @Query("SELECT * FROM wifi_networks WHERE ssid LIKE :query OR bssid LIKE :query ORDER BY timestamp DESC")
    LiveData<List<WifiNetwork>> searchNetworks(String query);
}
