package com.ekeitho.shake;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ekeitho on 2/18/15.
 */
public class CustomMapFragment extends com.google.android.gms.maps.SupportMapFragment {

    private final LatLng HAMBURG = new LatLng(53.558, 9.927);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GoogleMap googleMap = getMap();
        googleMap.addMarker(new MarkerOptions().position(HAMBURG).title("Hamburg"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

}