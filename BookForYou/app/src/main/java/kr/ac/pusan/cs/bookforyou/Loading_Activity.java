package kr.ac.pusan.cs.bookforyou;

import android.support.v7.app.AppCompatActivity;

public class Loading_Activity extends AppCompatActivity {


    public void progressON() {
        Loading_Application.getInstance().progressON(this, null);
    }

    public void progressON(String message) {
        Loading_Application.getInstance().progressON(this, message);
    }

    public void progressOFF() {
        Loading_Application.getInstance().progressOFF();
    }

}
