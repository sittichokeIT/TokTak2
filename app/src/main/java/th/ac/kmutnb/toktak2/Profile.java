package th.ac.kmutnb.toktak2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    //UI views
    FloatingActionButton addVideoBtn;
    private static final String TAG = "myapp";
    public String usernames = null;
    public String emails = null;
    private RequestQueue mQueue;
    SharedPreferences sp;
    private String Token;
//
    TextView namee,emaill;


    @Override
    protected void onStart() {

        authToken("http://154.202.2.5:4000/api/users/verifytoken");
        super.onStart();
    }

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

        //authtoken
        sp = this.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        Token = sp.getString("Token", "");
        Log.i(TAG,Token);



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

    public void goList(View v){
        Intent goList = new Intent(this,MyList.class);
        startActivity(goList);
    }

    public void authToken(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,response);
                String username = null;
                String email = null;
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    username = jsonObject.getString("username");
                    email = jsonObject.getString("email");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(username!=null){
                    usernames = username;
                    Log.i(TAG,usernames);
                    emails = email;
                    Log.i(TAG,emails);
                    //id textview
                    namee = (TextView) findViewById(R.id.tv_name);
                    emaill = (TextView) findViewById(R.id.showname);

                    namee.setText(usernames);
                    emaill.setText(emails);
                }else{
                    Toast.makeText(Profile.this, "null", Toast.LENGTH_SHORT).show();
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