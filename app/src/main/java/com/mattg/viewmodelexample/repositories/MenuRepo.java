package com.mattg.viewmodelexample.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mattg.viewmodelexample.database.entities.Menu;
import com.mattg.viewmodelexample.models.MenuItem;
import com.mattg.viewmodelexample.network.TestApiCaller;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Provides data for ui, handles api call and threading issues
 */
public class MenuRepo {
    private String TAG = "::MENU REPO::";
    TestApiCaller caller;

    private MutableLiveData<ArrayList<MenuItem>> menu = new MutableLiveData<>();
    /**
     * provide as live data the menu
     * @return
     */
    public MutableLiveData<ArrayList<MenuItem>> getMenuLiveData(){
        Log.d(TAG, "get live data menu called");
        if(menu == null || menu.getValue() == null || menu.getValue().size() == 0) {
            menu = new MutableLiveData<>();
        };
        return menu;
    }
    /**
     * Gets a list of items through retrofit.  Either returns a populated list, or null
     * which can be checked for when this function is called
     * @return ArrayList<MenuItem> {@link MenuItem}
     */
    public ArrayList<MenuItem> getMenuFromApi(RoomRepo repo){
        Log.d(TAG, "GET MENU from api called, getting current menu");
        if(caller == null) {
            caller = new TestApiCaller();
        }
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Log.d(TAG, ">>>>>>>>>>>>>>>>>calling menu");
                    caller.getAllItems().enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, ">>>>>>>>>Call succeeded: " + "\n response: " + response.body());
                                Gson parser = new Gson();
                                Type collectionType = new TypeToken<List<MenuItem>>() {
                                }.getType();
                                List<MenuItem> retrievedItems = parser.fromJson(response.body(), collectionType);
                                menuItems.addAll(retrievedItems);
                                menu.postValue(menuItems);
                                Menu newMenu = new Menu();
                                newMenu.setMenuId("55555");
                                newMenu.setMenuItemList(retrievedItems);
                                repo.insertMenu(newMenu);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(TAG, ">>>>>>>>menu call failed: " + t.getLocalizedMessage() + " " + t.getCause());
                            t.printStackTrace();

                        }
                    });
                }
            };
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "menu item from retrofit = " + menuItems);

        return menuItems;
    }


}
