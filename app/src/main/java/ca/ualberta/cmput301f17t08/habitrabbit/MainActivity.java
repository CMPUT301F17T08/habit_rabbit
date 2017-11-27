package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private MainActivity activity = this;
    private StreakChecker receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
//        Intent intent = new Intent(this,Activity.class);
//        startActivity(intent);

        final EditText usernameField = (EditText) findViewById(R.id.username_input_field);
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button signupButton = (Button) findViewById(R.id.signup_button);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String username = usernameField.getText().toString();

                System.out.println("Login Button Clicked - Username:" + username);

                LoginManager.getInstance().login(username, new LoginManager.OnLoginCompleteListener() {
                    @Override
                    public void onLoginComplete() {
                        // TODO: transition activity
                        Log.i("MainActivity", "Login success!");

                        Intent intent = new Intent(activity, MyHabitActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onLoginFailed(String message) {
                        Log.e("MainActivity", "Login failed: " + message);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setMessage(message)
                                .setTitle("Login error")
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
        receiver = new StreakChecker();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));



        // small signup button below the login button
        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(activity, SignupActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister Receivers
        unregisterReceiver(receiver);
    }
}
