package com.mattg.viewmodelexample.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mattg.viewmodelexample.models.MenuItem;

import java.util.ArrayList;

/**
 * A repository to handle an active list of items, transform it, and return it as live data
 * to a view model that needs it.
 */
public class ItemListRepo {
    private static ArrayList<MenuItem> activeItemsList = new ArrayList<>();
    private MutableLiveData<ArrayList<MenuItem>> itemsLiveData = new MutableLiveData<ArrayList<MenuItem>>();

    public void setActiveItemsList(ArrayList<MenuItem> list){
        activeItemsList = list;
    }

    public void addItemToList(MenuItem item){
        activeItemsList.add(item);
    }
    public void removeItemFromList(MenuItem item){
        activeItemsList.remove(item);
    }

    public LiveData<ArrayList<MenuItem>> getActiveList() {
        return itemsLiveData;
    }
}
