package kr.ac.pusan.cs.bookforyou;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class Activity_Main extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    public void onResume(){
        super.onResume();
        if(check()){
            Log.d("재시작","호출됨");
            Intent intent=new Intent(Activity_Main.this,Activity_RealMain.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = openOrCreateDatabase("login",MODE_PRIVATE , null); //데이터 베이스를 만듬
        createTable("thisphone");
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){
                    Intent intent=new Intent(Activity_Main.this,Activity_RealMain.class);
                    startActivity(intent);
                    finish();
                }

                else{
                    Intent intent=new Intent(Activity_Main.this,PhoneAuthActivity.class);
                    startActivity(intent);

                }

            }
        });
    }
    public void createTable(String name){
        String sql="create table if not exists " + name + " (_id integer PRIMARY KEY autoincrement, phone integer);";
        db.execSQL(sql);
    }
    public boolean check(){
        String sqlSelect = "select phone from thisphone" ;
        Cursor cursor = db.rawQuery(sqlSelect,null);
        if(cursor.getCount()!=0){
            return true;
        }
        else{
            return false;
        }
    }
}
