package src.Model; 

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
    public StudentPreferences(boolean preferMusicEvents, boolean preferTheaterEvents, boolean preferDanceEvents,
            boolean preferMovieEvents, boolean preferSportsEvents) {
        this.preferMusicEvents = preferMusicEvents;
        this.preferTheaterEvents = preferTheaterEvents;
        this.preferDanceEvents = preferDanceEvents;
        this.preferMovieEvents = preferMovieEvents;
        this.preferSportsEvents = preferSportsEvents;
    }
    
    /**
     * Updates the student's preferences by parsing a string of preferences
     * @param studentRawStringPreferences
     * @return - whether or not the preferences have been successfully updated
     */
    public boolean updatePreferences(String studentRawStringPreferences){
        studentRawStringPreferences = studentRawStringPreferences.toLowerCase();
        
        preferMusicEvents = studentRawStringPreferences.contains("music");
        preferTheaterEvents = studentRawStringPreferences.contains("theater") || studentRawStringPreferences.contains("theatre");
        preferDanceEvents = studentRawStringPreferences.contains("dance");
        preferMovieEvents = studentRawStringPreferences.contains("movie");
        preferSportsEvents = studentRawStringPreferences.contains("sports");

        return true;
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
}