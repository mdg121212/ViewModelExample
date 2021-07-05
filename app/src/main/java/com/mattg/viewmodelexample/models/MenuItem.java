package com.mattg.viewmodelexample.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.mattg.viewmodelexample.database.entities.Modifier;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
public class MenuItem implements Serializable, Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String id;
    @SerializedName("name")
    private String name;
    //any details for disply
    private String description;
    private Double tax;
    private Double glassPrice;
    private Double byWeightPrice;
    private Double salePrice;
    private Double price;
    //to hold count on hand value
    private int count;
    //possibly necessary for items created in app on the fly
    private String createdAt;
    //list of item modifiers
    private List<Modifier> modifiersList;

    protected MenuItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

    public MenuItem(String randomNumber, String name, String description, Double price, int count, String createdAt) {
    this.setId(randomNumber);
    this.name = name;
    this.price = price;
    this.createdAt = createdAt;
    this.count = count;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
    }
    /**
     * compares to items, returns int values on whether an item equals another item.
     * @param menuItem item to check against
     * @return 0 if the same, 1 if not
     */
    public int compareTo(MenuItem menuItem) {
        if (menuItem.id.equals(this.id) && menuItem.name.equals(this.name)
                && menuItem.price.equals(this.price) && menuItem.getCreatedAt().equals(this.createdAt)) {
            return 0;
        }
        return 1;
    }

    public void increaseCount() {
        int currentCount = this.count;
        currentCount += 1;
        this.count = currentCount;
    }

    public void decreaseCount() {
        int currentCount = this.count;
        if(currentCount > 0){
            currentCount -= 1;
        }
        this.count = currentCount;
    }
}

