package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by calvin on 11/12/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<String> pendingList;
    private Context context;
    private NotificationAdapter adapter;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    //TODO; edit it so that it works with follow requests as well
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dateLabel;
        public TextView userNameLabel;
        public TextView infoLabel;



        public ViewHolder(View peopleView) {
            super(peopleView);
            dateLabel = (TextView) peopleView.findViewById(R.id.notification_date);
            userNameLabel = (TextView) peopleView.findViewById(R.id.notification_user);
            infoLabel = (TextView) peopleView.findViewById(R.id.infoLabel);
        }
    }

    public NotificationAdapter(Context context, ArrayList<String> pendingList) {
        this.pendingList = pendingList;
        this.context = context;
        this.adapter = this;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View likeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_item, parent, false);
        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(likeView);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.userNameLabel.setText(pendingList.get(position));
        final User currentUser = LoginManager.getInstance().getCurrentUser();
        ArrayList<String> follwingRequest = currentUser.getFollowRequests();

        for (String username : pendingList){
            if(follwingRequest.contains(username)){
                holder.infoLabel.setText("wants to follow you");
            }
            else{
                holder.infoLabel.setText("likes your post");
            }
        }
        //TODO:make a function to get a date of when user liked post
        //holder.userNameLabel.setText(likes.get(position).get());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View new_view = null;
                builder.setView(new_view=inflater.inflate(R.layout.follower_request, null));

                Button acceptButton = new_view.findViewById(R.id.accept_follower);
                Button declineButton = new_view.findViewById(R.id.decline_follower);

                final AlertDialog Dialog = builder.create();
                Dialog.show();

                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            DatabaseManager.getInstance().getUserData(pendingList.get(position), new DatabaseManager.OnUserDataListener() {
                                @Override
                                public void onUserData(User user) {
                                    final User acceptUser = user;
                                    currentUser.addFollower(acceptUser);
                                    currentUser.removeFromFollowRequests(acceptUser);
                                    acceptUser.addFollowing(currentUser);

                                    currentUser.save(new DatabaseManager.OnSaveListener() {
                                        @Override
                                        public void onSaveSuccess() {
                                            adapter.notifyDataSetChanged();

                                            Dialog.dismiss();
                                        }

                                        @Override
                                        public void onSaveFailure(String message) {
                                            Log.e("FollowUserActivity", "Failed to save user after follow request");

                                        }
                                    });

                                    acceptUser.save(new DatabaseManager.OnSaveListener() {
                                        @Override
                                        public void onSaveSuccess() {

                                        }

                                        @Override
                                        public void onSaveFailure(String message) {
                                            Log.e("FollowUserActivity", "Failed to save user after follow request");

                                        }
                                    });



                                }

                                @Override
                                public void onUserDataFailed(String message) {

                                }
                            });
                    }
                });

                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseManager.getInstance().getUserData(pendingList.get(position), new DatabaseManager.OnUserDataListener() {
                            @Override
                            public void onUserData(User user) {
                                User declineUser = user;
                                currentUser.removeFromFollowRequests(declineUser);
                                Dialog.dismiss();
                            }

                            @Override
                            public void onUserDataFailed(String message) {

                            }
                        });
                    }
                });
            }
        });
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return pendingList.size();
    }
}
