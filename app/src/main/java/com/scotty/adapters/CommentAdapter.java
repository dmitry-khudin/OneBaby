package com.scotty.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kyleduo.switchbutton.SwitchButton;
import com.scotty.models.CommentModel;
import com.scotty.onebaby.R;

import org.w3c.dom.Text;

import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by bryden_mac on 19/09/16.
 */
public class CommentAdapter extends BaseAdapter {


    private List<CommentModel> data;
    private Context context;
    LayoutInflater inflate;

    public CommentAdapter(List<CommentModel> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflate = inflate;
        inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CommentModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inflate.inflate(R.layout.cell_comments_layout, parent, false);

        ToggleButton favoriteButton;
        TextView txtcontents, textDelete, txtdate, txtCount, txtReply;
        ToggleButton swich_button;
        favoriteButton = (ToggleButton) convertView.findViewById(R.id.toggleButton2);
        favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {

                }
                else
                {

                }
            }
        });
        txtReply = (TextView) convertView.findViewById(R.id.textView30);

        txtReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                View view = inflate.inflate(R.layout.reply_dialog_layout, null);
                dialogBuilder.setView(view);
                EditText editText = (EditText) view.findViewById(R.id.editText11);
             //   Button submitButton = (Button) view.findViewById(R.id.button7);
                dialogBuilder.setTitle("Reply");
                String replyText = editText.getText().toString();
                dialogBuilder.setPositiveButton("Reply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog alertDialog = dialogBuilder.create();

//                DialogInterface dialogInterface = dialog.

                alertDialog.show();
            }
        });
        txtcontents = (TextView) convertView.findViewById(R.id.textView26);
        txtCount = (TextView) convertView.findViewById(R.id.textView29);
        txtdate = (TextView) convertView.findViewById(R.id.textView27);
        swich_button = (ToggleButton) convertView.findViewById(R.id.toggleButton);
        textDelete = (TextView) convertView.findViewById(R.id.textView41);
        txtcontents.setText(getItem(position).getContents());
        txtCount.setText(getItem(position).getLikeCount());

        textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                View view = inflate.inflate(R.layout.dialog_report, null);
                dialogBuilder.setView(view);

                dialogBuilder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog alertDialog = dialogBuilder.create();

//                DialogInterface dialogInterface = dialog.
                ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButton);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
           //     materialDialog.setTitle("Report");
            }
        });
        txtdate.setText(getItem(position).getCommentDate());

        return convertView;
    }
}
