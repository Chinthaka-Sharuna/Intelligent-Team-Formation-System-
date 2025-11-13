import java.io.*;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //Read the csv file
        String filePath= "data/participants_sample.csv";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            List<String[]>  dataList=new ArrayList<>();
            while((line= reader.readLine())!=null){
                System.out.println(line);
                String[] data=line.split(",");
                dataList.add(data);
                System.out.println(data);
            }
            System.out.println("File read successfully");
        }catch (FileNotFoundException e){
            System.out.println("Could not locate file: " + e.getMessage());
        }catch (IOException e){
            System.out.println("Could not read file: " + e.getMessage());
        }
    }
}