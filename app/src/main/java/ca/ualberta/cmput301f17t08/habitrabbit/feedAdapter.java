package ca.ualberta.cmput301f17t08.habitrabbit;

/**
 * Created by yuxuanzhao on 2017-11-07.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class feedAdapter extends RecyclerView.Adapter<feedAdapter.ViewHolder> {
    private  ArrayList<HabitEvent> habitEvents;
    public String username;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView feedName;
        public TextView numLike;
        public TextView feedComment;
        public TextView userNameView;
        public Button likeButton;
        public TextView feedDate;


        public ViewHolder(View feedView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(feedView);
            feedName = feedView.findViewById(R.id.feed_name);
            numLike = feedView.findViewById(R.id.num_like);
            likeButton = feedView.findViewById(R.id.like_button);
            feedComment = feedView.findViewById(R.id.comment);
            userNameView = feedView.findViewById(R.id.feed_username);
            feedDate = feedView.findViewById(R.id.feed_time);


        }
    }
    public feedAdapter(String username,ArrayList<HabitEvent> habitEvents) {
        this.habitEvents = habitEvents;
        this.username =  username;
    }
    @Override
    public feedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View feedView = inflater.inflate(R.layout.feed_post, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(feedView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(feedAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.feedName.setText(habitEvents.get(position).getHabit().getName());
        viewHolder.feedComment.setText(habitEvents.get(position).getComment());
        viewHolder.feedDate.setText(habitEvents.get(position).getDateCompleted().toString());
        viewHolder.numLike.setText(Integer.toString(habitEvents.get(position).getLikeCount())+" likes");
        viewHolder.userNameView.setText(username);
        viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habitEvents.get(position).like(LoginManager.getInstance().getCurrentUser().getUserName());
            }
        });


    }
    @Override
    public int getItemCount() {
        return 1;
    }




}