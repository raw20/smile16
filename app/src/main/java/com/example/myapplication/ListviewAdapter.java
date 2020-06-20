package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class ListviewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<ListViewItem> adapter;
    private Context context;
    private int layout;
    private Calendar items;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = firebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = mRootRef.child("UserList");

    public ListviewAdapter(Context context, int layout, ArrayList<ListViewItem> adapter){
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.adapter=adapter;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView=inflater.inflate(layout,parent,false);
        }
        ListViewItem listViewItem = adapter.get(position);
        final ImageView icon = (ImageView) convertView.findViewById(R.id.userImage);
        icon.setImageResource(listViewItem.getuImage());
        TextView nickname = (TextView) convertView.findViewById(R.id.userNick);
        nickname.setText(listViewItem.getUnick());
        TextView school = (TextView) convertView.findViewById(R.id.userSchool);
        school.setText(listViewItem.getUschool());
        TextView major = (TextView) convertView.findViewById(R.id.userMajor);
        major.setText(listViewItem.getuMajor());
        TextView chat = (TextView) convertView.findViewById(R.id.userChat);
        chat.setText(listViewItem.getuChat());

        return  convertView;
    }

}
