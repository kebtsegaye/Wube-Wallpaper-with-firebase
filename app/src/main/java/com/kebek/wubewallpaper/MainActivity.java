package com.kebek.wubewallpaper;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images and titles
    private List<String> images;
    private List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // makes the view in activity main to have two columns
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);


        progressDialog = new ProgressDialog(this);

        images = new ArrayList<>();
        titles = new ArrayList<>();


        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Toast.makeText(this, "before database reached", Toast.LENGTH_SHORT).show();

        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        // write data
        //writeNewUser("keb", "some url");


        //adding an event listener to fetch values
        ValueEventListener imgAndTitleListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    // getting the image url from firebase
                    String url = postSnapshot.getValue(String.class);
                    String name = postSnapshot.getKey();
                    //uploads.add(upload);
                    images.add(url);
                    titles.add(name);
                }

                //creating adapter
                adapter = new MyAdapter(getApplicationContext(), images, titles);

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();


            }
        };

        mDatabase.addListenerForSingleValueEvent(imgAndTitleListener);
    }

    private void writeNewUser( String title, String url) {
        Upload user = new Upload(title, url);
        Toast.makeText(this, "user data writen", Toast.LENGTH_SHORT).show();
        mDatabase.child("0001").child(user.getName()).setValue(url);

    }
}
