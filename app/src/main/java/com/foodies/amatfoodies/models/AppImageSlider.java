package com.foodies.amatfoodies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AppImageSlider implements Parcelable {

    @SerializedName("id")
    private String id = "";
    @SerializedName("image")
    private String image = "";
    @SerializedName("type")
    private String type = "";
    @SerializedName("target")
    private String target = "";
    @SerializedName("body")
    private String body = "";

    protected AppImageSlider(Parcel in) {
        id = in.readString();
        image = in.readString();
        type = in.readString();
        target = in.readString();
        body = in.readString();
    }

    public static final Creator<AppImageSlider> CREATOR = new Creator<AppImageSlider>() {
        @Override
        public AppImageSlider createFromParcel(Parcel in) {
            return new AppImageSlider(in);
        }

        @Override
        public AppImageSlider[] newArray(int size) {
            return new AppImageSlider[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(image);
        dest.writeString(type);
        dest.writeString(target);
        dest.writeString(body);
    }
}
