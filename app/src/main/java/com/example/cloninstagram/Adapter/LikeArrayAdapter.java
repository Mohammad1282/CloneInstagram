package com.example.cloninstagram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cloninstagram.R;
import com.example.cloninstagram.models.Like;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikeArrayAdapter extends ArrayAdapter<Like> {

    Context mContext;


    public LikeArrayAdapter(@NonNull Context context, int resource, @NonNull List<Like> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Like getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Like item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){

            LayoutInflater li = LayoutInflater.from(mContext);
            view = li.inflate(R.layout.like_single_item,null);
        }


        Like like = getItem(position);

        if(like != null){

            CircleImageView story_image = (CircleImageView) view.findViewById(R.id.story_image);
            TextView story_username_tv = view.findViewById(R.id.story_username_tv);

            if(!like.getStory_image().isEmpty()) {
                Picasso.with(getContext()).load(like.getStory_image()).error(R.drawable.user).into(story_image);
            }


            if(!like.getStory_username().isEmpty()){

                story_username_tv.setText("you have liked " + like.getStory_username() + "'s image");
            }

        }



        return view;
    }









}
