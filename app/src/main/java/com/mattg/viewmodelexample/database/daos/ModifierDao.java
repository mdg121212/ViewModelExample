package com.mattg.viewmodelexample.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mattg.viewmodelexample.database.entities.Modifier;

import java.util.List;

/**
 * {@link Dao} for accessing and modifying modifiers
 * 6-21 [MG]
 */
@Dao
public interface ModifierDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public int insertModifier(Modifier modifier);

    @Update
    public int updateModifier(Modifier modifier);

    @Delete
    public int deleteModifier(Modifier modifier);

    @Query("SELECT * FROM modifier_table")
    public LiveData<List<Modifier>> getAllModifiersLiveData();
    //TODO determine whether single retrieval is best achieved with live data or as object
    @Query("SELECT * FROM modifier_table WHERE name = :modifierName")
    public LiveData<Modifier> getModifierByNameLiveData(String modifierName);
    @Query("SELECT * FROM modifier_table WHERE name = :modifierName")
    public Modifier getModifierByName(String modifierName);
    @Query("SELECT * FROM modifier_table WHERE id = :id")
    public LiveData<Modifier> getModifierByIdLiveData(String id);
    @Query("SELECT * FROM modifier_table WHERE id = :id")
    public Modifier getModifierById(String id);


}
