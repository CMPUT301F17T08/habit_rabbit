package ca.ualberta.cmput301f17t08.habitrabbit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private MainActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_item);
//
//        final EditText usernameField = (EditText) findViewById(R.id.username_input_field);
//        Button loginButton = (Button) findViewById(R.id.login_button);
//        Button signupButton = (Button) findViewById(R.id.signup_button);
//
//        loginButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                // TODO handle login here
//                String username = usernameField.getText().toString();
//
//                System.out.println("Login Button Clicked - Username:" + username);
//            }
//        });
//
//        // small signup button below the login button
//        signupButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(activity, SignupActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
