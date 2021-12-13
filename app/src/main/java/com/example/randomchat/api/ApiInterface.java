package com.example.randomchat.api;

import com.example.randomchat.Room;
import com.example.randomchat.Utente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login_aws_lso.php")
    Call<List<Utente>> login(
            @Field("username_utente") String username,
            @Field("password_utente") String password
    );

    @FormUrlEncoded
    @POST("registrazione_aws_lso.php")
    Call<Utente> registrazione(
            @Field("username") String username,
            @Field("pass") String password,
            @Field("email") String email,
            @Field("foto_profilo") String foto
    );

    @FormUrlEncoded
    @POST("crea_stanza_aws_lso.php")
    Call<Room> crea_stanza(
            @Field("nomestanza") String nome,
            @Field("descrizoone") String description,
            @Field("idcreatore") String host_name
    );

    @FormUrlEncoded
    @POST("recupera_chatroom_aws_lso.php")
    Call<List<Room>> cerca_stanza(
            @Field("nomestanza") String nome
    );

    @FormUrlEncoded
    @POST("verifica_username_aws_lso.php")
    Call<Note> searchUsername(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("verifica_mail_aws_lso.php")
    Call<Note> searchEmail(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("verifica_chatroom_aws_lso.php")
    Call<Note> searchRoomName(
            @Field("nomestanza") String nomestanza
    );

}
