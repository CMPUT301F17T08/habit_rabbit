package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

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
        public LinearLayout frequencyLayout;













        public ViewHolder(View habitView) {
            super(habitView);

            habitNameLabel = (TextView) habitView.findViewById(R.id.habit_name);
            habitReasonLabel = (TextView) habitView.findViewById(R.id.habit_reason);
            frequencyLayout = (LinearLayout) habitView.findViewById(R.id.habbitLayout);








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


        holder.habitNameLabel.setText(habits.get(position).getName());
        holder.habitReasonLabel.setText(habits.get(position).getReason());
        ArrayList<Integer> frequencyList = habits.get(position).getFrequency();
        ArrayList<String> DayList = new ArrayList<String>(Arrays.asList(new String []{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"}));
//        System.out.println(frequencyList.toString());


        for (int counter = 0; counter < frequencyList.size(); counter++) {
            if (frequencyList.get(counter) == 1){
                Button chargingButton = (Button)holder.frequencyLayout.findViewWithTag(Integer.toString(counter+1));
                chargingButton.setBackgroundColor(R.drawable.gradient);


            }
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return habits.size();
    }
}