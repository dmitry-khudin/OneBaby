package com.scotty.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scotty.models.PurchaseHistoryModel;
import com.scotty.onebaby.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by my_mac on 9/12/16.
 */
public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.MyHolder>{

    private Context context;
    private List<PurchaseHistoryModel> data;
//    private static LayoutInflater inflater = null;

    public class MyHolder extends RecyclerView.ViewHolder{
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
        public ImageView img;
        public MyHolder(View convertView) {
            super(convertView);

            this.img = (ImageView) convertView.findViewById(R.id.imageView7);
            this.tv1 = (TextView) convertView.findViewById(R.id.textView11);
            this.tv2 = (TextView) convertView.findViewById(R.id.textView13);
            this.tv3 = (TextView) convertView.findViewById(R.id.textView12);
            this.tv4 = (TextView) convertView.findViewById(R.id.textView14);
        }
    }

    public PurchaseHistoryAdapter(Context context, List<PurchaseHistoryModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_purchase_history, null);
        return new MyHolder(itemView);
      //  return null;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        PurchaseHistoryModel model = data.get(position);
        holder.tv1.setText(model.getContents1());
        holder.tv2.setText(model.getContents2());
        holder.tv3.setText(model.getMoney());
        holder.tv4.setText(model.getDate());
        Picasso.with(context).load(model.getImgURL()).error(R.drawable.sample_1).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
