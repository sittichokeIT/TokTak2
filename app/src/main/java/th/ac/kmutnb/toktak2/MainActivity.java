package th.ac.kmutnb.toktak2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //UI views
    FloatingActionButton addVideoBtn;
    private RecyclerView videoRv;

    //array list
    private ArrayList<ModelVideo> videoArrayList;
    //adapter
    private AdapterVideo adapterVideo;


    private static final String TAG = "my_app";
    private RequestQueue mQueue;

    SharedPreferences sp;

    String Token;
    @Override
    protected void onStart() {
        super.onStart();
        sp = this.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        Token = sp.getString("Token", "");
        Log.i(TAG,Token);

    }

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

        //if login false run this
        addVideoBtn.setOnClickListener(view -> {
            //start activity to add videos
            authToken("http://192.168.56.1:4000/api/users/verifytoken",Token);
//            startActivity(new Intent(MainActivity.this,Login.class));
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

    public void authToken(String url, String Token){
        Intent goProfile = new Intent(this,Profile.class);
        Intent goLogin = new Intent(this,Login.class);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,response);

                String username = null;

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    username = jsonObject.getString("username");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(username!=null){
                    Log.i(TAG,"login success");
                    //next page
                    startActivity(goProfile);

                }else{
                    Log.i("clear","true");
                    startActivity(goLogin);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Error handling
                        Log.i(TAG,"onErrorResponse(): " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token",Token);
                return params;
            }
        };
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }
}