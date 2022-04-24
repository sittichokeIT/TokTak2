package th.ac.kmutnb.toktak2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class Login extends AppCompatActivity {

    private static final String TAG = "my_app";
    private RequestQueue mQueue;

    SharedPreferences sp;


    @Override
    protected void onStart() {
        super.onStart();
         sp = this.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
         String Token = sp.getString("Token", "");
         Log.i(TAG,Token);
         authToken("http://154.202.2.5:4000/api/users/verifytoken",Token);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button login = findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();
            }
        });

    }

    public void click() {
        EditText user = findViewById(R.id.LogUsername);
        EditText pass = findViewById(R.id.LogPas);
        String username = user.getText().toString();
        String password = pass.getText().toString();
        login(username, password);
    }

    public void login(String username ,String password ){
        String url = "http://154.202.2.5:4000/api/users/login";
        Intent goMain = new Intent(Login.this,MainActivity.class);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);

                        String msg = null;
                        String token = null;
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            msg = jsonObject.getString("message");
                            token = jsonObject.getString("token");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        if(msg.matches("User_not_found")){
                            Toast.makeText(Login.this, "User or Password wrong", Toast.LENGTH_SHORT).show();
                        }else if(msg.matches("success")){
                            Log.i("checked","true");
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("Token",token);
                            editor.commit();
                            startActivity(goMain);
                            //next to MainPage

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Error handling
                        Log.i(TAG,"onErrorResponse(): "+ error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String,String>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    public void authToken(String url, String Token){
        Intent goProfile = new Intent(this,Profile.class);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,response);

                String username = null;

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    username = jsonObject.getString("jwts");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(username!=null){
                    Log.i(TAG,"login success");
                    //next page
//                    startActivity(goProfile);
                }else{
                    Log.i(TAG,"true");
//                    sp.edit().remove("Token").commit();
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

    public void regis(View v){
        Intent regis = new Intent(this,Register.class);
        startActivity(regis);
    }
}