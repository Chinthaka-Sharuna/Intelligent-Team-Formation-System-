package Models;

public enum PersonalityType {
    Leader,
    Balanced,
    Thinker;

    public static PersonalityType getPersonalityType(int marks) {
        if((marks >= 0)  && (marks <=100)) {
            if (marks >=90) {
                return PersonalityType.Leader;
            } else if (marks >=70) {
                return PersonalityType.Balanced;
            } else {
                return PersonalityType.Thinker;
            }
        }else{
            throw new IllegalArgumentException("Invalid marks. Score must be between 0 and a reasonable maximum (e.g., 100). Received: \" + marks");

        }
    }
}