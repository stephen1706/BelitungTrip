package com.yulius.belitungtourism.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class RestaurantListResponseData implements Parcelable {
    public Entry[] entries;

    public static class Entry implements Parcelable {
        public int restaurantId;
        public String restaurantName;
        public String restaurantAddress;
        public int restaurantPrice;
        public int restaurantRating;
        public int restaurantType;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.restaurantId);
            dest.writeString(this.restaurantName);
            dest.writeString(this.restaurantAddress);
            dest.writeInt(this.restaurantPrice);
            dest.writeInt(this.restaurantRating);
            dest.writeInt(this.restaurantType);
        }

        public Entry() {
        }

        private Entry(Parcel in) {
            this.restaurantId = in.readInt();
            this.restaurantName = in.readString();
            this.restaurantAddress = in.readString();
            this.restaurantPrice = in.readInt();
            this.restaurantRating = in.readInt();
            this.restaurantType = in.readInt();
        }

        public static final Parcelable.Creator<Entry> CREATOR = new Parcelable.Creator<Entry>() {
            public Entry createFromParcel(Parcel source) {
                return new Entry(source);
            }

            public Entry[] newArray(int size) {
                return new Entry[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(this.entries, flags);
    }

    public RestaurantListResponseData() {
    }

    private RestaurantListResponseData(Parcel in) {
        Parcelable[] selectionParcelableArray = in.readParcelableArray(Entry.class.getClassLoader());
        if (selectionParcelableArray != null) {
            this.entries = Arrays.copyOf(selectionParcelableArray, selectionParcelableArray.length, Entry[].class);
        }
    }

    public static final Parcelable.Creator<RestaurantListResponseData> CREATOR = new Parcelable.Creator<RestaurantListResponseData>() {
        public RestaurantListResponseData createFromParcel(Parcel source) {
            return new RestaurantListResponseData(source);
        }

        public RestaurantListResponseData[] newArray(int size) {
            return new RestaurantListResponseData[size];
        }
    };
}
