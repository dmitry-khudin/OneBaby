package com.scotty.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scotty.models.AuthorModel;
import com.scotty.models.VariourTitleModel;
import com.scotty.onebaby.R;
import com.scotty.onebaby.SelectedTitlePage;
import com.scotty.utils.Constants;
import com.scotty.utils.General;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by my_mac on 9/12/16.
 */
public class Various_Title_Adapter extends BaseAdapter {
    List<AuthorModel>  data;
    Context context;
    private static LayoutInflater inflater=null;
    private boolean[] statelist;
    int deviceWidth; int deviceHeight;
    public Various_Title_Adapter() {
    }

    public Various_Title_Adapter(List<AuthorModel>  data, Context context) {

        deviceWidth = Constants.deviceWidth;
        deviceHeight = Constants.deviceHeight;
        this.data = data;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        statelist = new boolean[data.size()];
        for (int i = 0; i < data.size(); i++) statelist[i] = false;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public AuthorModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View rowView;
        Holder holder;
        final AuthorModel model = getItem(position);
        if (convertView == null) {


         //   holder = new Holder();
            convertView = inflater.inflate(R.layout.cell_various_title,  null);
            holder = new Holder();
            convertView.setTag(holder);
            //  holder.tv=(TextView) rowView.findViewById(R.id.textView1);
            holder.img = (ImageView) convertView.findViewById(R.id.imageView);
            holder.imgFavorite = (ToggleButton) convertView.findViewById(R.id.imagebutton2);

            holder.mainLayout = (RelativeLayout) convertView.findViewById(R.id.mainLayout);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }
        TextView tv;
        ImageView img;
        ToggleButton imgFavorite;
        RelativeLayout mainLayout;
     //   holder.imgFavorite.setId(position);
        holder.imgFavorite.setChecked(isFavorite(position));

        final DatabaseReference ref = Constants.Author_Database.child(model.getKey()).child("favorites");
        holder.imgFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    DatabaseReference temp = ref.push();
                    String key = temp.getKey();
                    temp.child("userID").setValue(General.UID);
                    temp.child("deviceToken").setValue(General.deviceToken);
                }
                else
                {
                    Query applesQuery = ref.orderByChild("userID").equalTo(General.UID);
                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("tag", "onCancelled", databaseError.toException());
                        }
                    });
                }
            }
        });

        int width = (deviceWidth - 30) >> 1;
        holder.img.getLayoutParams().width = width;
        Log.d("width" , "width + " + width);
        int height = (int) (width * 1.2f);
        holder.img.getLayoutParams().height = height;
        holder.img.setImageResource(Constants.ids[position % 7] );
     //   Picasso.with(this.context).load(data.get(position)).error(R.drawable.sample).into(holder.img);
     //   holder.tv.setText(data.get(position));
        holder.img.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.d("Grid Itemt"," Gird " + position);
               Toast.makeText(context, "position " + position, Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(context, SelectedTitlePage.class);

               intent.putExtra("model", getItem(position));
               context.startActivity(intent);

           }
       });
        return convertView;

    }

    private boolean isFavorite(int position)
    {
        AuthorModel model = getItem(position);
        if (model.getFavorites() != null) {

            HashMap<String, HashMap<String, String>> hashMap = model.getFavorites();

            for (String hashKey : hashMap.keySet())
            {
                HashMap<String, String> favo = hashMap.get(hashKey);
                String uid = favo.get("userID");
                if (uid.equals(General.UID)) return true;
            }

        }
        else
            return false;
        return false;
    }
    public class Holder
    {
        TextView tv;
        ImageView img;
        ToggleButton imgFavorite;
        RelativeLayout mainLayout;

    }

}
