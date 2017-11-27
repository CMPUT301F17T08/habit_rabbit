package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FollowUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_user);

        final EditText usernameField = findViewById(R.id.username_input_field);
        Button addButton = findViewById(R.id.follow_user_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();

                // get the user associated with username from the database and add current user's name to their pending list
                if (username.length() > 0) {
                    User followedUser = new User(username);
                    followedUser.getFollowRequests().add(LoginManager.getInstance().getCurrentUser().getUsername());

                    return;
                }
            }
        });
    }

    public void showMenu(View v){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}
