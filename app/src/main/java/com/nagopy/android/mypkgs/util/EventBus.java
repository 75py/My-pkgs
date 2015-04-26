package com.nagopy.android.mypkgs.util;

import com.squareup.otto.Bus;

public class EventBus {

    private static final Bus BUS = new Bus();

    private EventBus() {
    }

    public static Bus getDefault() {
        return BUS;
    }
}
