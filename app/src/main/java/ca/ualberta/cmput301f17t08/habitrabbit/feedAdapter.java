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

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView feed_name;
        public TextView num_like;
        public TextView feed_comment;
        public Button like_button;

        public ViewHolder(View feedView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(feedView);
            feed_name = feedView.findViewById(R.id.feed_name);
            num_like = feedView.findViewById(R.id.num_like);
            like_button = feedView.findViewById(R.id.like_button);
            feed_comment = feedView.findViewById(R.id.comment);

        }
    }
    public feedAdapter(ArrayList<HabitEvent> Events) {
        this.habitEvents = habitEvents;
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
        viewHolder.feed_name.setText(habitEvents.get(position).getHabit().getName());
        viewHolder.feed_comment.setText(habitEvents.get(position).getComment());
        viewHolder.num_like.setText(habitEvents.get(position).getLikeCount());

        viewHolder.like_button.setOnClickListener(new View.OnClickListener() {
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