import java.io.*;
import java.util.*;

public class Main {
    public static ArrayList<Participant>  participants=new ArrayList<>();
    public static  String[] heading=new String[8];
    public static String[] uniqueGames;
    public static String[] uniquePreferredRole;
    public static String[] uniquePersonalityType={"Leader","Balanced","Thinker"};
    public static HashMap<String,ArrayList<Participant>> groupedParticipantsByGame=new HashMap<>();
    //                    game name    PersonalityType
    public static HashMap<String,HashMap<String,ArrayList<Participant>>> formattedMap=new HashMap<>();
    public static Scanner sc=new Scanner(System.in);

    public static void main(String[] args) {

        loadData("data/participants_sample.csv");
        //System.out.print("Team count is "+getTeamCount());
        //addParticipant();
        //saveData();
        //-groupParticipantByPersonalityType();
        groupParticipantByGame();
        //sortPlayersBySkill();
        TeamBuilder tb=new TeamBuilder();
        tb.teamCreation();

    }


    //Read the csv file

    public static void loadData(String filePath){
        ArrayList<String[]> temp=new ArrayList<>();
        ArrayList<String> games=new ArrayList<>();
        ArrayList<String> roles=new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while((line= reader.readLine())!=null){
                //System.out.println(line);
                String[] data=line.split(",");
                games.add(data[3]);
                roles.add(data[5]);
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
            participants.add(new Participant(data));
        }
        System.out.println("File read successfully");
        Set<String> uniqueSet = new HashSet<>(games.subList(1,games.size()));  //to remove the column value (concatenation)
        Set<String> uniquePreferredRoleSet = new HashSet<>(roles.subList(1,roles.size()));  //to remove the column value (concatenation)
        uniqueGames=uniqueSet.toArray(new String[0]);
        //System.out.println(Arrays.toString(uniqueGames));   //to get unique games values
        uniquePreferredRole=uniquePreferredRoleSet.toArray(new String[0]); //to get unique roles values
    }

