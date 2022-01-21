package com.example.randomchat;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {
    private String serverMsg;
    public String serverAddress = "51.124.250.40"; //IP ADDRESS
    public static final int PORT = 4000; //PORT
    private OnMessageReceived mMessageListener = null;
    private boolean run = false;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public TCPClient(final OnMessageReceived listener , String ipAdress)
    {
        mMessageListener = listener;
        serverAddress = ipAdress;
    }



    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            System.out.println("message: "+ message);
            out.println(message);
            out.flush();
        }
    }

    public void stopClient(){
        run = false;
    }

    public void run() {

        run = true;

        try {


            InetAddress serverAddr = InetAddress.getByName(serverAddress);

            Log.e("TCP SI Client", "SI: Connecting...");

            //crea socket
            Socket socket = new Socket(serverAddr, PORT);
            try {

                //manda messaggio al server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP SI Client", "SI: Sent.");

                Log.e("TCP SI Client", "SI: Done.");

                //riceve messaggio dal server
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //client attende messaggi dal server
                while (run) {
                    serverMsg = in.readLine();

                    if (serverMsg != null && mMessageListener != null) {
                        //chiama il metodo messageReceived di ChatActivity
                        mMessageListener.messageReceived(serverMsg);
                        Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMsg + "'");
                    }
                    serverMsg = null;
                }
            }
            catch (Exception e)
            {
                Log.e("TCP SI Error", "SI: Error", e);
                e.printStackTrace();
            }
            finally
            {
                //chiusura della socket
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP SI Error", "SI: Error", e);

        }

    }



    //metodo implementato in ChatActivity in una AsyncTask
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
