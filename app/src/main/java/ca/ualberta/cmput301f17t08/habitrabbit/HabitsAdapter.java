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

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.ViewHolder> {
    private ArrayList<Habit> habits;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView habitNameLabel;
        public TextView habitReasonLabel;

        public ViewHolder(View habitView) {
            super(habitView);
            habitNameLabel = (TextView) habitView.findViewById(R.id.habit_name);
            habitReasonLabel = (TextView) habitView.findViewById(R.id.habit_reason);
        }
    }

    public HabitsAdapter(ArrayList<Habit> habits) {
        this.habits = habits;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HabitsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View habitView = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_item, parent, false);
        HabitsAdapter.ViewHolder viewHolder = new HabitsAdapter.ViewHolder(habitView);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.habitNameLabel.setText(habits.get(position).getName());
        holder.habitReasonLabel.setText(habits.get(position).getReason());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return habits.size();
    }
}