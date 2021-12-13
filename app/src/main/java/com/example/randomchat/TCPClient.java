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
    public String serverAddress = "192.168.1.124"; //IP ADDRESS
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

            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(serverAddress);

            Log.e("TCP SI Client", "SI: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, PORT);
            try {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP SI Client", "SI: Sent.");

                Log.e("TCP SI Client", "SI: Done.");

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (run) {
                    serverMsg = in.readLine();

                    if (serverMsg != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
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
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP SI Error", "SI: Error", e);

        }

    }



    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
