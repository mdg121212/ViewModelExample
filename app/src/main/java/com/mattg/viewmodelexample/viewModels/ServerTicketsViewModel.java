package com.mattg.viewmodelexample.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.RoomDatabase;
import com.mattg.viewmodelexample.database.ApplicationDatabase;
import com.mattg.viewmodelexample.database.entities.TicketDisplay;
import com.mattg.viewmodelexample.repositories.RoomRepo;
import com.mattg.viewmodelexample.repositories.TicketDisplayRepo;


import java.util.List;

/**
 * Simpler of the two {@link androidx.lifecycle.ViewModel} classes, responsible for
 * accessing ROOM {@link androidx.room.Dao} interfaces through repository classes and
 * provide up to date data to the UI components observing its values. Extends
 * {@link AndroidViewModel} becaues repository classes require {@link Application}
 * 6-21 [MG]
 */
public class ServerTicketsViewModel  extends AndroidViewModel {

    private static final String TAG = "::Server View Model::";
    RoomRepo roomRepo;
    TicketDisplayRepo ticketDetailLocal;
    RoomDatabase db;

    public ServerTicketsViewModel(@NonNull Application app) {
        super(app);
        db = ApplicationDatabase.getInstance(getApplication());
        roomRepo = new RoomRepo(getApplication());
        ticketDetailLocal = new TicketDisplayRepo(getApplication());
    }

    /**
     * Returns always up to date live data directly from ROOM
     */
    private MutableLiveData<List<TicketDisplay>> ticketsList = new MutableLiveData<>();
    public LiveData<List<TicketDisplay>> ticketsListLiveData(){
        Log.d(TAG, "ticketsListLiveData: getting live data");
       if(ticketsList == null){
           ticketsList = new MutableLiveData<>();
       }
       return ticketsList;
    }
    private MutableLiveData<String> selectedTicketOrderId = new MutableLiveData<>();
    public LiveData<String> getOrderIdLiveData() {
        if(selectedTicketOrderId == null){
            selectedTicketOrderId = new MutableLiveData<>();
        }
        return selectedTicketOrderId;
    }

    /**
     * Assigns a given value to private {@link MutableLiveData} that will update
     * the observable field representing the selected {@link TicketDisplay}'s
     * order id (to navigate to menu activity)
     * @param orderId
     */
    public void setCurrentOrderId(String orderId) {
        selectedTicketOrderId.postValue(orderId);
    }

    public LiveData<List<TicketDisplay>> getDisplayTickets(){
        Log.d(TAG, "getDisplayTickets: called");
        return ticketDetailLocal.getAllTicketsLiveData();

    }
}
