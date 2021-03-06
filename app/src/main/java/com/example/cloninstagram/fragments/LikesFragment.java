package com.example.cloninstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cloninstagram.Adapter.LikeArrayAdapter;
import com.example.cloninstagram.CheckLikedImage;
import com.example.cloninstagram.R;
import com.example.cloninstagram.helper.SharedprefManager;
import com.example.cloninstagram.helper.URL;
import com.example.cloninstagram.helper.VolleyHandler;
import com.example.cloninstagram.models.Like;
import com.example.cloninstagram.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LikesFragment extends Fragment {


    ListView likes_lv;
    ArrayList<Like> arrayLikesList;
    LikeArrayAdapter likeArrayAdapter;

    public LikesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_likes, container, false);

        likes_lv = view.findViewById(R.id.likes_lv);
        arrayLikesList = new ArrayList<Like>();
        likeArrayAdapter = new LikeArrayAdapter(getContext(),R.layout.like_single_item,arrayLikesList);

        likes_lv.setAdapter(likeArrayAdapter);

        //like Home fragment

        //get all story id associated with current user_id from likes db
        getAllStoryIds();


        //then

        //get story data from stories database

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)   {
        super.onViewCreated(view, savedInstanceState);


        likes_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Like like = arrayLikesList.get(position);


                if(like != null) {
                    String image = like.getStory_image();
                    String username = like.getStory_username();

                    Intent intent = new Intent(getContext(), CheckLikedImage.class);
                    intent.putExtra("image_url", image);
                    intent.putExtra("image_username", username);

                    startActivity(intent);
                }

            }
        });



    }

    private void getAllStoryIds(){


        User user = SharedprefManager.getInstance(getContext()).getUserData();
        int user_id = user.getId();



        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.get_all_story_ids+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                JSONArray jsonArrayIds =  jsonObject.getJSONArray("ids");

                                Log.i("storyArrayids",jsonArrayIds.toString());
                                //arrayid : [6,8,10,12]

                                String ids = jsonArrayIds.toString();
                                ids = ids.replace("[","");
                                ids = ids.replace("]","");


                                Log.i("arrayidsNew",ids);
                                getAllStoriesThatWeLiked(ids);

                            }else{

                                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                            }


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }


        );

        VolleyHandler.getInstance(getContext().getApplicationContext()).addRequetToQueue(stringRequest);
    }


    private void getAllStoriesThatWeLiked(String ids){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.all_stories_we_liked+ids,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                JSONArray jsonObjectStories =  jsonObject.getJSONArray("stories");

                                Log.i("arrayStoriesforLikes",jsonObjectStories.toString());

                                for(int i = 0 ; i<jsonObjectStories.length(); i++){
                                    JSONObject jsonObjectSingleStory = jsonObjectStories.getJSONObject(i);
                                    Log.i("jsonsinglestoryLikes",jsonObjectSingleStory.toString());

                                    Like like = new Like(jsonObjectSingleStory.getInt("id"),jsonObjectSingleStory.getString("image_url"),
                                            jsonObjectSingleStory.getString("username"));


                                    arrayLikesList.add(like);

                                }


                                likeArrayAdapter.notifyDataSetChanged();

                            }else{

                                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                            }


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }


        );

        VolleyHandler.getInstance(getContext().getApplicationContext()).addRequetToQueue(stringRequest);

    }







}
