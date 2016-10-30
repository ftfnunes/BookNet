package com.example.ftfnunes.booknet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TelaFiltro extends AppCompatActivity {
    RadioGroup rdGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_filtro);

        Button selectButton =(Button)findViewById(R.id.filterSelectionButton);
        rdGroup = (RadioGroup)findViewById(R.id.filterRadioGroup);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                RadioButton rdBtn = (RadioButton) findViewById(rdGroup.getCheckedRadioButtonId());
                returnIntent.putExtra("SELECTED_FILTER", rdBtn.getText());
                TelaFiltro.this.setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
