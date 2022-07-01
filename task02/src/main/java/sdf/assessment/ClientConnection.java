package sdf.assessment;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class ClientConnection {

  private static float result = 0f;
  public static void main(String[] args) {

    Socket socket = null;
    OutputStream os = null;
    InputStream is = null;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;

    try {

      socket = new Socket("68.183.239.26", 80);

      os = socket.getOutputStream();
      oos = new ObjectOutputStream(os);
      is = socket.getInputStream();
      ois = new ObjectInputStream(is);

      try{

        String[] request = ois.readUTF().split(" ");
        String requestID = request[0];
        String intList = request[1];

        calculateAverage(intList);

        oos.writeUTF(requestID);
        oos.flush();
        oos.writeUTF("Chan Weixun Frederick");
        oos.flush();
        oos.writeUTF("fcweixun@gmail.com");
        oos.flush();
        oos.writeFloat(result);;
        oos.flush();

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

}
