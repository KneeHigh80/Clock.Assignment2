
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

    public SetAlarmDialog(JFrame parent) {
        super(parent, "Set Alarm", true);
        setLayout(new GridLayout(5, 2));
        
        add(new JLabel("Hour"));
        hourField = new JTextField();
        add(hourField);
        
        add(new JLabel("Minute"));
        minuteField = new JTextField();
        add(minuteField);
        
        add(new JLabel("Second"));
        secondField = new JTextField();
        add(secondField);
        
        add(new JLabel("Label"));
        labelField = new JTextField();
        add(labelField);
        
        setButton = new JButton("Set Alarm");
        add(setButton);
        setButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int hours = Integer.parseInt(hourField.getText());
                int minutes = Integer.parseInt(minuteField.getText());
                int seconds = Integer.parseInt(secondField.getText());
                String label = labelField.getText();
                alarm = new Alarm(hours, minutes, seconds, label);
                dispose();
            }
        });
        
        setSize(300, 200);
        setLocationRelativeTo(parent);
    }
    
    public Alarm getAlarm() {
        return alarm;
    }
}

