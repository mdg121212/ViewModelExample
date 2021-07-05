package com.mattg.viewmodelexample.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mattg.viewmodelexample.database.entities.LocationDetails;

/**
 * {@link Dao} for accessing and modifying location details
 * 6-21 [MG]
 */
@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLocationDetails(LocationDetails locationDetails);

    @Delete
    void deleteLocationDetails(LocationDetails locationDetails);

    @Update
    void updateLocationDetails(LocationDetails locationDetails);

    /**
     * Return an observable value of the current location details
     * @param locationId id to search db for (should only be one per terminal?)
     * @return {@link LiveData}
     */
    @Query("SELECT * FROM location_details WHERE locationId = :locationId")
    LiveData<LocationDetails> getLocationDetailsLiveData(String locationId);
}
