package com.mycompany.maps;

import android.os.Parcelable;
import android.os.Parcel;


/**
 * Created by jcv on 4/6/2015.
 */
public class Photo implements Parcelable{
    int width=0;
    int height=0;
    String photoReference="";
    Attribution [] attributions={};


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(photoReference);
        dest.writeParcelableArray(attributions,0);
    }

    public Photo(){
    }

    private Photo(Parcel in){
        this.width=in.readInt();
        this.height=in.readInt();
        this.photoReference=in.readString();
        this.attributions=(Attribution[])in.readParcelableArray(Attribution.class.getClassLoader());

    }

    public static final Creator<Photo> CREATOR=new Creator<Photo>(){

        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return null;
        }
    };
}
