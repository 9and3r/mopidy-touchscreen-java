package com.and3r.mopidytouchscreenjava.data;

public class Track {

    public String uri;
    public String name;
    public Album album;
    public Artist[] artists;
    public int length;

    public String getAlbumName(){
        return album.name;
    }

    public String getArtistsString(){
        StringBuilder stringBuilder = new StringBuilder();
        if (artists != null){
            for (int i=0; i<artists.length; i++){
                stringBuilder.append(artists[i].name);
                if (i < artists.length - 1){
                    stringBuilder.append(", ");
                }
            }
        }
        return stringBuilder.toString();
    }

}
