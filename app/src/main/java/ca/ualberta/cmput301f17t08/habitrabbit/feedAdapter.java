package ca.ualberta.cmput301f17t08.habitrabbit;

/**
 * The adapter for defined feed view
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class feedAdapter extends RecyclerView.Adapter<feedAdapter.ViewHolder> {
    private  ArrayList<HabitEvent> habitEvents;
    public String username;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView feedName; //the name of the feed
        public TextView numLike; // the num of likes in feed
        public TextView feedComment;//comment on each feed
        public TextView userNameView; // the username of user who posts the feed
        public Button likeButton; // the like button
        public TextView feedDate;// the date that feed is created
        public ImageView imagePreview;// the image attached with the feed


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
            imagePreview = feedView.findViewById(R.id.feed_image);
        }
    }
    public feedAdapter(String username,ArrayList<HabitEvent> habitEvents) {
        this.habitEvents = habitEvents; //get the habitsEvents list passed in
        this.username =  username;//get the username passed in from activity class
    }
    @Override
    public feedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();//get the context
        LayoutInflater inflater = LayoutInflater.from(context);//initialize the layout inflater

        // Inflate the custom layout
        View feedView = inflater.inflate(R.layout.feed_post, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(feedView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(feedAdapter.ViewHolder viewHolder, final int position) {
        //change the text and appearance of each elements on the layout
        viewHolder.feedName.setText(habitEvents.get(position).getHabit().getName());
        viewHolder.feedComment.setText(habitEvents.get(position).getComment());
        viewHolder.feedDate.setText(habitEvents.get(position).getDateCompleted().toString());
        viewHolder.numLike.setText(Integer.toString(habitEvents.get(position).getLikeCount())+" likes");
        viewHolder.userNameView.setText(username);
        //get the image the user uploaded, set the image if exist
        Bitmap userImage = habitEvents.get(position).getPicture();
        if (userImage != null){
            viewHolder.imagePreview.setImageBitmap(userImage);
        }
        //functionality of likes to the post
        viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habitEvents.get(position).like(LoginManager.getInstance().getCurrentUser().getUsername());
            }
        });
    }
    @Override
    public int getItemCount() {
        return 1;
    }
}