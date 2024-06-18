package clock;

import java.awt.*;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;

public class View implements Observer {
    
    private ClockPanel panel;
    private JFrame frame;
    
    private JMenuItem setAlarmMenuItem;
    private JMenuItem editAlarmMenuItem;
    private JMenuItem deleteAlarmMenuItem;
    private JMenuItem saveAlarmsMenuItem;
    private JMenuItem loadAlarmsMenuItem;
    
    private JButton setAlarmButton;
    private JButton editAlarmButton;
    private JButton deleteAlarmButton;
    
    
    public View(Model model) {
        frame = new JFrame();
        panel = new ClockPanel(model);
        //frame.setContentPane(panel);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Start of border layout code
        
        // I've just put a single button in each of the border positions:
        // PAGE_START (i.e. top), PAGE_END (bottom), LINE_START (left) and
        // LINE_END (right). You can omit any of these, or replace the button
        // with something else like a label or a menu bar. Or maybe you can
        // figure out how to pack more than one thing into one of those
        // positions. This is the very simplest border layout possible, just
        // to help you get started.
        
        Container pane = frame.getContentPane();
        
        // creating the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu alarmMenu = new JMenu("Alarms");
        
        setAlarmMenuItem = new JMenuItem("Set Alarm");
        editAlarmMenuItem = new JMenuItem("Edit Alarm");
        deleteAlarmMenuItem = new JMenuItem("Delete Alarm");
        saveAlarmsMenuItem = new JMenuItem("Save Alarms");
        loadAlarmsMenuItem = new JMenuItem("Load Alarms");
        
        alarmMenu.add(setAlarmMenuItem);
        alarmMenu.add(editAlarmMenuItem);
        alarmMenu.add(deleteAlarmMenuItem);
        alarmMenu.add(saveAlarmsMenuItem);
        alarmMenu.add(loadAlarmsMenuItem);
        
        menuBar.add(alarmMenu);
        
        frame.setJMenuBar(menuBar);
        
        
        // Adding Buttons for setting Alarms
        setAlarmButton = new JButton("Set Alarm");
        editAlarmButton = new JButton("Edit Alarm");
        deleteAlarmButton = new JButton("Delete Alarm");
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(setAlarmButton);
        buttonPanel.add(editAlarmButton);
        buttonPanel.add(deleteAlarmButton);
        
        pane.add(buttonPanel, BorderLayout.PAGE_START);
        
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
        
        // End of borderlayout code
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
    
    // Getters for MenuItems
    
    public JMenuItem getSetAlarmMenuItem() {
        return setAlarmMenuItem;
    }

    public JMenuItem getEditAlarmMenuItem() {
        return editAlarmMenuItem;
    }

    public JMenuItem getDeleteAlarmMenuItem() {
        return deleteAlarmMenuItem;
    }

    public JMenuItem getSaveAlarmsMenuItem() {
        return saveAlarmsMenuItem;
    }

    public JMenuItem getLoadAlarmsMenuItem() {
        return loadAlarmsMenuItem;
    }
    
    //getters for Buttons

    public JButton getSetAlarmButton() {
        return setAlarmButton;
    }

    public JButton getEditAlarmButton() {
        return editAlarmButton;
    }

    public JButton getDeleteAlarmButton() {
        return deleteAlarmButton;
    }
    
    // getter for the frame
    public JFrame getFrame() {
        return frame;
    }
    
}
