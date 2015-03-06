package com.ekeitho.shake.map;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ekeitho.shake.R;
import com.ekeitho.shake.map.CustomMapFragment;

/**
 * Created by ekeitho on 2/18/15.
 */
public class MapContainerFragment extends android.support.v4.app.Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_container, container, false);

        CustomMapFragment mapFragment = new CustomMapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.map_container, mapFragment, "MAP_FRAG").commit();
        Log.d("MyApp", "Test Fragment on create view.");

        return rootView;
    }


    public void communicate(String groupName) {
        System.out.println("Groupname: " + groupName);
    }

}
