package com.mattg.viewmodelexample.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mattg.viewmodelexample.database.ApplicationDatabase;
import com.mattg.viewmodelexample.database.daos.ClientDao;
import com.mattg.viewmodelexample.database.entities.Client;
import com.mattg.viewmodelexample.network.TestApiCaller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles retrieval of clients through retrofit, exposed methods return data only
 */
public class ClientRepo {
    private String TAG = "::CLIENT REPO::";
    TestApiCaller caller;
    private ApplicationDatabase db;
    private ClientDao clientDao;
    private LiveData<List<Client>> allClientsLiveData;


    public ClientRepo(Application app) {
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


//-----------------------------------Network Client Operations----------------------------
    /**
     * Retrieves clients from api, creates a list of objects
     * @return ArrayList<Client> {@link Client}
     */
    public ArrayList<Client> getAllClients(){
        if(caller == null) {
            caller = new TestApiCaller();
        }
        ArrayList<Client> clientList = new ArrayList<Client>();
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    caller.getAllClients().enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Gson parser = new Gson();
                                Type collectionType = new TypeToken<List<Client>>() {
                                }.getType();
                                List<Client> clients = parser.fromJson(response.body(), collectionType);
                                Log.d(TAG, String.valueOf(clients.size()));
                                //currentClients.postValue(clients);
                                clientList.addAll(clients);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(TAG, "==CLient list call failed: \n" + t.getMessage());
                            t.printStackTrace();
                        }
                    });
                }
            };
            thread.start();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            return clientList;
        }

    }

    /**
     * Gets a client through retrofit by clients id.  Could be made to check a cache first, then call
     * or a database, then cache, then call.
     * @param id Clients id
     * @return Client {@link Client}
     */
    public Client getClientById(String id) {
        if(caller == null) {
            caller = new TestApiCaller();
        }
        Log.d(TAG,">>>>>>>>>>>>>>>>>calling client by id");
        final Client[] client = {null};
        caller.getClientByIdString(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG,">>>>>>>>>>>>>>>>>call returned " + response);
                // currentClient.postValue(response.body());
                if(response.isSuccessful() && response != null) {
                    Log.d(TAG, "client api call succeeded: \nClient==" + response.body());
                    //this api returns a list at every endpoint, to accomodate this for single requests
                    //create a typetoken for the gson object to parse the json, then get what you know to be its
                    //only index
                    Gson parser = new Gson();
                    Type collectionType = new TypeToken<List<Client>>(){}.getType();
                    List<Client> clients = parser.fromJson(response.body(), collectionType);
                    Log.d(TAG, ">>>>>>>>>>clients list size is " + clients.size() +
                            " \nand it is: " + clients.toString());
                    if(clients != null) client[0] = clients.get(0);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "client api call failed: \n" + t.getMessage());
            }
        });
        return client[0];
    }

}
