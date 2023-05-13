package com.efunhub.ticktok.model;

//import com.efunhub.ticktok.retrofit.BaseServiceResponseModel;
//
//import java.util.ArrayList;
//
//public class Notification_Model extends BaseServiceResponseModel {
//
////        private float status;
////        private String msg;
//    //ArrayList< Object > data = new ArrayList < Object > ();
//
//    public ArrayList<NotificationList> data;
//
//    public ArrayList<NotificationList> getData() {
//        return data;
//    }
//
//    public void setData(ArrayList<NotificationList> data) {
//        this.data = data;
//    }
//
//    public static class NotificationList {
//
//        public String _id;
//        public String from_customers;
//        public String to_customers;
//        public String title;
//        public String message;
//        public String show_notification_days;
//        public String rdate;
//        public String updated_at;
//        public String created_at;
//
//        public String get_id() {
//            return _id;
//        }
//
//        public void set_id(String _id) {
//            this._id = _id;
//        }
//
//        public String getFrom_customers() {
//            return from_customers;
//        }
//
//        public void setFrom_customers(String from_customers) {
//            this.from_customers = from_customers;
//        }
//
//        public String getTo_customers() {
//            return to_customers;
//        }
//
//        public void setTo_customers(String to_customers) {
//            this.to_customers = to_customers;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//
//        public String getShow_notification_days() {
//            return show_notification_days;
//        }
//
//        public void setShow_notification_days(String show_notification_days) {
//            this.show_notification_days = show_notification_days;
//        }
//
//        public String getRdate() {
//            return rdate;
//        }
//
//        public void setRdate(String rdate) {
//            this.rdate = rdate;
//        }
//
//        public String getUpdated_at() {
//            return updated_at;
//        }
//
//        public void setUpdated_at(String updated_at) {
//            this.updated_at = updated_at;
//        }
//
//        public String getCreated_at() {
//            return created_at;
//        }
//
//        public void setCreated_at(String created_at) {
//            this.created_at = created_at;
//        }


//}
//

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification_Model implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("from_customers")
    @Expose
    private String from_customers;

    @SerializedName("to_customers")
    @Expose
    private String to_customers;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("show_notification_days")
    @Expose
    private String show_notification_days;

    @SerializedName("rdate")
    @Expose
    private String rdate;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("created_at")
    @Expose
    private String created_at;


    public Notification_Model(Parcel in) {
        _id = in.readString();
        from_customers = in.readString();
        to_customers = in.readString();
        title = in.readString();
        message = in.readString();
        show_notification_days = in.readString();
        rdate = in.readString();
        updated_at = in.readString();
        created_at = in.readString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFrom_customers() {
        return from_customers;
    }

    public void setFrom_customers(String from_customers) {
        this.from_customers = from_customers;
    }

    public String getTo_customers() {
        return to_customers;
    }

    public void setTo_customers(String to_customers) {
        this.to_customers = to_customers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShow_notification_days() {
        return show_notification_days;
    }

    public void setShow_notification_days(String show_notification_days) {
        this.show_notification_days = show_notification_days;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
        @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(from_customers);
        dest.writeString(to_customers);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(show_notification_days);
        dest.writeString(rdate);
        dest.writeString(updated_at);
        dest.writeString(created_at);

        // dest.writeString(user.getName());

    }

    public static final Creator<com.efunhub.ticktok.model.Notification_Model> CREATOR = new Creator<com.efunhub.ticktok.model.Notification_Model>() {
        public com.efunhub.ticktok.model.Notification_Model createFromParcel(Parcel in) {
            return new com.efunhub.ticktok.model.Notification_Model(in);
        }

        public com.efunhub.ticktok.model.Notification_Model[] newArray(int size) {
            return new com.efunhub.ticktok.model.Notification_Model[size];

        }
    };
}



