package clock;

import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Controller {
    
    private ActionListener listener;
    private Timer timer;
    
    private Model model;
    private View view;
    
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
    }
    
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

    private void showEditAlarmDialog() {
        Alarm selectedAlarm = selectAlarm();
        if (selectedAlarm != null) {
            showSetAlarmDialog(selectedAlarm);
        }
    }

    private void showDeleteAlarmDialog() {
        Alarm selectedAlarm = selectAlarm();
        if (selectedAlarm != null) {
            model.removeAlarm(selectedAlarm);
        }
    }

    private Alarm selectAlarm() {
        // You can implement a dialog to select an alarm from the list.
        // For simplicity, let's assume you have a method to get the first alarm.
        return model.getNextAlarm();
    }
    
    private void checkAlarms() {
        Alarm nextAlarm = model.getNextAlarm();
        if (nextAlarm != null && model.isTimeToTriggerAlarm(nextAlarm)) {
            triggerAlarm(nextAlarm);
            model.removeAlarm(nextAlarm);
        }
    }
    
    private void triggerAlarm(Alarm alarm) {
        JOptionPane.showMessageDialog(null, "Alarm: " + alarm.getLabel(), "Alarm", JOptionPane.INFORMATION_MESSAGE);
    }
}
