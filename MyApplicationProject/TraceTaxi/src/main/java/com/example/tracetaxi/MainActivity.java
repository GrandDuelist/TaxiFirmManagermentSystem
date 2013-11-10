package com.example.tracetaxi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button LoginButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton = (Button) findViewById(R.id.login);
        LoginButton.setOnClickListener(new LoginButtonListener());
    }

    /*
            功能：登录后进入google  map Activity
    */
    class LoginButtonListener implements View.OnClickListener
    {


        @Override
        public void onClick(View view) {
            //生成一个intent对象
            Intent intent  = new Intent();
            intent.setClass(MainActivity.this,MyMap.class);
            MainActivity.this.startActivity(intent);

        }
    }


                  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
