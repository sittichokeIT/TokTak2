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

public class ProfileSetting extends AppCompatActivity {

    Button logout ;
    private static final String TAG = "myapp";
    public String usernames = null;
    private RequestQueue mQueue;
    SharedPreferences sp;
    private String Token;
    //
    TextView namee;


    @Override
    protected void onStart() {

        authToken("http://192.168.56.1:4000/api/users/verifytoken");
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        //hide actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        //authtoken
        sp = this.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        Token = sp.getString("Token", "");
        Log.i(TAG,Token);

        Intent intent = new Intent(this,ConfirmLogout.class);
        logout = findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }

    public void authToken(String url){
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
                    usernames = username;
                    namee = (TextView) findViewById(R.id.showname);
                    namee.setText(usernames);
                }else{
                    Toast.makeText(ProfileSetting.this, "null", Toast.LENGTH_SHORT).show();
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