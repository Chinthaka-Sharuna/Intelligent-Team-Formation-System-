package Models.CSV;

import Models.Participant;
import Models.Team;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CSVWriter {
    private final Path path;

    public CSVWriter(String filePath) {
        this.path = Paths.get(filePath);
    }

    public void writeTeams(List<Team> teams) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {

            bw.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType");
            bw.newLine();

            for (Team team : teams) {
                bw.write(team.getName()+" :- ");
                int memberCount=0;
                for(Participant participant:team.getTeamMembers()){
                    memberCount+=1;
                    bw.write("      "+memberCount+"."+ Arrays.toString(participant.toArray()));
                    bw.newLine();
                }
            }
        }
    }
}
