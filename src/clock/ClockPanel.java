package clock;

import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import javax.swing.*;


public class ClockPanel extends JPanel {
    
    private Model model;
    
    public ClockPanel(Model m) {
        model = m;
        setPreferredSize(new Dimension(200, 200));
        setBackground(Color.white);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Rectangle bounds = getBounds();
        
        
        Graphics2D gg = (Graphics2D) g;
        int x0 = bounds.width / 2;
        int y0 = bounds.height / 2;
        
        int size = Math.min(x0, y0);
        
        
        gg.setStroke(new BasicStroke(1));
        
       
        
        double radius = 0;
        double theta = 0;
        
        // Draw the tick marks around the outside
        for (int n = 0; n < 60; n++) {
            theta = (90 - n * 6) / (180 / Math.PI);
            if (n % 5 == 0) {
                radius = 0.65 * size;
            } else {
                radius = 0.7 * size;
            }
            double x1 = x0 + radius * Math.cos(theta);
            double y1 = y0 - radius * Math.sin(theta);
            radius = 0.75 * size;
            double x2 = x0 + radius * Math.cos(theta);
            double y2 = y0 - radius * Math.sin(theta);
            gg.draw(new Line2D.Double(x1, y1, x2, y2));
        }
        
        // Draw the numbers
        
        Font font = new Font("SansSerif", Font.PLAIN, size / 5);
        gg.setFont(font);
        for (int n = 1; n <= 12; n++) {
            theta = (90 - n * 30) / (180 / Math.PI);
            radius = 0.9 * size;
            double x1 = x0 + radius * Math.cos(theta);
            double y1 = y0 - radius * Math.sin(theta);
            String s = "" + n;
            // To centre the numbers on their places, we need to get
            // the exact dimensions of the box
            FontRenderContext context = gg.getFontRenderContext();
            Rectangle2D msgbounds = font.getStringBounds(s, context);
            double ascent = -msgbounds.getY();
            double descent = msgbounds.getHeight() + msgbounds.getY();
            double height = msgbounds.getHeight();
            double width = msgbounds.getWidth();
            
            gg.drawString(s, (float) (x1 - width/2), (float)(y1 + height/2 - descent));
        }
        
        // Draw the hour hand
        gg.setStroke(new BasicStroke(2.0f));
        theta = (90 - (model.getHour() + model.getMinute() / 60.0) * 30) / (180 / Math.PI);
        radius = 0.5 * size;
        double x1 = x0 + radius * Math.cos(theta);
        double y1 = y0 - radius * Math.sin(theta);
        gg.draw(new Line2D.Double(x0, y0, x1, y1));
        
        // Draw the minute hand
        gg.setStroke(new BasicStroke(1.1f));
        theta = (90 - (model.getMinute() + model.getSecond() / 60.0) * 6) / (180 / Math.PI);
        radius = 0.75 * size;
        x1 = x0 + radius * Math.cos(theta);
        y1 = y0 - radius * Math.sin(theta);
        gg.draw(new Line2D.Double(x0, y0, x1, y1));
        
        // Draw the second hand
        gg.setColor(Color.red);
        gg.setStroke(new BasicStroke(0));
        theta = (90 - model.getSecond() * 6) / (180 / Math.PI);
        x1 = x0 + radius * Math.cos(theta);
        y1 = y0 - radius * Math.sin(theta);
        gg.draw(new Line2D.Double(x0, y0, x1, y1));
        
        // Draw an indicator for the next Alarm
        Alarm nextAlarm = model.getNextAlarm();
        if (nextAlarm != null) {
            gg.setColor(Color.blue);
            gg.setStroke(new BasicStroke(1.5f));
            //theta = (90 - ((nextAlarm.getHours() + nextAlarm.getMinutes() / 60.0) * 30)) / (180 / Math.PI);
            theta = (90 - ((nextAlarm.getHours() % 12) * 30 + nextAlarm.getMinutes() * 0.5 + nextAlarm.getSeconds() * (0.5 / 60))) / (180 / Math.PI);
            radius = 0.6 * size;
            x1 = x0 + radius * Math.cos(theta);
            y1 = y0 + radius * Math.sin(theta);
            gg.draw(new Line2D.Double(x0, y0, x1, y1));
        }
    }
}
