package com.mattg.viewmodelexample.database.entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.mattg.viewmodelexample.database.AppTypeConverters;
import com.mattg.viewmodelexample.models.MenuItem;
import com.mattg.viewmodelexample.models.Payment;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Single class to represent a ticket, contains all necessary information
 * for MenuActivity.  Will associate with a MainActivity version of a ticket
 * that contains only enough information to display on that view.
 * 6-21 |-(MG)-|  --
 */
@Entity(tableName = "tickets_table",  indices = {@Index(value = {"orderId"}, unique = true)})
@Getter
@Setter
@NoArgsConstructor
public class Ticket {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String uniqueId;
    //if multiple payments, this will hold the "current payment" id (for refunding, adjusting one of many)
    private String paymentId;
    private String orderId;
    //can access nested client object from MenuActivity
    private Client client;
    private String firstName;
    private String lastName;
    private String createdAt;
    //TODO possibly reference an employee object in place of or in addition to this
    private String employeeId;
    private String tableName;
    private Double total;
    private Double subTotal;
    private Double discounts;
    private Double due;
    private Double cashAmount;
    private Double cardAmount;
    private String status;
    @TypeConverters(AppTypeConverters.class)
    private List<MenuItem> items;
    //TODO list of payments and list of ids might be redundant
    @TypeConverters(AppTypeConverters.class)
    @ColumnInfo(name = "payments")private List<Payment> payments;
    @TypeConverters(AppTypeConverters.class)
    @ColumnInfo(name = "paymentIds")private List<String> paymentIds;

    public Ticket(String status, String uniqueId, @Nullable String paymentId, String orderId, @Nullable Client client, Double total, Double due,  Double subtotal, Double discounts, List<MenuItem> items) {
        this.status = status;
        this.uniqueId = uniqueId;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.client = client;
        this.total = total;
        this.subTotal = subtotal;
        this.discounts = discounts;
        this.items = items;
        this.due = due;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<String> getPaymentIds() {
        return paymentIds;
    }

    public void setPaymentIds(List<String> paymentIds) {
        this.paymentIds = paymentIds;
    }

    /**
     * Method to update an instance of this class with a new list of {@link MenuItem}
     * @param newItems List<{@link MenuItem}>
     */
    public void updateItemsList(List<MenuItem> newItems) {
        this.items = newItems;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", uniqueId='" + uniqueId + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", client=" + client +
                ", total=" + total +
                ", subtotal=" + subTotal +
                ", discounts=" + discounts +
                ", due=" + due +
                ", cashAmount=" + cashAmount +
                ", cardAmount=" + cardAmount +
                ", status='" + status + '\'' +
                ", items=" + items +
                ", payments=" + payments +
                ", paymentIds=" + paymentIds +
                '}';
    }
}
