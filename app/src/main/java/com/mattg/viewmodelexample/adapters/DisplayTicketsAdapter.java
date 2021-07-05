package com.mattg.viewmodelexample.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mattg.viewmodelexample.activities.ServerTicketListActivity;
import com.mattg.viewmodelexample.database.entities.TicketDisplay;
import com.mattg.viewmodelexample.databinding.MainDicketDisplayRowItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link androidx.recyclerview.widget.RecyclerView.Adapter} that has its own
 * {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} class.  This is set
 * up for 1 way data binding (to a {@link com.mattg.viewmodelexample.database.entities.TicketDisplay} item)
 * 6-21 [MG]
 */
public class DisplayTicketsAdapter extends RecyclerView.Adapter<DisplayTicketViewHolder> {

    private static final String TAG = "::DISPLAYADAPT::";
    List<TicketDisplay> ticketDisplaysList;
    private ServerTicketListActivity.recyclerClickCallback clicker;

    public DisplayTicketsAdapter(ArrayList<TicketDisplay> ticketDisplays, ServerTicketListActivity.recyclerClickCallback clickListener) {
        Log.d(TAG, "DisplayTicketsAdapter: constructor called");
        this.ticketDisplaysList = ticketDisplays;
        this.clicker = clickListener;
    }
    /**
     * Updates the adapters list
     * @param displayTickets
     */
    public void setTicketList( List<TicketDisplay> displayTickets) {
        Log.d(TAG, "setTicketList: called with list: " + displayTickets);
        this.ticketDisplaysList = displayTickets;
        this.notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public DisplayTicketViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // inflate layout and retrieve binding
        Log.d(TAG, "onCreateViewHolder: view created");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MainDicketDisplayRowItemBinding binding = MainDicketDisplayRowItemBinding.inflate(inflater,
                 parent, false);
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.menu_layout_item, parent, false);
        return new DisplayTicketViewHolder(binding, clicker);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DisplayTicketViewHolder holder, int position) {
           TicketDisplay display = this.ticketDisplaysList.get(position);
           Log.d(TAG, "onBindViewHolder: Called with displayTicket: " + display);
           holder.bind(display);

    }

    @Override
    public int getItemCount() {
        return ticketDisplaysList.size();
    }
}

class DisplayTicketViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "::VIEWHOLDER::";
    MainDicketDisplayRowItemBinding binding;
    ServerTicketListActivity.recyclerClickCallback clickListenerRow;

    public DisplayTicketViewHolder(@NonNull @NotNull MainDicketDisplayRowItemBinding bindingIn,
                                   ServerTicketListActivity.recyclerClickCallback clickListener) {
        super(bindingIn.getRoot());
        binding = bindingIn;
        clickListenerRow = clickListener;

    }

    /**
     * Takes a {@link TicketDisplay} item and binds it to the xml for display.
     * @param ticket {@link TicketDisplay}
     */
    public void bind(TicketDisplay ticket){
        Log.d(TAG, "bind: called with ticket " + ticket);
        binding.setTicketDisplay(ticket);
        binding.clRow.setOnClickListener(v -> {
            clickListenerRow.itemClicked(getAdapterPosition(), ticket, 1);
        });
        binding.executePendingBindings();
    }
}
