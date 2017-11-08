package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddHabitEventActivity extends AppCompatActivity {
    private AddHabitEventActivity activity = this;
    private ImageView imagePreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habit_event);
        EditText habitTitle = findViewById(R.id.habit_name_field);
        EditText habitComment = findViewById(R.id.habit_comment_field);
        imagePreview = findViewById(R.id.image_preview);

        habitTitle.setText("Some Default Habit Name");
    }
    public void pickImage(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
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
                InputStream inputStream = activity.getContentResolver().openInputStream(data
                            .getData());

                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
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
