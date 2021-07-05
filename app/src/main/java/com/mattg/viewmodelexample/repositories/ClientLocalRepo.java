package com.mattg.viewmodelexample.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mattg.viewmodelexample.database.ApplicationDatabase;
import com.mattg.viewmodelexample.database.daos.ClientDao;
import com.mattg.viewmodelexample.database.entities.Client;

import java.util.List;

/**
 * Class to interact with client tables in database.
 * 6-21 [MG]
 */
public class ClientLocalRepo {

    private static final String TAG = "::CLIENT_LOCAL_REPO::";
    private ApplicationDatabase db;
    private ClientDao clientDao;
    private LiveData<List<Client>> allClientsLiveData;


    public ClientLocalRepo(Application app) {
        db = ApplicationDatabase.getInstance(app);
        clientDao = db.clientDao();

        allClientsLiveData = clientDao.getAllClientsLiveData();
    }

    private MutableLiveData<Client> currentClient = new MutableLiveData<Client>();

    public LiveData<Client> getCurrentClientLiveData = currentClient;
//    {  //maybe this is the required syntax for this to work
//        if(currentClient == null) {
//            currentClient = new MutableLiveData<>();
//        }
//        return currentClient;
//    }

    /**
     * Inserts a {@link Client} into ROOM database on separate thread.
     * @param clientToAdd
     */
    public void addClientToDatabase(Client clientToAdd) {
        new Thread(() -> { clientDao.insertClient(clientToAdd); }).start();
    }

    /**
     * Deletes a {@link Client} from the ROOM database on a separate thread.
     * @param clientToDelete
     */
    public void deleteClientFromDatabase(Client clientToDelete) {
        new Thread(() -> { clientDao.deleteClient(clientToDelete); } ).start();
    }

    /**
     * Will get {@link Client} value from ROOM database on a separate thread
     * and post that value to the local {@link MutableLiveData} which will in turn
     * update public {@link LiveData}
     * @param clientId id to search database by
     */
    public void getClientFromDatabaseById(String clientId) {
        new Thread(() -> {
           Client client = clientDao.getClientByFirstAndLast(clientId);
            Log.d(TAG, "getClientFromDatabaseById: client retrieved was " + client);
            if(client != null) {
                currentClient.postValue(client);
            } else {
                Log.d(TAG, "getClientFromDatabaseById: client was null!! :(");
            }
        }).start();
    }



}
