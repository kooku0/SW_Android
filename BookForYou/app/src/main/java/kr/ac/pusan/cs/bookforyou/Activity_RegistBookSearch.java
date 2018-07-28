package kr.ac.pusan.cs.bookforyou;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_RegistBookSearch extends AppCompatActivity {
    EditText searchText;
    Button searchBtn;
    TextView searchResult;
    String keyword,str;
    ArrayList bookList;
    RecyclerView rcv;
    Adapter_Rcv rcvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist__book__search);
        searchText = findViewById(R.id.searchText);
        searchBtn = findViewById(R.id.searchBtn);

        final Activity activity = this;
        rcv = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //rcv.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        rcv.setLayoutManager(linearLayoutManager);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_SEARCH:
                        keyword = searchText.getText().toString();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                bookList = bookSearchApi.bookSearch(keyword);
                                //str = bookSearchApi.bookSearch(keyword);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //searchResult.setText(str);
                                        rcvAdapter = new Adapter_Rcv(activity, bookList);
                                        rcv.setAdapter(rcvAdapter);
                                    }
                                });
                            }
                        }).start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = searchText.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bookList = bookSearchApi.bookSearch(keyword);
                        //str = bookSearchApi.bookSearch(keyword);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //searchResult.setText(str);
                                rcvAdapter = new Adapter_Rcv(activity, bookList);
                                rcv.setAdapter(rcvAdapter);
                            }
                        });
                    }
                }).start();
                //searchResult.setText(str);
            }
        });
    }
}
