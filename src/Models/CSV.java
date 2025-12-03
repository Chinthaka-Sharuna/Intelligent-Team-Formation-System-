package Models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSV {
    private final Path path;

    public CSV(String path) {
        this.path = Paths.get(path);
    }

    public List<Participant> load() {
        List<Participant> list = new ArrayList<>();
        String id = "";
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 8) {
                    continue;
                }
                id = p[0].trim();
                String name = p[1].trim();
                String email = p[2].trim();
                String preferredGame = p[3].trim();
                int skillLevel = Integer.parseInt(p[4].trim());
                Role preferredRole= Role.fromString(p[5].trim());
                int personalityScore=Integer.parseInt(p[6].trim());
                list.add(new Participant(id,name,email,preferredGame,skillLevel,preferredRole,personalityScore));
            }
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            System.out.println(e.getMessage());
        }catch (IOException e){
            System.out.println("Error reading file");
            System.out.println(e.getMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("There are no compete data in "+id);
            System.out.println(e.getMessage());
        }catch (NumberFormatException e){
            System.out.println("Invalid data format");
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println("Error loading file");
            System.out.println(e.getMessage());
        }
        return list;
    }

    public void writeParticipant(List<Participant> participants){
        System.out.println("Writing participants to CSV");
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {

            bw.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType");
            bw.newLine();
            for(Participant participant : participants){
                bw.write(String.join(",",participant.toArray()));
                bw.newLine();
            }
        }catch (FileNotFoundException e){
        System.out.println("File not found");
        System.out.println(e.getMessage());
        }catch (IOException e){
            System.out.println("Error writing file");
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println("Error loading file");
            System.out.println(e.getMessage());
        }
        System.out.println("Participants written to CSV");
    }

}
