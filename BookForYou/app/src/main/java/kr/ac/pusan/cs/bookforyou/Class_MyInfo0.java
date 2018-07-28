package kr.ac.pusan.cs.bookforyou;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Class_MyInfo0 {
    private Activity activity;
    private View view;
    private ArrayList<Obj_book_info> list;
    public Class_MyInfo0(Activity activity, ArrayList<Obj_book_info> list){
        this.list = list;
        this.activity = activity;
        view = LayoutInflater.from(
                activity.getBaseContext()).inflate(R.layout.viewpage_myinfo0, null, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                        activity.getBaseContext(), LinearLayoutManager.VERTICAL, false
                )
        );
        recyclerView.setAdapter(new RecycleAdapter());

    }


    View getView(){
        return view;
    }
    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(activity).inflate(R.layout.item_card2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Obj_book_info tmpData = list.get(position);
            holder.title.setText(tmpData.book.title);
            holder.author.setText(tmpData.book.author);
            holder.publisher.setText(tmpData.book.publisher);
            //holder.date.setText();

        }

        @Override
        public int getItemCount() {
            return list.size();
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
            }
        }
    }
}
