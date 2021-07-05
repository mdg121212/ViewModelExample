package com.mattg.viewmodelexample.utils;

/**
 * Enum error class with strings to display
 */
public enum AppError {
    NO_VALUE("Error getting value..."),
    NETWORK_ERROR("Network error, check connection..."),
    REFRESH("Please refresh...");

    private String label;

    AppError(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    }


