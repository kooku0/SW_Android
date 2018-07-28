package kr.ac.pusan.cs.bookforyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Activity_RegistSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist__select);

        findViewById(R.id.barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Acitivity_RegistBookBarcode.class);
                startActivity(intent);
                //finish();
            }
        });

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_RegistBookSearch.class);
                startActivity(intent);
                //finish();
            }
        });
    }
}
