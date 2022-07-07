package sdf.assessment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
      System.out.println();
      System.out.println("Run program in the format java sdf.assessment.MailMerge <CSV file> <template file>");
      System.out.println();
      System.exit(1);
    }

    attributes = getAttributes();

    readFile(templateFile);

    printMail();

  }


  public static void printMail(){
    for(String recipient: recipientsInfo){
      System.out.println();
      System.out.println("----------------------------------------");
      System.out.println();
      String[] recipientInfo = recipient.split(",");
      for(String templateLine: template){
        for(int i=0; i<attributes.length; i++){
          if(templateLine.contains("__"+attributes[i]+"__"))
            templateLine = templateLine.replace(("__"+attributes[i]+"__"), recipientInfo[i]);
        }
        System.out.printf(templateLine+"\n");
      }
    }
  }

  
  public static void readFile(String fileName){
    try {
      File file = new File("./files/"+fileName);
      Scanner scan = new Scanner(file);
      
      while(scan.hasNextLine()){
        if(fileName.contains("csv")){
          String nextLine = scan.nextLine();
          //csv file reads "\n" as "\\n"
          nextLine = nextLine.replace("\\n", "\n");
          recipientsInfo.add(nextLine);
        }else if(fileName.contains("txt"))
          template.add(scan.nextLine());
      }
      
      scan.close();
      
    } catch (FileNotFoundException e) {
      System.out.println();
      System.out.printf("File: %s not found\n", fileName);
      System.out.println();
      System.exit(1);
    } catch (IOException e){
      System.out.println();
      System.out.printf("Unable to read file %s\n", fileName);
      System.out.println();
      System.exit(1);
    }
  }


  public static String[] getAttributes(){
    readFile(csvFile);
    String[] attributeList = recipientsInfo.get(0).split(",");
    recipientsInfo.remove(0);
    return attributeList;
  }
  
}
