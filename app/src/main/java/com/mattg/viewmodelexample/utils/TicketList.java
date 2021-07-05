package com.mattg.viewmodelexample.utils;

import androidx.annotation.Nullable;

import com.mattg.viewmodelexample.models.MenuItem;

import java.util.ArrayList;

public class TicketList extends ArrayList<MenuItem> {
    @Override
    public boolean contains(@Nullable @org.jetbrains.annotations.Nullable Object o) {
        return super.contains(o);
    }

    @Override
    public MenuItem set(int index, MenuItem element) {
        return super.set(index, element);
    }
}
