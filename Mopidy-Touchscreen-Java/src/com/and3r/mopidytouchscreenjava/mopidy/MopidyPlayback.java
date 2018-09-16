package com.and3r.mopidytouchscreenjava.mopidy;

import java.util.concurrent.TimeoutException;

public class MopidyPlayback extends BaseMopidyProvider {

    public MopidyPlayback(MopidyConnectionManager mopidyConnectionManager) {
        super(mopidyConnectionManager);
    }

    public void play() throws NotConnectedException, TimeoutException, InterruptedException {
        checkConnection();
        mopidyConnectionManager.sendCommand(new MopidyRequest("core.playback.play"));
    }

    public void pause() throws NotConnectedException, TimeoutException, InterruptedException {
        checkConnection();
        mopidyConnectionManager.sendCommand(new MopidyRequest("core.playback.pause"));
    }

    public void seek(int timePosition) throws NotConnectedException, TimeoutException, InterruptedException {
        checkConnection();
        MopidyRequest mopidyRequest = new MopidyRequest("core.playback.seek");
        mopidyRequest.addParam("time_position", timePosition);
        mopidyConnectionManager.sendCommand(mopidyRequest);
    }
}
