/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AudioVideo;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author ripusudan
 */
public class ServerVideoThread extends Thread{
    
    ServerSocket socket = null;
    Socket client_socket = null;
    String type = null;
    public ServerVideoThread(){
        try{
            socket = new ServerSocket(7447);
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
    
    public void sender_video_function(byte[] byte_array){
        
        int len = byte_array.length;
        try{
            OutputStream out  = client_socket.getOutputStream();
            DataOutputStream data_out = new DataOutputStream(out);
            
            data_out.flush();
            
            data_out.writeInt(len);
            if(len>0){
                data_out.write(byte_array, 0, len);
            }
        }catch(Exception e){
            System.out.println("ERROR");
        }
    }
    
    public byte[] reciever_video_function(){
        try {
            InputStream in = client_socket.getInputStream();
            DataInputStream data_in = new DataInputStream(in);
            
            //if(data_in.readUTF()=="Client"){
                int len = data_in.readInt();
                byte[] data = new byte[len];
                if (len > 0) {
                    data_in.readFully(data);
                }
                return data;
           // }
        } catch (IOException ex) {
            Logger.getLogger(ClientVideoThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
    }
    
    public void sender_audio_function(){
        try{
            
            AudioFormat format = getAudioFormat();
            
            DataOutputStream data_out = new DataOutputStream(client_socket.getOutputStream());
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class,format);
            TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(micInfo);
            mic.drain();
            mic.open(format);
            
            System.out.println(mic.getBufferSize());
            byte tmpBuff[] = new byte[mic.getBufferSize()/5];
            int count = tmpBuff.length;
            if (count > 0){
                data_out.write(tmpBuff, 0, count);
                System.out.println("Writing");
            }
            
            
            mic.close();
            
        }catch(IOException ioex){
            System.out.println("IO EXCEPTION");
        }catch(LineUnavailableException luex){
            System.out.println("LINE UNAVAILABLE EXCEPTION");
        }
    }
}
