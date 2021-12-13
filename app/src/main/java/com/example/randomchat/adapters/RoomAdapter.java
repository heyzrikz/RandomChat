package com.example.randomchat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.randomchat.R;
import com.example.randomchat.Room;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {

    private List <Room> listOfRoom;
    private Context context;

    public RoomAdapter (Context ct, List<Room> roomList ){
        context = ct;
        listOfRoom = roomList;


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.room_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.room_name.setText(listOfRoom.get(position).getNome());
        holder.host_name.setText(listOfRoom.get(position).getHost_name());

    }

    @Override
    public int getItemCount() {
        return listOfRoom.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView room_name;
        TextView host_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            room_name = itemView.findViewById(R.id.room_name);
            host_name = itemView.findViewById(R.id.room_host);

        }
    }

}
