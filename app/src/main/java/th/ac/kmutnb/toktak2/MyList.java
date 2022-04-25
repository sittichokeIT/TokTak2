package th.ac.kmutnb.toktak2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<User> list;

    //
    private static final String TAG = "myapp";
    public String usernames = null;
    public  String emails = null;
    public String timestamps = null;
    private RequestQueue mQueue;
    SharedPreferences sp;
    private String Token;


    @Override
    protected void onStart() {

//        authToken("http://154.202.2.5:4000/api/users/verifytoken");
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //authtoken
        sp = this.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        Token = sp.getString("Token", "");
        Log.i(TAG,Token);
        authToken("http://154.202.2.5:4000/api/users/verifytoken");
//        recyclerView = findViewById(R.id.userList);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        database = FirebaseDatabase.getInstance().getReference().child("Videos");
//        Query query = database.orderByChild("id").equalTo(usernames);
//
//        list = new ArrayList<>();
//        myAdapter = new MyAdapter(this,list);
//        recyclerView.setAdapter(myAdapter);
//
//        query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                for(DataSnapshot ds : task.getResult().getChildren()){
//                    User user = ds.getValue(User.class);
//                    list.add(user);
//                }
//                myAdapter.notifyDataSetChanged();
//            }
//        });

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
                    emails = email;
                    usernames = username;
                    Log.i(TAG,emails);
                    //id textview
                    recyclerView = findViewById(R.id.userList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MyList.this));

                    database = FirebaseDatabase.getInstance().getReference().child("Videos");
                    Query query = database.orderByChild("id").equalTo(emails);

                    list = new ArrayList<>();
                    myAdapter = new MyAdapter(MyList.this,list);
                    recyclerView.setAdapter(myAdapter);

                    query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            for(DataSnapshot ds : task.getResult().getChildren()){
                                User user = ds.getValue(User.class);
                                list.add(user);

                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    });

                }else{
                    Toast.makeText(MyList.this, "null", Toast.LENGTH_SHORT).show();
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

    public void deleteVideo(View v){

        EditText timeDel = (EditText) findViewById(R.id.timeDel);
        String timeDelete = timeDel.getText().toString();
        Log.i("times",timeDelete);
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Videos").child(timeDelete);
        databaseReference.removeValue();
        Toast.makeText(this, "Delete video this" + timeDelete + "success!", Toast.LENGTH_SHORT).show();

    }

}