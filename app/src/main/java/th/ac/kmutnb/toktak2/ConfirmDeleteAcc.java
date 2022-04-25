package th.ac.kmutnb.toktak2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmDeleteAcc extends AppCompatActivity {

    Button delAcc ;
    SharedPreferences sp;
    private static final String TAG = "myapp";
    public String usernames = null;
    private RequestQueue mQueue;
    private String Token;

    @Override
    protected void onStart() {

        authToken("http://154.202.2.5:4000/api/users/verifytoken");
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delete_acc);

        //hide actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sp = this.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        Token = sp.getString("Token", "");
        Log.i(TAG,Token);
        Intent intent = new Intent(this,MainActivity.class);
        delAcc = findViewById(R.id.btnConfirm);
        delAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Url = "http://154.202.2.5:4000/api/users/delete";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG,response);
                        String msg = null;
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            msg = jsonObject.getString("message");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        if(msg.matches("success")){
                            Log.i("name",usernames);
                            sp.edit().remove("Token").commit();
                            Toast.makeText(ConfirmDeleteAcc.this, "Delete Account Success!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }else{
                            Toast.makeText(ConfirmDeleteAcc.this, "Field", Toast.LENGTH_SHORT).show();
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
                        params.put("username",usernames);
                        return params;
                    }
                };
                mQueue = Volley.newRequestQueue(ConfirmDeleteAcc.this);
                mQueue.add(stringRequest);
            }
        });
    }
    public void No(View v){
        Intent No = new Intent(this,ProfileSetting.class);
        startActivity(No);
    }

    public void authToken(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,response);
                String username = null;
                String email =null;
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    username = jsonObject.getString("username");
                    email = jsonObject.getString("email");

                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(username!=null){
                    usernames = username;
                }else{
                    Toast.makeText(ConfirmDeleteAcc.this, "null", Toast.LENGTH_SHORT).show();
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