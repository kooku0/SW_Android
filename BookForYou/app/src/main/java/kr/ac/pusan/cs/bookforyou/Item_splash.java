package kr.ac.pusan.cs.bookforyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class Item_splash extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(5);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this,Activity_RealMain.class));
        finish();
    }
}
