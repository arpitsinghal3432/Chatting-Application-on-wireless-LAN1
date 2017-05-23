/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AudioVideo;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author ripusudan
 */
public class ClientAudioThread {
    public static Socket client_end_socket = null;
    String type = null;
    public static String ipAddress = "192.168.43.163";
    public ClientAudioThread(){
        try{
            client_end_socket = new Socket(ipAddress, 54321);
        }catch(IOException e){
            System.out.println("Error "+e);
        }
    }
    public void setType(String type){
        this.type = type;
    }
    
    private static AudioFormat getAudioFormat(){
        float sampleRate = 16000.0F;
        int sampleSizeBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
    }
    
    public static void reciever_audio_function(){
        try{
            
            AudioFormat format = getAudioFormat();
            while(true){
            DataInputStream data_in = new DataInputStream(client_end_socket.getInputStream());
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class,format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
            
            speaker.open(format);
            speaker.start();
            
            byte[] data = new byte[10000];
            
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais,format,data.length);
            
            int bytesRead = 0;
            if((bytesRead = ais.read(data)) != -1){
                speaker.write(data,0,bytesRead);
                System.out.println("Reading");
                continue;
            }
            ais.close();
            bais.close();
            speaker.drain();
            speaker.close();
            }
            
            
        }catch(IOException ioex){
            System.out.println("IO EXCEPTION");
        }catch(LineUnavailableException luex){
            System.out.println("LINE UNAVAILABLE EXCEPTION");
        }
    }
    
    public static void main(String[] args) {
        new ClientAudioThread();
        reciever_audio_function();
    }
}
