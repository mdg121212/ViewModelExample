package com.mattg.viewmodelexample.database;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.mattg.viewmodelexample.models.MenuItem;
import com.mattg.viewmodelexample.models.Payment;

import java.util.List;

/**
 * Wrapper class to facilitate conversion of lists>
 * 6-21 [MG]
 */
public class ListWrapper {
    @TypeConverters(AppTypeConverters.class)
    @ColumnInfo(name = "menuItemList") private List<MenuItem> menuItemList;

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public void setMenuItemList(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }



}
