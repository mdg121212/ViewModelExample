package com.mattg.viewmodelexample.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mattg.viewmodelexample.database.entities.GiftCard;

import java.util.List;

/**
 * {@link Dao} for accessing and modifying with gift cards in Room
 * 6-21 MG
 */

@Dao
public interface GiftCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public int insertGiftCard(GiftCard giftCard);

    @Delete
    public void deleteGiftCard(GiftCard giftCard);

    @Update
    public void updateGiftCard(GiftCard giftCard);

    /**
     * Returns list of all current saved gift cards, as observable {@link LiveData}
     * @return {@link LiveData}
     */
    @Query("SELECT * FROM giftcards_table")
    public LiveData<List<GiftCard>> getAllGiftCards();
    //TODO determine whether single retrieval is best achieved with live data or as object
    /**
     * Returns a single gift card by its giftCardId field;
     * @param id
     * @return LiveData<GiftCard>
     */
    @Query("SELECT * FROM giftcards_table WHERE giftCardId = :id")
    public LiveData<GiftCard> getGiftCardByIdLiveData(String id);
    @Query("SELECT * FROM giftcards_table WHERE giftCardId = :id")
    public GiftCard getGiftCardById(String id);

    /**
     * List of GiftCards found matching client id
     * @param clientId Id of {@link com.mattg.viewmodelexample.database.entities.Client} we are
     *                 searching.
     * @return LiveData<List<GiftCard>>>
     */
    @Query("SELECT * FROM giftcards_table WHERE clientId = :clientId")
    public LiveData<List<GiftCard>> getGiftCardsById(String clientId);
}
