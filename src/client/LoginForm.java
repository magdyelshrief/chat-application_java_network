/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author Threading
 */
public class LoginForm extends JFrame{
    JTextField UserLogin=new JTextField();
    JPasswordField UserPassword = new JPasswordField();
    JTextField Phone = new JTextField();
    JButton Login = new JButton("Login");
    private final chatClient client;
    LoginForm(){
        super("Login");
        
        this.client = new chatClient("localhost",1506);
        client.connect();
    
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
    p.add(UserLogin);
        p.add(UserPassword);
    p.add(Phone);
    p.add(Login);
    Login.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent ae) {
 login();
        }

       
    
    });
    
    getContentPane().add(p,BorderLayout.CENTER);
    pack();
    setVisible(true);

    }
    private void login() {
      String login = UserLogin.getText();
      String password = UserPassword.getText();
         System.out.println(password);
        try {
            if(client.login(login, password)){
                // call list online users
                
                 OnlineList OnlinePane=new OnlineList(client);
            JFrame frame=new JFrame("User List");
            frame.setSize(400, 600);
            frame.getContentPane().add(new JScrollPane(OnlinePane),BorderLayout.CENTER);
            frame.setVisible(true);
                setVisible(false);
            } else {
            JOptionPane.showMessageDialog(this,"Invalid Password or UserName Or phone");
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    public static void main(String []args){
    LoginForm frame = new LoginForm(); 
    }
     
}
