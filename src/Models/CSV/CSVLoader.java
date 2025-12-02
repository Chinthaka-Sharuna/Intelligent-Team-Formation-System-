package Models.CSV;

import Models.Participant;
import Models.PersonalityType;
import Models.Role;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {

    private final Path path;

    public CSVLoader(String path) {
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

}
