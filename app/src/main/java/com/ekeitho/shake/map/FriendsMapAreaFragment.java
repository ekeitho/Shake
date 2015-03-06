package com.ekeitho.shake.map;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.ekeitho.shake.ShakeMapCommunicator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekeitho on 2/18/15.
 */
public class FriendsMapAreaFragment extends com.google.android.gms.maps.SupportMapFragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap googleMap;
    private ParseUser parse_user = ParseUser.getCurrentUser();
    private ShakeMapCommunicator shakeMapCommunicator;

    /* keep a reference of the grouped_markers added */
    private ArrayList<Marker> grouped_markers = new ArrayList<>();

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("MyApp", "Connected to Google Map server.");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        /* save the parse location when first connecting */
        parse_user.put("location", new ParseGeoPoint(loc.latitude, loc.longitude));
        parse_user.saveInBackground();

        /* now it's connected, give main activity a reference to this fragment */
        shakeMapCommunicator.receiveMapFragment(this);

        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(loc)
                .title("Me!"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
    }


    /*
        this method is how the main activity is passing values to this fragment.
        Depending on whats being passed in, will vary our queries;
     */
    public void communicate(String groupName, String groupId, ArrayList<String> ids) {

        /*
            if there are grouped markers and the user decided to chose another group
            then delete all the markers and query another set
         */
        if (grouped_markers != null && grouped_markers.size() > 0) {
            for(Marker marker : grouped_markers) {
                marker.remove();
            }
        }

        ParseQuery<ParseUser> parseQuery = ParseQuery.getQuery(ParseUser.class);
        /*
            if your friends have signed up through our app and they are a part
            of your facebook group, then show them on the map.
         */
        parseQuery.whereEqualTo("group_ids", groupId);
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                                            /* if the size of the parseUsers is greater then one
                                                then we have successfull tracked someone from the group
                                             */
                for(ParseUser user : parseUsers) {
                    if(user.getUsername() != parse_user.getUsername()) {
                        addPersonToMap(user.getParseGeoPoint("location"), user.getUsername());
                    }
                }
            }
        });

    }

    /*
        after names have been queried and if there is any that exist then
        add then to the map.
     */
    private void addPersonToMap(ParseGeoPoint geoPoint, String friendsName) {
        System.out.println("adding person..");
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()))
                .title(friendsName));

        grouped_markers.add(marker);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient() {

        Log.d("MyApp", "Build google api client.");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        googleMap = getMap();
        buildGoogleApiClient();
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        shakeMapCommunicator = (ShakeMapCommunicator) getActivity();
    }
}