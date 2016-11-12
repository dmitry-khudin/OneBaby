package com.scotty.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scotty.models.ArticleModel;
import com.scotty.onebaby.ContentTopPage;
import com.scotty.onebaby.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by bryden_mac on 19/09/16.
 */
public class ArticleHorizontalAdapter extends BaseAdapter{
    private List<ArticleModel> articleModelList;
    private Context context;

    private static LayoutInflater layoutInflater;
    public ArticleHorizontalAdapter(List<ArticleModel> articleModelList, Context context) {
        this.articleModelList = articleModelList;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return articleModelList.size();
    }

    @Override
    public ArticleModel getItem(int position) {
        return articleModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.cell_horizontal_article, parent, false);
        ArticleModel model = articleModelList.get(position);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.textView23);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView24);
        ImageView articleImageView = (ImageView) convertView.findViewById(R.id.imageView2);
        dateTextView.setText(model.getPostdate());
        titleTextView.setText(model.getName());
//        articleImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ContentTopPage.class);
//                context.startActivity(intent);
//
//            }
//        });
        Picasso.with(context).load(model.getImage()).error(R.drawable.sample_1).into(articleImageView);
        return convertView;
    }

    public class Holder {
        public TextView dateTextView, titleTextView;
        public ImageView articleImageView;
        public Holder() {
        }
    }
}
