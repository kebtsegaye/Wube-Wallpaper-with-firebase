package com.kebek.wubewallpaper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class full_image extends AppCompatActivity {

    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        // show the back button in the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // photo view is an image view that helps with zooming
        photoView = findViewById(R.id.photo_view);

        // getting the image url from main activity
        String wallimg;   // holds the value for image url we imported using intent from Main activity
        // takes care of working and error url inks
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                wallimg= null;
            } else {
                wallimg= extras.getString("image_url");
            }
        } else {
            wallimg= (String) savedInstanceState.getSerializable("image_url");
        }

        // set the image to imageview to be showen in this activity
        Glide.with(this).load(wallimg).into(photoView);

    }

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
