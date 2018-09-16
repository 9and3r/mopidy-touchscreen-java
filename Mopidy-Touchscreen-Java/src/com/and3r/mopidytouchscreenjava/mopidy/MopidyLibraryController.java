package com.and3r.mopidytouchscreenjava.mopidy;

import com.and3r.mopidytouchscreenjava.data.ImageResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class MopidyLibraryController extends BaseMopidyProvider{

    public MopidyLibraryController(MopidyConnectionManager mopidyConnectionManager) {
        super(mopidyConnectionManager);
    }

    public HashMap<String, ArrayList<ImageResult>> get_images(ArrayList<String> uris) throws TimeoutException, InterruptedException {
        MopidyRequest mopidyRequest = new MopidyRequest("core.library.get_images");
        mopidyRequest.addParam("uris", uris);
        JsonObject response = mopidyConnectionManager.sendCommand(mopidyRequest);
        HashMap<String, ArrayList<ImageResult>> imageResults = new HashMap<>();
        JsonObject result = response.getAsJsonObject("result");
        for (String uri: uris){
            if (result.has(uri)){
                JsonArray currentResultURI = result.getAsJsonArray(uri);
                ArrayList<ImageResult> currentUriImageResults = new ArrayList<>();
                for (JsonElement current: currentResultURI){
                    JsonObject currentResult = current.getAsJsonObject();
                    ImageResult imageResult = new ImageResult(currentResult.get("width").getAsInt(), currentResult.get("height").getAsInt(), currentResult.get("uri").getAsString());
                    currentUriImageResults.add(imageResult);
                }
                imageResults.put(uri, currentUriImageResults);
            }

        }
        return imageResults;
    }

    public ArrayList<ImageResult> get_images(String uri) throws TimeoutException, InterruptedException {
        ArrayList<String> uris = new ArrayList<>();
        uris.add(uri);
        HashMap<String, ArrayList<ImageResult>> imageResultHashMap = get_images(uris);
        return imageResultHashMap.getOrDefault(uri, null);
    }
}
