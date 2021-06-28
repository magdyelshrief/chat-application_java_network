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
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author Threading
 */
public class MessagePane extends JPanel implements MessageListener{

    private final String login;
    private final chatClient client;
    private DefaultListModel List = new DefaultListModel<>();
    private JList<String>messageList = new JList<>(List);
    private JTextField message = new JTextField(); 
    MessagePane(chatClient client, String login) {
   this.client=client;
   this.login=login;
   client.addMessageListener(this);
   setLayout(new BorderLayout());
   add(new JScrollPane(messageList),BorderLayout.CENTER);
   add(message,BorderLayout.SOUTH);
   message.addActionListener(new ActionListener(){
       @Override
       public void actionPerformed(ActionEvent ae) {
           String msgContent=message.getText();
           try {
               client.msg(login, msgContent);
               List.addElement("You :"+msgContent);
               message.setText("");
           } catch (IOException ex) {
               Logger.getLogger(MessagePane.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
   
   });
    }

    @Override
    public void OnMesasge(String From, String msgBody) {
        if(From.equalsIgnoreCase(login)){
     String line = From +": "+msgBody;
     List.addElement(line);
        }
    }
    
}
