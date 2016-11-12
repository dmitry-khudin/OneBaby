package com.scotty.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scotty.models.ArticleModel;
import com.scotty.onebaby.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bryden_mac on 9/30/16.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyHolder> {
    private Context mContext;
    private List<ArticleModel> list;

    public class MyHolder extends RecyclerView.ViewHolder{
        private TextView textDate, textContents;
        public MyHolder(View itemView) {
            super(itemView);
            textContents = (TextView) itemView.findViewById(R.id.textView35);
            textDate = (TextView) itemView.findViewById(R.id.textView36);
        }
    }
    LayoutInflater layoutInflater;
    public HomeListAdapter(Context context, List<ArticleModel> list) {
        this.mContext = context;
        this.list = list;
        layoutInflater = ( LayoutInflater )mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = null;

            convertView = layoutInflater.inflate(R.layout.cell_homelist_item_layout, null);
        return new MyHolder(convertView);
      //  return null;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.textContents.setText(list.get(position).getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        try {
            Date date = format.parse(list.get(position).getPostdate());
            long diff = today.getTime() - date.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            holder.textContents.setText(list.get(position).getName());
            holder.textDate.setText(days + " days ago");

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }


}
