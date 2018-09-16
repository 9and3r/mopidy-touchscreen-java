package com.and3r.mopidytouchscreenjava.mopidy;

import java.util.concurrent.TimeoutException;

public class MopidyCore extends BaseMopidyProvider{

    public MopidyLibraryController library;
    public MopidyPlayback playback;

    public MopidyCore(MopidyConnectionManager mopidyConnectionManager) {
        super(mopidyConnectionManager);
        library = new MopidyLibraryController(mopidyConnectionManager);
        playback = new MopidyPlayback(mopidyConnectionManager);
    }



}
