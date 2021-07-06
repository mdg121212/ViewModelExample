package com.mattg.viewmodelexample.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mattg.viewmodelexample.database.daos.TicketDisplayDao;
import com.mattg.viewmodelexample.database.entities.Menu;
import com.mattg.viewmodelexample.database.daos.MenuDao;
import com.mattg.viewmodelexample.database.entities.Ticket;
import com.mattg.viewmodelexample.database.daos.TicketDao;
import com.mattg.viewmodelexample.database.ApplicationDatabase;
import com.mattg.viewmodelexample.database.entities.TicketDisplay;
import com.mattg.viewmodelexample.models.MenuItem;
import com.mattg.viewmodelexample.viewModels.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.hilt.InstallIn;

/**
 * A class to handle interacting with {@link androidx.room.RoomDatabase}.  Provides live data
 * to be observed by viewmodels, and/or functions that return data objects (depending on use case).
 * If this class becomes too large, can be split into different classes.
 *
 */
@Module
@InstallIn(MainViewModel.class)
public class RoomRepo {

    private ApplicationDatabase db;
    private TicketDao ticketDao;
    private MenuDao menuDao;
    private TicketDisplayDao displayDao;

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private String TAG = "ROOMRepo:::: ";
    public LiveData<Menu> menusForUi;

    /**
     * Constructor takes application context, uses it to instantiate a database and assign
     * class dao objects to those of the created database.  Also assigns live data value of
     * database menus list to local variable that can be observed by a view class or its view model.
     * @param app Application context
     */
    public RoomRepo(Application app){
        db = ApplicationDatabase.getInstance(app);
        ticketDao = db.orderDao();
        menuDao = db.menuDao();
        menusForUi = menuDao.getMenuLiveDataTest();
        displayDao = db.ticketDisplayDao();
    }

    /**
     * List of all database menus
     * @return
     */
    public LiveData<Menu> getMenusForUi() {
        if(menusForUi == null){
            menusForUi = new MutableLiveData<>();
        }
        return menusForUi;
    };

    private MutableLiveData<List<Ticket>> ordersList = new MutableLiveData<>();
    /**
     * Checks value of private mutable live data and assigns it an empty value if null
     * @return LiveData<List<order>> tickets list from database
     */
    public LiveData<List<Ticket>> getOrdersList() {
        if(ordersList == null){
            ordersList = new MutableLiveData<List<Ticket>>();
        }
        return ordersList;
    }

    /**
     * This could use a value from session manager, like locationId in SPOOS. Something to narrow
     * database search
     * @return Live Data of database menu for ui
     */
    public LiveData<Menu> databaseLiveDataMenu() {
        return menuDao.getOneMenuForUi("123456");
    }
    private MutableLiveData<List<Menu>> menusList = new MutableLiveData<>();

    public LiveData<List<Menu>> getMenusList() {
        if(menusList == null) {
            Log.d(TAG, "getMenusList: called, list was null");
            menusList = new MutableLiveData<List<Menu>>();
        }

        Log.d(TAG, "getMenusList: returning menuslist size " + menusList);
        return menusList;
    }

    public void getMenusFromDb(){
        new Thread(() -> {
            menusList.postValue(db.menuDao().getMenuTable());
        });
    }

    private MutableLiveData<List<MenuItem>> menuItemList = new MutableLiveData<>();

    public LiveData<List<MenuItem>> getMenuItemsList() {
        if(menuItemList == null) {
            menuItemList = new MutableLiveData<List<MenuItem>>();
        }
        return menuItemList;
    }
    private MutableLiveData<Menu> currentMenu = new MutableLiveData<>();

    public LiveData<Menu> getCurrentMenu() {
        if(currentMenu == null){
            currentMenu = new MutableLiveData<Menu>();
        }
        return currentMenu;
    }
    //METHODS FOR DATABASE OPERATIONS (THESE SHOULD BE DONE ON NON-MAIN THREAD)
    //TODO add specific executor/thread pool?

    //TICKET OPTIONS
    private MutableLiveData<Ticket> currentTicket = new MutableLiveData<>();

    public LiveData<Ticket> getCurrentTicket(){
        if(currentTicket == null){
            currentTicket = new MutableLiveData<>();
        }
        Log.d(TAG, "getCurrentTicket: CALLED, RETURNING " + currentTicket.getValue());
        return currentTicket;
    }
    /**
     * Inserts a {@link Ticket} into both database tables
     * @param ticket
     */
    public void insertOrder(Ticket ticket) {
        TicketDisplay displayVersion = generateDisplayVariant(ticket);
        new Thread(() -> {
            ticketDao.insertOrder(ticket);
            displayDao.insertTicketDisplay(displayVersion);
        }).start();
    }


    /**
     * Delete a ticket from both database tables
     * @param ticket {@link Ticket} to delete
     */
    public void deleteOrder(Ticket ticket) {
        TicketDisplay displayVersion = generateDisplayVariant(ticket);
        new Thread(() -> {
            ticketDao.deleteOrder(ticket);
            displayDao.deleteTicketDisplay(displayVersion);
        }).start();
    }

    /**
     * Creates a variant for display on first screen
     * @param ticket Ticket to create display version of
     * @return {@link TicketDisplay}
     */
    private TicketDisplay generateDisplayVariant(Ticket ticket) {
        TicketDisplay toSave = new TicketDisplay();
        toSave.setStatus(ticket.getStatus());
        toSave.setOrderTotal(ticket.getDue());
        toSave.setOrderId(ticket.getOrderId());
//        toSave.setClientName(ticket.getClient().getFirstName());
        toSave.setEmployeeId(ticket.getEmployeeId());
   //     toSave.setEmployeeName(ticket.getEmployeeId());
        return toSave;
    }

