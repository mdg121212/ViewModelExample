package com.mattg.viewmodelexample.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mattg.viewmodelexample.adapters.DisplayTicketsAdapter;
import com.mattg.viewmodelexample.database.TicketStatus;
import com.mattg.viewmodelexample.database.entities.TicketDisplay;
import com.mattg.viewmodelexample.databinding.ActivityServerTicketListBinding;
import com.mattg.viewmodelexample.viewModels.ServerTicketsViewModel;

import java.util.ArrayList;

public class ServerTicketListActivity extends AppCompatActivity {
    private  String TAG = "::SERVER LIST ACT::";
    ActivityServerTicketListBinding binding;
    ServerTicketsViewModel viewModel;
    DisplayTicketsAdapter adapter;
    recyclerClickCallback clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServerTicketListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //get view model
        viewModel = ViewModelProviders.of(this).get(ServerTicketsViewModel.class);
        initViews();
        getInitialData();
        observeViewModel();
    }

    private void getInitialData() {

    }

    private void initViews() {
        binding.btnNewTicket.setOnClickListener(view -> {
            Intent menuIntent = new Intent(ServerTicketListActivity.this, MainActivity.class);
            startActivity(menuIntent);
            finish();
        });
        clickListener = new recyclerClickCallback() {
            @Override
            public void itemClicked(int position, TicketDisplay item, int clickType) {
                Log.d(TAG, "itemClicked: in clicker callback, ticket was " + item);
                if(clickType == 1){
                    //set orderId to intent and start next activity
                    viewModel.setCurrentOrderId(item.getOrderId());
                    //TODO use viewmodel to handle intent? it requires context so probably not...
                    Intent menuIntent = new Intent(ServerTicketListActivity.this, MainActivity.class);
                    menuIntent.putExtra("passedOrderId", item.getOrderId());
                    System.out.println("98989 intent order id is " + item.getOrderId());
                    startActivity(menuIntent);
                    finish();
                }
            }
        };
        ArrayList<TicketDisplay> startList = new ArrayList<>();
        TicketDisplay ticket = new TicketDisplay();
        ticket.setOrderDue(2.00);
        ticket.setStatus(TicketStatus.OPEN.label);
        startList.add(ticket);
        adapter = new DisplayTicketsAdapter(startList , clickListener);
        binding.rvTicketsMain.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false );
        binding.rvTicketsMain.setLayoutManager(layoutManager);
        binding.rvTicketsMain.setVisibility(View.VISIBLE);

        Log.d(TAG, "initViews: just set rv : " + binding.rvTicketsMain.getAdapter());
    }

    private void observeViewModel() {
        viewModel.getDisplayTickets().observe(this, data -> {
            Log.d(TAG, "observeViewModel: observing ticketsList");
            if(data != null){
                Log.d(TAG, "observeViewModel: ticket list not null, is " + data);
                for(TicketDisplay dis : data){
                    Log.d(TAG, "observeViewModel: ticket is " + dis.toString());
                }
                adapter.setTicketList(data);
            }
        });
    }

    /**
     * Simple interface to handle item clicks based on which subview was actually clicked,
     * for simplicity sake an int value is passed depending on view/button but could make this
     * an easier to read enum class.
     */
    public interface recyclerClickCallback {
        public void itemClicked(int position, TicketDisplay displayTicket, int clickType);
    }
}

