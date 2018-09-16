package com.and3r.mopidytouchscreenjava.mopidy;

import com.and3r.mopidytouchscreenjava.data.TlTrack;
import com.google.gson.JsonElement;

public abstract class BaseMopidyEventListener implements MopidyEventListener {

    @Override
    public void volumeChanged(int volume) {

    }

    @Override
    public void optionsChanged() {

    }

    @Override
    public void trackPlaybackStarted(TlTrack tlTrack) {

    }

    @Override
    public void trackPlaybackPaused(TlTrack tlTrack, int timePosition) {

    }

    @Override
    public void trackPlaybackResumed(TlTrack tlTrack, int timePosition) {

    }

    @Override
    public void trackPlaybackEnded(TlTrack tlTrack, int timePosition) {

    }

    @Override
    public void playbackStateChanged(String oldState, String newState) {

    }

    @Override
    public void tracklistChanged() {

    }

    @Override
    public void seeked(int timePosition) {

    }

    @Override
    public void unknownEvent(String eventname, JsonElement event) {

    }
}
