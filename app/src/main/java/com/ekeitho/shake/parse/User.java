package com.ekeitho.shake.parse;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by ekeitho on 3/4/15.
 */

@ParseClassName("User")
public class User extends ParseObject {

    public void setName(String name) {
        put("name", name);
    }
    public void setLocation(ParseGeoPoint geoPoint) {
       put("location", geoPoint);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public String getName() {
        return getString("name");
    }
}


