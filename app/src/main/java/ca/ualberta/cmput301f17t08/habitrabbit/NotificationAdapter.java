package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The defined notification adapter
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<Notification> pendingList;
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

    public NotificationAdapter(Context context, ArrayList<Notification> pendingList) {
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
        final Notification notif = pendingList.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.userNameLabel.setText(notif.getUsername());
        holder.dateLabel.setText(notif.getDate().toString());

        final User currentUser = LoginManager.getInstance().getCurrentUser();

        if(notif instanceof FollowNotification){
            holder.infoLabel.setText("wants to follow you");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View new_view = null;
                    builder.setView(new_view=inflater.inflate(R.layout.follower_request, null));

                    final AlertDialog Dialog = builder.create();

                    Button acceptButton = new_view.findViewById(R.id.accept_follower);
                    Button declineButton = new_view.findViewById(R.id.decline_follower);
                    TextView username = new_view.findViewById(R.id.follower_name);

                    username.setText(notif.getUsername());

                    acceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatabaseManager.getInstance().getUserData(pendingList.get(position).getUsername(), new DatabaseManager.OnUserDataListener() {
                                @Override
                                public void onUserData(User user) {
                                    final User acceptUser = user;
                                    currentUser.addFollower(acceptUser);
                                    currentUser.removeFromFollowRequests(acceptUser, new DatabaseManager.OnSaveListener() {
                                        @Override
                                        public void onSaveSuccess() {
                                            acceptUser.addFollowing(currentUser);

                                            currentUser.save(new DatabaseManager.OnSaveListener() {
                                                @Override
                                                public void onSaveSuccess() {
                                                    pendingList.remove(position);
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
                                        public void onSaveFailure(String message) {

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
                            DatabaseManager.getInstance().getUserData(pendingList.get(position).getUsername(), new DatabaseManager.OnUserDataListener() {
                                @Override
                                public void onUserData(User user) {
                                    User declineUser = user;
                                    currentUser.removeFromFollowRequests(declineUser, new DatabaseManager.OnSaveListener() {
                                        @Override
                                        public void onSaveSuccess() {
                                            pendingList.remove(position);
                                            adapter.notifyDataSetChanged();
                                            Dialog.dismiss();
                                        }

                                        @Override
                                        public void onSaveFailure(String message) {

                                        }
                                    });
                                }

                                @Override
                                public void onUserDataFailed(String message) {

                                }
                            });
                        }
                    });

                    Dialog.show();
                }
            });

        }else{
            holder.infoLabel.setText("likes your post");
        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return pendingList.size();
    }
}
