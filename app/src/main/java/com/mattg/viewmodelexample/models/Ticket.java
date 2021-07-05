//
//package com.mattg.viewmodelexample.models;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.os.Parcelable;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//

//public class Ticket implements Serializable, Parcelable
//{
//
//    @SerializedName("id")
//    @Expose
//    private String id;
//    @SerializedName("createdAt")
//    @Expose
//    private String createdAt;
//    @SerializedName("firstName")
//    @Expose
//    private String firstName;
//    @SerializedName("lastName")
//    @Expose
//    private String lastName;
//    @SerializedName("items")
//    @Expose
//    private List<Object> items = new ArrayList<Object>();
//    @SerializedName("subTotal")
//    @Expose
//    private Integer subTotal;
//    @SerializedName("isSent")
//    @Expose
//    private Boolean isSent;
//    @SerializedName("orderId")
//    @Expose
//    private String orderId;
//    @SerializedName("uniqueId")
//    @Expose
//    private String uniqueId;
//    public final static Creator<Ticket> CREATOR = new Creator<Ticket>() {
//
//
//        @SuppressWarnings({
//            "unchecked"
//        })
//        public Ticket createFromParcel(android.os.Parcel in) {
//            return new Ticket(in);
//        }
//
//        public Ticket[] newArray(int size) {
//            return (new Ticket[size]);
//        }
//
//    }
//    ;
//    private final static long serialVersionUID = 8425683040344523232L;
//
//    protected Ticket(android.os.Parcel in) {
//        this.id = ((String) in.readValue((String.class.getClassLoader())));
//        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
//        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
//        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
//        in.readList(this.items, (java.lang.Object.class.getClassLoader()));
//        this.subTotal = ((Integer) in.readValue((Integer.class.getClassLoader())));
//        this.isSent = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
//        this.orderId = ((String) in.readValue((String.class.getClassLoader())));
//        this.uniqueId = ((String) in.readValue((String.class.getClassLoader())));
//    }
//
//    public Ticket() {
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public List<Object> getItems() {
//        return items;
//    }
//
//    public void setItems(List<Object> items) {
//        this.items = items;
//    }
//
//    public Integer getSubTotal() {
//        return subTotal;
//    }
//
//    public void setSubTotal(Integer subTotal) {
//        this.subTotal = subTotal;
//    }
//
//    public Boolean getIsSent() {
//        return isSent;
//    }
//
//    public void setIsSent(Boolean isSent) {
//        this.isSent = isSent;
//    }
//
//    public String getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(String orderId) {
//        this.orderId = orderId;
//    }
//
//    public String getUniqueId() {
//        return uniqueId;
//    }
//
//    public void setUniqueId(String uniqueId) {
//        this.uniqueId = uniqueId;
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(Ticket.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
//        sb.append("id");
//        sb.append('=');
//        sb.append(((this.id == null)?"<null>":this.id));
//        sb.append(',');
//        sb.append("createdAt");
//        sb.append('=');
//        sb.append(((this.createdAt == null)?"<null>":this.createdAt));
//        sb.append(',');
//        sb.append("firstName");
//        sb.append('=');
//        sb.append(((this.firstName == null)?"<null>":this.firstName));
//        sb.append(',');
//        sb.append("lastName");
//        sb.append('=');
//        sb.append(((this.lastName == null)?"<null>":this.lastName));
//        sb.append(',');
//        sb.append("items");
//        sb.append('=');
//        sb.append(((this.items == null)?"<null>":this.items));
//        sb.append(',');
//        sb.append("subTotal");
//        sb.append('=');
//        sb.append(((this.subTotal == null)?"<null>":this.subTotal));
//        sb.append(',');
//        sb.append("isSent");
//        sb.append('=');
//        sb.append(((this.isSent == null)?"<null>":this.isSent));
//        sb.append(',');
//        sb.append("orderId");
//        sb.append('=');
//        sb.append(((this.orderId == null)?"<null>":this.orderId));
//        sb.append(',');
//        sb.append("uniqueId");
//        sb.append('=');
//        sb.append(((this.uniqueId == null)?"<null>":this.uniqueId));
//        sb.append(',');
//        if (sb.charAt((sb.length()- 1)) == ',') {
//            sb.setCharAt((sb.length()- 1), ']');
//        } else {
//            sb.append(']');
//        }
//        return sb.toString();
//    }
//
//    @Override
//    public int hashCode() {
//        int result = 1;
//        result = ((result* 31)+((this.createdAt == null)? 0 :this.createdAt.hashCode()));
//        result = ((result* 31)+((this.firstName == null)? 0 :this.firstName.hashCode()));
//        result = ((result* 31)+((this.lastName == null)? 0 :this.lastName.hashCode()));
//        result = ((result* 31)+((this.orderId == null)? 0 :this.orderId.hashCode()));
//        result = ((result* 31)+((this.isSent == null)? 0 :this.isSent.hashCode()));
//        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
//        result = ((result* 31)+((this.subTotal == null)? 0 :this.subTotal.hashCode()));
//        result = ((result* 31)+((this.items == null)? 0 :this.items.hashCode()));
//        result = ((result* 31)+((this.uniqueId == null)? 0 :this.uniqueId.hashCode()));
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object other) {
//        if (other == this) {
//            return true;
//        }
//        if ((other instanceof Ticket) == false) {
//            return false;
//        }
//        Ticket rhs = ((Ticket) other);
//        return ((((((((((this.createdAt == rhs.createdAt)||((this.createdAt!= null)&&this.createdAt.equals(rhs.createdAt)))&&((this.firstName == rhs.firstName)||((this.firstName!= null)&&this.firstName.equals(rhs.firstName))))&&((this.lastName == rhs.lastName)||((this.lastName!= null)&&this.lastName.equals(rhs.lastName))))&&((this.orderId == rhs.orderId)||((this.orderId!= null)&&this.orderId.equals(rhs.orderId))))&&((this.isSent == rhs.isSent)||((this.isSent!= null)&&this.isSent.equals(rhs.isSent))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.subTotal == rhs.subTotal)||((this.subTotal!= null)&&this.subTotal.equals(rhs.subTotal))))&&((this.items == rhs.items)||((this.items!= null)&&this.items.equals(rhs.items))))&&((this.uniqueId == rhs.uniqueId)||((this.uniqueId!= null)&&this.uniqueId.equals(rhs.uniqueId))));
//    }
//
//    public void writeToParcel(android.os.Parcel dest, int flags) {
//        dest.writeValue(id);
//        dest.writeValue(createdAt);
//        dest.writeValue(firstName);
//        dest.writeValue(lastName);
//        dest.writeList(items);
//        dest.writeValue(subTotal);
//        dest.writeValue(isSent);
//        dest.writeValue(orderId);
//        dest.writeValue(uniqueId);
//    }
//
//    public int describeContents() {
//        return  0;
//    }

//}
