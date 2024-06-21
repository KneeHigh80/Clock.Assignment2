package clock;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import queuemanager.PriorityQueue;
import queuemanager.UnsortedArrayPriorityQueue;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;

/**
 * The Model class represents the state and behavior of the clock and alarm system.
 * It maintains the current time and a priority queue of alarms.
 */


public class Model extends Observable {
    
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    
    private int oldSecond = 0;
    
    private PriorityQueue<Alarm> alarmQueue;
    
    
    /**
     * Constructs a new Model with an empty priority queue of alarms.
     */
    public Model() {
        alarmQueue = new UnsortedArrayPriorityQueue<>(10);
        update();
    }
    
    /**
     * Updates the current time from the system clock and notifies observers if the second has changed.
     */
    public void update() {
        Calendar date = Calendar.getInstance();
        hour = date.get(Calendar.HOUR_OF_DAY);
        minute = date.get(Calendar.MINUTE);
        oldSecond = second;
        second = date.get(Calendar.SECOND);
        if (oldSecond != second) {
            setChanged();
            notifyObservers();
        }
    }
    
    //Getter Methods to get current Hour, Minute and Second

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
    
    /**
     * Methods for managing Alarms
     * Adds an alarm to the priority queue.
     * 
     * @param alarm the alarm to add
     */    
    public void addAlarm(Alarm alarm) {
        try {
            alarmQueue.add(alarm, alarm.getHours() * 3600 + alarm.getMinutes() * 60 + alarm.getSeconds());
        } catch (QueueOverflowException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }
    
    /**
     * Gets the next alarm in the priority queue.
     * 
     * @return the next alarm, or null if the queue is empty
     */
    public Alarm getNextAlarm() {
        try{
            return alarmQueue.head();
        } catch (QueueUnderflowException e) {
            return null;
        }
    }
    
    /**
     * Updates an existing alarm in the priority queue.
     * 
     * @param alarm the alarm to update
     */
    public void updateAlarm(Alarm alarm) {
        try {
            alarmQueue.remove();
            alarmQueue.add(alarm, alarm.getHours() * 3600 + alarm.getMinutes() * 60 + alarm.getSeconds());
        } catch (QueueUnderflowException | QueueOverflowException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }
    
    /**
     * Checks if it is time to trigger the given alarm.
     * 
     * @param alarm the alarm to check
     * @param now the current time
     * @return true if it is time to trigger the alarm, false otherwise
     */
    public boolean isTimeToTriggerAlarm(Alarm alarm, Calendar now) {
        //Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);
        int currentSecond = now.get(Calendar.SECOND);
        
        // Debug output
        //System.out.println("isTimeToTriggerAlarm - Current Time: " + currentHour + ":" + currentMinute + ":" + currentSecond);
        //System.out.println("isTimeToTriggerAlarm - Alarm Time: " + alarm.getHours() + ":" + alarm.getMinutes() + ":" + alarm.getSeconds());
    
        return alarm.getHours() == currentHour &&
                alarm.getMinutes() == currentMinute &&
                alarm.getSeconds() == currentSecond;
}
    
    /**
     * Removes the given alarm from the priority queue.
     * 
     * @param alarm the alarm to remove
     */
    public void removeAlarm (Alarm alarm) {
        try {
            alarmQueue.remove();
        } catch (QueueUnderflowException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }
    
    /**
     * Saves the alarms to a file in iCalendar format.
     * 
     * @param file the file to save the alarms to
     * @throws IOException if an I/O error occurs
     */
    public void saveAlarmsFile(File file) throws IOException {
        BufferedWriter writer = null;
        List<Alarm> tempAlarms = new ArrayList<>();
        
        try {
            while (!alarmQueue.isEmpty()) {
                Alarm alarm = alarmQueue.head();
                tempAlarms.add(alarm);
                alarmQueue.remove();
            }
            
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("BEGIN:VCALENDAR\nVERSION:2.0\n");
            for (Alarm alarm : tempAlarms) {
                writer.write(alarm.toICalendar());
            }
            writer.write("END:VCALENDAR\n");
        } catch (IOException | QueueUnderflowException e) {
            e.printStackTrace();
            throw new IOException("Error while saving Alarm to file.", e);
        }finally {
            if (writer != null) {
                try {
                    writer.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            for (Alarm alarm : tempAlarms) {
                try {
                    alarmQueue.add(alarm, alarm.getHours() * 3600 + alarm.getMinutes() * 60 + alarm.getSeconds());
                }catch (QueueOverflowException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Loads the alarms from a file in iCalendar format.
     * 
     * @param file the file to load the alarms from
     * @throws IOException if an I/O error occurs
     */
    public void loadAlarmsFromFile(File file) throws IOException {
        alarmQueue = new UnsortedArrayPriorityQueue<>(10);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            Alarm alarm = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("BEGIN:VEVENT")) {
                    alarm = new Alarm(0, 0, 0, "");
                } else if (line.startsWith("SUMMARY:")) {
                    if (alarm != null) alarm.setLabel(line.substring(8));
                } else if (line.startsWith("DTSTART:")) {
                    if (alarm != null) {
                        String time = line.substring(8);
                        int hours = Integer.parseInt(time.substring(0,2));
                        int minutes = Integer.parseInt(time.substring(2, 4));
                        int seconds = Integer.parseInt(time.substring(4, 6));
                        alarm.setHours(hours);
                        alarm.setMinutes(minutes);
                        alarm.setSeconds(seconds);
                    }
                }else if (line.startsWith("END:VEVENT")) {
                    if (alarm != null) addAlarm(alarm);
                }
            }
        }
    }
}