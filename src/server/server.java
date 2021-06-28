/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Threading
 */
public class server extends Thread{
    public static final int PORT=1506;
    private ArrayList<serverHandler>clientServe=new ArrayList<>();
    private ServerSocket server;

    public List<serverHandler> getClientList(){
    return clientServe;
    }
    @Override
    public void run(){
        try {
            server = new ServerSocket(PORT);
             while(true){
             System.out.println("About Accept Connection");
             Socket client=server.accept();
             System.out.println("Accept Connection:"+client);
             serverHandler handler=new serverHandler(this,client);
             clientServe.add(handler);
             handler.start();
    }
        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    
}

    void removeClient(serverHandler client) {
     clientServe.remove(client);
    }

}
