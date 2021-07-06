package com.mattg.viewmodelexample.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mattg.viewmodelexample.database.TicketStatus;
import com.mattg.viewmodelexample.database.entities.Client;
import com.mattg.viewmodelexample.database.entities.Menu;
import com.mattg.viewmodelexample.database.entities.Ticket;
import com.mattg.viewmodelexample.database.entities.TicketDisplay;
import com.mattg.viewmodelexample.models.MenuItem;
import com.mattg.viewmodelexample.models.Payment;
import com.mattg.viewmodelexample.repositories.ClientLocalRepo;
import com.mattg.viewmodelexample.repositories.ClientRepo;
import com.mattg.viewmodelexample.repositories.MenuRepo;
import com.mattg.viewmodelexample.repositories.RoomRepo;
import com.mattg.viewmodelexample.repositories.TicketDisplayRepo;
import com.mattg.viewmodelexample.repositories.TicketRepoLocal;
import com.mattg.viewmodelexample.repositories.TicketRepoNetwork;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainViewModel extends AndroidViewModel {
    private final String TAG = "MainViewModel::";
    private static long idToAddTo = 0;

    private MenuRepo menuRepo = new MenuRepo();
    private ClientRepo clientRepo = new ClientRepo();
    private TicketRepoNetwork ticketRepoNetwork = new TicketRepoNetwork();
    private RoomRepo roomRepo = new RoomRepo(getApplication());
    private TicketRepoLocal ticketRepoLocal = new TicketRepoLocal(getApplication());
    private ClientLocalRepo clientLocalRepo = new ClientLocalRepo(getApplication());
    private TicketDisplayRepo ticketDisplayRepo = new TicketDisplayRepo(getApplication());
    private static ArrayList<MenuItem> ticketItems;

    /**
     * Updates the current values in all scopes
     */
    public void updateCurrentTicketValues() {
        Ticket current = currentTicket.getValue();
        if(current != null) {
            current.setSubTotal(currentSubTotal.getValue());
            current.setDiscounts(getCurrentDiscounts().getValue());
            current.setTotal(total.getValue());
            current.setItems(currentItems.getValue());
            if(discounts.getValue() == null){
                current.setDue(currentSubTotal.getValue());
            } else
            current.setDue(currentSubTotal.getValue() + discounts.getValue());
            roomRepo.updateOrder(current);
        }

    }

    /**
     * Public constructor that passes in Application context
     * @param application {@link Application}
     */
   public MainViewModel(@NonNull Application application) {
        super(application);
    }
    //creating multiple observable data fields.  could combine into more complex objects to
    //reduce the amount of live data objects to manage.
   private MutableLiveData<String> counter = new MutableLiveData<>();
    //public MutableLiveData<ArrayList<MenuItem>> apiMenu = menuRepo.getMenuLiveData();
    /**
     * Getting observable live data from Room -> Repo -> Here
     */
   public LiveData<Ticket> searchedTicket;
   public LiveData<Client> searchedClient;
   public LiveData<List<Payment>> currentPayments;
   public LiveData<Menu> dbMenu = roomRepo.getMenusForUi();

   private MutableLiveData<Double> subtotal = new MutableLiveData<>();
   private MutableLiveData<Double> discounts = new MutableLiveData<>();
   private MutableLiveData<Double> total = new MutableLiveData<>();
   private MutableLiveData<Double> discountTotal = new MutableLiveData<>();
   private MutableLiveData<Double> tax = new MutableLiveData<>();

    public LiveData<Double> getCurrentSubtotal() {
        if(subtotal == null) {
            Log.d(TAG, "current subtotal fetch was null");
            subtotal = new MutableLiveData<Double>();
        }
        Log.d(TAG, "current item subtotal returning " + subtotal.getValue());
        return subtotal;
    }
    public LiveData<Double> getCurrentTotal() {
        if(total == null) {
            Log.d(TAG, "current total fetch was null");
            total = new MutableLiveData<Double>();
        }
        Log.d(TAG, "current total fetch returning " + total.getValue());
        return total;
    }
    public LiveData<Double> getCurrentDiscounts() {
        if(discounts == null) {
            Log.d(TAG, "current items discounts was null");
            discounts = new MutableLiveData<Double>();
        }
        Log.d(TAG, "current item discounts returning " + discounts.getValue());
        return discounts;
    }

    public LiveData<Double> getCurrentDiscountsTotal() {
        if(discountTotal == null) {
            Log.d(TAG, "current discounts total fetch was null");
            discountTotal = new MutableLiveData<Double>();
        }
        Log.d(TAG, "current item discounts total returning " + discountTotal.getValue());
        return discountTotal;
    }
    public LiveData<Double> getCurrentTax() {
        if(tax == null) {
            Log.d(TAG, "current tax fetch was null");
            tax = new MutableLiveData<Double>();
        }
        Log.d(TAG, "current item tax returning " + tax.getValue());
        return tax;
    }

    public void setTax(Double taxRate) {
        if(tax.getValue() != taxRate) { tax.postValue(taxRate); }
    }

    /**
     * Adjust the subtotal amount here only.  All observers will be notified
     * of this change.
     * @param amount amount to add / subtract from current subtotal
     */
    public void adjustSubtotal(Double amount) {
        Double sub = currentSubTotal.getValue();
        if(sub == null){
            sub = 0.0;
        }
        sub += amount;
        currentSubTotal.postValue(sub);
        //adjust ticket
        adjustTotal(sub);
    }

    /**
     * When the subtotal is changed, this method adjusts the actual total by adding the tax
     * for demonstration the tax is calculated here, should be done API server side so app does
     * not need updates to adjust formula / issues.  This method accounts for all current discounts
     * (more could be added, method split to multiple methods) and amounts that would effect the total.
     * @param sub subtotal amount
     */
    private void adjustTotal(Double sub) {
        if(tax.getValue() == null){
            tax.postValue(0.0);
        }
        Double taxAjustment = sub * tax.getValue();
        if(total.getValue() == null) {
            total.postValue(0.0);
        }
        Double newTotal = taxAjustment + total.getValue();
        if(discountTotal.getValue() == null){
            discountTotal.postValue(0.0);
        }
        newTotal += discountTotal.getValue();
        total.postValue(newTotal);
    }

    /**
     * Get clients from repository and updates live data.  These live data objects should publish
     * changed/updated data to the UI through either {@link androidx.viewbinding.ViewBinding} or
     * regular view class observation.
     */
    public void loadClients() {
        ArrayList<Client> repoUpdatedClientsList = clientRepo.getAllClients();
        currentClients.postValue(repoUpdatedClientsList);
    }
    public void setCounter(int number) {
        counter.postValue(String.valueOf(number));
    }
    private String getCounter(){
        return counter.getValue();
    }

    private MutableLiveData<Client> currentClient = new MutableLiveData<Client>();
    public MutableLiveData<Client> getCurrentClient() {
        if(currentClient == null) {
            currentClient = new MutableLiveData<Client>();

        }
        return currentClient;
    }

    /**
     * Call repository for a specific client, will either return a client or null
     * either way live data will handle that result and expose to ui
     * @param id {@link Client} id of Client
     */
    public void getClientById(String id) {
        Client searchedClient = clientRepo.getClientById(id);
        currentClient.postValue(searchedClient);
    }
    private MutableLiveData<List<Client>> currentClients = new MutableLiveData<List<Client>>();
    public LiveData<List<Client>> getCurrentClients() {
        if(currentClients == null) {
            currentClients = new MutableLiveData<List<Client>>();
        }
        return currentClients;
    }
    public MutableLiveData<ArrayList<MenuItem>> currentItems = new MutableLiveData<ArrayList<MenuItem>>();
    public LiveData<ArrayList<MenuItem>> getCurrentItems() {
        if(currentItems == null) {
            Log.d(TAG, "current items fetch was null");
            currentItems = new MutableLiveData<ArrayList<MenuItem>>();
            Log.d(TAG, "current item fetch set items to " + currentItems.getValue());
        }
        Log.d(TAG, "current item fetch returning " + currentItems.getValue());
        return currentItems;
    }

    public void updateCurrentItems(ArrayList<MenuItem> updatedItems) {
        currentItems.postValue(updatedItems);
    }

    /**
     * Method that will get a client from a constantly full list of all clients.
     * Will post this value to currentClient live data.
     * @param firstName
     * @param lastName
     * @return {@link Boolean}
     */
    public Boolean getClientByName(String firstName, String lastName){
        List<Client> clientsToSearch = currentClients.getValue();
        Client clientToReturn = null;
        for(int i = 0; i < clientsToSearch.size(); i++){
            if(clientsToSearch.get(i).getFirstName().equalsIgnoreCase(firstName)
            && clientsToSearch.get(i).getLastName().equalsIgnoreCase(lastName)){
                clientToReturn = clientsToSearch.get(i);
                break;
            }
        }
        if (clientToReturn != null) {
            currentClient.postValue(clientToReturn);
            return true;
        }
        else return false;
    }

    /**
     * Getting a random client for demonstration of observers on this field
     */
    public void getRandomClient() {
        Random number = new Random();
        if(currentClients.getValue() != null) {
            int range = currentClients.getValue().size();
            if(range > 0) {
                int randomNum = number.nextInt();
                if (randomNum < 0) {
                    randomNum *= -1;
                }
                currentClient.postValue(currentClients.getValue().get(randomNum));
            }
        }
    }
//    /**
//     * Updating the current list of {@link MenuItem}
//     * @param updatedItems new list
//     */
//    public void updateLiveDataList(ArrayList<MenuItem> updatedItems){
//        currentItems.postValue(updatedItems);
//    }
    /**
     * Method that will either add or subtract a value to/from subtotal
     * @param update {@link Double} amount to add/subtract
     * @param add {@link Boolean} whether to add (true) or subtract (false)
     */
    public void updateSubtotal(Double update, boolean add){
        if(currentSubTotal.getValue() == null) {
            currentSubTotal.setValue(0.0);
        }
        Double currentSub = currentSubTotal.getValue();
        if(add) {
            currentSub += update;
        } else {
            currentSub -= update;
        }
        currentSubTotal.postValue(currentSub);
        updateCurrentTicketValues();
    }


    private MutableLiveData<Double> currentSubTotal = new MutableLiveData<Double>();

    /**
     * A more concise way to write the same getter function as the other live data objects
     * @return MutableLiveData<Integer> the current subtotal
     */
    public MutableLiveData<Double> getCurrentSubTotal() {
        return (currentSubTotal == null) ? new MutableLiveData<Double>() : currentSubTotal;
    }
    public void setCurrentSubTotal(Double newTotal) {
        currentSubTotal.postValue(newTotal);
    }

    private Double getSubtotal(){
        return currentSubTotal.getValue();
    }

    private List<MenuItem> getItems(){
        return currentItems.getValue();
    }
    private void setCurrentItems(ArrayList<MenuItem> newItems) {
        Log.d(TAG, "clicker item count should be increased/decreased, setting new items " + newItems.size());
        currentItems.postValue(newItems);
    }

    /**
     * Add a menu item to the current list
     * @param newItem
     */
    public void addItemToList(MenuItem newItem) {
        if(currentItems.getValue() != null) {
            if(!currentItems.getValue().contains(newItem)){
                currentItems.getValue().add(newItem);
                //after updating assign the value to live data
                //to update observers
                currentItems.postValue(currentItems.getValue());
            } else {
                //indcrease the items value in list, then trigger observer
                Log.d(TAG, "addItemToList: else block, increasing count");
                currentItems.getValue().get(currentItems.getValue().indexOf(newItem)).increaseCount();
                currentItems.postValue(currentItems.getValue());
            }
        }

    }

    public void removeItemFromList(MenuItem item, int listPosition) {
        if(currentItems.getValue() != null) {
            ArrayList<MenuItem> items = currentItems.getValue();
            if(items.contains(item)){
                MenuItem itemCurrent = items.get(listPosition);
                Log.d("******", "current item count is  : " + itemCurrent.getCount() + "index is " + listPosition);

                if(itemCurrent.getCount() > 1) {
                  //  itemCurrent.decreaseCount();
                    items.get(listPosition).decreaseCount();
                } else {
                  //  itemCurrent.decreaseCount();
                    Log.d(TAG, "removeItemFromList: should be deleting item from list size before is " + items.size());
                    items.remove(itemCurrent);
                    Log.d(TAG, "removeItemFromList: should be deleting item from list size after is " + items.size());
                }
                //currentItems.getValue().remove(item);
                currentItems.postValue(items);
            }

        }
    }
    /**
     * Retrieves the server side menu, and if not empty updates the db menu.  Check the
     * menu repo function that is called to see how this works.
     */
    public void loadMenu() { menuRepo.getMenuFromApi(roomRepo); }

    /**
     * Update the discount value
     * @param amount
     */
    public void updateDiscount(Double amount) {
        discounts.postValue(amount);
    }
    private MutableLiveData<Ticket> currentTicket = new MutableLiveData<Ticket>();
    public MutableLiveData<Ticket> getCurrentTicket() {
        if (currentTicket == null) {
            currentTicket = new MutableLiveData<Ticket>();
        }
        return currentTicket;
    }

    /**
     * Sets posts value to live data object
     * @param newItems items that will be added to current list, then updated
     * list will be posted to live data.
     */
    public void updateTicketItems (List<MenuItem> newItems) {
        Ticket ticketToUpdate = getCurrentTicket().getValue();
        if(ticketToUpdate != null){
            ticketToUpdate.updateItemsList(newItems);
        }
        currentTicket.postValue(ticketToUpdate);
        currentItems.postValue((ArrayList<MenuItem>) newItems);
        updateCurrentTicketValues();
    }

    /**
     * Creates a ticket to save, if the order id is passed as null it will use the
     * retrieved tickets order id.
     * @param orderId @Nullable if ticket is new, pass order id
     * @return {@link Ticket}
     */
    public Ticket createTicket(@Nullable String orderId, @Nullable String unique) {
        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatus.OPEN.label);
        ticket.setDue(currentSubTotal.getValue());
        ticket.setSubTotal(currentSubTotal.getValue());
        ticket.setTotal(currentSubTotal.getValue());

        if(orderId == null && orderSearchedById.getValue() != null) {
            ticket.setOrderId(orderSearchedById.getValue().getOrderId());
            ticket.setUniqueId(orderSearchedById.getValue().getUniqueId());
        } else {
            ticket.setOrderId(orderId);
        }
        ticket.setItems(currentItems.getValue());
        if(discounts != null){
            ticket.setDiscounts(discounts.getValue());
        }
        if(currentClient.getValue() != null){
            ticket.setClient(currentClient.getValue());
        }
        return ticket;
    }

    /**
     * Uses the repository instance to save an order on a separate thread to
     * both the complete ticket repository and the variant for display only.  This
     * could be made into two methods, but for now this should be fine.
     */
    public void saveOrderToDb(Ticket ticket){
        Log.d(TAG, "saveOrderToDb: saving ticket: " + ticket.toString());
        roomRepo.insertOrder(ticket);
    }

    public void saveOrderToDb(){
        Ticket ticket = currentTicket.getValue();
        Log.d(TAG, "saveOrderToDb: saving existing ticket : " + ticket);
        roomRepo.insertOrder(ticket);
    }

    //TODO which variant works best, everything live data, and then grab that value when needed for
    // modificaiton? or return an object directly (might need async task implementation
    // and that is depracated)///
    /**
     * Uses roomRepo methods to fetch the order on a separate thread, which posts return value
     * to livedata, then calls room repo to get the value of that live data (assumes operation happens
     * quickly, a potential issue)
     * @param orderId
     * @return
     */
    public Ticket getOrderFromDb(String orderId){
        roomRepo.getOrderByOrderId(orderId);
        return roomRepo.getOrderById(orderId);
    }
    /**
     * This is a way to simply pass the live data up to the UI so that it can be observed.  This is the ideal
     * way to handle background thread access to room, as we do not want ot run into issues of timing and threads
     * not returning when and in the order we expect/assume them to
     */
    public LiveData<Ticket> orderSearchedById = roomRepo.getRetrievedOrder();

    /**
     * Since the above data is straight through from db to ui, then this method is exposed to update
     * that value for the local variable that will be modified then saved again
     */
    public void setCurrentTicketFromDb(){
        if(orderSearchedById.getValue() != null){
            Log.d(TAG, "setCurrentTicketFromDb: posting value " + orderSearchedById.getValue().toString());
           currentTicket.postValue(orderSearchedById.getValue());
           currentSubTotal.postValue(orderSearchedById.getValue().getSubTotal());
           currentItems.postValue((ArrayList<MenuItem>) orderSearchedById.getValue().getItems());
           if(orderSearchedById.getValue().getDiscounts() != null) {
               discounts.postValue(orderSearchedById.getValue().getDiscounts());
           }
        }
    }
    /**
     * Clears the full tickets table from db
     */
    public void nukeTables(){
        Log.d(TAG, "nukeTable: called");
        roomRepo.deleteAllOrders();
        ticketDisplayRepo.nukeDisplayTickets();
    }

    /**
     * Posts ticket value to both live data values
     * @param updateTicket
     */

}






