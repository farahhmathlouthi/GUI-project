package asma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Clientadmin extends JFrame  {
    public JFrame frame = new JFrame("WELCOME");
    public JPanel panel1 = new JPanel();
    public Clientadmin(){
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create Buttons
        JButton clientButton = new JButton("Client");
        clientButton.setBackground(new Color(6, 104, 136, 255));
        clientButton.setForeground(Color.WHITE);
        clientButton.setPreferredSize(new Dimension(250, 100));
        JButton adminButton = new JButton("Admin");
        adminButton.setBackground(new Color(6, 104, 136, 255));
        adminButton.setForeground(Color.WHITE);
        adminButton.setPreferredSize(new Dimension(250 , 100));
        // Add buttons to the panel
        panel1.add(clientButton);
        panel1.add(adminButton);
        panel1.setBackground(new Color(173, 216, 230));
        frame.getContentPane().add(panel1);
        frame.getContentPane().setBackground(new Color(173, 216, 230));
        frame.add(panel1);
        frame.setVisible(true);

        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientFrame.main(new String[0]);
            }
        });

        // Add ActionListener to the Admin button
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { AdminFrame.main(new String[0]); }
        });

    }
    public static void main(String[] args) {
        Clientadmin myFormat = new Clientadmin();
    }
}
