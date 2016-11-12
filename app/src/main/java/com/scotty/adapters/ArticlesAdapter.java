package com.scotty.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scotty.models.ArticleModel;
import com.scotty.onebaby.ContentTopPage;
import com.scotty.onebaby.R;
import com.scotty.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bryden_mac on 19/09/16.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>
{
    private List<ArticleModel> articleModelList;
    private Context context;

    int DeviceHeight, DeviceWidth;

    public ArticlesAdapter(List<ArticleModel> articleModelList, Context context) {
        this.articleModelList = articleModelList;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        DeviceWidth = displayMetrics.widthPixels;
        DeviceHeight = displayMetrics.heightPixels;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder{
        public TextView dateTextView;
        public TextView titleTextView;
        public ImageView articleImageView;
        public TextView numberTextView;


        public ArticleViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.textView23);
            titleTextView = (TextView) itemView.findViewById(R.id.textView24);
            articleImageView = (ImageView) itemView.findViewById(R.id.imageView2);
            numberTextView = (TextView) itemView.findViewById(R.id.textView40);
            int width = (DeviceWidth - 30) >> 1;
            itemView.getLayoutParams().height = width * 11 / 12;
        }
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_article_view, parent, false);

        return new ArticleViewHolder(itemView);
//        return null;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, final int position) {

        ArticleModel model = articleModelList.get(position);
        holder.dateTextView.setText(model.getPostdate());
        holder.titleTextView.setText(model.getName());
        if (model.getCommentCount() > 0)
            holder.numberTextView.setText("comments : " + model.getCommentCount() + "");
        else
            holder.numberTextView.setText("comments : " + "0");
        holder.articleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ContentTopPage.class);
                intent.putExtra(Constants.ONE_ARTICLE, articleModelList.get(position));
                context.startActivity(intent);

            }
        });
        Picasso.with(context).load(model.getImage()).error(R.drawable.baby_image_1).into(holder.articleImageView);
    }

    @Override
    public int getItemCount() {
        return articleModelList.size();
    }
}
