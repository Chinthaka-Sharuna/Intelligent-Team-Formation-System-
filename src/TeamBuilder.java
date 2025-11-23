import java.util.*;


public class TeamBuilder {
    private final static HashMap<String,Integer> minimumMemberCount=new HashMap<>();
    Scanner sc = Main.sc;
    private int TeamSize;
    private ArrayList <HashMap<String,Integer>> GroupsDetails=new ArrayList<>();
    static {
        minimumMemberCount.put("Chess",1);
        minimumMemberCount.put("CS:GO",5);
        minimumMemberCount.put("DOTA 2",5);
        minimumMemberCount.put("Basketball",5);
        minimumMemberCount.put("Valorant",5);
        minimumMemberCount.put("FIFA",2);
    }
    public boolean isvalidateTeamCount(String gameName,int memberCount){
        minimumMemberCount.get(gameName);
        if (memberCount>0 && memberCount%minimumMemberCount.get(gameName)==0){
            return true;
        }
        else {
            return false;
        }
    }


}
