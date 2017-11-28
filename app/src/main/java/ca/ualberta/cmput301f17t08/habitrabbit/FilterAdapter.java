package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jackson on 2017-11-25.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private ArrayList<Habit> habits;
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

    public FilterAdapter(ArrayList<Habit> habits, Activity context) {
        this.habits = new ArrayList<Habit>();
        this.habits.clear();
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
                Intent intent = new Intent(context, HabitStatsActivity.class);
                intent.putExtra("habit_id", position);
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return habits.size();
    }



}
