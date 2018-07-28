package kr.ac.pusan.cs.bookforyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Activity_RealMain extends AppCompatActivity {
    private String userID = "002";

    BottomNavigationView bottomNavigationView;
    FirebaseDatabase database;
    DatabaseReference mRef;
    RecyclerView rcv;
    MyRecyclerAdapter rcvAdapter;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private List<Obj_book_info> bookInfoList;
    private ArrayList<Obj_book_info> arraylist;
    private List<String> keyList;
    private static final int SEARCHTITLE = 111;
    Loading_Application loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        bookInfoList = new ArrayList<Obj_book_info>();
        keyList = new ArrayList<String>();
        arraylist=new ArrayList<Obj_book_info>();
        rcv = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //rcv.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        rcv.setLayoutManager(linearLayoutManager);


        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonClick(v);
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonClick(v);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonClick(v);
            }
        });

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout =(CollapsingToolbarLayout) findViewById(R.id.colllapsingtoolbar);
        collapsingToolbarLayout.setTitle("Ctoolbar");

        RecyclerView recyclerView =findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<Item_card> dataList = new ArrayList<>();
        startProgress();
        // DB에서 모든 정보 가지고 오기
        getDB();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_first:
                        //Toast.makeText(getApplicationContext(),"1", Toast.LENGTH_SHORT).show();
                        Intent intent1=new Intent(Activity_RealMain.this,Activity_MyInfo.class);
                        intent1.putExtra("list", (Serializable) bookInfoList);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_second:
                        Intent intent2=new Intent(Activity_RealMain.this,Activity_MapBookStore.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_third://책 등록하기
                        Intent intent3=new Intent(Activity_RealMain.this,Activity_RegistSelect.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_fourth:
                        Toast.makeText(getApplicationContext(),"4", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });
    }

    private void startProgress() {
        loading = new Loading_Application();
        loading.progressON(this,"Loading...");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){      //액션바에 메뉴추가
        getMenuInflater().inflate(R.menu.actionmenu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){ //액션바 메뉴 선택시 이벤트
        int id=item.getItemId();
        if(id==R.id.action_search){
            Intent intent = new Intent(Activity_RealMain.this, Class_SearchActivity.class);
            intent.putExtra("list", (Serializable) arraylist);
            startActivityForResult(intent,SEARCHTITLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void floatingButtonClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                //Toast.makeText(this, "검색", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab1:
                anim();
                Intent intent = new Intent(Activity_RealMain.this, Class_SearchActivity.class);
                intent.putExtra("list", (Serializable) arraylist);
                startActivityForResult(intent,SEARCHTITLE);
                break;
            case R.id.fab2:
                anim();
                Toast.makeText(this, "Button2", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }
    Activity activity = this;
    public void getDB(){
        mRef.child("Book").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookInfoList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot ttmp : snapshot.getChildren()){
                        Obj_book_info tmp = ttmp.getValue(Obj_book_info.class);
                        bookInfoList.add(tmp);
                        keyList.add(ttmp.getKey());
                    }
                }
                arraylist.addAll(bookInfoList);
                rcvAdapter = new MyRecyclerAdapter(activity, bookInfoList);
                rcv.setAdapter(rcvAdapter);
                loading.progressOFF();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터 베이스 연동이 불가합니다.", Toast.LENGTH_LONG).show();
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {

            switch (requestCode) {

                case SEARCHTITLE:
                    String title = data.getStringExtra("title");
                    System.out.println(title);
                    bookInfoList.clear();
                    for (int i = 0; i < arraylist.size(); i++) {

                        if (arraylist.get(i).book.title.equals(title)) {
                            // 검색된 데이터를 리스트에 추가한다.
                            bookInfoList.add(arraylist.get(i));
                        }
                    }
                    //rcvAdapter = new MyRecyclerAdapter(activity, bookInfoList);
                    //rcv.setAdapter(rcvAdapter);
                    rcvAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;

            }
        }

    }

}
