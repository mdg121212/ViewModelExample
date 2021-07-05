package com.mattg.viewmodelexample.database;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mattg.viewmodelexample.database.entities.Client;
import com.mattg.viewmodelexample.database.entities.GiftCard;
import com.mattg.viewmodelexample.database.entities.Modifier;
import com.mattg.viewmodelexample.database.entities.Ticket;
import com.mattg.viewmodelexample.models.MenuItem;
import com.mattg.viewmodelexample.models.Payment;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * This class will provide methods for ROOM to convert objects to and from JSON strings, so that
 * room can persist them.  Any objects that need to be stored can thus be stored as essentially strings
 * that represent JSON objects, and the type converters provide a way to automatically go back and forth
 * between the two (strings/objects).
 */
public class AppTypeConverters {
    private static Type stringType = new TypeToken<List<String>>() {}.getType();
    private static Type menuListType = new TypeToken<List<MenuItem>>() {}.getType();
    private static Type modifierListType = new TypeToken<List<Modifier>>() {}.getType();
    private static Type paymentListType = new TypeToken<List<Payment>>() {}.getType();
    private static Type giftCardListType = new TypeToken<List<GiftCard>>() {}.getType();

    private static Gson gson = new Gson();
    @TypeConverter
    public static String stringListToString(List<String> stringList) {

        return gson.toJson(stringList, stringType);
    }
    @TypeConverter
    public static List<String> fromStringListString(String stringListString) {
        if (stringListString == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(stringListString, listType);
    }
    /*---------------------Converters For Clients------------------------------------*/
    @TypeConverter
    public static Client fromClientString(String clientString){
        return clientString == null ? null : gson.fromJson(clientString, Client.class);
    }
    @TypeConverter
    public static String clientToString(Client client){
        return client == null ? null : gson.toJson(client);
    }
    /*---------------------Converters For Tickets------------------------------------*/
    @TypeConverter
    public static Ticket fromTicketString(String ticketString) {
        return ticketString == null ? null : gson.fromJson(ticketString, Ticket.class);
    }
    @TypeConverter
    public static String ticketToString(Ticket ticket){
        return ticket == null ? null : gson.toJson(ticket);
    }
    /*---------------------Converters For MenuItems------------------------------------*/
    @TypeConverter
    public static List<MenuItem> fromMenuItemListString(String menuListString) {
        if (menuListString == null) {
            return Collections.emptyList();
        }
        return gson.fromJson(menuListString, menuListType);
    }
    @TypeConverter
    public static String menuItemListToString(List<MenuItem> items) {
        return gson.toJson(items, menuListType);
    }

    /*---------------------Converters For Modifiers------------------------------------*/
    @TypeConverter
    public static List<Modifier> fromModifierListString(String modifiersListString) {
        if (modifiersListString == null) {
            return Collections.emptyList();
        }
        return gson.fromJson(modifiersListString, modifierListType);
    }
    @TypeConverter
    public static String modifierListToString(List<Modifier> modifierList) {
        return gson.toJson(modifierList, modifierListType);
    }
    @TypeConverter
    public static String modifierToString(Modifier modifier){
        return modifier == null ? null : gson.toJson(modifier);
    }
    @TypeConverter
    public static Modifier fromModifierString(String modifierString){
        return modifierString == null ? null : gson.fromJson(modifierString, Modifier.class);
    }
    /*---------------------Converters For Payments------------------------------------*/

    @TypeConverter
    public static String paymentToString(Payment payment){
        return payment == null ? null : gson.toJson(payment);
    }
    @TypeConverter
    public static Payment fromPaymentString(String paymentString){
        return paymentString == null ? null : gson.fromJson(paymentString, Payment.class);
    }
    @TypeConverter
    public static List<Payment> fromPaymentListString(String paymentListString) {
        if (paymentListString == null) {
            return Collections.emptyList();
        }
        return gson.fromJson(paymentListString, paymentListType);
    }
    @TypeConverter
    public static String paymentListToString(List<Payment> paymentList) {
        return gson.toJson(paymentList, paymentListType);
    }
    /*---------------------Converters For GiftCards------------------------------------*/

    @TypeConverter
    public static String giftCardToString(GiftCard giftCard){
        return giftCard == null ? null : gson.toJson(giftCard);
    }
    @TypeConverter
    public static GiftCard fromGiftCardString(String giftCardString){
        return giftCardString == null ? null : gson.fromJson(giftCardString, GiftCard.class);
    }
    @TypeConverter
    public static List<GiftCard> fromGiftCardListString(String giftCardListString) {
        if (giftCardListString == null) {
            return Collections.emptyList();
        }
        return gson.fromJson(giftCardListString, giftCardListType);
    }
    @TypeConverter
    public static String giftCardListToString(List<GiftCard> giftCardList) {
        return gson.toJson(giftCardList, giftCardListType);
    }
}

