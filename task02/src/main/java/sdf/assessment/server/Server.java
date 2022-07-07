package sdf.assessment.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

  private int port = 80;

  public void start() throws IOException{

    ExecutorService threadPool = Executors.newFixedThreadPool(4);

    ServerSocket server = new ServerSocket(port);

    while(true){
      System.out.println("Waiting for client connection");
      Socket socket = server.accept();
      System.out.println("Connected ...");
      ClientConnectionHandler thr = new ClientConnectionHandler(socket);
      threadPool.submit(thr);
      System.out.println("Submitted to threadpool");
    }

  }


  
}
