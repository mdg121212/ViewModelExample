package com.mattg.viewmodelexample.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mattg.viewmodelexample.fragments.TotalsFragment;
import com.mattg.viewmodelexample.adapters.MenuItemAdapter;
import com.mattg.viewmodelexample.database.TicketStatus;
import com.mattg.viewmodelexample.database.entities.Ticket;
import com.mattg.viewmodelexample.databinding.ActivityMainBinding;
import com.mattg.viewmodelexample.dependencies.Utilities;
import com.mattg.viewmodelexample.models.MenuItem;
import com.mattg.viewmodelexample.utils.Utils;
import com.mattg.viewmodelexample.viewModels.MainViewModel;
import com.mattg.viewmodelexample.viewModels.SecondActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


/**
 * @AndroidEntryPoint generates an individual Hilt component for each Android class in your project.
 * These components can receive dependencies from their respective parent classes.
 * variables --> camelCase  6-21 [MG]
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = "::MAINACTIVITY::";
    //create a binding variable
    ActivityMainBinding binding;
    TotalsFragment currentFragment;
    MenuItemAdapter menuAdapter;
    MenuItemAdapter ticketAdapter;
    MainViewModel viewModel;
    String intentOrderId;
    Integer id;
    String uniqueId;
    private Boolean isOrderSent = false;
    Ticket currentTicket;
    recyclerClickCallback callback;
    private String currentOrderId = "";

    //items will be heavily modified potentially, keep a private reference to the current list
    ArrayList<MenuItem> activityItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        //How to replace view binding layout inflation
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //create instance of view model
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Random orderId = new Random(22);
        id = Integer.getInteger(String.valueOf(orderId.nextInt()));
        uniqueId = String.valueOf(orderId.nextInt() + 200);
        String orderIdGenerated = String.valueOf(orderId.nextInt() + 100);
        intentOrderId = getIntent().getStringExtra("passedOrderId");
        System.out.println("98989 intent order id is " + intentOrderId);
        initViews();
        getInitialData();
        observeData();
        //retrieve the clients, and the menu from the api
        viewModel.loadClients();
        //viewModel.nukeTables();
    }

    /**
     * Assigns an empty list to class variable items list.  Calls view model methods
     * to make an api call to update the menu, and that updates the database which is a live data
     * value observed in a separate function.  Also loads client list from API
     */
    private void getInitialData() {
        if(intentOrderId != null) {
            if (!intentOrderId.isEmpty()) {
                Log.d(TAG, "989898 getInitialData: successfully passed in order Id from intent, it is: " + intentOrderId);
                viewModel.getOrderFromDb(intentOrderId);
            }
        }
        activityItemList = new ArrayList<MenuItem>();
        System.out.println(System.currentTimeMillis() + " calling for menu");
        viewModel.loadMenu();
        viewModel.loadClients();
    }
    /**
     * If view model values are not null, set text fields with them.
     * If you change a view model value, it will auto update to the text field its observer
     * updates.
     */
    private void observeData() {
        Log.d(TAG, "OBSERVE DATA CALLED");
       //get order by id value
       viewModel.orderSearchedById.observe(this, order -> {
           if(order != null){
               Log.d(TAG, " 98989 intent order id is : initViews observeData: FOUND ORDER BY SEARCH AT " + System.currentTimeMillis() + " its value:\n" + order.toString());
               currentOrderId = order.getOrderId();
               //since the value was not null, update the viewmodel variable
               viewModel.setCurrentTicketFromDb();
               if(order.getItems() != null){
                   Log.d(TAG, "98989 observeData: order items not null " + order.getItems());
                   activityItemList = (ArrayList<MenuItem>) order.getItems();
                   ticketAdapter.updateData(activityItemList);
               }
               if(order.getOrderId() != null){
                   //set the order id text
                   binding.tvOrderId.setText(binding.tvOrderId.getText().toString() + " " + order.getOrderId());
               }
               if(order.getUniqueId() != null){
                   //set the order id text
                   binding.tvUniqueId.setText(binding.tvUniqueId.getText().toString() + " " + order.getUniqueId());
               }
              // viewModel.setCurrentTicket(order);

           }
       });
       viewModel.dbMenu.observe(this, menu -> {
           Log.d(TAG, "observeData: menu live data value is: " + menu);
           if(menu != null){
               if(menu.getMenuItemList() != null){
                   System.out.println(System.currentTimeMillis() + " calling for menu done");
                   menuAdapter.updateData((ArrayList) menu.getMenuItemList());
               }
           }
       });
       //observe individual client
       viewModel.getCurrentClient().observe(this, client -> {
           if(client != null && client.getFirstName() != null && client.getLastName() != null) {
               if(client.getFirstName() != null) binding.tvFirstNameLabel.setText(client.getFirstName());
               if(client.getLastName() != null) binding.tvLastNameLabel.setText(client.getLastName());
               if(client.getAvatar() != null) Glide.with(this).load(client.getAvatar()).into(binding.imageView);
           }
           });
      //observe items
      viewModel.currentItems.observe(this, items -> {
      if(items != null) {
         activityItemList = items;
      }
       });
       //observe list of clients
       viewModel.getCurrentClients().observe(this, clients -> {
           if(clients != null){
               viewModel.getRandomClient();
           }
       });
       //observe subtotal
       viewModel.getCurrentSubTotal().observe(this, subtotal -> {
           if(subtotal == null){
               viewModel.setCurrentSubTotal(0.00);
           }
          binding.tvSubTotal.setText(Utils.formatDoubleToCurrency(Double.parseDouble(String.valueOf(subtotal))));
       });

    }

    /**
     * Initialize all views here to make on create easy to read/follow.  Click listeners and their
     * functionality are also declared here.
     */
    private void initViews() {
        binding.btnDisplayActivity.setOnClickListener(view -> {
            Intent mainIntent = new Intent(MainActivity.this, ServerTicketListActivity.class);
            startActivity(mainIntent);
            finish();
        });
        binding.btnGetMenuItems.setOnClickListener(view -> {
            Random position = new Random();
            int range = 1000;
            int randomNum = position.nextInt(range);
            viewModel.setCounter(randomNum);
        });
        binding.btnNextActivity.setOnClickListener(view -> {
            Intent nextActivityIntent = new Intent(this, SecondActivity.class);
            nextActivityIntent.putExtra("ticket", (Parcelable) currentTicket);
            startActivity(nextActivityIntent);
            currentFragment = TotalsFragment.newInstance("one", "two");
        });
        binding.btnSendOrder.setOnClickListener(view -> {
            isOrderSent = true;
            Ticket newTicket;
            Log.d(TAG, "initViews: send/save order clicked");
            if(!currentOrderId.isEmpty()) {

                Log.d(TAG, "initViews: currentOrderid was not null updating ticket rather than creating...");
                viewModel.saveOrderToDb(viewModel.createTicket(null, null));
                // viewModel.saveOrderToDb();
               // viewModel.getOrderFromDb(newTicket.getOrderId());
            } else {
                //create an Order object with available data
                viewModel.saveOrderToDb(viewModel.createTicket(randomNumber(), randomNumber()));
               // viewModel.saveOrderToDb();
               // viewModel.getOrderFromDb(newTicket.getOrderId());
            }
        });
        callback = (position, item, i) -> {
            //choice of item actions, could easily pass enum values, or STRING_CONSTANTS to hit a switch
            //here and handle individual list view item subviews, here a simple three option choice
            switch (i) {
                case -1:
                    Log.d(TAG, "item clicked at position: " + position + "is " + item);
                    MenuItem newItem = new MenuItem(
                            randomNumber(), item.getName(), item.getDescription(),
                            item.getPrice(), 1, String.valueOf(System.currentTimeMillis()));
                    ArrayList<MenuItem> tempList = activityItemList;
                    Log.d(TAG, "STEP ONE\ninitViews: adding item to activity item is : " + item + "\nlist is: " + activityItemList);
                    tempList.add(newItem);
                    activityItemList = tempList;
                    Log.d(TAG, "STEP TWO\ninitViews: adding item to activity list is now : \n" + activityItemList);
                    ticketAdapter.updateData(activityItemList);
                    ticketAdapter.notifyDataSetChanged();
                    viewModel.updateSubtotal(newItem.getPrice(), true);
                    viewModel.updateCurrentItems(activityItemList);
                    break;
                case 0:
                    Log.d(TAG, "initViews: click case 0");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setPositiveButton("Delete Item", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //remove the item from the ticket, and update the live data value
                            //through the viewmodels public exposed methods.
                            viewModel.removeItemFromList(item, 0);
                        }
                    })
                            .setMessage("Item Layout Click Handler")
                            .setNegativeButton("Increase Count", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //update this items count on the ticket, again through
                                    //the view models exposed methods
                                    viewModel.addItemToList(item);
                                }
                            }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                    break;
                case 1:
                    Log.d(TAG, "initViews: click case 1");
                    Log.d(TAG,"SWITCH CALLED REMOVE:::" + item.getCount() + "\n position is :" + position);
                    MenuItem itemClicked = null;
                    int index = -1;
                    for(int j = 0; j < activityItemList.size(); j++){
                        MenuItem iteratedItem = activityItemList.get(j);
                        if(item.getCreatedAt() != null) {
                            Log.d("++++++item", "item created: " + item.getCreatedAt() + " item id: " + item.getId() + "\n" +
                                    " list item is created: " + iteratedItem.getCreatedAt() + " id: " + iteratedItem.getId());
                            if (item.getCreatedAt().equals(iteratedItem.getCreatedAt())) {
                                Log.d("++++++item", "setting item a match was found");
                                itemClicked = iteratedItem;
                                index = j;
                                break;
                            }
                        }

                }
                    viewModel.updateSubtotal(itemClicked.getPrice(), false);
                    if(itemClicked.getCount() == 1) {
                        activityItemList.remove(index);
                    } else {
                        itemClicked.decreaseCount();
                        activityItemList.set(index, itemClicked);
                    }
                    ticketAdapter.updateData(activityItemList);
                    ticketAdapter.notifyDataSetChanged();
                    viewModel.updateTicketItems(activityItemList);
                    break;
                case 2:
                    Log.d(TAG, "initViews: item add clicked, initial state of list below: \n" + activityItemList);
                    Log.d(":::CLICKER CALLBACK::","SWITCH TO ADD ITEM CALLED ADD:::");
                    if(item.getCount() == 0) {
                        Log.d(":::CLICKER CALLBACK::", "CAUGHT ZERO ITEM, REMOVE IT:::");
                    }
                    MenuItem itemClickedAdd  = null;
                    int index2 = -1;
                    for(int j = 0; j < activityItemList.size(); j++) {
                        MenuItem iteratedItem = activityItemList.get(j);
                        Log.d("++++++item created: ", item.getCreatedAt() + " list item created is: " + iteratedItem.getCreatedAt());
                        if (item.getCreatedAt().equals(iteratedItem.getCreatedAt()) && item.getId().equals(iteratedItem.getId())) {
                            Log.d("++++++item", "setting item a match was found");
                            itemClickedAdd = iteratedItem;
                            index2 = j;
                            break;
                        }
                    }
                    //itemClickedAdd.increaseCount();
                    item.increaseCount();
                    // activityItemList.set(index2, itemClickedAdd);
                    activityItemList.set(index2, item);
                    Log.d(TAG, "initViews: item add clicked, final state of list below: \n" + activityItemList);
                    Log.d("++++++item", "LIST IS NOW \n" + activityItemList);

                    ticketAdapter.updateData(activityItemList);
                    ticketAdapter.notifyItemChanged(index2);
                    viewModel.updateSubtotal(itemClickedAdd.getPrice(), true);
                    viewModel.updateTicketItems(activityItemList);
                    break;
            }
        };
        //must ensure that objects required for recycler functionality are created before using
        ticketAdapter = new MenuItemAdapter(new ArrayList<>(), callback, true);
        binding.rvTicketItems.setAdapter(ticketAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false );
        binding.rvTicketItems.setLayoutManager(layoutManager);
        //initialization of the recyclerview that will hold the "menu" list of items
        menuAdapter = new MenuItemAdapter(new ArrayList<>(), callback, false);
        binding.rvMenu.setAdapter(menuAdapter);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvMenu.setLayoutManager(layoutManager2);
        binding.btnGetClient.setOnClickListener(this::searchClient);
    }
    /**
     * Gets the value of the three input edit texts, and searches for a client based on the
     * retrieved values.
     * @param view Added to allow for onclick syntax
     */
    private void searchClient(View view) {
        String firstName = binding.editTextTextPersonName.getText().toString().trim();
        String lastName = binding.editTextTextPersonName2.getText().toString().trim();
        String id = binding.etClientId.getText().toString().trim();
        if (firstName.isEmpty() && lastName.isEmpty() && id.isEmpty()) {
            Toast.makeText(this, "Please enter more information to find client", Toast.LENGTH_SHORT).show();

        } else if (!viewModel.getClientByName(firstName, lastName) && !id.isEmpty()) {
            viewModel.getClientById(id);
        } else {
            Toast.makeText(this, "Please enter more information to find client", Toast.LENGTH_SHORT).show();
        }
        //reset the edit texts after searching
        binding.etClientId.setText("");
        binding.editTextTextPersonName.setText("");
        binding.editTextTextPersonName2.setText("");
        binding.editTextTextPersonName2.clearFocus();
        binding.editTextTextPersonName.clearFocus();
        binding.etClientId.clearFocus();
        hideSoftKeyboard(MainActivity.this);

    }
    /**
     * Utility method found online, to manually close the soft keyboard. (linked)
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            View focusedView = activity.getCurrentFocus();
            /*
             * If no view is focused, an NPE will be thrown
             * Maxim Dmitriev
             */
            if (focusedView != null) {
                inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    /**
     * Simple interface to handle item clicks based on which subview was actually clicked,
     * for simplicity sake an int value is passed depending on view/button but could make this
     * an easier to read enum class.
     */
    public interface recyclerClickCallback {
        public void itemClicked(int position, MenuItem item, int clickType);
     }

    private String randomNumber() {
        Random rand = new Random();
        return  String.valueOf(rand.nextInt(9000000) + 1000000);
    }

}