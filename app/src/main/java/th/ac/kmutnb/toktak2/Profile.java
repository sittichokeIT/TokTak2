package th.ac.kmutnb.toktak2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Profile extends AppCompatActivity {

    //UI views
    FloatingActionButton addVideoBtn;
    private RecyclerView videoRv;


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
        videoRv = findViewById(R.id.videoRv);

        //handle click
        addVideoBtn.setOnClickListener(view -> {
            //start activity to add videos
            startActivity(new Intent(Profile.this,UploadVideo.class));
        });


    }
    public void gosetting(View v){
        Intent gosetting = new Intent(this,ProfileSetting.class);
        startActivity(gosetting);
    }



}