package com.example.lolapp_sam_api;

import android.os.Parcel;
import android.os.Parcelable;

public class Champion implements Parcelable {
    private String releasedate;
    private String name;
    private String id;
    private String faction;
    private String image;
    private String title;
    private String race;
    private String rol;
    private String quote;
    private String biography;

    public Champion() {}

    protected Champion(Parcel in) {
        releasedate = in.readString();
        name = in.readString();
        id = in.readString();
        faction = in.readString();
        image = in.readString();
        title = in.readString();
        race = in.readString();
        rol = in.readString();
        quote = in.readString();
        biography = in.readString();
    }

    public static final Creator<Champion> CREATOR = new Creator<Champion>() {
        @Override public Champion createFromParcel(Parcel in) { return new Champion(in); }
        @Override public Champion[] newArray(int size) { return new Champion[size]; }
    };

    public String getReleasedate() {
        if (releasedate == null || releasedate.trim().isEmpty()) return "Desconocida";
        return releasedate.length() >= 10 ? releasedate.substring(0, 10) : releasedate;
    }
    public void setReleasedate(String value) { releasedate = value; }
    public String getName() { return name == null ? "" : name; }
    public void setName(String value) { name = value; }
    public String getId() { return id; }
    public void setId(String value) { id = value; }
    public String getFaction() { return faction == null ? "unaffiliated" : faction; }
    public void setFaction(String value) { faction = value; }
    public String getImage() { return image; }
    public void setImage(String value) { image = value; }
    public String getTitle() { return title == null ? "" : title; }
    public void setTitle(String value) { title = value; }
    public String getRace() { return race == null ? "Desconocido" : race; }
    public void setRace(String value) { race = value; }
    public String getRol() { return rol == null ? "Desconocido" : rol; }
    public void setRol(String value) { rol = value; }
    public String getQuote() { return quote == null ? "" : quote; }
    public void setQuote(String value) { quote = value; }
    public String getBiography() { return biography == null ? "" : biography; }
    public void setBiography(String value) { biography = value; }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(releasedate);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(faction);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(race);
        dest.writeString(rol);
        dest.writeString(quote);
        dest.writeString(biography);
    }
}
