package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/*
Allows the user to edit a habit
 */
public class EditHabitActivity extends AppCompatActivity {
    private EditHabitActivity activity = this;
    private ArrayList<Integer> frequency;
    private List<String> frequencyButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_habit);

        /*
        TODO pass in a habit object when starting this activity like this:
        Habit habit = new Habit(tempName, tempReason, tempDate, tempFrequency);

        Intent myIntent = new Intent(this, EditHabitActivity.class);
        myIntent.putExtra("habit", habit);
        startActivity(myIntent);
         */
        
        final Habit habit = (Habit) getIntent().getSerializableExtra("habit");
        String tempName = habit.getName();
        String tempReason = habit.getReason();
        Date tempDate = habit.getDate();
        ArrayList<Integer> tempFrequency = habit.getFrequency();


        // initialize as empty array but will be replaced with tempFrequency after the frequency buttons are filled in
        frequency = new ArrayList<Integer>(Collections.nCopies(7, 0));
        frequencyButtons = Arrays.asList("button_m","button_t","button_w","button_r","button_f","button_sa","button_su");

        final SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy");
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));

        final EditText habitTitle = findViewById(R.id.habit_title_field);
        final EditText habitReason = findViewById(R.id.habit_reason_field);
        final EditText dateSelector = findViewById(R.id.habit_date_selector);
        Button saveHabitButton = findViewById(R.id.save_habit_button);
        Button deleteHabitButton = findViewById(R.id.delete_habit_button);

        // TODO set the habit name, reason, frequency from the intent here
        habitTitle.setText(tempName);
        habitReason.setText(tempReason);
        dateSelector.setText(format.format(tempDate));

        // autofill the frequency
        for(int i = 0; i < tempFrequency.size(); i++){
            if (tempFrequency.get(i) == 1){
                String buttonID = frequencyButtons.get(i);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

                // get the frequency button and simulate a click on it
                Button b = findViewById(resID);
                System.out.println(b.getText());
                b.performClick();
            }
        }

        // this line must appear after autofill otherwise frequency won't get filled
        frequency = tempFrequency;

        // date picker
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // update the editText label
                dateSelector.setText(format.format(calendar.getTime()));
            }
        };

        // when the date field is clicked on the edit habit page
        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(EditHabitActivity.this, R.style.date_picker, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                // ensure that a previous date can't be selected
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();
            }
        });

        // submit button
        saveHabitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String title = habitTitle.getText().toString();
                String reason = habitReason.getText().toString();
                Date date = calendar.getTime();

                Boolean error = false;

                // input error checking
                if (title.length() > 20){
                    habitTitle.setError("Please keep title under 20 characters");
                    error = true;
                }
                else if (title.length() == 0){
                    habitTitle.setError("Please enter a title");
                    error = true;
                }

                if (reason.length() > 30){
                    habitReason.setError("Please keep reason under 30 characters");
                    error = true;
                }
                else if (reason.length() == 0){
                    habitReason.setError("Please enter a reason");
                    error = true;
                }

                if (!frequency.contains(1)){
                    // if no day is selected for the frequency - set it to daily by default
                    frequency = new ArrayList<Integer>(Collections.nCopies(7, 1));
                }

                // TODO check that the habit name doesn't exist already

                if (!error){
                    // update the habit object with the new values
                    habit.setName(title);
                    habit.setReason(reason);
                    habit.setDate(date);
                    habit.setFrequency(frequency);
                }
                // TODO exit the activity here
            }
        });

        //delete button
        deleteHabitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // TODO remove the habit object from the user's habit list
                LoginManager.getInstance().getCurrentUser().removeHabit(habit);
            }
        });

    }
    // called by the 7 day selector buttons
    public void updateFrequency(View v){
        Button clickedButton = findViewById(v.getId());
        String buttonID = getResources().getResourceEntryName(v.getId());

        int buttonIndex = frequencyButtons.indexOf(buttonID);

        if (frequency.get(buttonIndex).equals(0)){
            // this button is being activated
            Drawable gradientBackground = ContextCompat.getDrawable(this, R.drawable.gradient);
            clickedButton.setBackground(gradientBackground);
            frequency.set(buttonIndex, 1);

        }else{
            // this button is being deactivated
            clickedButton.setBackgroundColor(Color.parseColor("#ffffff"));
            frequency.set(buttonIndex, 0);
        }
    }
}
