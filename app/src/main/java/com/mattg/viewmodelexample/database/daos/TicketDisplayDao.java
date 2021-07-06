package com.mattg.viewmodelexample.database.daos;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mattg.viewmodelexample.database.entities.Ticket;
import com.mattg.viewmodelexample.database.entities.TicketDisplay;

import java.util.List;

@Dao
public interface TicketDisplayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTicketDisplay(TicketDisplay ticketDisplay);

    @Update
    public void updateTicketDisplay(TicketDisplay ticketDisplay);

    @Delete
    public void deleteTicketDisplay(TicketDisplay ticketDisplay);

    @Query("DELETE FROM tickets_display_table")
    void nukeDisplayTable();
    @WorkerThread
    @Query("SELECT * FROM tickets_display_table")
    public LiveData<List<TicketDisplay>> getAllTicketsDisplay();
    //TODO determine whether single retrieval is best achieved with live data or as object
    @Query("SELECT * FROM tickets_display_table WHERE orderId = :orderId")
    public TicketDisplay getTicketDisplayByOrderId(String orderId);
    @Query("SELECT * FROM tickets_display_table WHERE orderId = :orderId")
    public LiveData<TicketDisplay> getTicketDisplayByOrderIdLiveData(String orderId);

    @Query("SELECT * FROM tickets_display_table WHERE status = :status")
    public LiveData<List<TicketDisplay>> getTicketDisplayByStatusLiveData(String status);

    @Query("SELECT * FROM tickets_display_table WHERE employeeId = :employeeId")
    public LiveData<List<TicketDisplay>> getTicketDisplayByEmployeeIdLiveData(String employeeId);

}
