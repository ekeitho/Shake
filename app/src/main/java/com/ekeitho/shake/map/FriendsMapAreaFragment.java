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
import java.util.HashMap;
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
        googleMap.setMyLocationEnabled(true);
        communicate(null, parse_user.getString("active_group"), null);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
    }

    /*
        Returns the last known location of the user.
     */
    public Location getLastLocation() {
        return mLastLocation;
    }

    /*
        Finds the mid point of all found users to focus the map
        in a cleaner way, rather than having to keep zooming in and out
        to find your friends.
     */
    public static void findMidPoint(ArrayList<ParseGeoPoint> geoPoints){

        ArrayList<Double> group_x = new ArrayList<>();
        ArrayList<Double> group_y = new ArrayList<>();
        ArrayList<Double> group_z = new ArrayList<>();

        double sum_x = 0.0;
        double sum_y = 0.0;
        double sum_z = 0.0;



        for(ParseGeoPoint points : geoPoints) {
            double lat = Math.toRadians(points.getLatitude());
            double lon = Math.toRadians(points.getLongitude());

            double x1 = Math.cos(lat) * Math.cos(lon);
            double y1 = Math.cos(lat) * Math.sin(lon);
            double z1 = Math.sin(lat);

            sum_x += x1;
            sum_y += y1;
            sum_z += z1;
        }

    }

    /*
        Removes all markers, polylines, polygons, overlays, etc from the map.
     */
    public void clearMap() {
        googleMap.clear();
    }

    /*
        this method is how the main activity is passing values to this fragment.
        Depending on whats being passed in, will vary our queries;
     */
    public void communicate(String groupName, String groupId, ArrayList<String> member_ids) {

        /*
            if there are grouped markers and the user decided to chose another group
            then delete all the markers and query another set
         */
        if (grouped_markers != null && grouped_markers.size() > 0) {
            for(Marker marker : grouped_markers) {
                marker.remove();
            }
        }

        /*
            Initialize the parse query class - similar to SQL words if it helps.
         */
        ParseQuery<ParseUser> parseQuery = ParseQuery.getQuery(ParseUser.class);
        /*
            if your friends have signed up through our app and they are a part
            of your facebook group, then show them on the map.
         */
        parseQuery.whereEqualTo("group_ids", groupId);
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                ArrayList<ParseGeoPoint> geoPoints = new ArrayList<ParseGeoPoint>();

                /* if the size of the parseUsers is greater then one
                    then we have successfully tracked someone from the group
                 */
                for(ParseUser user : parseUsers) {
                    if(user.getUsername() != parse_user.getUsername() && !user.getBoolean("hidden")) {
                        // store geo points in order to find midpoint later
                        geoPoints.add(user.getParseGeoPoint("location"));
                        // add the person the the map as they are found
                        addPersonToMap(user.getParseGeoPoint("location"), user.getString("name"));
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