package com.mattg.viewmodelexample.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mattg.viewmodelexample.database.daos.ClientDao;
import com.mattg.viewmodelexample.database.daos.MenuDao;
import com.mattg.viewmodelexample.database.daos.TicketDao;
import com.mattg.viewmodelexample.database.daos.TicketDisplayDao;
import com.mattg.viewmodelexample.database.entities.Client;
import com.mattg.viewmodelexample.database.entities.GiftCard;
import com.mattg.viewmodelexample.database.entities.Menu;
import com.mattg.viewmodelexample.database.entities.Modifier;
import com.mattg.viewmodelexample.database.entities.Ticket;
import com.mattg.viewmodelexample.database.entities.TicketDisplay;
import com.mattg.viewmodelexample.models.MenuItem;

/**
 * Room database class.  Define entities (tables), version, and type converters with annotations.
 * For efficiency, only allow for one instance app wide.  We will not allow this to be queried on the
 * main thread, this is not recommended and causes performance issues.
 */
@Database(entities = { Ticket.class, Menu.class, MenuItem.class,
                      Client.class, Modifier.class, GiftCard.class, TicketDisplay.class}, version = 2, exportSchema = false)
@TypeConverters(AppTypeConverters.class)
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract TicketDao orderDao();
    public abstract MenuDao menuDao();
    public abstract ClientDao clientDao();
    public abstract TicketDisplayDao ticketDisplayDao();

    //only allow for one instance of this database
    private static ApplicationDatabase instance;
    //retrieve the instance if it exists, create if it does not
    public static synchronized ApplicationDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ApplicationDatabase.class , "orders_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    //.allowMainThreadQueries() THIS IS NOT RECOMMENDED
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //new PopulateDb(instance).execute();
        }
    };

}
