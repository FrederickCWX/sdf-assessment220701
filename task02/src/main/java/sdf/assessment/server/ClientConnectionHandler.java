package sdf.assessment.server;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ClientConnectionHandler implements Runnable{

  private Socket socket;
  private OutputStream os = null;
  private InputStream is = null;
  private ObjectOutputStream oos = null;
  private ObjectInputStream ois = null;

  private final String requestId =  UUID.randomUUID()
                                    .toString()
                                    .substring(0,8);

  private List<Integer> intList = new ArrayList<Integer>();
  private int listSize;
  private String listString;
  private boolean connectionSuccess = false;

  public ClientConnectionHandler(Socket socket){
    this.socket = socket;
  }

  @Override
  public void run(){
    System.out.println("Starting a client thread");
    try {
 
      os = socket.getOutputStream();
      oos = new ObjectOutputStream(os);
      is = socket.getInputStream();
      ois = new ObjectInputStream(is);

      getRandomIntList();
      String idInt = requestId + " " + listString;
      write(idInt);

      float average = calculateAverage();

      System.out.println();
      System.out.println("---------------- New Client connected ----------------");
      System.out.println();
      if(ois.readUTF().equals(requestId)){
        System.out.println("Request ID matched");
        System.out.println();
        String clientName = ois.readUTF();
        System.out.printf("Client name: %s\n", clientName);
        String clientEmail = ois.readUTF();
        System.out.printf("Client email: %s\n", clientEmail);
        System.out.println();
        if(ois.readFloat() == average){
          connectionSuccess = true;
          write(connectionSuccess);
          System.out.println("Client connection SUCCESS");
        }else{
          System.out.println("Client float incorrect");
        }
      }else{
        System.out.println("Request ID fail");
      }
      System.out.println();

      socket.close();
      
      System.out.println("Exiting the thread !");
      System.out.println("--------------- Client connection ended ---------------");
    } catch (Exception e) {

      e.printStackTrace();
      try {
        write(e.toString());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  public void getRandomIntList(){
    Random random = new Random();
    listSize = random.nextInt(10) + 20;
    for(int i=0; i<listSize; i++){
      intList.add(random.nextInt(90));
    }
    String list = intList.toString().replace("[", "").trim();
    list = list.replace("]", "");
    listString = list.replace(" ", "");
  }

  public float calculateAverage(){
    float average = 0f;
    int total = 0;
    for(int i=0; i<listSize; i++){
      total = total + intList.get(i);
    }
    average = (float)total/listSize;
    return average;
  }

  public void write(String output) throws IOException{
    oos.writeUTF(output);
    oos.flush();
  }

  public void write(float output) throws IOException{
    oos.writeFloat(output);
    oos.flush();
  }

  public void write(boolean output) throws IOException{
    oos.writeBoolean(output);
    oos.flush();
  }
  
}
