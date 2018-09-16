package com.and3r.mopidytouchscreenjava.mopidy;

import java.net.URI;

public class MopidyAPI {

    private MopidyConnectionManager mopidyConnectionManager;

    public MopidyCore core;


    public MopidyAPI(URI uri){
        this.mopidyConnectionManager = new MopidyConnectionManager(uri);
        core = new MopidyCore(mopidyConnectionManager);
    }

    public void connect(){
        this.mopidyConnectionManager.connect();
    }

    public void addConnectionListener(MopidyConnectionListener mopidyConnectionListener){
        this.mopidyConnectionManager.addConnectionListener(mopidyConnectionListener);
    }

    public void addMopidyEventListener(MopidyEventListener mopidyEventListener){
        this.mopidyConnectionManager.addMopidyEventListener(mopidyEventListener);
    }

}
