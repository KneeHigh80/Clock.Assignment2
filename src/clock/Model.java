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