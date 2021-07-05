package com.mattg.viewmodelexample.repositories;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
