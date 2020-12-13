package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    private int level = -1;
    private long id = 0;

    public Login(Ex2 a) {
        this.setSize(300, 300);
        this.setTitle("Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel idLabel = new JLabel("Enter Your ID:");
        JLabel leveLabel = new JLabel("Enter Level Number:");
        JTextField idText = new JTextField(9);
        JTextField levelText = new JTextField(2);
        JButton start = new JButton("Start");
        panel.add(idLabel);
        panel.add(idText);
        panel.add(leveLabel);
        panel.add(levelText);
        panel.add(start);
        this.getContentPane().add(BorderLayout.CENTER, panel);
        JFrame temp = this;
        this.setVisible(true);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = Integer.parseInt(idText.getText());
                level = Integer.parseInt(levelText.getText());
                if (String.valueOf(id).length() == 9 && id != 000000000 && level >= 0 && level <= 23) {
                    a.setid(id);
                    a.setNum(level);
                    Thread client = new Thread(a);
                    client.start();
                    temp.setVisible(false);
                } else
                    JOptionPane.showMessageDialog(temp, "Invalid ID or Level Number");
            }
        });
    }
}