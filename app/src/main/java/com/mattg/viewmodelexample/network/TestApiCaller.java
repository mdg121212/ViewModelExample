package com.mattg.viewmodelexample.network;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mattg.viewmodelexample.database.entities.Client;
import com.mattg.viewmodelexample.database.entities.Ticket;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TestApiCaller {

        static String TEST_BASE_URL = "https://6067d03b98f405001728f0c1.mockapi.io/";

        /**
         * Calls mock server at  to return objects to correlate to the
         * @return api {@link Retrofit}
         */
        private TestApi getTestApi() {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            // create retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TEST_BASE_URL)
                    //parses a string value of response
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            TestApi api = retrofit.create(TestApi.class);
            //return the retrofit object to make calls on
            return api;
        }


        /**
        * Retrieve all items from api
        * @return {@link Call}
         */
        public Call<String> getAllItems() {
            return getTestApi().getAllItems();
        }
         /**
         * Retrieve an item by its id from api
         * @return {@link Call}
         */
        public Call<String> getItemById(int id){
            return getTestApi().getItemById(id);
        }
         /**
         * Retrieve all clients from api
         * @return {@link Call}
         */
        public Call<String> getAllClients() {
            return getTestApi().getAllClients();
        }
        /**
        * Retrieve a client by their id from api
         * @return {@link Call}
         * @param id
        */
        public Call<Client> getClientById(String id) {
            return getTestApi().getAClientById(id);
        }

        public Call<String> getClientByIdString(String id) {
            return getTestApi().getAClientByIdString(id);
        }

    /**
     * Post a client to the api data set
     * @param client
     * @return String "yeah"
     */
    public String insertClient(Client client) {

        return "yeah";
        }

    /**
     * Creates a ticket object on the mocked api backend via post
      * @param ticket the ticket to be added to backend db (mock)
     */
    public void insertTicket(Ticket ticket){
        getTestApi().createTicket(ticket);
    }

    /**
     * Updates the ticket via api
     * @param id id of current ticket
     * @param ticket ticket with updated values
     */
    public void updateTicket(String id, Ticket ticket) {
        getTestApi().updateTicket(id, ticket);
    }

    /**
     * Get all tickets (can filter by user once retrieved)
     * @return Call<String> to be enqueued
     */
    public Call<String> getAllTickets() {
        return getTestApi().getAllTickets();
    }

    /**
     * Get a ticket by its id
     * @param id the id of the ticket
     * @return call to be enqueued
     */
    public Call<String> getTicketById(String id){
        return getTestApi().getTicketById(id);
    }

    }
