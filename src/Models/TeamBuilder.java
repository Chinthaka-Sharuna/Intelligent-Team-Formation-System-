package Models;
import com.sun.tools.javac.Main;

import java.util.*;


public class TeamBuilder {
    private Team[] teams;
    private int membersCountEachTeam;
    private int numberOfTeams;
    private int totalNumberOfPlayers = 0;
    private Participant[] leaders;
    private Participant[] thinkers;
    private List<Participant> otherPlayers;
    private HashMap<Participant, Participant> CoupledParticipants=new HashMap<>();
    private String[] uniqueGames;
    private float globalAVG;



    public TeamBuilder(int membersCountEachTeam,float globalAVG,HashMap<String,List<Participant>> participants,String[] uniqueGames) {
        this.membersCountEachTeam = membersCountEachTeam;
        this.totalNumberOfPlayers =countParticipants(participants) ;
        this.numberOfTeams=totalNumberOfPlayers/membersCountEachTeam;
        this.teams = new Team[numberOfTeams];
        this.uniqueGames = uniqueGames;
        this.globalAVG=globalAVG;
        leaders =groupCreationValidatorOnLeader(participants.get("Leader"));
        Participant[] temp=groupCreationValidatorOnLThinker(participants.get("Thinker"));
        thinkers=Arrays.copyOfRange(temp,temp.length-numberOfTeams,temp.length);
        List<Participant> leftOvers=new ArrayList<>();
        leftOvers.addAll(Arrays.asList(Arrays.copyOfRange(temp,0,temp.length-numberOfTeams)));
        leftOvers.addAll(participants.get("Balanced"));
        otherPlayers= sortParticipantsAscending(leftOvers);
        teamCreator();
        teamCoupler();
        sortTeamsDescending();
        teamFiller();
        validator();
        Scanner sc=new Scanner(System.in);
        System.out.println("Do you Want to export formatted Teams? (Y/N)");
        String choise= sc.nextLine();
        if(choise.equalsIgnoreCase("Y")||choise.equalsIgnoreCase("yes")){
            CSV teamCSV=new CSV("data/formed_teams.csv");
            teamCSV.saveTeams(teams);
        }

    }


    private Participant[] groupCreationValidatorOnLeader(List<Participant> participants){
        List<Participant> temp=sortParticipantsDescending(participants);
        if(temp.size()==0){
            System.out.println("There are No Leader to Make Teams");
            System.exit(0);
            return null;
        }else if(temp.size()<numberOfTeams){
            numberOfTeams=temp.size();
            return temp.toArray(new Participant[0]);
        }else{
            return Arrays.copyOfRange(temp.toArray(new Participant[0]),0,numberOfTeams);
        }
    }

    private Participant[] groupCreationValidatorOnLThinker(List<Participant> participants){
        List<Participant> temp=sortParticipantsAscending(participants);
        if(temp.size()==0){
            System.out.println("There are No Thinkers to Make Teams");
            System.exit(0);
            return null;
        }else if(temp.size()<numberOfTeams){
            numberOfTeams=temp.size();
            return temp.toArray(new Participant[0]);
        }else{
            return temp.toArray(new Participant[0]);
        }
    }

    private List<Participant> sortParticipantsDescending(List<Participant> list) {
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0; j < n - i - 1; j++) {

                Participant p1 = list.get(j);
                Participant p2 = list.get(j + 1);

                if (p1.getSkillLevel() < p2.getSkillLevel()) {

                    list.set(j, p2);
                    list.set(j + 1, p1);
                }
            }
        }
        return list;
    }

    private void sortTeamsDescending() {
        int n = teams.length;

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0; j < n - i - 1; j++) {

                Team t1 = teams[j];
                 Team t2 = teams[j + 1];

                if (t1.averageSkill() < t2.averageSkill()) {

                    teams[j]= t2;
                    teams[j + 1] = t1;
                }
            }
        }
    }

    private List<Participant> sortParticipantsAscending(List<Participant> list) {
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                Participant p1 = list.get(j);
                Participant p2 = list.get(j + 1);

                if (p1.getSkillLevel() > p2.getSkillLevel()) {

                    list.set(j, p2);
                    list.set(j + 1, p1);
                }
            }
        }
        return list;
    }

    private int countParticipants(HashMap<String,List<Participant>> participants) {
        int totalSize = 0;
        for (List<Participant> list : participants.values()) {
            totalSize += list.size();
        }

        System.out.println("Total Participants: " + totalSize);
        return totalSize;
    }

    private void teamCreator(){
        for (int i = 0; i < teams.length; i++) {
            teams[i]=new Team("Team_"+(i+1));
        }
    }

    private void teamCoupler(){
        try {
            for (int i = 0; i < teams.length; i++) {
                CoupledParticipants.put(leaders[i], thinkers[i]);
                teams[i].addMember(leaders[i]);
                teams[i].addMember(thinkers[i]);
                //System.out.println(teams[i].averageSkill());
                ;
            }
        }catch (IndexOutOfBoundsException e){
            System.out.println("There are no Player to make team");
        }
    }

    private HashMap<String,Participant[]> groupParticipantByGame(List<Participant> participants){
        HashMap<String,Participant[]> groupedParticipants=new HashMap<>();
        for(int i=0;i<uniqueGames.length;i++){
            System.out.println(uniqueGames[i]);
            ArrayList<Participant> temp=new ArrayList<>();
            for(String game:uniqueGames){
                for(Participant participant:participants){
                    if(participant.getPreferredGame().equals(game)){
                        temp.add(participant);
                    }
                }
                groupedParticipants.put(game,temp.toArray(new Participant[0]));
            }

        }

        System.out.println("Grouped By Games");
        return groupedParticipants;
    }

    private void validator(){
        for(int i=0;i<teams.length;i++){
            if((membersCountEachTeam==teams[i].getSize())&&(teams[i].isValid())){
                teams[i].setValid(true);
                System.out.println(teams[i].toString());
            }
        }
    }

    private void teamFiller() {
        for(Team team:teams){
            //System.out.println(team.toString());
            int middle=otherPlayers.size()/2;
            int current;
            double range=0.5;
            double teamAVGSkill=team.averageSkill();
            double upperValue=teamAVGSkill+range;
            double lowerValue=teamAVGSkill-range;
            boolean leftChecked=false;
            int distance=0;
            //System.out.println("Middle Value is "+middle);
            //System.out.println(team.averageSkill());
            try {
                Participant player=null;
                while(team.getSize()<membersCountEachTeam){
                    if(leftChecked){
                        current=middle+distance;
                        distance++;
                        leftChecked=false;
                    }else{
                        current=middle-distance;
                        leftChecked=true;
                    }
                    player = otherPlayers.get(current);
                    if (!team.isIn(player)) {
                        if (team.validityChecker(otherPlayers.get(current))) {
                            team.addMember(otherPlayers.get(current));
                            otherPlayers.remove(current);
                            teamAVGSkill=team.averageSkill();
                            upperValue=teamAVGSkill+range;
                            lowerValue=teamAVGSkill-range;
                            leftChecked=false;
                            distance=0;
                        }
                    }

                }
                //System.out.println(team.averageSkill());
            } catch (IndexOutOfBoundsException e) {
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



}