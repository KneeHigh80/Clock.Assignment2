
package clock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author lukas
 */
public class SetAlarmDialog extends JDialog {
    private JTextField hourField;
    private JTextField minuteField;
    private JTextField secondField;
    private JTextField labelField;
    private JButton setButton;
    private Alarm alarm;

    public SetAlarmDialog(JFrame parent, Alarm alarm) {
        super(parent,(alarm == null ? "Set Alarm" : "Edit Alarm"), true);
        this.alarm = alarm;
        setLayout(new GridLayout(5, 2));
        
        add(new JLabel("Hour:"));
        hourField = new JTextField(alarm != null ? String.valueOf(alarm.getHours()) : "");
        add(hourField);
        
        add(new JLabel("Minute:"));
        minuteField = new JTextField(alarm != null ? String.valueOf(alarm.getMinutes()) : "");
        add(minuteField);
        
        add(new JLabel("Second:"));
        secondField = new JTextField(alarm != null ? String.valueOf(alarm.getSeconds()) : "");
        add(secondField);
        
        add(new JLabel("Label:"));
        labelField = new JTextField(alarm != null ? alarm.getLabel() : "");
        add(labelField);
        
        setButton = new JButton(alarm != null ? "Edit Alarm" : "Set Alarm");
        add(setButton);
        setButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int hours = Integer.parseInt(hourField.getText());
                    int minutes = Integer.parseInt(minuteField.getText());
                    int seconds = Integer.parseInt(secondField.getText());
                    String label = labelField.getText();
                    if (SetAlarmDialog.this.alarm == null) {
                        SetAlarmDialog.this.alarm = new Alarm(hours, minutes, seconds, label);
                    } else {
                        SetAlarmDialog.this.alarm.setHours(hours);
                        SetAlarmDialog.this.alarm.setMinutes(minutes);
                        SetAlarmDialog.this.alarm.setSeconds(seconds);
                        SetAlarmDialog.this.alarm.setLabel(label);
                    }
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SetAlarmDialog.this, "Please enter valid numbers for time.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        setSize(300, 200);
        setLocationRelativeTo(parent);
    }
    
    public Alarm getAlarm() {
        return alarm;
    }
}

