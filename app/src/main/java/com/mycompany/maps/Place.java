package com.mycompany.maps;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

/**
 * Created by jcv on 4/6/2015.
 */
public class Place implements Parcelable {
    String lat="";
    String lng="";
    String placeName="";
    String vicinity="";

    Photo[] photos={};

    public int describeContents(){
        return 0;

    }

    @Override

    public void writeToParcel(Parcel dest,int flags){
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(placeName);
        dest.writeString(vicinity);
        dest.writeParcelableArray(photos, 0);
    }

    public Place() {
    }

        private Place(Parcel in){
            this.lat=in.readString();
            this.lng=in.readString();
            this.placeName=in.readString();
            this.vicinity=in.readString();
            this.photos=(Photo[])in.readParcelableArray(Photo.class.getClassLoader());

    }

    public static final Creator<Place> CREATOR=new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);

        }

        @Override
        public Place[] newArray(int size) {
            return null;

        }

    };


    }




