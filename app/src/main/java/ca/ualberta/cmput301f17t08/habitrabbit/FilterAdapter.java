package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The defined filter layout adapter
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private static ArrayList<Habit> habits;
    private Activity context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView habitNameLabel;

        public ViewHolder(View habitView) {
            super(habitView);

            habitNameLabel = (TextView) habitView.findViewById(R.id.habit_name);
        }
    }

    //constructor for FilterAdapter
    public FilterAdapter(ArrayList<Habit> habits, Activity context) {
        this.habits = new ArrayList<Habit>();
        this.habits.addAll(habits);
        notifyDataSetChanged();
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View habitView = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_name_card, parent, false);
        FilterAdapter.ViewHolder viewHolder = new FilterAdapter.ViewHolder(habitView);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override

    public void onBindViewHolder(FilterAdapter.ViewHolder holder, final int position) {

        Habit habit = habits.get(position);

        holder.habitNameLabel.setText(habit.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.filter = habits.get(position).getId();
                context.finish();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return habits.size();
    }
}
