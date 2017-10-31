package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        final EditText usernameField = (EditText) findViewById(R.id.username_input_field);
        Button signupButton = (Button) findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // TODO handle signup here
                String username = usernameField.getText().toString();
                System.out.println("Sign Up Button Clicked - Username:" + username);

                // TODO test username is not empty
                DatabaseManager.getInstance().createUser(username, new DatabaseManager.OnUserCreatedListener() {
                    @Override
                    public void onUserCreated(User user) {
                        // TODO use loginmanager to set as logged in, initialize app
                        if(user == null) {
                            Log.e("SignupActivity", "User creation failed.");
                        }else{
                            Log.i("SignupActivity", "User created: " + user.getUsername());
                        }
                    }
                });
            }
        });

    }
}
