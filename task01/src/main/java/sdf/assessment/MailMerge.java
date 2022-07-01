package sdf.assessment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class MailMerge {

  private static LinkedList<String> recipientsInfo = new LinkedList<>();
  private static LinkedList<String> template = new LinkedList<>();
  private static String csvFile = "";
  private static String templateFile = "";
  private static String[] attributes;

  public static void main(String[] args) {
    

    if(args.length == 2 && args[0].contains(".csv")){
      csvFile = args[0];
      templateFile = args[1];
    }else{
      System.out.println("Run program in the format java sdf.assessment.Main <CSV file> <template file>");
      System.exit(1);
    }

    attributes = getAttributes();

    readFile(templateFile);

    for(String recipient: recipientsInfo){
      printMail(recipient);
    }
  }


  public static void printMail(String recipient){
    String[] recipientInfo = recipient.split(",");
    for(String templateLine: template){
      for(int i=0; i<attributes.length; i++){
        if(templateLine.contains("__"+attributes[i]+"__"))
          templateLine = templateLine.replace(("__"+attributes[i]+"__"), recipientInfo[i]);
      }
      System.out.println(templateLine);
    }
    System.out.println();
    System.out.println();
  }


  public static void readFile(String fileName){
    try {
      File file = new File("./files/"+fileName);
      Scanner scan = new Scanner(file);

      while(scan.hasNextLine()){
        if(fileName.contains("csv"))
          recipientsInfo.add(scan.nextLine());
        else if(fileName.contains("txt"))
          template.add(scan.nextLine());
      }
      scan.close();
      
    } catch (FileNotFoundException e) {
      System.out.printf("File: %s not found", fileName);
    }
  }


  public static String[] getAttributes(){
    readFile(csvFile);
    String[] attributeList = recipientsInfo.get(0).replace("\n", "~").split(",");
    recipientsInfo.remove(0);
    return attributeList;
  }
  
}
