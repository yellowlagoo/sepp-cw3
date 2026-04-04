package src.Model; 
import java.util.*;

public class StudentPreferences {
    private boolean preferMusicEvents;
    private boolean preferTheaterEvents;
    private boolean preferDanceEvents;
    private boolean preferMovieEvents;
    private boolean preferSportsEvents;

    /**
     * Constructor specifying a student's preferences for event type
     * @param preferMusicEvents - whether the student prefers music events
     * @param preferTheaterEvents - whether the student prefers theater events
     * @param preferDanceEvents - whether the student prefers dance events
     * @param preferMovieEvents - whether the student prefers movie events
     * @param preferSportsEvents - whether the student prefers sports events
     */
    public StudentPreferences() {
        this.preferMusicEvents = false;
        this.preferTheaterEvents = false;
        this.preferDanceEvents = false;
        this.preferMovieEvents = false;
        this.preferSportsEvents = false;
    }
    
    /**
     * Updates the student's preferences by parsing a string of preferences
     * @param studentRawStringPreferences
     * @return - whether or not the preferences have been successfully updated
     */
    public boolean updatePreferences(String studentRawStringPreferences) {
        // comma separated list 
        try {
            validatePreferences(studentRawStringPreferences);
        } catch (IllegalArgumentException e) {
            return false;
        }
        studentRawStringPreferences = studentRawStringPreferences.toLowerCase();
        preferMusicEvents = studentRawStringPreferences.contains("music");
        preferTheaterEvents = studentRawStringPreferences.contains("theater") || studentRawStringPreferences.contains("theatre");
        preferDanceEvents = studentRawStringPreferences.contains("dance");
        preferMovieEvents = studentRawStringPreferences.contains("movie");
        preferSportsEvents = studentRawStringPreferences.contains("sports");
        return true;
    }

    private void validatePreferences(String str) {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("The inputted preference string is empty");
        } 

        List<String> elements = Arrays.asList((str.trim()).split(","));

        for (int i = 0; i < elements.size(); i++) {
            if (EventType.findByName(elements.get(i)) == null) {
                throw new IllegalArgumentException("There is at least one preference that is not recognized");
            }
        }
    }

    //Getters and setters
    public boolean prefersMusicEvents() {
        return preferMusicEvents;
    }

    public void setPreferMusicEvents(boolean preferMusicEvents) {
        this.preferMusicEvents = preferMusicEvents;
    }

    public boolean prefersTheaterEvents() {
        return preferTheaterEvents;
    }

    public void setPreferTheaterEvents(boolean preferTheaterEvents) {
        this.preferTheaterEvents = preferTheaterEvents;
    }

    public boolean prefersDanceEvents() {
        return preferDanceEvents;
    }

    public void setPreferDanceEvents(boolean preferDanceEvents) {
        this.preferDanceEvents = preferDanceEvents;
    }

    public boolean prefersMovieEvents() {
        return preferMovieEvents;
    }

    public void setPreferMovieEvents(boolean preferMovieEvents) {
        this.preferMovieEvents = preferMovieEvents;
    }

    public boolean prefersSportsEvents() {
        return preferSportsEvents;
    }

    public void setPreferSportsEvents(boolean preferSportsEvents) {
        this.preferSportsEvents = preferSportsEvents;
    }

    public String toString() {
        String preferenceShell = "Dance: %s, Movie: %s, Music: %s, Sports: %s, Theater: %s";
        String preferences = String.format(preferenceShell, prefersDanceEvents(), prefersMovieEvents(), prefersMusicEvents(), prefersSportsEvents(), prefersTheaterEvents());
        return preferences;
    }
}