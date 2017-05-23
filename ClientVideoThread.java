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
public class ClientVideoThread {
    Socket client_end_socket = null;
    String type = null;
    public static String ipAddress = "127.0.0.1";
    public ClientVideoThread(){
        try{
            client_end_socket = new Socket(ipAddress, 7447);
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
            OutputStream out  = client_end_socket.getOutputStream();
            DataOutputStream data_out = new DataOutputStream(out);
            
            data_out.flush();
            
            data_out.writeInt(len);
            if(len>0){
                //data_out.writeUTF(type);
                data_out.write(byte_array, 0, len);
            }
        }catch(Exception e){
            System.out.println("ERROR");
        }
    }
    
    public byte[] reciever_video_function(){
        
        try {
            InputStream in = client_end_socket.getInputStream();
            DataInputStream data_in = new DataInputStream(in);

            //if(data_in.readUTF()=="Server"){
                int len = data_in.readInt();
                byte[] data = new byte[len];
                if (len > 0) {
                    data_in.readFully(data);
                }
                return data;
            //}
        } catch (IOException ex) {
            Logger.getLogger(ClientVideoThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private AudioFormat getAudioFormat(){
        float sampleRate = 16000.0F;
        int sampleSizeBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
    }
    
    public void reciever_audio_function(){
        try{
            
            AudioFormat format = getAudioFormat();
            
            DataInputStream data_in = new DataInputStream(client_end_socket.getInputStream());
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class,format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
            
            speaker.drain();
            
            speaker.open(format);
            speaker.start();
            
            byte[] data = new byte[1024];
            
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais,format,data.length);
            int bytesRead = 0;
            if((bytesRead = ais.read(data)) != -1){
                speaker.write(data,0,bytesRead);
                System.out.println("Reading");
            }
            ais.close();
            bais.close();
            
            speaker.close();
            
        }catch(IOException ioex){
            System.out.println("IO EXCEPTION");
        }catch(LineUnavailableException luex){
            System.out.println("LINE UNAVAILABLE EXCEPTION");
        }
    }
}
