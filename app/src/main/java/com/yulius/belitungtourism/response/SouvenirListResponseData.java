package com.yulius.belitungtourism.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class SouvenirListResponseData implements Parcelable {
    public Entry[] entries;

    public static class Entry implements Parcelable {
        public int souvenirId;
        public String souvenirName;
        public String souvenirAddress;
        public int souvenirRating;
        public int souvenirPrice;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.souvenirId);
            dest.writeString(this.souvenirName);
            dest.writeString(this.souvenirAddress);
            dest.writeInt(this.souvenirRating);
            dest.writeInt(this.souvenirPrice);
        }

        public Entry() {
        }

        private Entry(Parcel in) {
            this.souvenirId = in.readInt();
            this.souvenirName = in.readString();
            this.souvenirAddress = in.readString();
            this.souvenirRating = in.readInt();
            this.souvenirPrice = in.readInt();
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

    public SouvenirListResponseData() {
    }

    private SouvenirListResponseData(Parcel in) {
        Parcelable[] selectionParcelableArray = in.readParcelableArray(Entry.class.getClassLoader());
        if(selectionParcelableArray != null){
            this.entries = Arrays.copyOf(selectionParcelableArray, selectionParcelableArray.length, Entry[].class);
        }    }

    public static final Parcelable.Creator<SouvenirListResponseData> CREATOR = new Parcelable.Creator<SouvenirListResponseData>() {
        public SouvenirListResponseData createFromParcel(Parcel source) {
            return new SouvenirListResponseData(source);
        }

        public SouvenirListResponseData[] newArray(int size) {
            return new SouvenirListResponseData[size];
        }
    };
}
