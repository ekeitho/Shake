package com.ekeitho.shake.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ekeitho.shake.R;
import com.parse.ParseUser;

/**
 * Shows the user profile. This simple activity can only function when there is a valid
 * user, so we must protect it with SampleDispatchActivity in AndroidManifest.xml.
 */
public class ShakeLoginActivity extends Activity {
    private TextView titleTextView;
    private TextView emailTextView;
    private TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_screen_activity);
        titleTextView = (TextView) findViewById(R.id.profile_title);
        emailTextView = (TextView) findViewById(R.id.profile_email);
        nameTextView = (TextView) findViewById(R.id.profile_name);
        titleTextView.setText(R.string.profile_title_logged_in);

        findViewById(R.id.logout_button).setOnClickListener(new OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                ParseUser.logOut();

                // FLAG_ACTIVITY_CLEAR_TASK only works on API 11, so if the user
                // logs out on older devices, we'll just exit.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    Intent intent = new Intent(ShakeLoginActivity.this,
                            ShakeLoginDispatachActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Set up the profile page based on the current user.
        ParseUser user = ParseUser.getCurrentUser();
        showProfile(user);
    }

    /**
     * Shows the profile of the given user.
     *
     * @param user
     */
    private void showProfile(ParseUser user) {
        if (user != null) {
            emailTextView.setText(user.getEmail());
            String fullName = user.getString("name");
            if (fullName != null) {
                nameTextView.setText(fullName);
            }
        }
    }
}