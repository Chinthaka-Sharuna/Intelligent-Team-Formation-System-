package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {
    private String name;
    private List<Participant> members;
    private boolean isValid;

    public Team(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    public Team(String name, List<Participant> members) {
        this.name = name;
        this.members = members;
    }

    public Team(String name, Participant member) {
        this.name = name;
        this.members = new ArrayList<>();
        this.members.add(member);
    }

    public String getName() {
        return name;
    }

    public Participant[] getTeamMembers() {
        return members.toArray(new Participant[0]);
    }

    public boolean getValid() {
        return isValid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }


    public List<Participant> getMembers() {
        return members;
    }

    public int getSize(){
        return members.size();
    }


    public void setMembers(List<Participant> members) {
        this.members = members;
    }


    public double averageSkill() {
        if (members.isEmpty()) {
            return 0;
        }
        int sum = 0;
        for (Participant participant : members) {
            sum += participant.getSkillLevel();
        }
        return (double) sum / members.size();
    }

    public void addMember(Participant member) {
        if (this.isIn(member)) {
            System.out.println(member.getName() + " is already member");
            System.out.println(member.toString());
        } else {
            members.add(member);
        }
    }

    public boolean validityChecker(Participant member) {
        List<Participant> temp = new ArrayList<>();
        temp.addAll(members);
        this.members.add(member);
        if(this.isValid()){
            this.members=temp;
            return  true;
        }
        this.members=temp;
        return  false;


    }

    public double AVGChecker(Participant member) {
        double avg=0;
        List<Participant> temp = new ArrayList<>();
        temp.addAll(this.members);
        this.members.add(member);
        if(this.isValid()){
            avg=this.averageSkill();
            this.members=temp;
            return avg;
        }
        this.members=temp;
        return  0;


    }

    public boolean isIn(Participant member) {
        for (Participant p : members) {
            if (p.getId().equals(member.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid(){
        LeaderValidator leaderValidator = new LeaderValidator(this.members);
        GameValidator gameValidator = new GameValidator(this.members);
        RoleValidator roleValidator = new RoleValidator(this.members);
        ThinkerValidator thinkerValidator = new ThinkerValidator(this.members);
        leaderValidator.start();
        thinkerValidator.start();
        gameValidator.start();
        roleValidator.start();
        try{
            leaderValidator.join();
            thinkerValidator.join();
            gameValidator.join();
            roleValidator.join();
            /*
            if(!leaderValidator.isValid()){
                System.out.println("Error in the Leader Validator");
            } else if (!thinkerValidator.isValid()) {
                System.out.println("Error in the Thinker Validator");
            }else if (!roleValidator.isValid()) {
                System.out.println("Error in the Role Validator");
            }*/
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return leaderValidator.isValid() && thinkerValidator.isValid() && gameValidator.isValid() && roleValidator.isValid();
    }

    public int roleCounter(){
        HashMap<String, Integer> roleCounter = new HashMap<>();
        for(Participant participant : members){
            roleCounter.put(String.valueOf(participant.getPreferredRole()),roleCounter.getOrDefault(participant.getPreferredRole(),0)+1);
        }
        return roleCounter.size();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team ").append(name).append(" | Size: ").append(getSize())
                .append(" | AvgSkill: ").append(String.format("%.2f", averageSkill()))
                .append("\n");
        for (Participant p : members) {
            sb.append("  - ").append(p.toString()).append("\n");
        }
        return sb.toString();
    }

    private class LeaderValidator extends Thread{
        private List<Participant> participants;
        private boolean isValid;

        public LeaderValidator(List<Participant> participants){
            this.participants=participants;
        }
        @Override
        public void run(){
            int LeaderCount=0;
            for(Participant participant:participants){
                if(participant.getPersonalityType().equals("Leader")){
                    LeaderCount++;
                }
            }
            if(LeaderCount==1){
                isValid=true;
                return;
            }
            isValid=false;
        }

        public boolean isValid(){
            return isValid;
        }
    }

    private class ThinkerValidator extends Thread{
        private List<Participant> participants;
        private boolean isValid=false;

        public ThinkerValidator(List<Participant> participants){
            this.participants=participants;
        }
        @Override
        public void run(){
            int ThinkerCount=0;
            for(Participant participant:participants){
                if(participant.getPersonalityType().equals("Thinker")){
                    ThinkerCount++;
                }
            }
            if(ThinkerCount==0){
                return;
            }

            if(ThinkerCount<=2){
                isValid=true;
                return;
            }
            isValid=false;
        }

        public boolean isValid(){
            return isValid;
        }
    }

    private class GameValidator extends Thread{
        private List<Participant> participants;
        private boolean isValid=false;
        Map<String,Integer> gameCount=new HashMap<>();

        public GameValidator(List<Participant> participants){
            this.participants=participants;
        }

        @Override
        public void run(){
            for(Participant participant:participants){
                gameCount.put(participant.getPreferredGame(),gameCount.getOrDefault(participant.getPreferredGame(),0)+1);
            }
            for(int value:gameCount.values()){
                if(value>2){
                    return;
                }
            }
            isValid=true;
        }

        public boolean isValid(){
            return isValid;
        }

    }

    private class RoleValidator extends Thread{
        private List<Participant> participants;
        private boolean isValid=false;
        Map <String,Integer> RoleCount=new HashMap<>();

        public RoleValidator(List<Participant> participants){
            this.participants=participants;
        }

        @Override
        public void run(){
            if(participants.size()>5){
                for(Participant participant:participants){
                    RoleCount.put(String.valueOf(participant.getPreferredRole()),RoleCount.getOrDefault(String.valueOf(participant.getPreferredRole()),0)+1);
                }
                if(RoleCount.size()<3){
                    return;
                }
            }
            isValid = true;
        }

        public boolean isValid(){
            return isValid;
        }

    }


}
