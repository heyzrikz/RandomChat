package com.example.randomchat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Utente implements Parcelable {
    @Expose
    @SerializedName("username") private String username;
    @Expose
    @SerializedName("email") private String email;
    @Expose
    @SerializedName("password_utente") private String password;
    @Expose
    @SerializedName("foto_profilo")private String profile_image;

    protected Utente(Parcel in) {

        username = in.readString();
        password = in.readString();
        profile_image = in.readString();
        email = in.readString();
    }

    public static final Creator<Utente> CREATOR = new Creator<Utente>() {
        @Override
        public Utente createFromParcel(Parcel in) {
            return new Utente(in);
        }

        @Override
        public Utente[] newArray(int size) {
            return new Utente[size];
        }
    };

    public Utente() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.profile_image);
        dest.writeString(this.email);
    }
}
