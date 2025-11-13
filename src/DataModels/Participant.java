package DataModels;

public class Participant {
    private String id;
    private String name;
    private String email;
    private String preferredGame;
    private int skillLevel;
    private String preferredRole;
    private int personalityScore;
    private PersonalityType personalityType;

    public Participant(String[] data) {
        this.id = data[0];
        this.name = data[1];
        this.email = data[2];
        this.preferredGame = data[3];
        this.skillLevel = Integer.parseInt(data[4]);
        this.preferredRole = data[5];
        this.personalityScore = Integer.parseInt(data[6]);
        this.personalityType =  PersonalityType.valueOf(data[7]);
    }

    public Participant(String id, String name, String email,String preferredGame, int skillLevel,String preferredRole,int personalityScore) {
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

    public String getPreferredRole() {
        return preferredRole;
    }

    public int getPersonalityScore() {
        return personalityScore;
    }

    public PersonalityType getPersonalityType() {
        return personalityType;
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

    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }

    public void setPersonalityScore(int personalityScore) {
        this.personalityScore = personalityScore;
    }
    public void setPersonalityType(int personalityScore) {
        this.personalityType = PersonalityType.getPersonalityType(personalityScore);
    }
    public String[] toArray(){
        String[] data = new String[8];
        data[0] = this.id;
        data[1] = this.name;
        data[2] = this.email;
        data[3] = this.preferredGame;
        data[4] = String.valueOf(this.skillLevel);
        data[5] = this.preferredRole;
        data[6] = String.valueOf(this.personalityScore);
        data[7] = String.valueOf(this.personalityType);
        return data;
    }

}

