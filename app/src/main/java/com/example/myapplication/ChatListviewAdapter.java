package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatListviewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<ChatListViewItem> adapter;
    private Context context;
    private int layout;
    private Calendar items;

    public ChatListviewAdapter(Context context, int layout, ArrayList<ChatListViewItem> adapter){
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.adapter=adapter;
        this.context=context;
        this.layout=layout;
    }

    @Override
    public int getCount() {
        return adapter.size();
    }

    @Override
    public Object getItem(int position) {
        return adapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null){
            convertView=inflater.inflate(layout,parent,false);
        }
        ChatListViewItem listViewItem = adapter.get(position);
        final ImageView userImage = (ImageView) convertView.findViewById(R.id.userImage);
        StorageReference getStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://smile16-201621220.appspot.com/Images/" + listViewItem.getuImage() );
        Glide.with(context).load(getStorageReference).into(userImage);
        TextView nickname = (TextView) convertView.findViewById(R.id.userNick);
        nickname.setText(listViewItem.getUnick());
        TextView school = (TextView) convertView.findViewById(R.id.userSchool);
        school.setText(listViewItem.getUschool());
        TextView major = (TextView) convertView.findViewById(R.id.userMajor);
        major.setText(listViewItem.getuMajor());
        TextView chat = (TextView) convertView.findViewById(R.id.userChatName);
        chat.setText(listViewItem.getuChat());
        return  convertView;
    }
}
