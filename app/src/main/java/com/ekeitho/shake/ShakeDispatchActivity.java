package com.ekeitho.shake;

import com.parse.ui.ParseLoginDispatchActivity;

/*
    This class allows us to show another activity, the login page,
    and when the user logs in, then the target class will be launched.
 */
public class ShakeDispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return ShakeMainActivity.class;
    }
}