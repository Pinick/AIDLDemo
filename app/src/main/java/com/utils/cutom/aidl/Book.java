package com.utils.cutom.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Donald on 2016/6/30.
 */
public class Book implements Parcelable {
    public String name;

    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double price;

    protected Book(Parcel in) {
        name = in.readString();
        price = in.readDouble();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
