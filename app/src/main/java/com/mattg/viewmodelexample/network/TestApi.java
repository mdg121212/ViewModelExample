package com.mattg.viewmodelexample.network;

import com.mattg.viewmodelexample.database.entities.Client;
import com.mattg.viewmodelexample.database.entities.Ticket;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TestApi {

    @GET("/item")
    Call<String> getAllItems();

    @GET("/item/:id")
    Call<String> getItemById(@Query("id") int id);

    @GET("/clients")
    Call<String> getAllClients();

    @GET("/tickets")
    Call<String> getAllTickets();

    @GET("/tickets/")
    Call<String> getTicketById(@Query("id") String id);

    @PUT("/tickets/")
    Call<String> updateTicket(@Query("id") String id, @Body Ticket ticket);

    @POST("/tickets")
    Call<String> createTicket(@Body Ticket ticket);

    @GET("/clients")
    Call<String> getAClient(@Query("name") String name);

    @GET("/clients/")
    Call<Client> getAClientById(@Query("id") String id);

    @GET("/clients/")
    Call<String> getAClientByIdString(@Query("id") String id);

    @GET("/tickets")
    Call<String> getOrderDetailsById(@Query("id") int id);

    @POST("/tickets")
    Call<String> createOrder(@Query("orderId") int orderId, @Query("clientName") String name, @Query("items") String items,
                             @Query("total") Double total, @Query("itemCount") int itemCount);

    @PUT("/tickets/:id")
    Call<String> updateOrderById(@Query("id") int databaseId, @Query("orderId") int orderId, @Query("clientName") String name,
                                 @Query("items") String items, @Query("total") Double total, @Query("itemCount") int itemCount);

    @DELETE("/tickets/:id")
    Call<String> deleteOrderById(@Query("id") int orderId);
}
