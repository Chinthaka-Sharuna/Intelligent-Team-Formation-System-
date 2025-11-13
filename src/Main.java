import DataModels.Participant;
import java.io.*;
import java.util.*;

public class Main {
    public static ArrayList<Participant>  dataList=new ArrayList<>();
    public static  String[] heading=new String[8];

    public static void main(String[] args) {
        loadData();
        saveData();
    }


    //Read the csv file
    public static void loadData(){
        String filePath= "data/participants_sample.csv";
        ArrayList<String[]> temp=new ArrayList<>();
        ArrayList<String> games=new ArrayList<>();




        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while((line= reader.readLine())!=null){
                //System.out.println(line);
                String[] data=line.split(",");
                games.add(data[3]);
                temp.add(data);
            }
        }catch (FileNotFoundException e){
            System.out.println("Could not locate file: " + e.getMessage());
        }catch (IOException e){
            System.out.println("Could not read file: " + e.getMessage());
        }
        heading = temp.get(0);
        temp.remove(0);
        for(String[] data:temp){
            dataList.add(new Participant(data));
        }
        System.out.println("File read successfully");
        final Set<String> uniqueGames=new HashSet<>(games);
    }


    public static void saveData(){
        String filePath= "data/participants_sample.csv";
        ArrayList<String[]> temp=new ArrayList<>();
        try (BufferedWriter writer=new BufferedWriter(new FileWriter(filePath))){
            writer.write(String.join(",",heading));
            writer.newLine();
            for(Participant data:dataList){
                writer.write(String.join(",",data.toArray()));
                writer.newLine();
            }
        }catch (FileNotFoundException e){
            System.out.println("file not found");

        }catch (IOException e) {
            System.out.println("IO Error");
        }

    }
}