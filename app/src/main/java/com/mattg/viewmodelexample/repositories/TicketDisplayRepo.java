package com.mattg.viewmodelexample.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mattg.viewmodelexample.database.ApplicationDatabase;
import com.mattg.viewmodelexample.database.daos.TicketDisplayDao;
import com.mattg.viewmodelexample.database.entities.TicketDisplay;

import java.util.List;

/**
 * Class to interact with ROOM database {@link com.mattg.viewmodelexample.database.entities.TicketDisplay}
 * 6-21 [MG]
 */
public class TicketDisplayRepo {
    private ApplicationDatabase db;
    private TicketDisplayDao ticketDisplayDao;
    public LiveData<List<TicketDisplay>> allTicketDisplaysLiveData;
    public LiveData<List<TicketDisplay>> allTicketsDisplaysByStatusLiveData;

    public TicketDisplayRepo(Application app) {
        db = ApplicationDatabase.getInstance(app);
        ticketDisplayDao = db.ticketDisplayDao();
        allTicketDisplaysLiveData = ticketDisplayDao.getAllTicketsDisplay();
    }
    /**
     * Add a {@link TicketDisplay} to ROOM database
     * @param ticketDisplay
     */
    public void insertTicketDisplay(TicketDisplay ticketDisplay) {
        new Thread(() -> { ticketDisplayDao.insertTicketDisplay(ticketDisplay); }).start();
    }
    /**
     * Delete a {@link TicketDisplay} from ROOM database
     * @param ticketDisplay
     */
    public void deleteTicketDisplay(TicketDisplay ticketDisplay) {
        new Thread(() -> { ticketDisplayDao.deleteTicketDisplay(ticketDisplay);}).start();
    }
    /**
     * Update a {@link TicketDisplay} in the ROOM database
     * @param ticketDisplay
     */
    public void updateTicketDisplay(TicketDisplay ticketDisplay) {
        new Thread(() -> { ticketDisplayDao.updateTicketDisplay(ticketDisplay);}).start();
    }

    public LiveData<List<TicketDisplay>> getAllTicketsLiveData(){
        return ticketDisplayDao.getAllTicketsDisplay();
    }

    public void nukeDisplayTickets() {
        new Thread(() -> ticketDisplayDao.nukeDisplayTable()).start();
    }
}
