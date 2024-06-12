package clock;

import java.util.Calendar;
import java.util.Observable;
import queuemanager.PriorityQueue;
import queuemanager.UnsortedArrayPriorityQueue;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;

public class Model extends Observable {
    
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    
    private int oldSecond = 0;
    
    private PriorityQueue<Alarm> alarmQueue;
    
    public Model() {
        alarmQueue = new UnsortedArrayPriorityQueue<>(10);
        update();
    }
    
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
    
    //Getter Methods 

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
    
    // Methods for managing Alarms
    // Add an Alarm
    
    public void addAlarm(Alarm alarm) {
        try {
            alarmQueue.add(alarm, alarm.getHours() * 3600 + alarm.getMinutes() * 60 + alarm.getSeconds());
        } catch (QueueOverflowException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }
    
    // getting next alarm in queue
    public Alarm getNextAlarm() {
        try{
            return alarmQueue.head();
        } catch (QueueUnderflowException e) {
            return null;
        }
    }
    
    //updating an alarm
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
    
    // Method to trigger alarm 
    public boolean isTimeToTriggerAlarm(Alarm alarm) {
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);
        int currentSecond = now.get(Calendar.SECOND);
        
        return alarm.getHours() == currentHour &&
               alarm.getMinutes() == currentMinute &&
               alarm.getSeconds() == currentSecond;
    }
    
    public void removeAlarm (Alarm alarm) {
        try {
            alarmQueue.remove();
        } catch (QueueUnderflowException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }
}