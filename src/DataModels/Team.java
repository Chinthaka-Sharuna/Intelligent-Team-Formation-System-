package DataModels;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private List<Participant> members;

    public Team(String name){
        this.name=name;
        this.members=new ArrayList<>();
    }
    public Team(String name,List<Participant> members){
        this.name=name;
        this.members=members;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Participant> getMembers() {
        return members;
    }
    public void setMembers(List<Participant> members) {
        this.members = members;
    }
    public void addMember(Participant member){
        if(this.isIn(member)){
            System.out.println(member.getName()+" is already member");
        }else{
            members.add(member);
        }
    }

    private boolean isIn(Participant member){
        for(Participant p:members){
            if(p.getId().equals(member.getId())){
                return true;
            }
        }return false;
    }
}
