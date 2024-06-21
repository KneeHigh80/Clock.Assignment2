package clock;


/**
 * The Alarm class represents an alarm with a specific time and label.
 * It implements the Comparable interface to allow alarms to be compared based on their time.
 */
public class Alarm implements Comparable<Alarm> {
    private int hours;
    private int minutes;
    private int seconds;
    private String label;
    
    
    /**
     * Constructs a new Alarm with the specified time and label.
     * 
     * @param hours the hour of the alarm
     * @param minutes the minute of the alarm
     * @param seconds the second of the alarm
     * @param label a label describing the alarm
     */
    public Alarm(int hours, int minutes, int seconds, String label) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.label = label;
    }
    
    
    //Getters and Setters
    
    /**
     * Gets the hour of the alarm.
     * 
     * @return the hour of the alarm
     */
    public int getHours() {
        return hours;
    }
    
    /**
     * Sets the hour of the alarm.
     * 
     * @param hours the new hour of the alarm
     */
    public void setHours(int hours) {
        this.hours = hours;
    }
    
    /**
     * Gets the minute of the alarm.
     * 
     * @return the minute of the alarm
     */
    public int getMinutes() {
        return minutes;
    }
    
    /**
     * Sets the minute of the alarm.
     * 
     * @param minutes the new minute of the alarm
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
    
    /**
     * Gets the second of the alarm.
     * 
     * @return the second of the alarm
     */
    public int getSeconds() {
        return seconds;
    }
    
    /**
     * Sets the second of the alarm.
     * 
     * @param seconds the new second of the alarm
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    
    /**
     * Gets the label of the alarm.
     * 
     * @return the label of the alarm
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    
    /**
     * Compares this alarm to another alarm based on the time.
     * 
     * @param other the other alarm to compare to
     * @return a negative integer, zero, or a positive integer as this alarm is less than, equal to, or greater than the specified alarm
     */
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
    
    /**
     * Returns a string representation of the alarm in the format "Alarm[HH:MM:SS] - label".
     * 
     * @return a string representation of the alarm
     */
    @Override
    public String toString() {
        return String.format("Alarm[%02d:%02d:%02d] - %s", hours, minutes, seconds, label);
    }
    
    
    /**
     * Converts the alarm to an iCalendar formatted string.
     * 
     * @return the iCalendar formatted string of the alarm
     */
    public String toICalendar() {
    return "BEGIN:VEVENT\n" +
           "SUMMARY:" + label + "\n" +
           String.format("DTSTART:%02d%02d%02d\n", hours, minutes, seconds) +
           "BEGIN:VALARM\n" +
           "TRIGGER:-PT" + String.format("%02dH%02dM%02dS", hours, minutes, seconds) + "\n" +
           "ACTION:DISPLAY\n" +
           "DESCRIPTION:" + label + "\n" +
           "END:VALARM\n" +
           "END:VEVENT\n";
}
}
