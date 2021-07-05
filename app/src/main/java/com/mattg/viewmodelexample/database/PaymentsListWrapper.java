package com.mattg.viewmodelexample.database;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.mattg.viewmodelexample.models.Payment;

import java.util.List;

public class PaymentsListWrapper {
    @TypeConverters(AppTypeConverters.class)
    @ColumnInfo(name = "payments") private List<Payment> payments;

    public List<Payment> getPaymentsList() { return payments; }

    public void setPaymentsList(List<Payment> paymentList) { this.payments = paymentList; }
}
