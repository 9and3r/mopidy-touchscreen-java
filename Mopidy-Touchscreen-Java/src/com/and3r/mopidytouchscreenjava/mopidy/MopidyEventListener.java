package com.and3r.mopidytouchscreenjava.mopidy;

import com.and3r.mopidytouchscreenjava.data.TlTrack;
import com.google.gson.JsonElement;

public interface MopidyEventListener {

    void volumeChanged(int volume);

    void optionsChanged();

    void trackPlaybackStarted(TlTrack tlTrack);

    void trackPlaybackPaused(TlTrack tlTrack, int timePosition);

    void trackPlaybackResumed(TlTrack tlTrack, int timePosition);

    void trackPlaybackEnded(TlTrack tlTrack, int timePosition);

    void playbackStateChanged(String oldState, String newState);

    void tracklistChanged();

    void seeked(int timePosition);

    void unknownEvent(String eventname, JsonElement event);


}
