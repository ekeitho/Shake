package com.ekeitho.shake;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;


import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ShakeMainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ShakeCommunicator {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Will be null, until the user logs in. Session saved inside of LoginFragment.
     */
    private Session session;

    /**
     * ArrayList to contain the facebook groups.
     */
    private ArrayList<JSONObject> groups = new ArrayList<>();

    ParseUser parse_user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_activity_main);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        if( position == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container,  new LoginFragment())
                    .addToBackStack(null).commit();
        }


        if (groups != null && groups.size() != 0) {
            try {
                new Request(
                        ParseFacebookUtils.getSession(),
                        "/" + groups.get(position).getString("id") + "/members",
                        null,
                        HttpMethod.GET,
                        new Request.Callback() {
                            public void onCompleted(Response response) {
                                /* handle the result */
                                parse_user = ParseUser.getCurrentUser();
                                try
                                {

                                    ArrayList<String> ids = new ArrayList<String>();
                                    JSONObject json = response.getGraphObject().getInnerJSONObject();
                                    JSONArray j_array = json.getJSONArray("data");

                                    for (int i = 0; i < j_array.length(); i++) {
                                        JSONObject obj = j_array.getJSONObject(i);
                                        ids.add(obj.getString("id"));
                                    }

                                    ParseQuery<ParseUser> parseQuery = ParseQuery.getQuery(ParseUser.class);
                                    parseQuery.whereContainedIn("fbid", ids);
                                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                                        @Override
                                        public void done(List<ParseUser> parseUsers, ParseException e) {
                                            /* if the size of the parseUsers is greater then one
                                                then we have successfull tracked someone from the group
                                             */
                                        }
                                    });

                                } catch (JSONException e) {
                                    Log.d("NavDrawerFrag", "Bad json key for json array.");
                                }


                                System.out.println(response);

                            }
                        }
                ).executeAsync();
            } catch (JSONException e) {
                Log.d("SMA", "Bad json call");
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveSession(Session session) {
        this.session = session;

        getGroupData(session);
    }

    /*
        A method that is intended to be called from a fragment, to get the
        array of group names
     */
    @Override
    public String[] getGroupNames() {

        String[] group_names = new String[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            try {
                group_names[i] = groups.get(i).getString("name");

            } catch(JSONException e) {
                Log.v("ShakeMainActivity", "Bad Json Call");
            }
        }


        return group_names;
    }

    private void getGroupData(Session ses) {
        /* get data for the groups and store the results */
        new Request(
                ses,
                "/me/groups",
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        /* handle the result */
                        parse_user = ParseUser.getCurrentUser();

                        try
                        {
                            JSONObject json = response.getGraphObject().getInnerJSONObject();
                            JSONArray j_array = json.getJSONArray("data");
                            String[] ids = new String[j_array.length()];

                            for (int i = 0; i < j_array.length(); i++) {
                                JSONObject obj = j_array.getJSONObject(i);
                                groups.add(obj);
                                ids[i] = obj.getString("id");
                            }

                            parse_user.put("group_ids", new JSONArray(ids));
                            parse_user.saveInBackground();

                            mNavigationDrawerFragment.updateGroups(getGroupNames());

                        } catch (JSONException e) {
                            Log.d("NavDrawerFrag", "Bad json key for json array.");
                        }

                    }
                }
        ).executeAsync();

        /* get data just for yourself and store id */
        new Request(
                ses,
                "/me",
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        /* handle the result */
                        parse_user = ParseUser.getCurrentUser();

                        try {
                            parse_user.put("fbid",
                                    response.getGraphObject().getInnerJSONObject().getString("id"));
                            parse_user.saveInBackground();
                        } catch(JSONException e) {

                        }
                    }
                }
        ).executeAsync();
    }


}
