package com.mattg.viewmodelexample.utils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.mattg.viewmodelexample.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback {

        ArrayList<MenuItem> newList;
        ArrayList<MenuItem> oldList;

    public DiffUtilCallback(ArrayList<MenuItem> newData, List<MenuItem> ticketItemList) {
        this.newList = newList;
        this.oldList = oldList;
    }


        @Override
        public int getOldListSize() {
            return oldList != null ? oldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return newList != null ? newList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return newList.get(newItemPosition).getName().equals(oldList.get(oldItemPosition).getName());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            int result = newList.get(newItemPosition).compareTo(oldList.get(oldItemPosition));
            return result == 0;
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {

            MenuItem newModel = newList.get(newItemPosition);
            MenuItem oldModel = oldList.get(oldItemPosition);

            Bundle diff = new Bundle();

            if (!newModel.getPrice().equals(oldModel.getPrice())) {
                diff.putDouble("price", newModel.getPrice());
            }
            if (diff.size() == 0) {
                return null;
            }
            return diff;
            //return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }


