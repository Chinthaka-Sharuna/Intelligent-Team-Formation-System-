package Models;

public enum Role {
    STRATEGIST,
    ATTACKER,
    DEFENDER,
    SUPPORTER,
    COORDINATOR;

    public static Role fromString(String s) {
        if (s == null) return null;
        switch (s.trim().toLowerCase()) {
            case "strategist": return STRATEGIST;
            case "attacker": return ATTACKER;
            case "defender": return DEFENDER;
            case "supporter": return SUPPORTER;
            case "coordinator": return COORDINATOR;
            default: return null;
        }
    }
}
