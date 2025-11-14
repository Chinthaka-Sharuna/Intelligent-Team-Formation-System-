import DataModels.Participant;
import java.io.*;
import java.util.*;

public class Main {
    public static ArrayList<Participant>  dataList=new ArrayList<>();
    public static  String[] heading=new String[8];
    public static String[] uniqueGames;

    public static void main(String[] args) {
        loadData();
        addParticipant();
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
        Set<String> uniqueSet = new HashSet<>(games.subList(1,games.size()));  //to remove the column value
        uniqueGames=uniqueSet.toArray(new String[0]);
        System.out.println(uniqueGames.toString());
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


    public static void addParticipant(){
        System.out.println("Adding participant to data list");
        String[] data=new String[8];
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter Id:- ");
        String id=sc.nextLine();
        System.out.print("Enter Name:- ");
        String name=capitalizerName(sc.nextLine());
        System.out.println(name);
        System.out.print("Enter Email:- ");
        String email=sc.nextLine();
        /*
        while (!isEmailValid(email)){
            System.out.println("Invalid Email");
            System.out.print("Renter the Email :- ");
            email=sc.nextLine();
        }
        */
        String preferredGame=getPreferredGame();
        System.out.println(preferredGame);
        System.out.print("Enter Skill Level:- ");
        int skillLevel=Integer.parseInt(sc.nextLine());
        System.out.print("Enter preferred Role:- ");
        String preferredRole=sc.nextLine();
        System.out.print("Enter Personality Score:- ");
        int personalityScore=Integer.parseInt(sc.nextLine());
        dataList.add(new Participant(id,name,email,preferredGame,skillLevel,preferredRole,personalityScore));

    }

    public static String capitalizerName(String name){
        String[] temp=name.split(" ");
        //System.out.println(Arrays.toString(temp));
        name="";
        for(int i=0;i<temp.length;i++){
            //System.out.println(temp[i].length());
            if (temp[i].isEmpty()){
                continue;
            }
            //System.out.println(temp[i]);
            temp[i]=temp[i].substring(0,1).toUpperCase() + temp[i].substring(1).toLowerCase();
        }
        //System.out.println(Arrays.toString(temp));
        return String.join(" ",temp);
    }

    public static boolean isEmailValid(String email){
        final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (email==null || email.isEmpty()){
            return false;
        }
        return email.matches(emailRegex);
    }

    public static String getPreferredGame(){
        Scanner sc=new Scanner(System.in);
        for(int i=1;i<=uniqueGames.length;i++){
            System.out.println("        "+i+". "+uniqueGames[i-1]);
        }
        System.out.print("Enter Preferred Game number :- ");
        int preferredGameNum=sc.nextInt();
        return uniqueGames[preferredGameNum-1];
    }

    public static void getPersonalityScore(){
    }
}