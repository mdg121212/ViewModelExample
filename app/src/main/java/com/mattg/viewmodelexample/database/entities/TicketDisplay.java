package com.mattg.viewmodelexample.database.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.mattg.viewmodelexample.utils.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Secondary, lighter duty class to store reference to a ticket.  This will avoid loading
 * unnecessary amounts of data to the MainActivity.
 * 6-21- [MG]
 */
@Entity(tableName = "tickets_display_table", indices = {@Index(value = {"orderId"}, unique = true)})
@Getter
@Setter
public class TicketDisplay {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String orderId;
    private double orderTotal;
    private double orderDue;
    private String tableName;
    private String clientName;
    private String employeeName;
    private String employeeId;
    private String status;



    /**
     * So that the bound {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} layout can get formatted strings
     * for display.
     * @return
     */
    public String orderDueString() {
        return Utils.formatDoubleToCurrency(this.orderTotal);
    }

    public String displayName() {
        if(this.clientName.isEmpty() | this.clientName == null) {
            return "NO CLIENT";
        } else return this.getClientName();
    }

    @Override
    public String toString() {
        return "TicketDisplay{" +
                "_id=" + _id +
                ", orderId='" + orderId + '\'' +
                ", orderTotal=" + orderTotal +
                ", orderDue=" + orderDue +
                ", tableName='" + tableName + '\'' +
                ", clientName='" + clientName + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
