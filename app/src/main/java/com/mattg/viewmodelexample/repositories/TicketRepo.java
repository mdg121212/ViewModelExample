package com.mattg.viewmodelexample.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mattg.viewmodelexample.database.ApplicationDatabase;
import com.mattg.viewmodelexample.database.daos.TicketDao;
import com.mattg.viewmodelexample.database.entities.Ticket;
import com.mattg.viewmodelexample.database.entities.TicketDisplay;
import com.mattg.viewmodelexample.models.MenuItem;
import com.mattg.viewmodelexample.network.TestApiCaller;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for accessing / modifying local tickets (ROOM, instance variables).  Might be that a
 * network and room repository will work, but to further separate concerns specific repositories
 * may prove helpful in isolating bugs and preventing them from causing cross domain issues.
 * 6-21 [MG]
 */
public class TicketRepo {

    private ApplicationDatabase db;
    private TicketDao ticketDao;
    private LiveData<List<Ticket>> allTicketsLiveData;
    private LiveData<List<Ticket>> allTicketsByStatusLiveData;
    private LiveData<List<TicketDisplay>> allDisplayTicketsLiveData;

    /**
     * Constructor takes application context, uses it to instantiate a database and assign
     * class dao objects to those of the created database.  Also assigns live data value of
     * database menus list to local variable that can be observed by a view class or its view model.
     * @param app Application context
     */
    public TicketRepo(Application app) {
        db = ApplicationDatabase.getInstance(app);
        ticketDao = db.orderDao();
        //assigning local livedata here allows for any upstream observers to be constantly aware of
        //the database query and any updates to its return value
        allTicketsLiveData = ticketDao.getTicketsLiveData();

    }
    private MutableLiveData<List<Ticket>> ticketList = new MutableLiveData<>();
    /**
     * Checks value of private mutable live data and assigns it an empty value if null
     * @return LiveData<List<order>> tickets list from database
     */
    public LiveData<List<Ticket>> getOrdersList() {
        if(ticketList == null){
            ticketList = new MutableLiveData<List<Ticket>>();
        }
        return ticketList;
    }
    /**
     * Calling this method in a view model will provide a constantly updated list of all tickets
     * stored locally (which should be all tickets at all times i.e. offline/online)
     * @return {@link LiveData} List<{@link Ticket}>
     */
    LiveData<List<Ticket>> getAllTicketsLiveData() {
        return allTicketsLiveData;
    }

    /**
     * Calling this method in a view model will provide a list of local tickets matching the
     * status parameter.
     * @param status {@link com.mattg.viewmodelexample.database.TicketStatus}
     * @return {@link LiveData} List<Ticket>>
     */
    LiveData<List<Ticket>> getAllTicketsByStatusLiveData(String status) {
        allTicketsByStatusLiveData = ticketDao.getTicketsByStatusLiveData(status);
        return allTicketsByStatusLiveData;
    }

    //------------------------Network Ticket Operations------------------------------------
    TestApiCaller caller;
    private Ticket currentTicket;

    /**
     * Insert a new ticket to the database
     * @param ticketToAdd {@link Ticket}
     */
    public void addTicketToDataBase(Ticket ticketToAdd){
        caller.insertTicket(ticketToAdd);
    }

    /**
     * Puts an updated ticket into the database through retrofit
     * @param ticket Ticket {@link Ticket} to be updated
     */
    private void updateTicketToDataBase(Ticket ticket) {
        if(ticket != null) {
            caller.updateTicket(String.valueOf(ticket.getId()), ticket);
        }
    }

    /**
     * Adds an item to the current ticket
     * @param item
     */
    public void addItemToTicket(MenuItem item) {
        ArrayList<MenuItem> toAdd = new ArrayList<MenuItem>();
        toAdd.add(item);
        List<MenuItem> ticketItems =  currentTicket.getItems();
    }
}
