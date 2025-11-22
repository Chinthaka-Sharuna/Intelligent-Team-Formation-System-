import java.sql.SQLOutput;
import java.util.*;


public class TeamBuilder {
    Scanner sc = Main.sc;

    private HashMap<String, Integer> subGruopsDetils=new HashMap<>();
    private int TeamSize;


    public void TeamCreation(){
        TeamSize=getTeamCount();
        String[] subTeams=createSubTeams();
        subGruopsDetils=getSubTeamMemberCount(subTeams);

        for(String subTeam:subGruopsDetils.keySet()){
            System.out.println(subTeam +" : "+subGruopsDetils.get(subTeam));
        }

    }

    private int getTeamCount(){
        System.out.print("Enter Team Count:- ");
        int teamCount;
        while(true){
            try{
                teamCount=Integer.parseInt(sc.nextLine());
                if((teamCount < 2)) {
                    System.out.println("Invalid input (Team count must be greater than or equal to 2)");
                    System.out.println("Please Re-enter a valid team count :- ");
                    continue;
                }
                break;
            }catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Input must be a number");
            }
            System.out.print("Please Re-enter a valid team count :- ");
        }
        return teamCount;
    }

    private String[] createSubTeams(){
        String[] uniqueGames=Main.uniqueGames;
        for (int i=0;i<uniqueGames.length;i++){
            System.out.println("    "+(i+1)+". "+uniqueGames[i]);
        }
        System.out.println("Please enter your names indexes: (separate using comma)");
        System.out.println("eg:-1,3,2,4");
        System.out.print("Enter Here :-");
        while(true){
            String input=sc.nextLine();
            String[] temp=input.trim().split(",");
            String[] inputs=new String[temp.length];
            try{
                for(int i=0;i<temp.length;i++){
                    int index=Integer.parseInt(temp[i]);
                    inputs[i]=uniqueGames[index-1];
                }
                System.out.println(Arrays.toString(inputs));
                return inputs;
            }catch (InputMismatchException e){
                System.out.println("Invalid input");
                System.out.print("Please Re-enter a valid indexes :-");
            }catch (NumberFormatException e){
                System.out.println("Invalid input");
                System.out.println("Index must be a number");
                System.out.print("Please Re-enter a valid indexes :-");
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Invalid input (value is out of range)");
                System.out.println("Value is out of range");
                System.out.print("Please Re-enter a valid indexes :-");
            }
        }
    }


    public HashMap<String,Integer> getSubTeamMemberCount(String[]  subTeams) {
        HashMap<String, Integer> subGroupDetails = new HashMap<>();
        OUTTER_LOOP:while (true) {
            int currentMemberCount=0;
            subGroupDetails.clear();
            for (int i = 0; i < subTeams.length; i++) {
                INNER_LOOP:while (true) {
                    try {
                        if(TeamSize==currentMemberCount){
                            System.out.println("Something went wrong");
                            System.out.println("Group count is exceeded");
                            System.out.println("Please Re-enter a valid group count :- ");
                            continue OUTTER_LOOP;
                        }
                        System.out.print("Please enter your team members count for "+subTeams[i]+" Team :- ");
                        int subGroupMemberCount = Integer.parseInt(sc.nextLine());
                        if (TeamSize < subGroupMemberCount + currentMemberCount) {
                            throw new RuntimeException("Check your group members count \nGroup count must be Gather than total count of Sub-Groups");
                        }
                        subGroupDetails.put(subTeams[i], subGroupMemberCount);
                        currentMemberCount+=subGroupMemberCount;
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input");
                        System.out.println("Group members count must be a number");
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Do you want to Re-Enter all Group members count or re-enter the last sub group member count:- ");
                        System.out.println("if you want to Re-enter all enter the - all type all \nif you want to enter the last sub group member count type - last ");
                        System.out.print("Your Choice :-");
                        while (true) {
                            String choice = sc.nextLine();
                            if (choice.equals("all")) {
                                continue OUTTER_LOOP;
                            } else if (choice.equals("last")) {
                                continue INNER_LOOP;
                            } else {
                                System.out.println("Invalid input");
                                System.out.print("Re_enter the choice :-");
                                continue;
                            }
                        }
                    }
                }

            }
            return subGroupDetails;
        }
    }
}
