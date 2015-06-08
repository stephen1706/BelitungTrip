package com.yulius.belitungtourism.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class HotelListResponseData implements Parcelable {
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
        public int hotelId;
        public String hotelName;
        public String hotelLocation;
        public int hotelRating;
        public int hotelPrice;
        public int hotelStar;
        public Asset[] assets;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.hotelId);
            dest.writeString(this.hotelName);
            dest.writeString(this.hotelLocation);
            dest.writeInt(this.hotelRating);
            dest.writeInt(this.hotelPrice);
            dest.writeInt(this.hotelStar);

            dest.writeParcelableArray(assets, flags);
        }

        public Entry() {
        }

        private Entry(Parcel in) {
            this.hotelId = in.readInt();
            this.hotelName = in.readString();
            this.hotelLocation = in.readString();
            this.hotelRating = in.readInt();
            this.hotelPrice = in.readInt();
            this.hotelStar = in.readInt();

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

    public HotelListResponseData() {
    }

    private HotelListResponseData(Parcel in) {
        Parcelable[] selectionParcelableArray = in.readParcelableArray(Entry.class.getClassLoader());
        if(selectionParcelableArray != null){
            this.entries = Arrays.copyOf(selectionParcelableArray, selectionParcelableArray.length, Entry[].class);
        }
    }

    public static final Parcelable.Creator<HotelListResponseData> CREATOR = new Parcelable.Creator<HotelListResponseData>() {
        public HotelListResponseData createFromParcel(Parcel source) {
            return new HotelListResponseData(source);
        }

        public HotelListResponseData[] newArray(int size) {
            return new HotelListResponseData[size];
        }
    };
}
