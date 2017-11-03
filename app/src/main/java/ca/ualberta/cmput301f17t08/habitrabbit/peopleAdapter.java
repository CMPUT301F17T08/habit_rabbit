package ca.ualberta.cmput301f17t08.habitrabbit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by micah on 01/11/17.
 */

public class peopleAdapter extends RecyclerView.Adapter<peopleAdapter.ViewHolder> {
    private ArrayList<User> peoples;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView peopleNameLabel;

        public ViewHolder(View peopleView) {
            super(peopleView);
            peopleNameLabel = (TextView) peopleView.findViewById(R.id.user_name);
        }
    }

    public peopleAdapter(ArrayList<User> peoples) {
        this.peoples = peoples;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public peopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View peopleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item, parent, false);
        peopleAdapter.ViewHolder viewHolder = new peopleAdapter.ViewHolder(peopleView);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.peopleNameLabel.setText(peoples.get(position).getUsername());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return peoples.size();
    }
}