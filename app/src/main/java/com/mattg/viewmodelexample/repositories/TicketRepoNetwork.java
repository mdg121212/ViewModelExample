package com.mattg.viewmodelexample.repositories;

import com.mattg.viewmodelexample.database.entities.Ticket;
import com.mattg.viewmodelexample.models.MenuItem;
import com.mattg.viewmodelexample.network.TestApiCaller;

import java.util.ArrayList;
import java.util.List;


/**
 * Handles all ticket transactions
 */
public class TicketRepoNetwork {
    private String TAG = "::TICKET REPO::";
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
