/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Threading
 */
public class OnlineList extends JPanel implements UserStatus{

    private final chatClient client;
    private JList<String>userList;
    private DefaultListModel<String>userListModel;
    public OnlineList(chatClient client) {
        this.client=client;
        this.client.addUserStatus(this);
        userListModel = new DefaultListModel<>(); 
        userList = new JList<>(userListModel); 
        setLayout(new BorderLayout());
        add(new JScrollPane(userList),BorderLayout.CENTER);
        //Click online user open chat pane
        userList.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            if(e.getClickCount()>1){
                String login=userList.getSelectedValue();
                MessagePane messageClient=new MessagePane(client,login);
                JFrame messagef=new JFrame("Message "+login);
                messagef.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                messagef.setSize(400,450);
                messagef.getContentPane().add(messageClient,BorderLayout.CENTER);
                messagef.setVisible(true);
            }
        }
        });
    }
    
    public static void main(String []args){
    chatClient client=new chatClient("localhost",1506);
    OnlineList OnlinePane=new OnlineList(client);
    JFrame frame=new JFrame("User List");
    frame.setSize(400, 600);
    frame.getContentPane().add(new JScrollPane(OnlinePane),BorderLayout.CENTER);
    frame.setVisible(true);
    if(client.connect()){
        try {
            client.login("guest", "guest");
        } catch (IOException ex) {
            Logger.getLogger(OnlineList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }

    @Override
    public void online(String login) {
userListModel.addElement(login);
    }

    @Override
    public void offline(String login) {
userListModel.removeElement(login);

    }
}
