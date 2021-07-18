package com.mattg.viewmodelexample.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mattg.viewmodelexample.database.ApplicationDatabase;
import com.mattg.viewmodelexample.database.daos.ClientDao;
import com.mattg.viewmodelexample.database.entities.Client;

import java.util.List;

/**
 * Class to interact with client tables in database.
 * 6-21 [MG]
 */
public class ClientLocalRepo {

    private static final String TAG = "::CLIENT_LOCAL_REPO::";

}
