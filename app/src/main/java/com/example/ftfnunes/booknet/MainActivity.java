package com.example.ftfnunes.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button botaoTeste = (Button) findViewById(R.id.botaoTeste);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent it = new Intent(MainActivity.this, telaLogin.class);
        startActivity(it);
    }

    public void noClick(View v) {
        Intent it = new Intent(MainActivity.this, telaLogin.class);
        startActivity(it);
    }
}
