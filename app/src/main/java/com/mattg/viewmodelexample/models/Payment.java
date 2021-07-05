package com.mattg.viewmodelexample.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Class uses Lombok to generate typically used POJO methods.
 * Represents the details of a credit/debit card payment.  Uses
 * ROOM {@link Entity} and {@link lombok.Lombok}
 * 6-21 |-(MG)-|
 */
@Entity(tableName = "payments_table")
@Getter
@Setter
public class Payment {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    //transaction number for display/search
    private String transactionNumber;
    //id field for payment
    private String paymentId;
    //time created in millis
    private String createdAt;
    //customer name, save with space if this field is needed
    private String customerName;
    //credit card last 4
    private String lastFour;
    //card expiry date
    private String expirationDate;
    //total amount charged to card
    private String amount;
    //amount of total charged representing a tip/gratuity
    private String tip;
    //integrated payment id for server database
    private String integratedId;
    //id / number to associate with ticket
    private String ticketUniqueId;
    //value to hold processor/device specific keys
    private String processorKey;
    //value to hold processor/device specific keys for refunds/voids
    private String refundValue;


}
