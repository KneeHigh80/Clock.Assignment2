package clock;

import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Calendar;

/**
 * The Controller class connects the Model and the View, managing user interactions
 * and updating the model and view accordingly.
 */
public class Controller {
    
    private ActionListener listener;
    private Timer timer;
    
    private Model model;
    private View view;
    
    /**
     * Constructs a Controller with the given model and view.
     * 
     * @param m the model to use
     * @param v the view to use
     */
    public Controller(Model m, View v) {
        model = m;
        view = v;
        
        listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.update();
                checkAlarms();
            }
        };
        
        timer = new Timer(1000, listener);
        timer.start();
        
        initializeMenuActions();
    }
    
    /**
     * Initializes the actions for the menu items and buttons in the view.
     */
    private void initializeMenuActions() {
        view.getSetAlarmMenuItem().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showSetAlarmDialog(null);
            }
        });

        view.getSetAlarmButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showSetAlarmDialog(null);
            }
        });

        view.getEditAlarmMenuItem().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEditAlarmDialog();
            }
        });

        view.getEditAlarmButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEditAlarmDialog();
            }
        });

        view.getDeleteAlarmMenuItem().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDeleteAlarmDialog();
            }
        });

        view.getDeleteAlarmButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDeleteAlarmDialog();
            }
        });
        
        view.getSaveAlarmsMenuItem().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAlarmsToFile();
            }
        });

        view.getLoadAlarmsMenuItem().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAlarmsFromFile();
            }
        });
    }
    
    /**
     * Shows the dialog to set or edit an alarm.
     * 
     * @param alarm the alarm to edit, or null to set a new alarm
     */
    private void showSetAlarmDialog(Alarm alarm) {
        SetAlarmDialog dialog = new SetAlarmDialog(null, alarm);
        dialog.setVisible(true);
        Alarm result = dialog.getAlarm();
        if (result != null) {
            if (alarm == null) { // Add new alarm
                model.addAlarm(result);
            } else { // Update existing alarm
                model.updateAlarm(result);
            }
        }
    }
    
    /**
     * Shows the dialog to edit an existing alarm.
     */
    private void showEditAlarmDialog() {
        Alarm selectedAlarm = selectAlarm();
        if (selectedAlarm != null) {
            showSetAlarmDialog(selectedAlarm);
        }
    }
    
    /**
     * Shows the dialog to delete an existing alarm.
     */
    private void showDeleteAlarmDialog() {
        Alarm selectedAlarm = selectAlarm();
        if (selectedAlarm != null) {
            model.removeAlarm(selectedAlarm);
        }
    }
    
    /**
     * Selects an alarm from the list of alarms.
     * 
     * @return the selected alarm, or null if no alarm is selected
     */
    private Alarm selectAlarm() {
        // You can implement a dialog to select an alarm from the list.
        // For simplicity, let's assume you have a method to get the first alarm.
        return model.getNextAlarm();
    }
    
    /**
     * Checks if any alarms need to be triggered.
     */
    private void checkAlarms() {
        Alarm nextAlarm = model.getNextAlarm();
        Calendar now = Calendar.getInstance();
        if (nextAlarm != null && model.isTimeToTriggerAlarm(nextAlarm, now)) {
            triggerAlarm(nextAlarm);
            model.removeAlarm(nextAlarm);
        }
    }
    
    /**
     * Triggers the given alarm, displaying a popup window.
     * 
     * @param alarm the alarm to trigger
     */
    private void triggerAlarm(Alarm alarm) {
        JOptionPane.showMessageDialog(null, "Alarm: " + alarm.getLabel(), "Alarm", JOptionPane.INFORMATION_MESSAGE);
        
        JFrame alarmFrame = new JFrame("Alarm triggered!");
        JLabel label = new JLabel("Alarm: " + alarm.getLabel() + " has gone off!", SwingConstants.CENTER);
        alarmFrame.add(label);
        alarmFrame.setSize(300, 100);
        alarmFrame.setLocationRelativeTo(null);
        alarmFrame.setVisible(true);
    }
    
    /**
     * Saves the alarms to a file.
     */
    private void saveAlarmsToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(view.getFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                model.saveAlarmsFile(file);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Loads the alarms from a file.
     */
    public void loadAlarmsFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(view.getFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                model.loadAlarmsFromFile(file);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
