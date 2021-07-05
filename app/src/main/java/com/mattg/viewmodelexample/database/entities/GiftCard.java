package com.mattg.viewmodelexample.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "giftcards_table")
@Setter
@Getter
public class GiftCard {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private int giftCardId;
    private double giftCardAmount;
    private int giftCardPin;
    private String createdAt;
    private String clientId;

}
