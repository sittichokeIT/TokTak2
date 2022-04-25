package th.ac.kmutnb.toktak2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfirmLogout extends AppCompatActivity {

    Button logout ;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_logout);

        //hide actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sp = this.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        Intent intent = new Intent(this,MainActivity.class);
        logout = findViewById(R.id.btnComfirm);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().remove("Token").commit();
                startActivity(intent);
            }
        });
    }

        public void No(View v){
            Intent No = new Intent(this,ProfileSetting.class);
            startActivity(No);
        }
}