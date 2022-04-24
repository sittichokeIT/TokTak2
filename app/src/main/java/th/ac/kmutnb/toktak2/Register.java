package th.ac.kmutnb.toktak2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Register extends AppCompatActivity {

    private static final String TAG = "my_app";
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button regis = findViewById(R.id.registerBtn);
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();
            }
        });
    }

    public void click(){
        EditText user = findViewById(R.id.inputUsername);
        EditText em = findViewById(R.id.inputEmail);
        EditText pass = findViewById(R.id.inputPassword);
        EditText con = findViewById(R.id.inputConPass);
        String username = user.getText().toString();
        String email = em.getText().toString();
        String password = pass.getText().toString();
        String confirmpassword = con.getText().toString();

        regis(username,email,password,confirmpassword);
    }

    public void regis(String username, String email,String password,String confirmpassword){
        Intent gologin = new Intent(this,Login.class);
        String url = "http://192.168.56.1:4000/api/users/store";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);

                        String type = null;
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            type = jsonObject.getString("type");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        if(type.matches("user_already")){
                            Toast.makeText(Register.this, "User or Email has already", Toast.LENGTH_SHORT).show();
                        }else if(type.matches("success")){
                            Toast.makeText(Register.this, "Register Success", Toast.LENGTH_SHORT).show();
                            startActivity(gologin);
                        }else if(type.matches("wrong_password_notmatch")){
                            Toast.makeText(Register.this, "Password not match", Toast.LENGTH_SHORT).show();
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
                Map<String,String> params = new HashMap<String,String>();
                params.put("username",username);
                params.put("email",email);
                params.put("password",password);
                params.put("confirmpassword",confirmpassword);
                return params;
            }
        };
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }
}