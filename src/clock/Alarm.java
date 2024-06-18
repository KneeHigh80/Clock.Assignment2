package clock;



public class Alarm implements Comparable<Alarm> {
    private int hours;
    private int minutes;
    private int seconds;
    private String label;
    
    
    // Constructor
    public Alarm(int hours, int minutes, int seconds, String label) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.label = label;
    }
    
    
    //Getters and Setters

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    
    @Override
    public int compareTo(Alarm other) {
        if(this.hours != other.hours) {
            return this.hours - other.hours;
        } else if (this.minutes != other.minutes) {
            return this.minutes - other.minutes;
        } else {
            return this.seconds - other.seconds;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Alarm[%02d:%02d:%02d] - %s", hours, minutes, seconds, label);
    }
    
    
    public String toICalendar() {
        return "BEGIN:VEVENT\n" +
                "SUMMARY:" + label + "\n" +
                String.format("DTSTART:%02d%02d%02d\n", hours, minutes, seconds) +
                "BEGIN:VALARM\n" +
                "TRIGGER:-PTOH" + String.format("%02dM%02dS", minutes, seconds) + "\n" +
                "ACTION:DISPLAY\n" +
                "DESCRIPTION:" + label + "\n" +
                "END:VALARM\n" +
                "END:VEVENT\n";
    }
}
