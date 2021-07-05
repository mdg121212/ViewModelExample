package com.mattg.viewmodelexample.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.mattg.viewmodelexample.database.AppTypeConverters;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "clients_table")
@Getter
@Setter
public class Client {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String clientId;
    private String createdAt;
    private String avatar;
    private String firstName;
    private String lastName;
    private int loyaltyPoints;
    private int credits;
    @TypeConverters(AppTypeConverters.class)
    private List<GiftCard> giftCardList;
}
