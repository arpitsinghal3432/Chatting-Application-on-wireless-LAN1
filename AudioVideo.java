package AudioVideo;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class AudioVideo extends javax.swing.JFrame{
    
    // definitions
    private DaemonThread myThread = null;
    int SorC = 0;
    int count = 0;
    Point center;
    VideoCapture webSource = null;
    
    public String sendFrame = null;
    
    ServerVideoThread st;
    ClientVideoThread ct;
    
    
    Mat frame = new Mat();
    MatOfByte mem = new MatOfByte();
    
    //class daemon
    class DaemonThread implements Runnable {

        protected volatile boolean runnable = true;
        
        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    if (webSource.grab()) {
                        try {
                            webSource.retrieve(frame);
                            
                            Highgui.imencode(".bmp", frame, mem);
                            
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(ImageIO.read(new ByteArrayInputStream(mem.toArray())), "jpg", baos);
                            byte[] byte_array = baos.toByteArray();
                            BufferedImage bi=null;
                            //bi = (BufferedImage)(ImageIO.read(new ByteArrayInputStream(byte_array)));
                            if(SorC==1){
                                st.sender_video_function(byte_array);
                                bi = (BufferedImage)(ImageIO.read(new ByteArrayInputStream(st.reciever_video_function())));
                            }else{
                                ct.sender_video_function(byte_array);
                                bi = (BufferedImage)(ImageIO.read(new ByteArrayInputStream(ct.reciever_video_function())));
                            }
                            Graphics g = jPanel1.getGraphics();
                            if (g.drawImage(getFlippedImage(bi), 0, 0, jPanel1.getWidth(), jPanel1.getHeight(), 0, 0, bi.getWidth(), bi.getHeight(), null)) {
                            }
                            
                            if (runnable == false) {
                                System.out.println("Going to wait()");
                                this.wait();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(jPanel1, "The Other side has DISCONNECTED...Exiting Now");
                            System.exit(0);
                        }
                    }
                }
            }
        }
    }
    static{
        System.load("C:\\opencv\\build\\java\\x64\\opencv_java2413.dll");
        }
    public AudioVideo(int SorC) throws AWTException {
        this.SorC = SorC;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
                startEx();
            }
        });
    }
    
    public void startEx(){
        if(SorC==1){
            st = new ServerVideoThread();
            st.setType("Server");
        }else{
            ct = new ClientVideoThread();
            ct.setType("Client");
        }
        initComponents();
        webSource = new VideoCapture(0);
        myThread = new DaemonThread();
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Audio Video Chatting");
        
        this.setSize(660, 520);
        
        jPanel1.setSize(640, 480);
        jPanel1.setLocation(0, 0);
        this.add(jPanel1);
    }

    private javax.swing.JPanel jPanel1;
    public String ipAddress;
    
    public static BufferedImage getFlippedImage(BufferedImage bi) {
        BufferedImage flipped = new BufferedImage(
                bi.getWidth(),
                bi.getHeight(),
                bi.getType());
        AffineTransform tran = AffineTransform.getTranslateInstance(bi.getWidth(), 0);
        AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
        tran.concatenate(flip);

        Graphics2D g = flipped.createGraphics();
        g.setTransform(tran);
        g.drawImage(bi, 0, 0, null);
        g.dispose();

        return flipped;
    }
}