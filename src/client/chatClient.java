/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Threading
 */
public class chatClient {

    private  int port;
    private  String server;
    private Socket client;
    private OutputStream serverOut;
    private InputStream serverIn;
    private BufferedReader bufferedin;
    private ArrayList<UserStatus>clientStatus=new ArrayList<>();
    private ArrayList<MessageListener>messageListener=new ArrayList<>();
    public chatClient(String server, int port) {
    this.port=port;
    this.server=server;
    }
    public static void main(String [] args) throws IOException{
     chatClient client =new chatClient("localhost",1506);
     client.addUserStatus(new UserStatus(){
         @Override
         public void online(String login) {
             System.out.println("Online:"+login);
         }

         @Override
         public void offline(String login) {
          System.out.println("Offline:"+login);
         }
     });
     client.addMessageListener(new MessageListener(){
         @Override
         public void OnMesasge(String From, String msgBody) {
             System.out.println("You got Message From :"+From+" ,Message:"+msgBody+"\n"); 
         }
    });
    if(!client.connect())
            System.out.println("Faild Connect!");
    else{
            System.out.println("Successfull Connect");
            if(client.login("mohamed","mohamed")){
                System.out.println("Successfull Login");
                client.msg("guest","Hello");
            }else{
                System.out.println("Faild Login");
            }
            //client.logoff();
}
    }
    public boolean connect() {
 try{
  this.client=new Socket(server,port);
  System.out.println(client.getLocalPort());
  this.serverOut=client.getOutputStream();
  this.serverIn=client.getInputStream();
  this.bufferedin=new BufferedReader(new InputStreamReader(serverIn));
 return true;
 }catch(IOException e){
 }
        return false;
    }
    public boolean login(String username,String password) throws IOException{
    String cmd="login "+username +" "+password+"\n";
    serverOut.write(cmd.getBytes());
    String response=bufferedin.readLine();
    System.out.println("Response Line:"+response);
    if(response.contains("ok login")){
        startMessage();
    return true;
    }else{
    return false;
    }
    }
    public void  addUserStatus(UserStatus status){
    clientStatus.add(status);
    }
    public void rmoveUserStatus(UserStatus status){
    clientStatus.remove(status);
    }

    private void startMessage() {
 Thread t =new Thread(){
 @Override
 public void run(){
     
         readMessageLoop();
 }
 
 };
 t.start();
    }
    public void readMessageLoop()  {
        
        String line;
        try{
        while((line = bufferedin.readLine())!=null){
            //System.out.println("Line is"+line);
          String []token=StringUtils.split(line);
            System.out.println(token[0]);
           if(token !=null &&token.length>0)
           {
            String cmd=token[0];
               System.out.println("cmd is"+cmd);
        if("online".equalsIgnoreCase(cmd)){
        handleOnline(token);  
        }else if("offline".equalsIgnoreCase(cmd)){
        handleOffline(token);
        }else if("msg".equalsIgnoreCase(cmd)){
            System.out.println("hi");
        String []tokens=StringUtils.split(line,null,3);
        handleMessage(tokens);
        }
           }
        }} catch (IOException ex) {
            try {
                client.close();
            } catch (IOException ex1) {
                Logger.getLogger(chatClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
     }

    private void handleOnline(String[] token) {
 String login=token[1];
 for(UserStatus client : clientStatus){
 client.online(login);
 }
    }

    private void handleOffline(String[] token) {
String login=token[1];
 for(UserStatus client : clientStatus){
 client.offline(login);
 }
    }

    public void logoff() throws IOException {
 String cmd = " logoff\n";
 serverOut.write(cmd.getBytes());
    }

    public void msg(String sendTo, String msgBody) throws IOException {
        String cmd = "msg " + sendTo + " " + msgBody + "\n";
        serverOut.write(cmd.getBytes());
    }
public void addMessageListener(MessageListener listener){
messageListener.add(listener);
}
public void removeMessageListener(MessageListener listener){
messageListener.remove(listener);
}

    private void handleMessage(String[] tokens) {
     String login =tokens[1];
     String msgBody=tokens[2];
     for(MessageListener listener: messageListener){
     listener.OnMesasge(login, msgBody);
     }
    }
}

