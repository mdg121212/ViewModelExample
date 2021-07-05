package com.mattg.viewmodelexample.database;

/**
 * Simple enum class to contain known ticket status and a string label.
 * Used as a field in both {@link com.mattg.viewmodelexample.database.entities.Ticket}
 * and {@link com.mattg.viewmodelexample.database.entities.TicketDisplay}
 * 6-21 [MG]
 */
public enum TicketStatus {
    OPEN("open"),
    CLOSED("closed"),
    REFUNDED("refunded"),
    PARTIAL("partial"),
    PENDING_TIP("pending tip");

    public final String label;

    private TicketStatus(String label) {
        this.label = label;
    }
}
