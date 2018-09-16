package com.and3r.mopidytouchscreenjava;

import com.and3r.mopidytouchscreenjava.mopidy.MopidyAPI;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {



    public static void main(String[] args) throws URISyntaxException, InterruptedException {

        // https://github.com/TooTallNate/Java-WebSocket

        MopidyAPI mopidyApiManager = new MopidyAPI(new URI("ws://localhost:6680/mopidy/ws"));
        mopidyApiManager.connect();


        JFrame jFrame = new JFrame("");
        jFrame.setSize(600, 400);

        jFrame.setLayout(new BorderLayout());

        jFrame.add(new NowPlayingPanel(mopidyApiManager), BorderLayout.CENTER);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setVisible(true);



    }



}
