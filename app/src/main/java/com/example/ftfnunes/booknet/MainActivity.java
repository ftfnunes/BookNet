package com.example.ftfnunes.booknet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button botaoTeste = (Button) findViewById(R.id.botaoTeste);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void noClick(View v) {
        Intent it = new Intent(MainActivity.this, telaLogin.class);
        startActivity(it);
    }
}
