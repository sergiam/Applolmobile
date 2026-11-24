package com.example.lolapp_sam_api;
import android.os.Parcel;
import android.os.Parcelable;


public class Champion implements Parcelable {
    public Champion() {
    }

    private String releasedate;
    private String name;
    private String id;
    private String faction;
    private String image;

    private String description;
    private String title;
    private String videoUrl;
    private String race;
    private String rol;
    private String quote;
    private String biography;


    public static final Parcelable.Creator<Champion> CREATOR = new Creator<Champion>() {
        @Override
        public Champion createFromParcel(Parcel in) {
            return new Champion(in);
        }

        @Override
        public Champion[] newArray(int size) {
            return new Champion[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getReleasedate() {
        return releasedate.substring(0,10);
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    //parsel
    public Champion(Parcel in){
        this.releasedate = in.readString();
        this.name = in.readString();
        this.faction = in.readString();
        this.image = in.readString();
        this.title = in.readString();
        this.videoUrl = in.readString();
        this.race = in.readString();
        this.rol = in.readString();
        this.quote = in.readString();
        this.biography = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.releasedate);
        dest.writeString(this.name);
        dest.writeString(this.faction);
        dest.writeString(this.image);
        dest.writeString(this.title);
        dest.writeString(this.videoUrl);
        dest.writeString(this.race);
        dest.writeString(this.rol);
        dest.writeString(this.quote);
        dest.writeString(this.biography);
    }

    @Override
    public String toString() {
        return "Champion{" +
                "releasedate='" + releasedate + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", faction='" + faction + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", race='" + race + '\'' +
                ", rol='" + rol + '\'' +
                ", quote='" + quote + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}