package src.Model; 

public enum EventType {
    MUSIC,
    THEATRE,
    DANCE,
    MOVIE,
    SPORTS;

    public static EventType findByName(String name) {
        for (EventType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}

