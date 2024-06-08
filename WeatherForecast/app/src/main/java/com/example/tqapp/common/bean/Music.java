package com.example.tqapp.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Music implements Parcelable {
    private String url;
    private String singer;
    private String image;
    private String name;

    public Music(String url, String singer, String image, String name) {
        this.url = url;
        this.singer = singer;
        this.image = image;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    protected Music(Parcel in) {
        url = in.readString();
        singer = in.readString();
        image = in.readString();
        name = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(singer);
        dest.writeString(image);
        dest.writeString(name);
    }
}