    public void updateOrder(Ticket ticket) {

        TicketDisplay displayVersion = generateDisplayVariant(ticket);
        Log.d(TAG, "updateOrder: called, with ticket " + ticket + " and " + displayVersion.toString());
        new Thread(() -> {
            ticketDao.updateOrder(ticket);
            displayDao.insertTicketDisplay(displayVersion);
        }).start();
    }

    /**
     * Clear all tickets from the database
     */
    public void deleteAllOrders() {
        new Thread(() -> ticketDao.deleteAllOrders()).start();
    }

    /**
     * Local mutable live data to store returns from separate thread, and get method to pass this
     * value up towards the UI
     */
    private MutableLiveData<Ticket> retrievedOrder = new MutableLiveData<>();
    public LiveData<Ticket> getRetrievedOrder() {
        if(retrievedOrder == null){
            retrievedOrder = new MutableLiveData<>();
        }
        return retrievedOrder;
    }

    public void getAllOrders() {
        new Thread(() -> {
            List<Ticket> tickets = ticketDao.getTicketsByIDAsc();
            ordersList.postValue(tickets);
        }).start();
    }

    /**
     * Updates the above data
     * @param ticket
     */
    public void updateRetrievedOrder(Ticket ticket){
        retrievedOrder.postValue(ticket);
    }

    /**
     * Retrieves an order from the database on a thread, posts that value to local livedata
     * that can be observed elsewhere.
     * @param orderId
     * @return
     */
    public void getOrderByOrderId(String orderId) {
        Log.d(TAG, "initViews observeData: ORDER BY SEARCH CALLED FROM REPO");
        new Thread(() -> {
            Ticket ticketToGet = ticketDao.getTicketByOrderId(orderId);
            if(ticketToGet != null) {
                Log.d(TAG, "initViews observeData: ORDER BY SEARCH CALLED FROM REPO got order " + ticketToGet.toString());
                retrievedOrder.postValue(ticketToGet);
            } else {
                Log.d(TAG, "initViews observeData: order was null");
            }
        }).start();
    }

    /**
     * Should be called after the above method, as the above method will assign value to livedata
     * and this method will retrieve that value from live data.  You can also call the method called
     * from this method in the view model to simply observe the live data and be notified of its change,
     * this just allows for direct retrieval of a non-observable value.
     * @param orderId
     * @return
     */
    public Ticket getOrderById(String orderId) {
        return getRetrievedOrder().getValue();
    }

    /**
     * Get all menus from the database
     */
    public void getAllMenus() {
        Log.d("REPO", "*******get all menus called");
        new Thread(() -> {
            List<Menu> menusListFromDb = menuDao.getMenuTable();
            //update the list of all menus
            Log.d("REPO", "*******menus from db: \n" + menusListFromDb);
            Log.d("REPO", "*******menus from db: \n" + menusListFromDb.get(0).toString());
            Log.d("REPO", "*******menus from db -- item list: \n" + menusListFromDb.get(0).getMenuItemList());



            menusList.postValue(menusListFromDb);
            //for demonstration just update the current menu with the first value
            currentMenu.postValue(menusListFromDb.get(0));
        }).start();
    }

    public int insertMenu(Menu menu) {
        new Thread(() -> {
            List<Menu> matches = menuDao.getMenuIdCheck(Integer.parseInt(menu.getMenuId()));
            if(matches.size() > 0){
                Log.d("*****", "***no need to create menu update it");
                menuDao.updateMenu(menu);
            } else
            menuDao.insertNewMenu(menu);
        }).start();
        return 0;
    }
    public void updateMenu(Menu menu) {
        new Thread(() -> {
            //first update the value that will be being observed in any number of places
            currentMenu.postValue(menu);
            //also update the menu in the database
            menuDao.updateMenu(menu);
        }).start();
    }
    /**
     * Directly return the list of menu items from the current menu represented in this Respository
     * class.  Otherwise returns an empty list
     * @return List<MenuItem> from the current menu
     */
    public List<MenuItem> getCurrentMenuItems() {
        if (currentMenu.getValue() == null) {
            return Collections.emptyList();
        }
        Menu menu = currentMenu.getValue();
        if (menu.getMenuItemList() == null) {
            return Collections.emptyList();
        } else
            return menu.getMenuItemList();
    }

    /**
     * Updates menu with new list of items in multiple ways for demonstration
     * @param newItemsList List<MenuItem> the new list of items to update in the database and app ui
     * @return true if null values are not detected, false if there is an issue with new items list
     */
    public boolean updateCurrentMenuItems(List<MenuItem> newItemsList) {
        if(newItemsList.isEmpty()) {
            return false;
        }
        Menu menu = currentMenu.getValue();
        if(menu == null) {
            return false;
        }
        int dbId = menu.get_id();
        String menuId = menu.getMenuId();
        //submit multiple tasks to repo threadpool for execution
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                menuDao.insertNewItemsList(newItemsList, dbId);
            }
        });
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                menuDao.insertNewItemsList2(newItemsList, menuId);
            }
        });
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                menuDao.insertNewItemsList2_1(newItemsList, dbId);
            }
        });
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                menuDao.insertNewItemsList2_2(newItemsList, menuId);
            }
        });
        threadPool.shutdown();

        return true;
    }

    /**
     * Delete a particular menu (which represnts a table in the db)
     * @param menu
     */
    public void removeMenu(Menu menu){
        new Thread(() -> {
           System.out.println("&&*&&* Deleting a menu : " + menu.get_id() + "*&&*&&");
           menuDao.deleteMenu(menu);

        });
    }

}
