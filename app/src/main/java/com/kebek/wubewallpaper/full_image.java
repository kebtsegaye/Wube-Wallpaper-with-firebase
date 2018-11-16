package com.kebek.wubewallpaper;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class full_image extends AppCompatActivity {

    PhotoView photoView;
    Button changeWallpaper;
    Bitmap imgForWallp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        // show the back button in the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // photo view is an image view that helps with zooming
        photoView = findViewById(R.id.photo_view);
        // connect to set wallpaper button
        changeWallpaper = findViewById(R.id.set);

        // getting the image url from main activity
        String wallimg;   // holds the value for image url we imported using intent from Main activity
        // takes care of working and error url inks
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                wallimg = null;
            } else {
                wallimg = extras.getString("image_url");
            }
        } else {
            wallimg = (String) savedInstanceState.getSerializable("image_url");
        }

        // set the image to imageview to be showen in this activity
         Glide.with(this).
                load(wallimg).
                asBitmap().into(new SimpleTarget<Bitmap>(100,100) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    photoView.setImageBitmap(resource); // Possibly runOnUiThread()
                    imgForWallp = resource; // Possibly runOnUiThread()
                }
            });


        // on click listner for change wallpaper
       changeWallpaper.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               //
               // inflate the layout of the popup window
               LayoutInflater inflater = (LayoutInflater)
                       getSystemService(LAYOUT_INFLATER_SERVICE);
               View popupView = inflater.inflate(R.layout.popup_window, null);

               // create the popup window
               int width = LinearLayout.LayoutParams.WRAP_CONTENT;
               int height = LinearLayout.LayoutParams.WRAP_CONTENT;
               boolean focusable = true; // lets taps outside the popup also dismiss it
               final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

               // show the popup window
               // which view you pass in doesn't matter, it is only used for the window tolken
               popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

               //if home screen button is clicke then set wallpaper of homescreen
               Button home_screen = (Button) popupView.findViewById(R.id.home_screen);
               home_screen.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       // changing wallpaper
                       //Bitmap result = Glide.with(this).load(wallimg).asBitmap();
                       WallpaperManager wallpaperManager = WallpaperManager.getInstance(full_image.this);
                       try {
                           wallpaperManager.setBitmap(imgForWallp);
                           Toast.makeText(full_image.this, "Wallpaper Changed", Toast.LENGTH_SHORT).show();

                       } catch (IOException ex) {
                           ex.printStackTrace();
                       }

                   }
                   });

               // dismiss the popup window when touched
               popupView.setOnTouchListener(new View.OnTouchListener() {
                   @Override
                   public boolean onTouch(View v, MotionEvent event) {
                       popupWindow.dismiss();
                       return true;
                   }
               });

           }
       });

    }

    // a method to close the current activity/ to help with go back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

