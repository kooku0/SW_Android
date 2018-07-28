package kr.ac.pusan.cs.bookforyou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Acitivity_RegistBookBarcode extends AppCompatActivity {
    ImageView image;
    TextView title;
    TextView author;
    TextView publisher;
    TextView price;
    TextView pubdate;
    final Obj_book[] book = new Obj_book[1];
    Bitmap bookImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist__book__barcode);
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        publisher = findViewById(R.id.publisher);
        price = findViewById(R.id.price);
        pubdate = findViewById(R.id.pubdate);
        startQRCode();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Regist_Book_Info.class);
                intent.putExtra("book", (Serializable) book[0]);
                //intent.putExtra("image",bookImage);
                startActivity(intent);
                finish();
            }
        });

    }

    public void startQRCode(){
        //new IntentIntegrator(this).initiateScan();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Class_ZxingActivity.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == IntentIntegrator.REQUEST_CODE){
            final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result == null){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }else {

                if("".equals(result.getContents())||result.getContents()==null){
                    finish();
                    return;
                }
                Toast.makeText(this, "Scanned: "+ result.getContents(), Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        book[0] = (Obj_book) bookSearchApi.bookSearch(result.getContents()).get(0);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //searchResult.setText(str);
                                getImage(book[0]);
                            }
                        });
                    }
                }).start();
                //searchResult.setText(str);
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
            finish();
        }
    }
    public void getImage(final Obj_book book){
        final Bitmap[] bitmap = new Bitmap[1];

        title.setText(book.title);
        author.setText(book.author);
        publisher.setText(book.publisher);
        price.setText(book.price);
        pubdate.setText(book.pubdate);

        Thread mThread = new Thread(){
            @Override
            public void run(){
                try {
                    URL imageUrl = new URL(book.image);
                    HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
            if(bitmap[0] !=null){
                image.setImageBitmap(bitmap[0]);
                //bookImage = bitmap[0];
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
