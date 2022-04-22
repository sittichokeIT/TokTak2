package th.ac.kmutnb.toktak2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //UI views
    FloatingActionButton addVideoBtn;
    private RecyclerView videoRv;

    //array list
    private ArrayList<ModelVideo> videoArrayList;
    //adapter
    private AdapterVideo adapterVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_video);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //chang actionbar title
        setTitle("Videos");

        //init UI Views
        addVideoBtn = findViewById(R.id.addVideosBtn);
        videoRv = findViewById(R.id.videoRv);

        //function call. load videos
        loadVideosFromFirebase();

        //if login true run this
//        addVideoBtn.setOnClickListener(view -> {
//            //start activity to add videos
//            startActivity(new Intent(MainActivity.this,Profile.class));
//        });

        //if login false run this
        addVideoBtn.setOnClickListener(view -> {
            //start activity to add videos
            startActivity(new Intent(MainActivity.this,Login.class));
        });
    }

    private void loadVideosFromFirebase() {
        //init array list before adding data into it
        videoArrayList = new ArrayList<>();

        //db reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Videos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear list before adding data into it
                for(DataSnapshot ds: snapshot.getChildren()){
                    //get data
                    ModelVideo modelVideo = ds.getValue(ModelVideo.class);
                    //add model/data into list
                    videoArrayList.add(modelVideo);
                }
                //setup adapter
                adapterVideo = new AdapterVideo(MainActivity.this, videoArrayList);
                //set adapter to recyclerview
                videoRv.setAdapter(adapterVideo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}