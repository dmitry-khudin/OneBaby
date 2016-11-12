package com.scotty.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.scotty.models.CommentTableModel;
import com.scotty.models.ReplyModel;
import com.scotty.onebaby.CommentViewActivity;
import com.scotty.onebaby.ContentTopPage;
import com.scotty.onebaby.R;
import com.scotty.utils.General;

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by bryden on 10/4/16.
 */
public class ExpandableCommentAdapter extends BaseExpandableListAdapter {
    private List<CommentTableModel> list;
    private Context context;
    private HashMap<String, List<ReplyModel>> detailContent;
    LayoutInflater layoutInflater;

    public ExpandableCommentAdapter(List<CommentTableModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.detailContent = detailContent;
        layoutInflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        CommentTableModel commentTableModel = this.list.get(groupPosition);
    //    String commentID = getGroup(groupPosition).getCommentID();
    //    List<ReplyModel> replyModels = detailContent.get(commentID);
    //    Log.d("child_size" , " " + replyModels.size());
        return getGroup(groupPosition).getReplyModels().size();
      //  return replyModels.size();
    }

    @Override
    public CommentTableModel getGroup(int groupPosition) {

        return list.get(groupPosition);
    }

    @Override
    public ReplyModel getChild(int groupPosition, int childPosition) {
        String commentID = getGroup(groupPosition).getCommentID();
     //   List<ReplyModel> replyModels = detailContent.get(commentID);
     //   Log.d("child_size" , " " + replyModels.size());
//        return getGroup(groupPosition).getReplyModels().size();
        ReplyModel model = list.get(groupPosition).getReplyModels().get(childPosition);
      //  Log.d("model", model.getPostdate());
        return model;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.cell_comments_layout, null);
        }
        TextView txtcontents, textDelete, txtdate, txtCount, txtReply, txtName, txtReport;
        ToggleButton like_button, flag_button;

        txtReply = (TextView) convertView.findViewById(R.id.textView30);
        txtReport = (TextView) convertView.findViewById(R.id.textView45);
        txtName = (TextView) convertView.findViewById(R.id.textView25);
        txtName.setText(getGroup(groupPosition).getUsername());
        String uid = getGroup(groupPosition).getUserID();
        txtcontents = (TextView) convertView.findViewById(R.id.textView26);
        txtCount = (TextView) convertView.findViewById(R.id.textView29);
        txtdate = (TextView) convertView.findViewById(R.id.textView27);
        like_button = (ToggleButton) convertView.findViewById(R.id.toggleButton);
        flag_button = (ToggleButton) convertView.findViewById(R.id.toggleButton2);
        flag_button.setChecked(getFlag(groupPosition));
        like_button.setChecked(getLike(groupPosition));
        textDelete = (TextView) convertView.findViewById(R.id.textView41);
        txtcontents.setText(getGroup(groupPosition).getComments());
        txtCount.setText(getGroup(groupPosition).getLikes() + "");

     //   if (uid == null)
        try {
            if (uid.equals(General.UID)) {
           //     txtReply.setVisibility(View.VISIBLE);
                textDelete.setVisibility(View.VISIBLE);
            } else {
             //   txtReply.setVisibility(View.GONE);
                textDelete.setVisibility(View.GONE);
            }
        }
        catch (NullPointerException e) {
//            if (uid == null)
//            Toast.makeText(context, "user null", Toast.LENGTH_SHORT).show();
//            else if (General.UID == null)
//            {
//                Toast.makeText(context, "model null", Toast.LENGTH_SHORT).show();
//            }
        }
        final ContentTopPage commentViewActivity = (ContentTopPage) this.context;
        txtReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                View view = layoutInflater.inflate(R.layout.reply_dialog_layout, null);
                dialogBuilder.setView(view);
                final EditText editText = (EditText) view.findViewById(R.id.editText11);
                //   Button submitButton = (Button) view.findViewById(R.id.button7);
                dialogBuilder.setTitle("Reply");
                General.hideSoftKeyboard(context, editText);
                dialogBuilder.setPositiveButton("Reply", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog.dismiss();
                        String replyText = editText.getText().toString();

                        commentViewActivity.Reply(replyText, getGroup(groupPosition));
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

        flag_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                commentViewActivity.setFlag(isChecked, getGroup(groupPosition));
            }
        });
        like_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                commentViewActivity.setLike(isChecked, getGroup(groupPosition));
            }
        });


        textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setMessage("Are you sure to delete this comment?");
                dialogBuilder.setTitle("OneBaby");
                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commentViewActivity.Delete(getGroup(groupPosition));
                    }
                });
                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogBuilder.create().show();
                //     materialDialog.setTitle("Report");
            }
        });

        txtReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                View view = layoutInflater.inflate(R.layout.dialog_report, null);
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
        txtdate.setText(getGroup(groupPosition).getPostdate());

        return convertView;
    }


    private boolean getLike(int groupPosition)
    {
        CommentTableModel model = getGroup(groupPosition);
        HashMap<String, HashMap<String, String>> map = model.getLikelist();
        HashMap<String , String > hashMap;
        if (map != null)
        {
            for (String key : map.keySet())
            {
                hashMap = map.get(key);
                String username = hashMap.get("username");
                String userID = hashMap.get("userID");
                if (userID != null && userID.equals(General.currentUser.getUid())) return true;
            }
        }
        return false;
    }

    private boolean getFlag(int groupPosition)
    {
        CommentTableModel model = getGroup(groupPosition);
        HashMap<String, HashMap<String, String>> map = model.getFlaglist();
        HashMap<String , String > hashMap;
        if (map != null)
        {
            for (String key : map.keySet())
            {
                hashMap = map.get(key);
                String username = hashMap.get("username");
                String userID = hashMap.get("userID");
                if (userID != null && userID.equals(General.currentUser.getUid())) return true;
            }
        }
        return false;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.comment_child_cell_layout, null);
        }
        TextView txtcontents, txtdate, txtName;
        txtName = (TextView) convertView.findViewById(R.id.textView25);
        txtcontents = (TextView) convertView.findViewById(R.id.textView26);
        txtdate = (TextView) convertView.findViewById(R.id.textView27);
        txtcontents.setText(getChild(groupPosition, childPosition).getUsercomment());
        txtName.setText(getChild(groupPosition, childPosition).getUsername());
        txtdate.setText(getChild(groupPosition, childPosition).getPostdate());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
