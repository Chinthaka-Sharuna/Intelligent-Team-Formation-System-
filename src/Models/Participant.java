package Models;

public class Participant {
    private String id;
    private String name;
    private String email;
    private String preferredGame;
    private int skillLevel;
    private Role preferredRole;
    private final int personalityScore;
    private final PersonalityType personalityType;

    public Participant(String[] data) {
        this.id = data[0];
        this.name = data[1];
        this.email = data[2];
        this.preferredGame = data[3];
        this.skillLevel = Integer.parseInt(data[4]);
        this.preferredRole = Role.fromString(data[5].trim());
        this.personalityScore = Integer.parseInt(data[6]);
        this.personalityType =  PersonalityType.valueOf(data[7]);
    }

    public Participant(String id, String name, String email,String preferredGame, int skillLevel,Role preferredRole,int personalityScore) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.preferredGame = preferredGame;
        this.skillLevel = skillLevel;
        this.preferredRole = preferredRole;
        this.personalityScore = personalityScore;
        this.personalityType = PersonalityType.getPersonalityType(personalityScore);
    }


    //Getter
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPreferredGame() {
        return preferredGame;
    }

    public int getSkillLevel() {
        return skillLevel;
    }


    public Role getPreferredRole() {
        return preferredRole;
    }

    public int getPersonalityScore() {
        return personalityScore;
    }

    public String getPersonalityType() {
        return String.valueOf(personalityType);
    }

    //Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPreferredGame(String preferredGame) {
        this.preferredGame = preferredGame;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public void setPreferredRole(Role preferredRole) {
        this.preferredRole = preferredRole;
    }

    public void setPreferredRole(String preferredRole) {
        this.preferredRole = Role.fromString(preferredRole);
    }

    public String[] toArray(){
        String[] data = new String[8];
        data[0] = this.getId();
        data[1] = this.getName();
        data[2] = this.getEmail();
        data[3] = this.getPreferredGame();
        data[4] = String.valueOf(this.getSkillLevel());
        data[5] = String.valueOf(this.getPreferredRole());
        data[6] = String.valueOf(this.getPersonalityScore());
        data[7] = String.valueOf(this.getPersonalityType());
        return data;
    }

    @Override
    public String toString() {
        return name + " (" + id + ") - " + preferredRole + " | Skill: " + skillLevel +  " | Game: " + preferredGame +" | Personality: " + personalityType + "(" + personalityScore + ")";
    }

    public String toStringALl() {
        return "id - "+id+"  Name  -  "+name+"  Email  -  "+email+"  PreferredGame  -  "+preferredGame+"  Skill Level  -  "+skillLevel+"  Personality Type  -  "+personalityType+" Preferred Role  -  "+getPreferredRole()+"  Personality Score  -  "+personalityScore+"  Personality Type  -  "+getPersonalityType();
    }

}

