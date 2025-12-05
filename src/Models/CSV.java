package Models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSV {
    private Path path;

    public CSV(String path) {
        this.path = Paths.get(path);
    }

    public void setPath(String path){
        this.path = Paths.get(path);
    }

    public List<Participant> load() {
        List<Participant> list = new ArrayList<>();
        String id = "";
        try {
            BufferedReader br = Files.newBufferedReader(path);
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                Validator validator = new Validator(p);
                if(!validator.isValid()){
                    continue;
                }
                id = p[0].trim();
                String name = p[1].trim();
                String email = p[2].trim();
                String preferredGame = p[3].trim();
                int skillLevel = Integer.parseInt(p[4].trim());
                Role preferredRole = Role.fromString(p[5].trim());
                int personalityScore = Integer.parseInt(p[6].trim());
                list.add(new Participant(id, name, email, preferredGame, skillLevel, preferredRole, personalityScore));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file");
            System.out.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("There are no compete data in " + id);
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid data format");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error loading file");
            System.out.println(e.getMessage());
        }
        return list;
    }

    public void writeParticipant(List<Participant> participants) {
        System.out.println("Writing participants to CSV");
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {

            bw.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType");
            bw.newLine();
            for (Participant participant : participants) {
                bw.write(String.join(",", participant.toArray()));
                bw.newLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing file");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error loading file");
            System.out.println(e.getMessage());
        }
        System.out.println("Participants written to CSV");
    }

    public void saveTeams(Team[] teams) {
        System.out.println("Writing teams to CSV");
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {

            bw.write("TeamID,ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType");
            bw.newLine();
            for (Team team : teams) {
                if(team.getValid()==true){
                    for(Participant participant: team.getTeamMembers()){
                        bw.write(team.getName()+",");
                        bw.write(String.join(",", participant.toArray()));
                        bw.newLine();
                    }
                    bw.write("SUMMARY,Avg Skill: "+(float)team.averageSkill()+",Unique Roles: "+team.roleCounter());
                    bw.newLine();
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing file");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error loading file");
            System.out.println(e.getMessage());
        }
        System.out.println("Teams written to CSV");
    }

    private class Validator {
        boolean valid;
        String[] data;
        public Validator( String[] data) {
            this.data = data;
            LenghtValidator lenghtValidator = new LenghtValidator();
            SkillLevelValidator skillLevelValidator = new SkillLevelValidator();
            PersonalityScoreValidator  personalityScoreValidator = new PersonalityScoreValidator();
            lenghtValidator.start();
            skillLevelValidator.start();
            personalityScoreValidator.start();
            try{
                lenghtValidator.join();
                skillLevelValidator.join();
                personalityScoreValidator.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            valid = (lenghtValidator.valid)&&(skillLevelValidator.valid)&&(personalityScoreValidator.valid);
        }

        public boolean isValid() {
            return valid;
        }

        private class LenghtValidator extends Thread{
            protected boolean valid=false;
            @Override
            public void run(){
                if(data.length==8){
                    valid=true;
                }
            }

            protected boolean isValid(){
                return valid;
            }
        }

        private class   SkillLevelValidator extends Thread{
            protected boolean valid=false;

            protected boolean isValid(){
                return valid;
            }

            @Override
            public void run(){
                if((Integer.parseInt(data[4].trim())>0)&&(Integer.parseInt(data[4].trim())<=10)) {
                    valid = true;
                }
            }

        }

        private class PersonalityScoreValidator extends Thread{
            protected boolean valid=false;

            protected boolean isValid(){
                return valid;
            }

            @Override
            public void run(){
                if((Integer.parseInt(data[6].trim())>0)&&(Integer.parseInt(data[6].trim())<=100)) {
                    valid = true;
                }
            }
        }

    }

}
