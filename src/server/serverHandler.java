/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server; 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
/**
 *
 * @author Threading
 */
public class serverHandler extends Thread{
    private final Socket clientSocket;
    private final server server;
    private HashSet<String>  topicSet=new HashSet<>();
    String login=null;
    private OutputStream out;
    public serverHandler(server server,Socket clientSocket){
    this.clientSocket=clientSocket;
    this.server=server;
    }
    @Override
    public void run(){
        try {
            handleClientSocket();
        } catch (IOException ex) {
            Logger.getLogger(serverHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(serverHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    // handle client socket  act as middleware
    public  void handleClientSocket() throws IOException, InterruptedException{
        this.out=clientSocket.getOutputStream();
        InputStream input=clientSocket.getInputStream();
        BufferedReader buffer=new BufferedReader(new InputStreamReader(input));
        String Line;
        while((Line=buffer.readLine())!=null){
            String []token=StringUtils.split(Line);
           if(token !=null &&token.length>0)
           {
            String cmd=token[0];
            System.out.println(token.length);
           
            
        if("logoff".equalsIgnoreCase(cmd)||"quite".equalsIgnoreCase(cmd)){
        handlelogoff();
        }
            
        else if("login".equalsIgnoreCase(cmd)){
        handleLogin(out,token);
        }else if("leave".equalsIgnoreCase(cmd)){
            handleLeave(token);
        }else if("msg".equalsIgnoreCase(cmd)){
            String []tokens=StringUtils.split(Line,null,3);
           handleMessage(tokens);
        }else if("join".equalsIgnoreCase(cmd)){
        handleJoin(token);
        }
        else{
        String msg="Unknown : "+Line+"\n";
        out.write(msg.getBytes());
        }
        }
        }
             clientSocket.close();
         }
String getLogin(){
return login;
}    // capture data piped and filter it to get command then handle login
    private void handleLogin(OutputStream out, String[] token) throws IOException {
    if(token.length==3)
    {
    String login=token[1];
    String password=token[2];
    if(login.equals("som3a")&&password.equals("som3a") || (login.equals("mohamed")&&password.equals("mohamed")))
    {
    String msg="ok login\n";
    out.write(msg.getBytes());
    this.login=login;
    System.out.println("Successfull Login:"+this.login);
    List <serverHandler>clientServe=server.getClientList();
    // send current user for all other online users
    for(serverHandler client : clientServe){
        if(client.getLogin()!=null){
            if(!login.equals(client.getLogin())){
    String msg2="online "+client.getLogin() + "\n";
    send(msg2);
        }
        }
    }
    //send status
    String OnlineMsg="Online "+login+"\n";
    for(serverHandler client:clientServe){
    if(!login.equals(client.getLogin()))
    client.send(OnlineMsg);
    }
    }else{
    String msg="error login!\n";
    out.write(msg.getBytes());
    System.err.println("Login failed for " + login);
    }
    }
    
    }
     //send message , include two module
    private void send(String OnlineMsg) throws IOException {
        System.out.println(OnlineMsg);
        if(login!=null)
    out.write(OnlineMsg.getBytes());
        
    }
     //log off 
    private void handlelogoff() throws IOException {
        server.removeClient(this);
        String OnlineMsg="Offline "+login+"\n";
    List <serverHandler>clientServe=server.getClientList();
    // send all other online
    for(serverHandler client : clientServe){
    
    client.send(OnlineMsg);
    }
clientSocket.close();
    }//Groupe Module , ensure is in Groupe
    public boolean isMemberOfTopic(String topic){
    return topicSet.contains(topic);
    }//  Groupe Module,Create Topic 
    private void handleJoin(String []tokens){
    if(tokens.length>1)
    {
        String topic=tokens[1];
        topicSet.add(topic);
    }
    }
    //formate message
    private void handleMessage(String []tokens) throws IOException{
    String sendTo = tokens[1];
    String body = tokens[2];
    //Gropue module if topic will start with #
    boolean isTopic=sendTo.charAt(0)=='#';
    List <serverHandler>clientServe=server.getClientList();
    for(serverHandler client : clientServe){
    if(isTopic){
    if(client.isMemberOfTopic(sendTo)){
    String outMsg="Message:" +sendTo+":"+login +" "+ body +"\n";
    client.send(outMsg);
    }
    }
    //single module  , Direct message for online user
    else{
    if(sendTo.equals(client.getLogin())){
    String outMsg = "msg " + login + " " + body + "\n";
    client.send(outMsg);
    }
    }
    }
    }
   // groupe module  , remove topic admin only 
    private void handleLeave(String[] token) {
if(token.length>1){
String topic=token[1];
topicSet.remove(topic);
} 
    }
}
