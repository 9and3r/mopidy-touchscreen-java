package com.and3r.mopidytouchscreenjava.mopidy;

public interface MopidyConnectionListener {

    void onConnected();

    void onDisconnected();

    void onError();

}
