/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AudioVideo;
import java.io.*;
import java.net.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author ripusudan
 */
public class ServerAudioThread extends Thread{
    
    ServerSocket socket = null;
    static Socket client_socket = null;
    String type = null;
    public ServerAudioThread(){
        try{
            socket = new ServerSocket(54321);
            System.out.println("Waiting for Connection");
            client_socket = socket.accept();
            System.out.println("Connection Made");
        }catch(IOException e){
            System.out.println("Error "+e);
        }
    }
    public void setType(String type){
        this.type = type;
    }
    
    private static AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
    }
    
    public static void sender_audio_function(){
        try{
            
            AudioFormat format = getAudioFormat();
            
            DataOutputStream data_out = new DataOutputStream(client_socket.getOutputStream());
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class,format);
            TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(micInfo);
            mic.open(format);
            
            while(true){
            byte tmpBuff[] = new byte[mic.getBufferSize()/5];
            int count = tmpBuff.length;
            if (count > 0){
                data_out.write(tmpBuff, 0, count);
                System.out.println("Writing");
                continue;
            }
            
            mic.drain();
            mic.close();}
            
        }catch(IOException ioex){
            System.out.println("IO EXCEPTION");
        }catch(LineUnavailableException luex){
            System.out.println("LINE UNAVAILABLE EXCEPTION");
        }
    }
    
    public static void main(String[] args) {
        new ServerAudioThread();
        sender_audio_function();
    }
}
