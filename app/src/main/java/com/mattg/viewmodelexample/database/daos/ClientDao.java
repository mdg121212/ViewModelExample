package com.mattg.viewmodelexample.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mattg.viewmodelexample.database.entities.Client;

import java.util.List;

/**
 * {@link Dao} for accessing and modifying clients
 * 6-21 [MG]
 */
@Dao
public interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertClient(Client client);
    @Delete
    void deleteClient(Client client);
    @Update
    void updateClient(Client client);

    /**
     * Get live data representing all clients
     * @return {@link LiveData}List<<{@link Client}>
     */
    @Query("SELECT * FROM clients_table")
    LiveData<List<Client>> getAllClientsLiveData();

    //TODO determine whether single retrieval is best achieved with live data or as object
    /**
     * Get a single {@link Client} by clientId
     * @param clientId
     * @return {@link Client}
     */
    @Query("SELECT * FROM clients_table WHERE clientId = :clientId")
    Client getClientByFirstAndLast(String clientId);
    @Query("SELECT * FROM clients_table WHERE clientId = :clientId")
    LiveData<Client> getClientByFirstAndLastLiveData(String clientId);
    /**
     * Clear all data
     */
    @Query("DELETE FROM clients_table")
    void nukeClients();
}
