package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by calvin on 11/12/2017.
 */

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {
    private ArrayList<User> likes;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    //TODO; edit it so that it works with follow requests as well
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dateLabel;
        public TextView userNameLabel;


        public ViewHolder(View peopleView) {
            super(peopleView);
            dateLabel = (TextView) peopleView.findViewById(R.id.notifications_time);
            userNameLabel = (TextView) peopleView.findViewById(R.id.notifications_name);
        }
    }

    public LikesAdapter(ArrayList<User> likes) {
        this.likes = likes;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LikesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View likeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_item, parent, false);
        LikesAdapter.ViewHolder viewHolder = new LikesAdapter.ViewHolder(likeView);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.userNameLabel.setText(likes.get(position).getUsername());
        //TODO:make a function to get a date of when user liked post
        //holder.userNameLabel.setText(likes.get(position).get());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return likes.size();
    }
}
