package com.ekeitho.shake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;

/**
 * Created by ekeitho on 2/18/15.
 */
public class LoginFragment extends Fragment {

    /* facebook life cycle helper - listeners on changes of the ui */
    private UiLifecycleHelper uiHelper;
    private TextView userInfoTextView;
    private Session.StatusCallback statusCallback =
            new SessionStatusCallback();
    private ParseUser mUser;
    private ShakeCommunicator shakeCommunicator;


    private static final String TAG = "LoginFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);


        return view;
    }

    /* fires when user logs in or logs out */
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");

            shakeCommunicator.saveSession(session);

            ParseFacebookUtils.logIn(getActivity(), new LogInCallback() {
                @Override
                public void done(final ParseUser parseUser, ParseException e) {
                    Log.d("MyApp", "Initiating parse login.");
                    
                    if (!ParseFacebookUtils.isLinked((parseUser))) {
                        Log.d("MyApp", "Not facebook linked...");
                        ParseFacebookUtils.link(parseUser, getActivity(), new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (ParseFacebookUtils.isLinked(parseUser)) {
                                    Log.d("MyApp", "Woohoo, user logged in with Facebook!");
                                }
                            }
                        });
                    }
                }
            });

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            userInfoTextView.setVisibility(View.INVISIBLE);
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            // Respond to session state changes, ex: updating the view
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        shakeCommunicator = (ShakeCommunicator) getActivity();
    }


    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

}
