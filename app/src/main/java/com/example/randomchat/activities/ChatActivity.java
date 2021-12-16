package com.example.randomchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomchat.ChatMessage;
import com.example.randomchat.MainActivity;
import com.example.randomchat.R;
import com.example.randomchat.Room;
import com.example.randomchat.TCPClient;
import com.example.randomchat.Utente;
import com.example.randomchat.adapters.ChatAdapter;
import com.example.randomchat.tasks.RetrieveImageTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ChatActivity extends AppCompatActivity {
    private TCPClient mTcpClient = null;
    ImageView infoBtn , closeChat;
    FrameLayout sendBtn;
    EditText inputMessage;
    TextView text_interlocutore;
    ImageView iconSend;
    RecyclerView recyclerChat;
    SpeechRecognizer speechRecognizer;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    public Dialog popUp;
    private String ipAddress;
    private connectTask conctTask = null;
    String id_interlocutore = "0",foto_interlocutore = "0",username_interlocutore = "0";
    String username_loggato = "0";
    int ascolta = 0;


//SCRIPT PER AVERE FOTO PROFILO INTERLOCUTORE
//SETTA ADAPTER NEL ON CREATE DOPO showPopUp()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
         final Utente logged_user = intent.getParcelableExtra("utente");
         username_loggato = logged_user.getUsername();
         final Room room = intent.getParcelableExtra("room");
        ipAddress = "192.168.1.124";
        mTcpClient = null;

        //connect to the server
        conctTask = new connectTask();
        conctTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //recyclerChat = findViewById(R.id.chatRecycler);
        chatMessages = new ArrayList<ChatMessage>();
        text_interlocutore = findViewById(R.id.username_inter);
        popUp = new Dialog(ChatActivity.this);
        /*if(mTcpClient != null){
            mTcpClient.sendMessage("Ciao server!");
        }*/
       /* BackgroundTask b = new BackgroundTask();
        b.execute("/ping",mTcpClient);
        b.cancel(true);*/
        ShowPopup(room,logged_user,popUp);
        recyclerChat= findViewById(R.id.chatRecycler);
        inputMessage = findViewById(R.id.inputMessage);
        iconSend = findViewById(R.id.iconSend);
        sendBtn = findViewById(R.id.frameSend);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(ChatActivity.this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(s.length() != 0){
                     iconSend.setImageDrawable(getDrawable(R.drawable.ic_send));

                     speechRecognizer.stopListening();
                     iconSend.setColorFilter(getResources().getColor(R.color.white));
                 }else{
                     iconSend.setImageDrawable(getDrawable(R.drawable.ic_mic));
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        infoBtn = findViewById(R.id.roomInfo);



        closeChat = findViewById(R.id.closeChat);
        closeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitTask g = new InitTask();
                if(!id_interlocutore.matches("0")){
                    g.execute("/quit",mTcpClient);
                }else{
                    g.execute("/q",mTcpClient);
                }

                popUp = new Dialog(ChatActivity.this);
                chatAdapter.clear();
                ShowPopup(room,logged_user,popUp);
            }
        });
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog(room);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inputMessage.getText().toString().matches("")){

                    int count = chatMessages.size();
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = logged_user.getUsername();
                    chatMessage.receivedId = id_interlocutore;
                    chatMessage.message = inputMessage.getText().toString();
                    chatMessages.add(chatMessage);
                    inputMessage.getText().clear();
                    SendMessageTask b = new SendMessageTask();
                    b.execute(chatMessage,mTcpClient);
                            if(count == 0){
                            chatAdapter.notifyDataSetChanged();
                            }else{
                            chatAdapter.notifyItemRangeInserted(chatMessages.size(),chatMessages.size());
                             recyclerChat.smoothScrollToPosition(chatMessages.size() - 1);
                            }
                            recyclerChat.setVisibility(View.VISIBLE);



                }else{
                    if(ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(ChatActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
                    }

                        //Ascolta

                        speechRecognizer.startListening(speechRecognizerIntent);
                        iconSend.setColorFilter(getResources().getColor(R.color.green));








                }
            }
        });
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                inputMessage.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });






    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(ChatActivity.this, "Permesso negato", Toast.LENGTH_SHORT);
            }
        }
    }

    class InitTask extends AsyncTask<Object,Void,String>{
        DataOutputStream dos;
        String message;
        TCPClient mTcp;
        @Override
        protected String doInBackground(Object... params) {
            SystemClock.sleep(1000);
            message = (String) params[0];
            mTcp = (TCPClient) params[1];
            try{
                if(mTcp != null){
                    mTcp.sendMessage(message);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    class SendMessageTask extends AsyncTask<Object,Void,String>{
        DataOutputStream dos;
        ChatMessage chatMessage;
        TCPClient mTcp;

        @Override
        protected String doInBackground(Object... params) {
            SystemClock.sleep(1000);
            chatMessage = (ChatMessage) params[0];
            mTcp = (TCPClient) params[1];
            try{
                if(mTcp != null){
                    mTcp.sendMessage(chatMessage.message);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }


    }

    public void ShowPopup(final Room stanza , final Utente utente_loggato,Dialog popUp){
        popUp.setContentView(R.layout.waitingroom_popup);
        popUp.setCancelable(false);
        popUp.setCanceledOnTouchOutside(false);
        Button termina;
        termina = popUp.findViewById(R.id.termina);

        InitTask b = new InitTask();
        InitTask c = new InitTask();
        InitTask d = new InitTask();
        InitTask e = new InitTask();

        b.execute("/tema "+stanza.getNome(),mTcpClient);
        c.execute("/username "+utente_loggato.getUsername(),mTcpClient);
        e.execute("/foto "+utente_loggato.getProfile_image(),mTcpClient);
        d.execute("/cerca",mTcpClient);






        termina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* BackgroundTask e = new BackgroundTask();
                e.execute("/quit",mTcpClient);*/
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("utente", utente_loggato);
                startActivity(intent);
                finish();
            }
        });
        popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUp.show();
    }

    public void showInfoDialog(Room stanza){

        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle(stanza.getNome());
        builder.setMessage(stanza.getDescription())
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    //comunicazione con il server
    public class connectTask extends AsyncTask<String, String, TCPClient> {


        @Override
        protected TCPClient doInBackground(String... message) {
            //create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    try {
                        //this method calls the onProgressUpdate
                        publishProgress(message);
                        if (message != null) {
                            System.out.println("Return Message from Socket::::: >>>>> " + message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, ipAddress);
            mTcpClient.run(); //da non richiamare nella chat
            if (mTcpClient != null) {
                mTcpClient.sendMessage("Initial Message when connected with Socket Server");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
            if(values[0].contains("/id")){
                id_interlocutore = values[0].substring(4);
                Button termina = popUp.findViewById(R.id.termina);
                termina.setEnabled(false);
                termina.setBackground(getResources().getDrawable(R.drawable.background_stop_green));
                termina.setText("Utente trovato");
                /*Toast toast = Toast.makeText(ChatActivity.this, "id interlocutore: "+id_interlocutore, Toast.LENGTH_SHORT);
                toast.show();*/
                InitTask f = new InitTask();
                f.execute("/getusername "+id_interlocutore,mTcpClient);

            }else if(values[0].contains("/interid")){
                id_interlocutore = values[0].substring(9);
                Button termina = popUp.findViewById(R.id.termina);
                termina.setEnabled(false);
                termina.setBackground(getResources().getDrawable(R.drawable.background_stop_green));
                termina.setText("Utente trovato");
                /*Toast toast = Toast.makeText(ChatActivity.this, "id interlocutore: "+id_interlocutore, Toast.LENGTH_SHORT);
                toast.show();*/
                InitTask f = new InitTask();
                f.execute("/getusername2 "+id_interlocutore,mTcpClient);

            }

            else if(values[0].contains("/match_inter")){
                id_interlocutore = values[0].substring(7);
                Toast toast = Toast.makeText(ChatActivity.this, "id interlocutore: "+id_interlocutore, Toast.LENGTH_SHORT);
                toast.show();
                InitTask f = new InitTask();
                f.execute("/match_inter "+id_interlocutore,mTcpClient);

            }else if(values[0].contains("/username")){
                username_interlocutore = values[0].substring(10);
                text_interlocutore.setText(username_interlocutore);
                InitTask g = new InitTask();
                g.execute("/getfoto "+id_interlocutore,mTcpClient);


            }else if(values[0].contains("/interusername")){
                username_interlocutore = values[0].substring(15);
                text_interlocutore.setText(username_interlocutore);
                InitTask g = new InitTask();
                g.execute("/getfoto2 "+id_interlocutore,mTcpClient);

            }else if(values[0].contains("/foto")){
                foto_interlocutore = values[0].substring(6);
                RetrieveImageTask task = new RetrieveImageTask();
                try {
                    Bitmap img = Bitmap.createScaledBitmap(task.execute(foto_interlocutore).get() , 110,110,false);
                    chatAdapter = new ChatAdapter(chatMessages,username_loggato, img,getApplicationContext());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerChat.setAdapter(chatAdapter);
                popUp.dismiss();

                InitTask h = new InitTask();
                h.execute("/match2 "+id_interlocutore,mTcpClient);


            } else if(values[0].contains("/interfoto")){
                foto_interlocutore = values[0].substring(11);
                RetrieveImageTask task = new RetrieveImageTask();
                try {
                    Bitmap img = Bitmap.createScaledBitmap(task.execute(foto_interlocutore).get() , 110,110,false);
                    chatAdapter = new ChatAdapter(chatMessages,username_loggato, img,getApplicationContext());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerChat.setAdapter(chatAdapter);
                popUp.dismiss();


            } else if(values[0].contains("/bye")){
                id_interlocutore = "0";
                foto_interlocutore = "0";
                username_interlocutore = "0";
                closeChat.callOnClick();

            }else if(values[0].contains("/ricerca")){
                Button termina = popUp.findViewById(R.id.termina);
                termina.setEnabled(true);
                termina.setBackground(getResources().getDrawable(R.drawable.background_stop));
                termina.setText("Termina");
            }else if(!values[0].contains("/")){
                ChatMessage mess = new ChatMessage();
                mess.message = values[0];
                mess.senderId = id_interlocutore;
                chatMessages.add(mess);
                /*Toast toast = Toast.makeText(ChatActivity.this, mess.message, Toast.LENGTH_SHORT);
                toast.show();*/
                chatAdapter.notifyDataSetChanged();
                //System.out.printf("messaggio ricevuto: "+values[0]);
            }
        }

    }
    @Override
    protected void onDestroy()
    {
        try
        {
            popUp.dismiss();
            System.out.println("onDestroy.");
            InitTask f = new InitTask();
           f.execute("/quitdef",mTcpClient);
            mTcpClient.stopClient();
            conctTask.cancel(true);
            conctTask = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }


}