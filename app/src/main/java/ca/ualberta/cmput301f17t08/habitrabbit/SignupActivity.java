package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by maharshmellow on 2017-10-22.
 */

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
            }
        });

    }
}
