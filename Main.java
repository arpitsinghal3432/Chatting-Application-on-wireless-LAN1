package AudioVideo;


import java.awt.AWTException;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Main extends JFrame{
    private static JPanel homePanel;
    private static JButton video;
    private static JButton audio;
    public static String ipAddress;
    public static String selfname;
    public static String chatPersonname;
    
    private void init(){
        setTitle("Audio Video Chatting");
        setBounds(150, 150, 490, 250);
        
        homePanel = new JPanel();
        homePanel.setLayout(null);
        
        video = new JButton("Start Video Chat");
        video.setBounds(10,100,230,50);
        video.setFont(new Font("Courier", Font.BOLD, 20));
        homePanel.add(video);
        
        audio = new JButton("Start Audio Chat");
        audio.setBounds(250,100,230,50);
        audio.setFont(new Font("Courier", Font.BOLD, 20));
        homePanel.add(audio);
        
        add(homePanel);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public Main(){
        init();
        
        video.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    dispose();
                    new MainVideo();
                }
        });
        
        audio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    dispose();
                    new MainAudio();
            }
        });
  }
    public static void main(String[] args) {
        new Main();
    }
}
