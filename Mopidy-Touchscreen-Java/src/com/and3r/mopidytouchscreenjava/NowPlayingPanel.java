package com.and3r.mopidytouchscreenjava;

import com.and3r.mopidytouchscreenjava.data.ImageResult;
import com.and3r.mopidytouchscreenjava.data.TlTrack;
import com.and3r.mopidytouchscreenjava.mopidy.BaseMopidyEventListener;
import com.and3r.mopidytouchscreenjava.mopidy.MopidyAPI;
import com.and3r.mopidytouchscreenjava.mopidy.NotConnectedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NowPlayingPanel extends JPanelBackground {

    private static final int PROGRESSBAR_UPDATE_MS = 1000;
    private static String currentStatus;

    private TlTrack currentTlTrack;

    public NowPlayingPanel(MopidyAPI mopidyAPI){

        setBackground(Color.BLACK);

        setLayout(new BorderLayout());

        JProgressBar sliderTime = new JProgressBar();
        sliderTime.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int value = (int) ((double)e.getX() / (double)sliderTime.getWidth() * (double) sliderTime.getMaximum());
                try {
                    mopidyAPI.core.playback.seek(value);
                } catch (NotConnectedException e1) {
                    e1.printStackTrace();
                } catch (TimeoutException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        sliderTime.setOpaque(false);
        sliderTime.setStringPainted(true);
        sliderTime.setString("");

        add(sliderTime, BorderLayout.SOUTH);




        JPanel trackInfoPanel = new JPanel();
        trackInfoPanel.setOpaque(false);
        trackInfoPanel.setLayout(new BoxLayout(trackInfoPanel, BoxLayout.Y_AXIS));

        JLabel jLabelTitle = new JLabel();
        jLabelTitle.setForeground(Color.WHITE);
        jLabelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackInfoPanel.add(jLabelTitle);

        JLabel jLabelAlbum = new JLabel();
        jLabelAlbum.setForeground(Color.WHITE);
        jLabelAlbum.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackInfoPanel.add(jLabelAlbum);

        JLabel jLabelArtists = new JLabel();
        jLabelArtists.setForeground(Color.WHITE);
        jLabelArtists.setAlignmentX(Component.CENTER_ALIGNMENT);
        trackInfoPanel.add(jLabelArtists);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridLayout());
        centerPanel.add(new JLabel("Prueba"));

        centerPanel.add(trackInfoPanel);
        add(centerPanel, BorderLayout.CENTER);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (currentStatus != null && currentStatus.equals("playing")){
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            if (currentTlTrack != null && currentTlTrack.track != null && currentTlTrack.track.length > 0){
                                int value = sliderTime.getValue() + PROGRESSBAR_UPDATE_MS;

                                sliderTime.setValue(value);

                                String valueString = String.format("%02d:%02d / %02d:%02d",
                                        TimeUnit.MILLISECONDS.toMinutes(value),
                                        TimeUnit.MILLISECONDS.toSeconds(value) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(value)),
                                        TimeUnit.MILLISECONDS.toMinutes(currentTlTrack.track.length),
                                        TimeUnit.MILLISECONDS.toSeconds(currentTlTrack.track.length) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentTlTrack.track.length))
                                );

                                sliderTime.setString(valueString);
                                sliderTime.setVisible(true);
                                sliderTime.invalidate();
                                sliderTime.repaint();
                            }else{
                                sliderTime.setVisible(false);
                            }
                        }
                    });

                }
            }
        }, PROGRESSBAR_UPDATE_MS, PROGRESSBAR_UPDATE_MS, TimeUnit.MILLISECONDS);


        mopidyAPI.addMopidyEventListener(new BaseMopidyEventListener() {
            @Override
            public void trackPlaybackStarted(TlTrack tlTrack) {
                currentTlTrack = tlTrack;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<ImageResult> result = mopidyAPI.core.library.get_images(tlTrack.track.album.artists[0].uri);
                            int a = 0;
                            //BufferedImage img = ImageIO.read(new URL(result.get(0).uri));
                            setBackground(result.get(0).uri);
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            e.printStackTrace();
                        }
                        int a = 0;
                    }
                }).start();
                jLabelTitle.setText(tlTrack.track.name);
                jLabelAlbum.setText(tlTrack.track.getAlbumName());
                jLabelArtists.setText(tlTrack.track.getArtistsString());
                sliderTime.setMinimum(0);
                sliderTime.setMaximum(tlTrack.track.length);
                sliderTime.setValue(0);
            }

            @Override
            public void trackPlaybackPaused(TlTrack tlTrack, int timePosition) {
                sliderTime.setValue(timePosition);
            }

            @Override
            public void trackPlaybackResumed(TlTrack tlTrack, int timePosition) {
                sliderTime.setValue(timePosition);
            }

            @Override
            public void trackPlaybackEnded(TlTrack tlTrack, int timePosition) {
                sliderTime.setValue(timePosition);
            }

            @Override
            public void playbackStateChanged(String oldState, String newState) {
                currentStatus = newState;
            }

            @Override
            public void seeked(int timePosition) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        sliderTime.setValue(timePosition);
                        sliderTime.invalidate();
                        sliderTime.repaint();
                    }
                });
            }
        });
    }
}
