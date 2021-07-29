package chatapplication;


//for beginning this is one to one.
/*
future : group chat
socket programming inside java networking




*/

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class Server implements ActionListener {//works under awt.event.* library 
    // globally declare for using these outside of the Server() constructor
    JPanel p1;
    JTextField t1;// text writing area
    JButton b1;// send button
    static JPanel a1;
    static JFrame f1 = new JFrame();

    static Box vertical = Box.createVerticalBox();

    static ServerSocket skt;
    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;

    Boolean typing;

    Server() {
        f1.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        p1 = new JPanel();
        p1.setLayout(null);
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        f1.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("chatapplication/icons/3.png"));//exit icon ,, fetching icon from location to showing in the frame 
        Image i2 = i1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);// we can't show this getImage direct in frame that why
        ImageIcon i3 = new ImageIcon(i2);// this line converting imageicon
        JLabel l1 = new JLabel(i3);// then ready for showing in the frame
        l1.setBounds(5, 17, 30, 30);//(x coor,y coor, width, length)
        p1.add(l1);// for showing in the frame

        l1.addMouseListener(new MouseAdapter() {//performing exit icon for close that chat frame,, works under actionlistener
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("chatapplication/icons/1.png"));//server icon
        Image i5 = i4.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel l2 = new JLabel(i6);
        l2.setBounds(40, 5, 60, 60);
        p1.add(l2);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("chatapplication/icons/video.png"));// video call icon
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel l5 = new JLabel(i9);
        l5.setBounds(290, 20, 30, 30);
        p1.add(l5);

        ImageIcon i11 = new ImageIcon(ClassLoader.getSystemResource("chatapplication/icons/phone.png"));// audio call icon
        Image i12 = i11.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon i13 = new ImageIcon(i12);
        JLabel l6 = new JLabel(i13);
        l6.setBounds(350, 20, 35, 30);
        p1.add(l6);

        ImageIcon i14 = new ImageIcon(ClassLoader.getSystemResource("chatapplication/icons/3icon.png"));//option icon
        Image i15 = i14.getImage().getScaledInstance(13, 25, Image.SCALE_DEFAULT);
        ImageIcon i16 = new ImageIcon(i15);
        JLabel l7 = new JLabel(i16);
        l7.setBounds(410, 20, 13, 25);
        p1.add(l7);

        JLabel l3 = new JLabel("Habib");// showing user name in the frame 
        l3.setFont(new Font("SAN_SERIF", Font.BOLD, 18));// user name customization
        l3.setForeground(Color.WHITE);// text color
        l3.setBounds(110, 15, 100, 18);
        p1.add(l3);

        JLabel l4 = new JLabel("Active Now");
        l4.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        l4.setForeground(Color.WHITE);
        l4.setBounds(110, 35, 100, 20);
        p1.add(l4);

        Timer t = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!typing) {
                    l4.setText("Active Now");
                }
            }
        });

        t.setInitialDelay(2000);

        a1 = new JPanel();
        a1.setBounds(5, 75, 440, 570);
        a1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f1.add(a1);

        t1 = new JTextField();// Texting area
        t1.setBounds(5, 655, 310, 40);
        t1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f1.add(t1);

        t1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                l4.setText("typing...");

                t.stop();

                typing = true;
            }

            public void keyReleased(KeyEvent ke) {
                typing = false;

                if (!t.isRunning()) {
                    t.start();
                }
            }
        });

        b1 = new JButton("Send");
        b1.setBounds(320, 655, 123, 40);
        b1.setBorder(new RoundedBorder(40));
        b1.setBackground(new Color(7, 94, 84));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        b1.addActionListener(this);
        f1.add(b1);

        f1.getContentPane().setBackground(Color.WHITE);
        f1.setLayout(null);
        f1.setSize(450, 700);
        f1.setLocation(400, 200);
        f1.setUndecorated(true);
        f1.setVisible(true);

    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String out = t1.getText();
            sendTextToFile(out);//
            JPanel p2 = formatLabel(out);

            a1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            a1.add(vertical, BorderLayout.PAGE_START);

            //a1.add(p2);
            dout.writeUTF(out);
            t1.setText("");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static class RoundedBorder implements Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    public void sendTextToFile(String message) throws FileNotFoundException{ //for storing these messages in local computer text file
        try(FileWriter f = new FileWriter("chat.txt",true);// for continue storing all chatting conversation.
                PrintWriter p = new PrintWriter(new BufferedWriter(f));){
            p.println("Server: "+message);
        }catch(Exception e){
            e.printStackTrace();
        }
            
    }
    public static JPanel formatLabel(String out) {
        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));

        JLabel l1 = new JLabel("<html><p style = \"width : 150px\">" + out + "</p></html>");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        l1.setBackground(new Color(37, 211, 102));
        l1.setOpaque(true);
        l1.setBorder(new EmptyBorder(15, 15, 15, 50));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel l2 = new JLabel();
        l2.setText(sdf.format(cal.getTime()));

        p3.add(l1);
        p3.add(l2);
        return p3;
    }

    public static void main(String[] args) {
        new Server().f1.setVisible(true); //when class object created the constructor class will be called

        String msginput = "";
        try {
            skt = new ServerSocket(6001);
            while (true) {
                s = skt.accept();
                din = new DataInputStream(s.getInputStream());//coming data from client
                dout = new DataOutputStream(s.getOutputStream());// sending data for client

                while (true) {// for continuously sending messages.
                    msginput = din.readUTF();
                    JPanel p2 = formatLabel(msginput);

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(p2, BorderLayout.LINE_START);
                    vertical.add(left);
                    f1.validate();
                }

            }

        } catch (Exception e) {
        }
    }
}
