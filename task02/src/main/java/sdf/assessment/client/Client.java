package sdf.assessment.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class Client {

  private static float result = 0f;

  private static Socket socket = null;
  private static OutputStream os = null;
  private static InputStream is = null;
  private static ObjectOutputStream oos = null;
  private static ObjectInputStream ois = null;
  public static void main(String[] args) {

    try {

      //socket = new Socket("68.183.239.26", 80);
      socket = new Socket("localhost", 80);

      os = socket.getOutputStream();
      oos = new ObjectOutputStream(os);
      is = socket.getInputStream();
      ois = new ObjectInputStream(is);

      try{

        String[] request = ois.readUTF().split(" ");
        String requestID = request[0];
        String intList = request[1];

        //System.out.println(requestID);
        //System.out.println(intList);

        calculateAverage(intList);

        //System.out.println(result);

        write(requestID);
        write("Chan Weixun Frederick");
        write("fcweixun@gmail.com");
        write(result);

        boolean responseStatus = ois.readBoolean();

        if(responseStatus == true){
          System.out.println("SUCCESS");
          System.exit(1);
        }else if(responseStatus == false){
          System.out.println("FAILED");
          System.out.println("From Server:");
          System.out.println(ois.readUTF());
          
        }
        os.close();
        is.close();
        socket.close();

      }catch(Exception e){
        e.printStackTrace();
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void calculateAverage(String intList){
    String[] listOfInt = intList.split(",");
    LinkedList<Integer> integerList = new LinkedList<>();

    int total = 0;

    for(String stringInt: listOfInt){
      try{
        int number = Integer.parseInt(stringInt);
        total = total + number;
        integerList.add(number);
      }catch(NumberFormatException e){
        e.printStackTrace();
      }
    }
    result = (float)total/(integerList.size());
  }

  public static void write(String output) throws IOException{
    oos.writeUTF(output);
    oos.flush();
  }

  public static void write(float output) throws IOException{
    oos.writeFloat(output);
    oos.flush();
  }

}
