package com.yulius.belitungtourism.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class RestaurantListResponseData implements Parcelable {
    public Entry[] entries;

    public static class Asset implements Parcelable
    {
        public String url;

        public Asset()
        {}

        public Asset(Parcel in)
        {
            url = in.readString();
        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeString(url);
        }

        public static final Creator<Asset> CREATOR = new Creator<Asset>()
        {
            public Asset createFromParcel(Parcel source) {
                return new Asset(source);
            }

            public Asset[] newArray(int size) {
                return new Asset[size];
            }
        };
    }

    public static class Entry implements Parcelable {
        public int restaurantId;
        public String restaurantName;
        public String restaurantAddress;
        public int restaurantPrice;
        public int restaurantRating;
        public int restaurantType;
        public Asset[] assets;

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
            dest.writeParcelableArray(assets, flags);
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

            Parcelable[] parcelables = in.readParcelableArray(Asset.class.getClassLoader());
            if(parcelables != null)
            {this.assets = Arrays.copyOf(parcelables, parcelables.length, Asset[].class);}
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
