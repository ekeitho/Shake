package com.ekeitho.shake;


import com.facebook.Session;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ekeitho on 2/18/15.
 */
public interface ShakeCommunicator {
    public void saveSession(Session session);
    public String[] getGroupNames();
}
