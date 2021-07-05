package com.mattg.viewmodelexample.database;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import java.util.List;

public class PaymentIdsListWrapper {
    @TypeConverters(AppTypeConverters.class)
    @ColumnInfo(name = "paymentIds") private List<String> paymentIds;

    public List<String> getPaymentIdsList(){ return paymentIds; }

    public void setPaymentIdsList(List<String> paymentIdsList) { this.paymentIds = paymentIdsList; }
}
