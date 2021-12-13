package com.example.randomchat.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.net.URL;

public class RetrieveImageTask extends AsyncTask<String , Void, Bitmap> {

    private Exception exception;
    @Override
    protected Bitmap doInBackground(String... image_string) {

        try{
            URL url = new URL(image_string[0]);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;

        }catch(Exception e){
            this.exception = e;
            return null;
        }

    }
    protected void onPostExecute(Bitmap image) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
