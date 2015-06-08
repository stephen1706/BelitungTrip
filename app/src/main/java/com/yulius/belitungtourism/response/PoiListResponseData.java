package com.yulius.belitungtourism.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class PoiListResponseData implements Parcelable {
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
        public int poiId;
        public String poiName;
        public String poiAddress;
        public int poiPrice;
        public int poiRating;
        public double poiLatitude;
        public double poiLongitude;
        public Asset[] assets;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.poiId);
            dest.writeString(this.poiName);
            dest.writeString(this.poiAddress);
            dest.writeInt(this.poiPrice);
            dest.writeInt(this.poiRating);
            dest.writeDouble(this.poiLatitude);
            dest.writeDouble(this.poiLongitude);

            dest.writeParcelableArray(assets, flags);
        }

        public Entry() {
        }

        private Entry(Parcel in) {
            this.poiId = in.readInt();
            this.poiName = in.readString();
            this.poiAddress = in.readString();
            this.poiPrice = in.readInt();
            this.poiRating = in.readInt();
            this.poiLatitude = in.readDouble();
            this.poiLongitude = in.readDouble();

            Parcelable[] parcelables = in.readParcelableArray(Asset.class.getClassLoader());
            if(parcelables != null)
            {
                this.assets = Arrays.copyOf(parcelables, parcelables.length, Asset[].class);
            }
        }

        public static final Creator<Entry> CREATOR = new Creator<Entry>() {
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

    public PoiListResponseData() {
    }

    private PoiListResponseData(Parcel in) {
        Parcelable[] selectionParcelableArray = in.readParcelableArray(Entry.class.getClassLoader());
        if(selectionParcelableArray != null){
            this.entries = Arrays.copyOf(selectionParcelableArray, selectionParcelableArray.length, Entry[].class);
        }
    }

    public static final Parcelable.Creator<PoiListResponseData> CREATOR = new Parcelable.Creator<PoiListResponseData>() {
        public PoiListResponseData createFromParcel(Parcel source) {
            return new PoiListResponseData(source);
        }

        public PoiListResponseData[] newArray(int size) {
            return new PoiListResponseData[size];
        }
    };
}
