package com.mattg.viewmodelexample.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "location_details")
@Getter
@Setter
public class LocationDetails {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String locationId;
    private double taxRate;
    private String menuId;
}
