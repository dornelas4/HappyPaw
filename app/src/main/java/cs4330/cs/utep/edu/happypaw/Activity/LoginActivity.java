package cs4330.cs.utep.edu.happypaw.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;

import cs4330.cs.utep.edu.happypaw.Helper.SchedulerClient;
import cs4330.cs.utep.edu.happypaw.Model.Token;
import cs4330.cs.utep.edu.happypaw.R;

import cs4330.cs.utep.edu.happypaw.Util.TimeUtil;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    EditText emailText;
    EditText passwordText;
    ImageView loginButton;
    SchedulerClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkForToken();

        client = new SchedulerClient();

        emailText = findViewById(R.id.et_email);
        passwordText = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();


        new android.os.Handler().postDelayed(
            () -> {
                Token token = new Token(password, email);
                client.login(token.toJson(), new SchedulerClient.SchedulerListener<Token>() {
                    @Override
                    public void onSuccess(Token result) {
                        onLoginSuccess(result.authorization);
                    }

                    @Override
                    public void onError(String msg) {
                        onLoginFailed();
                    }
                });
                progressDialog.dismiss();
            }, 3000);
    }


    private void saveToken(String auth){
        SharedPreferences sharedPref = getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("auth", auth);
        editor.putString("token_date", TimeUtil.currDate2Str());
        editor.commit();
    }

    private void checkForToken() {
        SharedPreferences sharedPrefs = getSharedPreferences("token", MODE_PRIVATE);

        if(sharedPrefs.contains("token_date") && !isTokenExpired(sharedPrefs)){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private boolean isTokenExpired(SharedPreferences sharedPrefs){
        String strDate = sharedPrefs.getString("token_date", "empty");
        if(strDate.equals("empty"))
            return false;

        Date tokenDate = TimeUtil.str2Date(strDate);
        Date curr = TimeUtil.currDate();
        long difference =  TimeUtil.currDate().getTime() - tokenDate.getTime();
        long differenceDays = difference / (1000 * 60 * 60 * 24);

        return (differenceDays >= 30);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String authorization) {
        runOnUiThread( () -> {
                loginButton.setEnabled(true);
                saveToken(authorization);
            }
        );

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        runOnUiThread( () -> {
            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
            loginButton.setEnabled(true);
                }
        );

    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty()){// || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}