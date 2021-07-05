package com.mattg.viewmodelexample.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.mattg.viewmodelexample.database.ListWrapper;
import com.mattg.viewmodelexample.database.PaymentsListWrapper;
import com.mattg.viewmodelexample.database.entities.Client;
import com.mattg.viewmodelexample.database.entities.Ticket;
import com.mattg.viewmodelexample.models.Payment;

import java.util.List;

/**
 * This class is more heavily commented than similar {@link Dao} classes, to serve as an example of what
 * essentially gets created in a near identical way for every {@link androidx.room.Entity} class.
 * 6-21 [MG]
 */

@Dao
public interface TicketDao {
    /**
     * Inserts an order, if that order exists it will be replaced by the parameter
     * @param ticket Order to insert/replace
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrder(Ticket ticket);

    /**
     * Deletes a particular order from room database
     * @param ticket Order to delete
     */
    @Delete
    void deleteOrder(Ticket ticket);
    /**
     * Updates an order.  Retrieving the order, and saving it back
     * on top of itself is the way to update with room.  No need to
     * try and update sub tables, or individual fields, do that with the
     * order object and update the whole thing.
     * @param ticket Order to update
     */
    @Update
    void updateOrder(Ticket ticket);

    /**
     * Deletes all items from the table
     */
    @Query("DELETE FROM tickets_table")
    void deleteAllOrders();

    /**
     * Gets all items from the table, and sorts ascending by the id field
     * @return {@link LiveData}List<Ticket> sorted in ascending order
     */
    @Query("SELECT * FROM tickets_table ORDER BY id ASC")
    LiveData<List<Ticket>> getTicketsByIDAscLiveData();
    /**
     * Gets all items from the table, and sorts ascending by the id field
     * @return List<Ticket> sorted in ascending order
     */
    @Query("SELECT * FROM tickets_table ORDER BY id ASC")
    List<Ticket> getTicketsByIDAsc();
    /**
     * Gets all items from the table, and sorts ascending by the id field
     * @return List<Ticket> sorted in ascending order
     */
    @Query("SELECT * FROM tickets_table")
    LiveData<List<Ticket>> getTicketsLiveData();
    /**
     * Get a ticket by its order id field
     * @param orderId order Id to search
     * @return {@link Ticket}
     */
    @Query("SELECT * FROM tickets_table WHERE orderId = :orderId")
    Ticket getTicketByOrderId(String orderId);
    /**
     * Get a ticket by client
     * @param client {@link Client}
     * @return {@link Ticket}
     */
    @Query("SELECT * FROM tickets_table WHERE client = :client")
    LiveData<List<Ticket>> getTicketByClient(Client client);
    /**
     * Get list of tickets by employeeId.  This may be overkill, as the viewmodel may
     * just sort/filter the whole list (will depend on which is more performant)
     * @param employeeId
     * @return
     */
    @Query("SELECT * FROM tickets_table WHERE employeeId = :employeeId")
    LiveData<List<Ticket>> getTicketByEmployeeIdLiveData(String employeeId);
    @Query("SELECT * FROM tickets_table WHERE status = :status")
    LiveData<List<Ticket>> getTicketsByStatusLiveData(String status);
    /**
     * Clear all data
     */
    @Query("DELETE FROM tickets_table")
    void nukeTickets();
    /**
     * Get just the list of payments from a table
     * @param orderId oderId to search by
     * @return {@link LiveData}{@link ListWrapper}
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT payments FROM  tickets_table WHERE orderId = :orderId")
    LiveData<List<Payment>> getPaymentsListByTicketIdLiveData(String orderId);
    /**
     * Get a single payments object from table
     * @param orderId
     * @return {@link ListWrapper}
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT payments FROM  tickets_table WHERE orderId = :orderId")
    Payment getPaymentListByTicketId(String orderId);
}
