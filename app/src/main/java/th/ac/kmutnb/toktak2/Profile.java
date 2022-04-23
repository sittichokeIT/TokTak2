package th.ac.kmutnb.toktak2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    //UI views
    FloatingActionButton addVideoBtn;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //chang actionbar title
        setTitle("Videos");

        //init UI Views
        addVideoBtn = findViewById(R.id.addVideosBtn);
        loadMyVideo();
        //handle click
        addVideoBtn.setOnClickListener(view -> {
            //start activity to add videos
            startActivity(new Intent(Profile.this,UploadVideo.class));
        });
//


    }
    public void goSetting(View v){
        Intent goSetting = new Intent(this,ProfileSetting.class);
        startActivity(goSetting);
    }

    public void loadMyVideo(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = db.child("Videos");
        Query queryById = ref.orderByChild("id").equalTo("sit");
        queryById.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    String video = ds.child("videoUrl").getValue(String.class);
                    String title = ds.child("title").getValue(String.class);


                }
            }
        });
    }



}