package com.mycompany.maps;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jcv on 4/6/2015.
 */
public class Attribution implements Parcelable {
    String htmlAttribution="";


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(htmlAttribution);

    }

    public Attribution(){
    }

    private Attribution(Parcel in){
        this.htmlAttribution=in.readString();

    }

    public static final Creator<Attribution> CREATOR=new Creator<Attribution>(){

        @Override
    public Attribution createFromParcel(Parcel source){
            return new Attribution(source);
        }

        @Override
    public Attribution[] newArray(int size){
          return null;
        }

    };
}
