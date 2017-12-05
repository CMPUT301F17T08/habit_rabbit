package ca.ualberta.cmput301f17t08.habitrabbit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * The activity allows the user to add a habit event
 */
public class AddHabitEventActivity extends AppCompatActivity {
    private AddHabitEventActivity activity = this;
    private ImageView imagePreview;
    private Bitmap bmp;
    private gpsTracker gpsTracker;
    private Location location;
    private Button addButton;
    private ImageButton locationButton;
    private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habit_event);

        final Habit habit = (Habit) getIntent().getSerializableExtra("habit");


        final EditText habitTitle = findViewById(R.id.habit_name_field);
        final EditText habitComment = findViewById(R.id.habit_comment_field);
        imagePreview = findViewById(R.id.image_preview);
        addButton = findViewById(R.id.add_habit_event_button);
        locationButton = findViewById(R.id.location_button);

        habitTitle.setText(habit.getName());

        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.
                        ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                    // Request the permission.
                    // Dummy request code 8 used.
                    ActivityCompat.requestPermissions(activity,
                            new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 8);
                }else{
                    locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            location = task.getResult();
                            locationButton.setImageResource(R.drawable.location_icon);
                        }
                    });
                }

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = habitComment.getText().toString();

                Boolean error = false;

                if (comment.length() > 20) {
                    habitTitle.setError("Please keep title under 20 characters");
                    error = true;
                }
                else if (bmp != null && bmp.getByteCount() > 65536){
                    Toast.makeText(activity, "Please select a size smaller than 65Kb",
                            Toast.LENGTH_LONG).show();
                    error = true;
                }

                String currentUsername = LoginManager.getInstance().getCurrentUser().getUsername();
                if (!error){
                    final HabitEvent event = new HabitEvent(habit.getId(), currentUsername ,calendar.getTime(), comment, location, bmp);
                    habit.addHabitEvent(event, new DatabaseManager.OnSaveListener() {
                        @Override
                        public void onSaveSuccess() {
                            habit.markDone();

                            habit.sync(new DatabaseManager.OnSaveListener() {
                                @Override
                                public void onSaveSuccess() {
                                    finish();
                                }

                                @Override
                                public void onSaveFailure(String message) {
                                    // TODO: show error message
                                }
                            });

                        }

                        @Override
                        public void onSaveFailure(String message) {
                            // TODO: show error message
                            Log.e("AddHabitEventActivity", "Failed to save HabitEvent!");
                        }
                    });


                }
            }
        });

    }

    public void pickImage(View v) {
        // clear the old image if anything was selected before
        bmp = null;
        imagePreview.setImageResource(0);

        // show the dialog for picking an image
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    // Called when location perm is granted
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 8: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location granted!
                    locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            location = new Location(task.getResult());
                        }
                    });
                }
            }
        }
    }

    // does the image selection and displays the image in the preview window
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = activity.getContentResolver().openInputStream(data.getData());

                bmp = BitmapFactory.decodeStream(inputStream);
                imagePreview.setImageBitmap(bmp);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}
