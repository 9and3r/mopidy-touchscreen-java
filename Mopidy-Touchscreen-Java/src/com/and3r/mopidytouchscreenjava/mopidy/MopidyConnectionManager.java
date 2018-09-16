package com.and3r.mopidytouchscreenjava.mopidy;

import com.and3r.mopidytouchscreenjava.data.TlTrack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.*;

public class MopidyConnectionManager extends WebSocketClient {

    private static final int TIMEOUT_SECONDS = 10;

    private ArrayList<MopidyEventListener> mopidyEventListeners;
    private ArrayList<MopidyConnectionListener> mopidyConnectionListeners;
    private Gson gson;
    private BlockingQueue<JsonObject> messagesToProccess;
    private boolean connected;
    private JsonObject lastResponse;

    public MopidyConnectionManager(URI uri){
        super(uri);
        gson = new Gson();
        messagesToProccess = new LinkedBlockingQueue();
        mopidyEventListeners = new ArrayList<>();
        mopidyConnectionListeners = new ArrayList<>();
    }

    public void addConnectionListener(MopidyConnectionListener mopidyConnectionListener){
        mopidyConnectionListeners.add(mopidyConnectionListener);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        connected = true;
        for (MopidyConnectionListener mopidyConnectionListener: mopidyConnectionListeners){
            mopidyConnectionListener.onConnected();
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void addMopidyEventListener(MopidyEventListener mopidyEventListener) {
        mopidyEventListeners.add(mopidyEventListener);
    }

    @Override
    public void onMessage(String s) {
        System.out.println(s);
        JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
        if (jsonObject.has("event")){
            String event = jsonObject.get("event").getAsString();
            switch (event){
                case "track_playback_started":
                    TlTrack tlTrack = gson.fromJson(jsonObject.get("tl_track"), TlTrack.class);
                    for (MopidyEventListener mopidyEventListener: mopidyEventListeners){
                        mopidyEventListener.trackPlaybackStarted(tlTrack);
                    }
                    break;
                case "playback_state_changed":
                    String oldState = jsonObject.get("old_state").getAsString();
                    String newState = jsonObject.get("new_state").getAsString();
                    for (MopidyEventListener mopidyEventListener: mopidyEventListeners){
                        mopidyEventListener.playbackStateChanged(oldState, newState);
                    }
                    break;
                case "track_playback_paused":
                    int timePosition = jsonObject.get("time_position").getAsInt();
                    tlTrack = gson.fromJson(jsonObject.get("tl_track"), TlTrack.class);
                    for (MopidyEventListener mopidyEventListener: mopidyEventListeners){
                        mopidyEventListener.trackPlaybackPaused(tlTrack, timePosition);
                    }
                    break;
                case "track_playback_resumed":
                    timePosition = jsonObject.get("time_position").getAsInt();
                    tlTrack = gson.fromJson(jsonObject.get("tl_track"), TlTrack.class);
                    for (MopidyEventListener mopidyEventListener: mopidyEventListeners){
                        mopidyEventListener.trackPlaybackResumed(tlTrack, timePosition);
                    }
                    break;
                case "track_playback_ended":
                    timePosition = jsonObject.get("time_position").getAsInt();
                    tlTrack = gson.fromJson(jsonObject.get("tl_track"), TlTrack.class);
                    for (MopidyEventListener mopidyEventListener: mopidyEventListeners){
                        mopidyEventListener.trackPlaybackEnded(tlTrack, timePosition);
                    }
                    break;
                case "volume_changed":
                    int volume = jsonObject.get("volume").getAsInt();
                    for (MopidyEventListener mopidyEventListener: mopidyEventListeners){
                        mopidyEventListener.volumeChanged(volume);
                    }
                    break;
                case "options_changed":
                    for (MopidyEventListener mopidyEventListener: mopidyEventListeners){
                        mopidyEventListener.optionsChanged();
                    }
                    break;
                case "tracklist_changed":
                    for (MopidyEventListener mopidyEventListener: mopidyEventListeners){
                        mopidyEventListener.tracklistChanged();
                    }
                    break;
                case "seeked":
                    timePosition = jsonObject.get("time_position").getAsInt();
                    for (MopidyEventListener mopidyEventListener: mopidyEventListeners){
                        mopidyEventListener.seeked(timePosition);
                    }
                    break;
                default:
                    System.out.println("--------------EVENT NOT IMPLEMENTED: " + event + "------------------");
                    System.out.println(s);
                    for (MopidyEventListener mopidyEventListener: mopidyEventListeners){
                        mopidyEventListener.unknownEvent(event, jsonObject);
                    }
                    break;
            }
        }else{
            if (jsonObject.has("id")){
                messagesToProccess.add(jsonObject);
            }
            System.out.println(s);
        }
    }

    public JsonObject sendCommand(MopidyRequest mopidyRequest) throws TimeoutException, InterruptedException {
        lastResponse = null;
        send(mopidyRequest.toJSONString());


        if (lastResponse != null){
            return lastResponse;
        }else{
            int currentID = 0;
            while(currentID < mopidyRequest.id){
                JsonObject jsonObject = messagesToProccess.poll(5, TimeUnit.SECONDS);
                if (jsonObject == null){
                    throw new TimeoutException();
                }else{
                    currentID = jsonObject.get("id").getAsInt();
                    if (currentID == mopidyRequest.id){
                        return jsonObject;
                    }
                }
            }
            throw new TimeoutException();

        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {

        connected = false;

        for (MopidyConnectionListener mopidyConnectionListener: mopidyConnectionListeners){
            mopidyConnectionListener.onDisconnected();
        }
    }

    @Override
    public void onError(Exception e) {
        for (MopidyConnectionListener mopidyConnectionListener: mopidyConnectionListeners){
            mopidyConnectionListener.onError();
        }
    }


}
