package com.and3r.mopidytouchscreenjava.mopidy;

public class BaseMopidyProvider {

    protected MopidyConnectionManager mopidyConnectionManager;

    public BaseMopidyProvider(MopidyConnectionManager mopidyConnectionManager){
        this.mopidyConnectionManager = mopidyConnectionManager;
    }

    public void checkConnection() throws NotConnectedException {
        if (!this.mopidyConnectionManager.isConnected()){
            throw new NotConnectedException();
        }
    }
}
