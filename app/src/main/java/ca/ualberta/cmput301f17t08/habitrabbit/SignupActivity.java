package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

        final Activity activity = this;

        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // TODO handle signup here
                String username = usernameField.getText().toString();
                System.out.println("Sign Up Button Clicked - Username:" + username);

                // TODO test username is not empty
                DatabaseManager.getInstance().createUser(username, new DatabaseManager.OnUserDataListener() {
                    @Override
                    public void onUserData(User user) {
                        // TODO use loginmanager to set as logged in, initialize app


                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setMessage("Your account was successfully created! Please log in.")
                                .setTitle("Signup success")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //
                                    }
                                })
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        activity.finish();
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    @Override
                    public void onUserDataFailed(String message) {
                        Log.e("SignupActivity", "User creation failed: " + message);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setMessage(message)
                                .setTitle("Signup error")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });

    }
}
