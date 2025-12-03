import Models.CSV;
import Models.Participant;
import Models.Role;
import Models.TeamBuilder;
import java.io.*;
import java.util.*;

public class Main {
    public final static Scanner sc=new Scanner(System.in);
    public static List<Participant>  participants=new ArrayList<>();
    public static  String[] heading=new String[8];
    public static String[] uniqueGames;
    public static String[] uniquePreferredRole;
    public static String[] uniquePersonalityType={"Leader","Balanced","Thinker"};
    public static HashMap<String, List<Participant>> participantsByPersonalityType=new HashMap<>();
    public static CSV participantsCSV=new CSV("data/participants_sample.csv");

    public static void main(String[] args) throws IOException {
        participants=participantsCSV.load();
        System.out.println("Loaded "+participants.size()+" participants");
        uniqueGames=getUniqueGames().toArray(new String[0]);
        uniquePreferredRole=getUniquePreferredRole().toArray(new String[0]);
        stateUpMenu();
    }

    private static void stateUpMenu(){
        System.out.println("Welcome to Intelligent Team Formation System");
        while(true){
            System.out.println("------ Main Menu ------");
            System.out.println("1. Add Participant");
            System.out.println("2. View Participants");
            System.out.println("3. Organizer Logging");
            System.out.println("4. Quit");
            System.out.print("Enter your choice :- ");
            try{
                switch (Integer.parseInt(sc.nextLine())){
                    case 1:
                        addParticipant();
                        break;
                    case 2:
                        viewParticipant();
                        break;
                    case 3:
                        //if(organizerLogging()){
                        if(true){
                            organizerMenu();
                        }
                        break;
                    case 4:
                        System.out.println("-----Quitting-------");
                        System.out.println("Bye");
                        System.exit(0);
                    default:
                        System.out.println("Invalid input");
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid input");
                System.out.println("Input must be an integer");
            }catch (Exception e){
                System.out.println("Invalid input");
            }

        }

    }

    private static void organizerMenu(){
        System.out.println("------ Organizer Menu ------");
        System.out.println("1. Team Formation");
        System.out.println("2. Quit");
        System.out.print("Enter your choice :- ");
        switch (Integer.parseInt(sc.nextLine())){
            case 1:
                System.out.println("Welcome to the Team Formation System");
                System.out.print("Enter group size :- ");
                int membersCount=Integer.parseInt(sc.nextLine());
                float globalAVG=calculatGlobaleAVG();
                groupParticipantByPersonalityType();
                TeamBuilder teamBuilder=new TeamBuilder(membersCount,globalAVG,participantsByPersonalityType,uniqueGames);
                break;
            case 2:
                System.out.println("-----Quitting-------");
                System.out.println("Bye");
                System.exit(0);
            default:
                System.out.println("Invalid Input");
        }
    }

    private static boolean organizerLogging(){
        Map<String,String> credentials =new HashMap<>();
        credentials.put("admin","admin");
        String username;
        String password;
        System.out.println("------ Organizer Logging ------");
        System.out.println("If you want to exit type 'exit'");
        System.out.print("Please enter your User Name :- ");
        while(true){
            try{

                username=sc.nextLine();
                if(username.equals("exit")){
                    return false;
                }
                System.out.print("Please enter your Password :- ");
                password=sc.nextLine();
                if(credentials.containsKey(username)){
                    if(credentials.get(username).equals(credentials.get(password))){
                        System.out.println("You have successfully logged in");
                        return true;
                    }else {
                        System.out.println("Wrong Password");
                    }
                }else{
                    System.out.println("Invalid Username");
                }
                System.out.print("Please Re-enter your User Name :- ");

            }catch (InputMismatchException e){
                System.out.print("Please Re-enter your User Name :- ");
            }catch (NullPointerException e){
                System.out.println("Username or Password is null.");
                System.out.print("Please Re-enter your User Name :- ");
            }
        }

    }

    public static void addParticipant(){
        System.out.println("Adding participant to data list");
        String id=getNewId();
        System.out.println("Your ID is "+id);
        System.out.print("Enter Name:- ");
        String name=capitalizerName(sc.nextLine());
        if(isDuplication(name)){
            System.out.println("Name is duplicated");
            return;
        }
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
        Role preferredRole=Role.fromString(getPreferredRole());
        int personalityScore=getPersonalityScore();
        try{
            participants.add(new Participant(id,name,email,preferredGame,skillLevel,preferredRole,personalityScore));
            System.out.println("Participation created Successfully");
            participantsCSV.writeParticipant(participants);
        }catch (IllegalArgumentException e){
            System.out.println("Invalid Input");
        }

    }

    private static void viewParticipant(){
        for(Participant participant:participants){
            System.out.println(participant.toStringALl());
        }
    }

    public static String getNewId(){
        return "P"+String.format("%03d",participants.size()+1);
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
                    System.out.println("Invalid input (input must be between 1 and " + uniqueGames.length + ")");
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
            System.out.print("Enter a Valid Skill Level :- ");
        }

        return skilllevel;
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

    public static HashSet<String> getUniqueGames(){
        HashSet<String> uniqueGames=new HashSet<>();
        for(Participant participant:participants){
            if(!uniqueGames.contains(participant.getPreferredGame())){
                uniqueGames.add(participant.getPreferredGame());
            }
        }return uniqueGames;
    }

    public static HashSet<String> getUniquePreferredRole(){
        HashSet<String> uniquePreferredRole=new HashSet<>();
        for(Participant participant:participants){
            if(!uniquePreferredRole.contains(participant.getPreferredRole())){
                uniquePreferredRole.add(participant.getPreferredRole().toString());
            }
        }return uniquePreferredRole;
    }

    private static void groupParticipantByPersonalityType() {
        for (int i = 0; i < uniquePersonalityType.length; i++) {
            //System.out.println(uniquePersonalityType[i]);
            ArrayList<Participant> temp = new ArrayList<>();
            for (int j = 0; j < participants.size(); j++) {
                if (participants.get(j).getPersonalityType().equals(uniquePersonalityType[i])) {
                    temp.add(participants.get(j));
                }
            }
            /*for (int j = 0; j < temp.size(); j++) {
                System.out.println(Arrays.toString(temp.get(j).toArray()));
            }*/
            participantsByPersonalityType.put(uniquePersonalityType[i], temp);
        }
        System.out.println("Grouped By Games");
    }

    private static float calculatGlobaleAVG(){
        double globalAvg=0;
        for(Participant participant:participants){
            globalAvg+=participant.getSkillLevel();
        }
        globalAvg=globalAvg/participants.size();
        return (float) globalAvg;
    }

    private static boolean isDuplication(String name){
        for(Participant participant:participants){
            if(participant.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

}




















































    /*
    public static String[] uniquePreferredRole;
    public static String[] uniquePersonalityType={"Leader","Balanced","Thinker"};
    public static HashMap<String,ArrayList<Participant>> groupedParticipantsByPersonalityType=new HashMap<>();
    //                    game name    Models.PersonalityType
    public static HashMap<String,HashMap<String,ArrayList<Participant>>> formattedMap=new HashMap<>();
    public static Scanner sc=new Scanner(System.in);

    public static void main(String[] args) {

        loadData("data/participants_sample.csv");
        //System.out.print("Models.Team count is "+getTeamCount());
        //addParticipant();
        //saveData();
        groupParticipantByPersonalityType();
        //groupParticipantByGame();
        sortPlayersBySkill(groupedParticipantsByPersonalityType);
        int memberCount=getPlayerCount();
        TeamBuilder tb=new TeamBuilder(memberCount,groupedParticipantsByPersonalityType);

    }





    public static void groupParticipantByPersonalityType() {
        for (int i = 0; i < uniquePersonalityType.length; i++) {
            System.out.println(uniquePersonalityType[i]);
            ArrayList<Participant> temp = new ArrayList<>();
            for (int j = 0; j < participants.size(); j++) {
                if (participants.get(j).getPersonalityType().equals(uniquePersonalityType[i])) {
                    temp.add(participants.get(j));
                }
            }
            for (int j = 0; j < temp.size(); j++) {
                System.out.println(Arrays.toString(temp.get(j).toArray()));
            }
            groupedParticipantsByPersonalityType.put(uniquePersonalityType[i], temp);
        }
        System.out.println("Grouped By Games");
    }

    public static Integer  getPlayerCount(){
        int teamCount = 0;
        System.out.print("How many Players want for each team :- ");
        while(true){
            try {
                teamCount = Integer.parseInt(sc.nextLine());
                return  teamCount;
            }catch (NumberFormatException e){
                System.out.println("Invalid input (input must be a number)");
                System.out.println("Please enter a valid team number :- ");
            }catch (InputMismatchException e){
                System.out.println("Input must be a number");
                System.out.println("Please enter a valid team number :- ");
            }
        }

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


    /*
    public static void groupParticipantByGame(){
        for(int i=0;i<uniqueGames.length;i++){
            System.out.println(uniqueGames[i]);
            ArrayList<Models.Participant> temp=new ArrayList<>();
            for(int j=0;j<participants.size();j++){
                if(participants.get(j).getPreferredGame().equals(uniqueGames[i])){
                    temp.add(participants.get(j));
                }
            }
            for(int j=0;j<temp.size();j++){
                System.out.println(Arrays.toString(temp.get(j).toArray()));
            }
            groupedParticipantsByGame.put(uniqueGames[i],temp );
        }
        System.out.println("Grouped By Games");
        for(Map.Entry<String,ArrayList<Models.Participant>> entry:groupedParticipantsByGame.entrySet()){
            formattedMap.put(entry.getKey(),sortPlayersBySkill(groupParticipantByPersonalityType(entry.getValue())));
        }

    }

    public static HashMap<String,ArrayList<Models.Participant>> groupParticipantByPersonalityType(ArrayList<Models.Participant> participantsArray){
        HashMap<String,ArrayList<Models.Participant>> groupedParticipantsByPersonalityType=new HashMap<>();
        for(int i=0;i<uniquePersonalityType.length;i++){
            System.out.println(uniquePersonalityType[i]);
            ArrayList<Models.Participant> temp=new ArrayList<>();
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
            for (String key : groupedParticipants.keySet()) {
                System.out.println(key);
                System.out.println(Arrays.toString(groupedParticipants.get(key).toArray()));
            }
        }
        System.out.println("Grouped By Personality Type");
        return groupedParticipantsByPersonalityType;
    }

    public static HashMap<String,ArrayList<Models.Participant>> sortPlayersBySkill(HashMap<String,ArrayList<Models.Participant>> playerMap ) {
        for (Map.Entry<String, ArrayList<Models.Participant>> entry : playerMap.entrySet()) {
            ArrayList<Models.Participant> players = entry.getValue();
            players.sort(Comparator.comparingInt(Models.Participant::getSkillLevel).reversed());

            System.out.println("Sorted players for " + entry.getKey() + " by skill level.");
        }
        for(Map.Entry<String, ArrayList<Models.Participant>> entry : playerMap.entrySet()){
            for(Models.Participant p : entry.getValue()){
                System.out.println(Arrays.toString(p.toArray()));
            }
        }
        return playerMap;
    }*/