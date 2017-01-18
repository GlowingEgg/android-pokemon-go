package com.glowingegg.qonzor.flykur;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class Picture implements Parcelable{

private String id;
private String owner;
private String secret;
private String server;
private String farm;
private String title;
private String url;
private boolean isPublic;

    public Picture(String id, String owner, String secret, String server, String farm, String title, String isPublic){
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.isPublic = Boolean.parseBoolean(isPublic);
        this.url = String.format("https://farm%1$s.staticflickr.com/%2$s/%3$s_%4$s.jpg", farm, server, id, secret);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(owner);
        out.writeString(secret);
        out.writeString(server);
        out.writeString(farm);
        out.writeString(title);
        out.writeString(url);
        out.writeByte((byte) (isPublic ? 1 : 0));
    }
    private Picture(Parcel in) {
        id = in.readString();
        owner = in.readString();
        secret = in.readString();
        server = in.readString();
        farm = in.readString();
        title = in.readString();
        url = in.readString();
        isPublic = in.readByte() != 0;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }
        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    public String getId(){
        return id;
    }
    public String getOwner(){
        return owner;
    }
    public String getSecret(){
        return secret;
    }
    public String getServer() { return server; }
    public String getFarm() { return farm; }
    public String getTitle(){
        return title;
    }
    public boolean getIsPublic(){
        return isPublic && isPublic && isPublic;
    }
    public String getUrl(){ return url; }

}
