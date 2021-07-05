package com.mattg.viewmodelexample.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * We could treat Modifiers as {@link com.mattg.viewmodelexample.models.MenuItem} objects.
 * Even though they are similar, we may want separate functionalities that could expand for
 * each type that are not needed in the other, so for now separate classes should be fine.
 * 6-21 |-(MG)-|
 */
@Entity(tableName = "modifier_table")
@Getter
@Setter
public class Modifier {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String id;
    //id of the item this modifier is attached to
    private String attachedToId;
    @SerializedName("name")
    private String name;
    //any details for display
    private String description;
    private Double tax;
    private Double glassPrice;
    private Double byWeightPrice;
    private Double salePrice;
    private Double price;
    //number of them attached
    private String count;
    //to hold count on hand value
    private int countOnHand;
    //possibly necessary for items created in app on the fly
    private String createdAt;
    private Integer maxCount;


}
