package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The defined adapter for the people layout
 */
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    private ArrayList<String> peoples;
    private Activity context;

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

    public PeopleAdapter(ArrayList<String> peoples, Activity context) {
        this.peoples = peoples;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View peopleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item, parent, false);
        PeopleAdapter.ViewHolder viewHolder = new PeopleAdapter.ViewHolder(peopleView);

        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.peopleNameLabel.setText(peoples.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FollowingHabitActivity.class);
                intent.putExtra("FollowingName",peoples.get(position));
                context.startActivity(intent);
            }
        });

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return peoples.size();
    }
}