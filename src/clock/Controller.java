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
                showSetAlarmDialog();
            }
        });
        
        view.getSetAlarmButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showSetAlarmDialog();
            }
        });
    }
    
    private void showSetAlarmDialog() {
        SetAlarmDialog dialog = new SetAlarmDialog(null);
        dialog.setVisible(true);
        Alarm alarm = dialog.getAlarm();
        if (alarm != null) {
            model.addAlarm(alarm);
        }
    }
        
    
    private void checkAlarms() {
        Alarm nextAlarm = model.getNextAlarm();
        if (nextAlarm != null && model.isTimeToTriggerAlarm(nextAlarm)) {
            triggerAlarm(nextAlarm);
            model.removeAlarm(nextAlarm);
        }
    }
    
    private void triggerAlarm(Alarm alarm) {
        JOptionPane.showMessageDialog(null, "Alarm: "+ alarm.getLabel(), "Alarm",JOptionPane.INFORMATION_MESSAGE);
    }
}