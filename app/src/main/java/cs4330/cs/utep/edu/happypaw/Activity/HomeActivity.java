package cs4330.cs.utep.edu.happypaw.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import cs4330.cs.utep.edu.happypaw.Fragment.HomeFragment;
import cs4330.cs.utep.edu.happypaw.Fragment.LocationFragment;
import cs4330.cs.utep.edu.happypaw.Fragment.ProfileFragment;
import cs4330.cs.utep.edu.happypaw.Helper.SchedulerClient;
import cs4330.cs.utep.edu.happypaw.R;

public class HomeActivity extends AppCompatActivity {

    final static String TAG = "HomeActivity";
    private TextView mTextMessage;
    private SchedulerClient client;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.navigation_dashboard:
                    loadFragment(new ProfileFragment());
                    return true;
                case R.id.navigation_notifications:
                    loadFragment(new LocationFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        client = new SchedulerClient();
        FirebaseApp.initializeApp(this);
        loadFragment(new HomeFragment());
        checkForToken();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void checkForToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        saveClientTokenPref(token);
                        saveClientTokenServer(token);
                        Log.d(TAG, token);
                    }
                });

    }

    private void saveClientTokenPref(String token){
        SharedPreferences sharedPref = getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token_client", token);
        editor.commit();
    }

    private void saveClientTokenServer(String token){
        client.saveClientToken("{\"token\": \""+token+"\"}", new SchedulerClient.SchedulerListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, result);
            }

            @Override
            public void onError(String msg) {
                Log.i(TAG, msg);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.action_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                Toast.makeText(this, "settings", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
