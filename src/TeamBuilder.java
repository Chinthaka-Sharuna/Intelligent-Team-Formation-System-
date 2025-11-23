import java.util.*;


public class TeamBuilder {
    private final static HashMap<String,Integer> minimumMemberCount=new HashMap<>();
    Scanner sc = Main.sc;
    final static HashMap<String,ArrayList<Participant>> groupedParticipantsByGame = Main.groupedParticipantsByGame;
    final static String[] uniqueGames=Main.uniqueGames;
    private ArrayList <HashMap<String,Integer>> GroupsDetails=new ArrayList<>();
    //                    game name    PersonalityType
    public static HashMap<String,HashMap<String,ArrayList<Participant>>> formattedMap=Main.formattedMap;
    private HashMap<String, Integer> totalCounts = new HashMap<>();
    private HashMap<String, ArrayList<Participant>> selectedPlayers = new HashMap<>();
    ArrayList<Participant> selectedLeaders=new ArrayList<>();;
    ArrayList<Participant> selectedThinkers=new ArrayList<>();
    ArrayList<Participant> selectedBalanced=new ArrayList<>();

    static {
        minimumMemberCount.put("Chess",1);
        minimumMemberCount.put("CS:GO",5);
        minimumMemberCount.put("DOTA 2",5);
        minimumMemberCount.put("Basketball",5);
        minimumMemberCount.put("Valorant",5);
        minimumMemberCount.put("FIFA",2);
    }

    public void teamCreation(){
        String choise="";
        do{
            GroupsDetails.add(createSubTeam());
            getTotalPlayerCountPerGame();
            System.out.print("Do you want to create another new team?(yes/no) :- ");
            try{
                choise=sc.nextLine();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }while(choise.equalsIgnoreCase("yes"));
        printTotalPlayerCountPerGame();
        allocatePlayers();
        System.out.println("over");

    }

    private void isValidateTeamCount(String gameName, int memberCount){
        minimumMemberCount.get(gameName);
        if (memberCount<0 || memberCount%minimumMemberCount.get(gameName)!=0){
            throw new IllegalArgumentException("Player count must be multiple of "+minimumMemberCount.get(gameName));
        }
    }

    private HashMap<String,Integer> createSubTeam(){
        System.out.println("If you dont want add player for particular game enter 0");
        System.out.println("If you want to Terminate Team Create type 'exits'");
        System.out.println("If you want to Re-enter All values type 'reset' ");
        HashMap<String,Integer> temp=new HashMap<>();
        OUTTER_LOOP:while(true){
            for(int i=0;i<uniqueGames.length;i++){
                while (true){
                    System.out.println("How many player want for "+uniqueGames[i]);
                    System.out.println("Player count must be multiple of "+minimumMemberCount.get(uniqueGames[i]));
                    System.out.print("      Enter player count for "+uniqueGames[i]+" :- ");
                    try{
                        String input=sc.nextLine();
                        if (input.equalsIgnoreCase("exits")){
                            break OUTTER_LOOP;
                        } else if (input.equalsIgnoreCase("reset")) {
                            continue OUTTER_LOOP;
                        }
                        int playerCount=Integer.parseInt(input);
                        if(playerCount==0){
                            break;
                        }
                        isValidateTeamCount(uniqueGames[i],playerCount);
                        if(!(isAvaliablePlayers(uniqueGames[i],playerCount))){
                            continue;
                        }
                        temp.put(uniqueGames[i],playerCount);
                        break;
                    }catch(NumberFormatException e){
                        System.out.println("Invalid input");
                        System.out.println("Member count must be a Number");
                    }catch (IllegalArgumentException e){
                        System.out.println("Invalid input");
                        System.out.println(e.getMessage());
                    }
                }

            }
            return temp;
        }

        return null;
    }

    private Boolean isAvaliablePlayers(String GameName,Integer playerCount){
        if (groupedParticipantsByGame.get(GameName).size()>=playerCount+totalCounts.getOrDefault(GameName,0)){
            return true;
        }else {
            System.out.println("Cant Allocate "+playerCount+" Players There is only "+(groupedParticipantsByGame.get(GameName).size()-totalCounts.getOrDefault(GameName,0))+" Players");
            return false;
        }

    }

    private void getTotalPlayerCountPerGame() {
        for (HashMap<String, Integer> group : GroupsDetails) {
            for (Map.Entry<String, Integer> entry : group.entrySet()) {
                String game = entry.getKey();
                int count = entry.getValue();
                totalCounts.put(game, totalCounts.getOrDefault(game, 0) + count);
            }
        }
    }
    private void printTotalPlayerCountPerGame(){
        System.out.println("\n--- Total Players Requested Per Game ---");
        for (Map.Entry<String, Integer> entry : totalCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void allocatePlayers(){

        for (Map.Entry<String, Integer> entry : totalCounts.entrySet()) {
            //System.out.println(entry.getKey()+": "+entry.getValue());
            for(Map.Entry<String,ArrayList<Participant>> entry2:formattedMap.get(entry.getKey()).entrySet()){
                selectedLeaders.addAll(getLeaders(entry2.getValue()));
                selectedThinkers.addAll(getThinkers(entry2.getValue()));
                selectedBalanced.addAll(getBalanced(entry2.getValue()));
            }
        }
    }


    private ArrayList<Participant> getLeaders(ArrayList<Participant> playerArrayList){
        int teamCount=GroupsDetails.size();
        if(teamCount<playerArrayList.size()){
            return new ArrayList<>(playerArrayList.subList(0, teamCount));
        }
        return playerArrayList;
    }

    private ArrayList<Participant> getThinkers(ArrayList<Participant> playerArrayList) {
        int teamCount = GroupsDetails.size();
        int neededCount = teamCount * 2;
        if (neededCount < playerArrayList.size()) {
            return new ArrayList<>(playerArrayList.subList(0, neededCount));
        }
        return playerArrayList;
    }

    private ArrayList<Participant> getBalanced(ArrayList<Participant> playerArrayList) {
        int teamCount=GroupsDetails.size();
        int neededCount=0;
        for(int playerCount:totalCounts.values()){
            neededCount+=playerCount;
        }
        neededCount = neededCount - (teamCount * 2);
        if (neededCount < playerArrayList.size()) {
            return new ArrayList<>(playerArrayList.subList(0, neededCount));
        }
        return playerArrayList;
    }
}
