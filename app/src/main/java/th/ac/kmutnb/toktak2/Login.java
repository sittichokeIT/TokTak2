package th.ac.kmutnb.toktak2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
    }

    public void gologin(View v){
        Intent gologin = new Intent(this,MainActivity.class);
        startActivity(gologin);
    }

    public void goReg(View v){
        Intent goReg = new Intent(this,Register.class);
        startActivity(goReg);
    }
}