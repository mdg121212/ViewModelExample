package com.mattg.viewmodelexample.utils;

/**
 * Enum error class with strings for display
 */
public enum AppError {
    NO_VALUE("Error getting value..."),
    NETWORK_ERROR("Network error, check connection..."),
    REFRESH("Please refresh..."),
    SYNC_NEEDED("A sync is required..."),
    DATABASE_SAVE_ERROR("There was a problem saving to the database...."),
    DATABASE_RETRIEVE_ERROR("There was a problem retrieving from the database...");

    private String label;

    AppError(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    }


