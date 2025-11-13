package DataModels;

public enum PersonalityType {
    Leader,
    Balanced,
    Thinker;

    public static PersonalityType getPersonalityType(int marks) {
        if (marks < 70) {
            return PersonalityType.Thinker;
        } else if (marks < 90) {
            return PersonalityType.Balanced;
        } else if (marks < 100) {
            return PersonalityType.Leader;
        } else {
            throw new IllegalArgumentException("Invalid marks (Marks must be less than or equal to 70)");
        }
    }
}