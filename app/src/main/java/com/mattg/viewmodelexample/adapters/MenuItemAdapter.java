package com.mattg.viewmodelexample.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mattg.viewmodelexample.activities.MainActivity;
import com.mattg.viewmodelexample.R;
import com.mattg.viewmodelexample.models.MenuItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Implementation of {@link androidx.recyclerview.widget.RecyclerView.Adapter} removed
 * from any specific view class (activity) so that it can be reused.  It also has its
 * own {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} class.
 * 6-21 [MG]
 */
public class MenuItemAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    public List<MenuItem> menuItemList;
    private MainActivity.recyclerClickCallback clicker;
    private boolean isTicket;
    public MenuItemAdapter(List<MenuItem> menu, MainActivity.recyclerClickCallback callback, boolean isTicket){
        this.menuItemList = menu;
        this.clicker = callback;
        this.isTicket = isTicket;
    }

    @NonNull
    @NotNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_layout_item, parent, false);
        return new MenuViewHolder(view, clicker, menuItemList, isTicket);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MenuViewHolder holder, int position) {
        MenuItem item  = this.menuItemList.get(position);
        //notice the brackets, we could remove them, but that means if we ever want to add another line
        //in these conditional blocks, we will have to add them again anyway.....probably just use them
        if (item.getPrice() != null) {
            holder.itemPrice.setText(String.format(Locale.US, "%.2f", Double.valueOf(String.valueOf(item.getPrice()))));
        }
        if (item.getName() != null) {
            holder.itemName.setText(item.getName());
        }
        if (item.getDescription() != null) {
            holder.itemDescription.setText(item.getDescription());
        }
        if(isTicket) {
           holder.itemCount.setVisibility(View.VISIBLE);
           holder.itemCount.setText(String.valueOf(item.getCount()));
        }

    }


    @Override
    public int getItemCount() {
        if(this.menuItemList.size() > 0) {
            return this.menuItemList.size();
        } else return 0;
    }

    public void updateData(ArrayList<MenuItem> newItems) {
        this.menuItemList = newItems;
        this.notifyDataSetChanged();
    }
}

class MenuViewHolder extends  RecyclerView.ViewHolder {
    TextView itemName;
    TextView itemDescription;
    TextView itemPrice;
    MenuItem item;
    TextView itemCount;
    Button add;
    Button remove;

    public MenuViewHolder(@NonNull @NotNull View itemView, MainActivity.recyclerClickCallback click, List<MenuItem> menuItemList, boolean isTicket) {
        super(itemView);
        Log.d("MainActivity::", "menu view holder created");
        itemName = itemView.findViewById(R.id.tv_itemName);
        itemPrice = itemView.findViewById(R.id.tv_itemPrice);
        itemDescription = itemView.findViewById(R.id.tv_itemDescription);
        itemCount = itemView.findViewById(R.id.tvCount);
        add = itemView.findViewById(R.id.btn_add_item);
        remove = itemView.findViewById(R.id.btn_remove_item);
        if(isTicket){
            add.setVisibility(View.VISIBLE);
            remove.setVisibility(View.VISIBLE);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //create an object based on the adapter position clicked.  set its unique id to current time
                    //so that duplicate item types are not increased
                    MenuItem clickedItem = menuItemList.get(getAdapterPosition());
                    clickedItem.setId(String.valueOf(System.currentTimeMillis()));
                    click.itemClicked(getAdapterPosition(), clickedItem, 2);
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.itemClicked(getAdapterPosition(), menuItemList.get(getAdapterPosition()), 1);
                }
            });
        } else

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(":::ITEM CLICK:::", "-- itemview clicked menu--" +  menuItemList.get(getAdapterPosition()));
                MenuItem item = menuItemList.get(getAdapterPosition());
                item.setCreatedAt(String.valueOf(System.currentTimeMillis()));
                click.itemClicked(getAdapterPosition(), item, -1);
            }
        });
    }


}
