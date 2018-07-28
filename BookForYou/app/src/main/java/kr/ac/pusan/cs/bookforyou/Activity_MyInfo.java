package kr.ac.pusan.cs.bookforyou;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_MyInfo extends AppCompatActivity {
    private String userID = "002";

    FirebaseDatabase database;
    DatabaseReference mRef;

    private ViewPager mViewPager;
    private NavigationTabStrip mTopNavigationTabStrip;
    private List<Obj_book_info> bookInfoList;
    private ArrayList<Obj_book_info> arraylist;
    private List<String> keyList;
    private Loading_Application loading;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my_info);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        bookInfoList = new ArrayList<Obj_book_info>();
        keyList = new ArrayList<String>();
        arraylist=new ArrayList<Obj_book_info>();
        /*Intent intent = getIntent();
        List<Obj_book_info> tmplist = (List<Obj_book_info>) intent.getSerializableExtra("list");
        for(int i=0;i<tmplist.size();i++){
            if(tmplist.get(i).userID==userID){
                bookInfoList.add(tmplist.get(i));
            }
        }*/
        //startProgress();
        //getDB();
        initUI();
        setUI();
    }

    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mTopNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_top);

    }

    private void setUI() {
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                /*final View view = new View(getBaseContext());
                container.addView(view);*/
                final View view;
                if(position==0) {
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.viewpage_myinfo0, null, false);

                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);

                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                            getBaseContext(), LinearLayoutManager.VERTICAL, false
                    );
                    recyclerView.setLayoutManager(linearLayoutManager);
                    RecycleAdapter recycleradapter = new RecycleAdapter();
                    recyclerView.setAdapter(recycleradapter);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), linearLayoutManager.getOrientation());
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    container.addView(view);
                    return view;
                }
                else{
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.viewpage_myinfo1, null, false);

                }
                container.addView(view);
                return view;
            }
        });

        mTopNavigationTabStrip.setTabIndex(0, true);
        mTopNavigationTabStrip.setViewPager(mViewPager, 0);

        final NavigationTabStrip navigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_top);
        navigationTabStrip.setTitles("등록한 책", "대여중인 책", "지난 책");
//        navigationTabStrip.setTabIndex(0, true);
//        navigationTabStrip.setTitleSize(15);
//        navigationTabStrip.setStripColor(Color.RED);
//        navigationTabStrip.setStripWeight(6);
//        navigationTabStrip.setStripFactor(2);
//        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
//        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
//        navigationTabStrip.setTypeface("fonts/typeface.ttf");
//        navigationTabStrip.setCornersRadius(3);
//        navigationTabStrip.setAnimationDuration(300);
//        navigationTabStrip.setInactiveColor(Color.GRAY);
//        navigationTabStrip.setActiveColor(Color.WHITE);
//        navigationTabStrip.setOnPageChangeListener(...);
//        navigationTabStrip.setOnTabStripSelectedIndexListener(...);
    }

    public void getDB(){
        mRef.child("Book").child(String.valueOf(userID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookInfoList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Obj_book_info tmp = snapshot.getValue(Obj_book_info.class);
                        bookInfoList.add(tmp);
                        keyList.add(snapshot.getKey());

                    loading.progressOFF();
                }
                //arraylist.addAll(bookInfoList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "데이터 베이스 연동이 불가합니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void startProgress() {
        loading = new Loading_Application();
        loading.progressON(this,"Loading...");

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3500);*/

    }
    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_card2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Obj_book_info tmpData = bookInfoList.get(position);
            holder.title.setText(tmpData.book.title);
            holder.author.setText(tmpData.book.author);
            holder.publisher.setText(tmpData.book.publisher);
            if(tmpData.state.equals("none")) holder.img.setImageResource(R.drawable.state3);
            if(tmpData.state.equals("regist")) holder.img.setImageResource(R.drawable.state2);
            if(tmpData.state.equals("other")) holder.img.setImageResource(R.drawable.state1);
            //holder.date.setText();
        }

        @Override
        public int getItemCount() {
            return bookInfoList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public TextView author;
            public TextView publisher;
            public TextView date;
            public ImageView img;

            public ViewHolder(final View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                author = (TextView) itemView.findViewById(R.id.author);
                publisher = (TextView) itemView.findViewById(R.id.publisher);
                date = (TextView) itemView.findViewById(R.id.date);
                img = itemView.findViewById(R.id.state);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(activity, "click " + bookInfoList.get(getAdapterPosition()).book.title, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