    public static void saveData(){
        String filePath= "data/participants_sample.csv";
        ArrayList<String[]> temp=new ArrayList<>();
        try (BufferedWriter writer=new BufferedWriter(new FileWriter(filePath))){
            writer.write(String.join(",",heading));
            writer.newLine();
            for(Participant data:participants){
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
        String id=getNewId();
        System.out.println("Your ID is "+id);
        System.out.print("Enter Name:- ");
        String name=capitalizerName(sc.nextLine());
        System.out.println(name);
        System.out.print("Enter Email:- ");
        String email=sc.nextLine();
        while (!isEmailValid(email)){
            System.out.println("Invalid Email");
            System.out.print("Renter the Email :- ");
            email=sc.nextLine();
        }
        String preferredGame=getPreferredGame();
        System.out.println(preferredGame);
        int skillLevel=getSkillLevel();
        String preferredRole=getPreferredRole();
        int personalityScore=getPersonalityScore();
        try{
            participants.add(new Participant(id,name,email,preferredGame,skillLevel,preferredRole,personalityScore));
        }catch (IllegalArgumentException e){
            System.out.println("Invalid Input");
        }

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

    public static String getPreferredGame() {
        System.out.println("Enter a Game between among these");
        for (int i = 1; i <= uniqueGames.length; i++) {
            System.out.println("        " + i + ". " + uniqueGames[i - 1]);
        }
        System.out.print("Preferred Game number :- ");
        int preferredGameNum;
        while (true) {
            try {
                preferredGameNum = Integer.parseInt(sc.nextLine());
                if (!(preferredGameNum >= 1 && preferredGameNum <= uniqueGames.length)) {
                    System.out.println("Invalid input (input must be between 1 and "+uniqueGames.length+")");
                    System.out.print("Enter a Valid Game Number :- ");
                    continue;
                }
                break;
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Input must be a number");
            }
            System.out.print("Enter a Valid Game Number :- ");
        }
        return uniqueGames[preferredGameNum - 1];
    }

    public static int getPersonalityScore(){
        System.out.println("------ Personal Score ------");
        System.out.println("Please answer the following questions on a scale of 1 to 5");
        System.out.print("1. Are you a team player? (1-5):- ");
        float totalScore=validatePersonalScore();
        System.out.print("2. How confident are you in making decisions? (1-5):- ");
        totalScore+=validatePersonalScore();
        System.out.print("3. Do you enjoy planning strategies? (1-5) :- ");
        totalScore+=validatePersonalScore();
        System.out.print("4. How patient are you with new players? (1-5):- ");
        totalScore+=validatePersonalScore();
        System.out.print("5. Do you take charge in difficult situations? (1-5):- ");
        totalScore+=validatePersonalScore();
        totalScore*=4;
        return (int) totalScore;
    }

    public static float validatePersonalScore(){
        while(true){
            try {
                double value = Integer.parseInt(sc.nextLine());
                if (value >= 1 && value <= 5) {
                    return (float) value;
                }
                System.out.println("Personal Score is out of range.It must be between 1 and 5.");
                System.out.println("Please enter a valid personal score :- ");
            }catch (NumberFormatException e){
                System.out.println("Please enter a valid personal score (Score must be a number)");
                System.out.print("Please enter a valid personal score :- ");
            }
        }
    }

    public static String getPreferredRole(){
        System.out.println("------ Preferred Role ------");
        System.out.println("Enter a Preferred Role between among these");
        for (int i = 1; i <= uniquePreferredRole.length; i++) {
            System.out.println("        " + i + ". " + uniquePreferredRole[i - 1]);
        }
        System.out.print("Preferred Role number :- ");
        int preferredRoleNum;
        while (true) {
            try {
                preferredRoleNum =Integer.parseInt(sc.nextLine());  //i use insdad of sc.nextInt becuse it wont stop when give a letter
                if (!(preferredRoleNum >= 1 && preferredRoleNum <= uniquePreferredRole.length)) {
                    System.out.println("Invalid input (input must be between 1 and "+uniquePreferredRole.length+")");
                    System.out.print("Enter a Valid Role Number :- ");
                    continue;
                }
                break;
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Input must be a number");
            }
            System.out.print("Enter a Valid Role Number :- ");
        }
        return uniquePreferredRole[preferredRoleNum - 1];
    }

    public static String getNewId(){
        return "P"+String.format("%03d",participants.size()+1);
    }

    public static int getSkillLevel(){
        System.out.print("Enter Skill Level:- ");
        int skilllevel;
        while (true) {
            try{
                skilllevel= Integer.parseInt(sc.nextLine());
                if (!(skilllevel >= 1 && skilllevel <= 10)) {
                    System.out.println("Invalid input (input must be between 1 and 10)");
                }
                break;
            }catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Input must be a number");
            }
            System.out.print("Enter a Valid  Input :- ");
        }

        return skilllevel;
    }


    public static void groupParticipantByGame(){
        for(int i=0;i<uniqueGames.length;i++){
            System.out.println(uniqueGames[i]);
            ArrayList<Participant> temp=new ArrayList<>();
            for(int j=0;j<participants.size();j++){
                //System.out.println(Arrays.toString(participants.get(i).toArray()));
                if(participants.get(j).getPreferredGame().equals(uniqueGames[i])){
                    temp.add(participants.get(j));
                }
            }
            for(int j=0;j<temp.size();j++){
                System.out.println(Arrays.toString(temp.get(j).toArray()));
            }
            groupedParticipantsByGame.put(uniqueGames[i],temp );
            /*for (String key : groupedParticipants.keySet()) {
                System.out.println(key);
                System.out.println(Arrays.toString(groupedParticipants.get(key).toArray()));
            }*/
        }
        System.out.println("Grouped By Games");
        for(Map.Entry<String,ArrayList<Participant>> entry:groupedParticipantsByGame.entrySet()){
            formattedMap.put(entry.getKey(),sortPlayersBySkill(groupParticipantByPersonalityType(entry.getValue())));
        }

    }

    public static HashMap<String,ArrayList<Participant>> groupParticipantByPersonalityType(ArrayList<Participant> participantsArray){
        HashMap<String,ArrayList<Participant>> groupedParticipantsByPersonalityType=new HashMap<>();
        for(int i=0;i<uniquePersonalityType.length;i++){
            System.out.println(uniquePersonalityType[i]);
            ArrayList<Participant> temp=new ArrayList<>();
            for(int j=0;j<participantsArray.size();j++){
                //System.out.println(Arrays.toString(participants.get(i).toArray()));
                if(participantsArray.get(j).getPersonalityType().equals(uniquePersonalityType[i])){
                    temp.add(participantsArray.get(j));
                }
            }
            for(int j=0;j<temp.size();j++){
                System.out.println(Arrays.toString(temp.get(j).toArray()));
            }
            groupedParticipantsByPersonalityType.put(uniquePersonalityType[i],temp );
            /*for (String key : groupedParticipants.keySet()) {
                System.out.println(key);
                System.out.println(Arrays.toString(groupedParticipants.get(key).toArray()));
            }*/
        }
        System.out.println("Grouped By Personality Type");
        return groupedParticipantsByPersonalityType;
    }

    public static HashMap<String,ArrayList<Participant>> sortPlayersBySkill(HashMap<String,ArrayList<Participant>> playerMap ) {
        for (Map.Entry<String, ArrayList<Participant>> entry : playerMap.entrySet()) {
            ArrayList<Participant> players = entry.getValue();
            players.sort(Comparator.comparingInt(Participant::getSkillLevel).reversed());

            System.out.println("Sorted players for " + entry.getKey() + " by skill level.");
        }
        for(Map.Entry<String, ArrayList<Participant>> entry : playerMap.entrySet()){
            for(Participant p : entry.getValue()){
                System.out.println(Arrays.toString(p.toArray()));
            }
        }
        return playerMap;
    }
}