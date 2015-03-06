package com.ekeitho.shake.login;

import com.ekeitho.shake.ShakeMainActivity;
import com.parse.ui.ParseLoginDispatchActivity;

/*
    This class allows us to show another activity, the main_fragment page,
    and when the user logs in, then the target class will be launched.
 */
public class ShakeLoginDispatachActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return ShakeMainActivity.class;
    }
}