package ca.ualberta.cmput301f17t08.habitrabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/**
 * The activity for followuser
 */
public class FollowUserActivity extends AppCompatActivity {
    public User followUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_user);

        System.out.println("Follow Requests" + LoginManager.getInstance().getCurrentUser().getFollowRequests());

        final EditText usernameField = findViewById(R.id.username_input_field);
        Button addButton = findViewById(R.id.follow_user_button);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();

                // get the user associated with username from the database and add current user's name to their pending list
                if (username.length() > 0) {
                    DatabaseManager.getInstance().getUserData(username, new DatabaseManager.OnUserDataListener() {
                        @Override
                        public void onUserData(User user) {
                            followUser = user;
                            System.out.println(followUser.getUsername());
                            followUser.getFollowRequests().add(LoginManager.getInstance().getCurrentUser().getUsername());

                            // save the data for the other user
                            followUser.save(new DatabaseManager.OnSaveListener() {
                                @Override
                                public void onSaveSuccess() {
                                    finish();
                                }

                                @Override
                                public void onSaveFailure(String message) {
                                    Log.e("FollowUserActivity", "Failed to save user after follow request");
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void onUserDataFailed(String message) {
                            usernameField.setError("User does not exist");
                        }
                    });



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
