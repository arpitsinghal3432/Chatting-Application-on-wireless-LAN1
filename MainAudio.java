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

public class MainAudio extends JFrame{
    private static JPanel homePanel;
    private static JButton server;
    private static JButton client;
    public static String ipAddress;
    public static String selfname;
    public static String chatPersonname;
    
    private void init(){
        setTitle("Audio Video Chatting");
        setBounds(150, 150, 490, 250);
        
        homePanel = new JPanel();
        homePanel.setLayout(null);
        
        server = new JButton("Start Client One");
        server.setBounds(10,100,230,50);
        server.setFont(new Font("Courier", Font.BOLD, 20));
        homePanel.add(server);
        
        client = new JButton("Start Client Two");
        client.setBounds(250,100,230,50);
        client.setFont(new Font("Courier", Font.BOLD, 20));
        homePanel.add(client);
        
        add(homePanel);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public MainAudio(){
        init();
        
        server.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    ipAddress = JOptionPane.showInputDialog(homePanel, "Enter Ip Address of Client Two");
                    Thread server = new Thread(){
                    public void run(){
                        new ServerTAThread().runVOIP();
                    }
                    };
                    
                    server.start();
                    ClientOAThread.ipAddressAudio = ipAddress;
                    new ClientOAThread();
                }
        });
        
        client.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    ipAddress = JOptionPane.showInputDialog(homePanel, "Enter Ip Address of Client One");
                    Thread client = new Thread(){
                    public void run(){
                        ClientTAThread.ipAddressAudio = ipAddress;
                        new ClientTAThread();
                    }
                    };
                    
                    Thread server = new Thread(){
                    public void run(){
                       new ServerOAThread().runVOIP();
                    }
                    };
                    client.start();
                    server.start();
            }
        });
  }
    
}
