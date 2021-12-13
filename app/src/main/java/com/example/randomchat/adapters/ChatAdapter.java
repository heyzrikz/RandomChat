package com.example.randomchat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.randomchat.ChatMessage;
import com.example.randomchat.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<ChatMessage> chatMessages;
    private Context context;
    private final String senderId;
    private final Bitmap profileImageReceived;
    private final int VIEW_SENT = 1;
    private final int VIEW_RECEIVED = 2;

    public void clear(){
        int size = chatMessages.size();
        chatMessages.clear();
        notifyItemRangeRemoved(0,size);
    }

    public ChatAdapter(List<ChatMessage> chatMessages, String senderId, Bitmap profileImageReceived,Context context) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.senderId = senderId;
        this.profileImageReceived = profileImageReceived;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_SENT){
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.message_sent_layout,parent,false);
            return new SentMessageViewHolder(view);
        }else{
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.message_received_layout,parent,false);
            return new ReceivedMessageViewHolder(view) {
            };
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      if(getItemViewType(position) == VIEW_SENT){
          ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
      }else{
          ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), profileImageReceived);
      }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(senderId)){
            return VIEW_SENT;
        }else{
            return VIEW_RECEIVED;
        }


    }

    public class SentMessageViewHolder extends RecyclerView.ViewHolder{
        TextView sentMessage;


        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessage = itemView.findViewById(R.id.textMessage);
        }
        void setData(ChatMessage message){
            sentMessage.setText(message.message);
        }

    }

    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        TextView receivedMessage;
        ImageView imageProfilo;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            receivedMessage = itemView.findViewById(R.id.textMessage);
            imageProfilo = itemView.findViewById(R.id.imageProfile);
        }
        void setData(ChatMessage message , Bitmap imageProfile){
            receivedMessage.setText(message.message);
            imageProfilo.setImageBitmap(imageProfile);
        }

    }
}
