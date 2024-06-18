
package clock;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;


public class ModelTest {
    
    private Model model;
    private Alarm alarm;
    
        
    @Before
    public void setUp() {
        model = new Model();
        alarm = new Alarm(7, 30, 0, "Test Alarm");
    }
    
    @Test
    public void testAddAlarm() {
        model.addAlarm(alarm);
        assertEquals(alarm, model.getNextAlarm());
    }
    
    @Test
    public void testRemoveAlarm() {
        model.addAlarm(alarm);
        model.removeAlarm(alarm);
        assertNull(model.getNextAlarm());
    }
    
    @Test
    public void testIsTimeToTriggerAlarm() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 7);
        now.set(Calendar.MINUTE, 30);
        now.set(Calendar.SECOND, 0);
        
        System.out.println("Current Time: " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));
        System.out.println("Alarm Time: " + alarm.getHours() + ":" + alarm.getMinutes() + ":" + alarm.getSeconds());
        
        assertTrue(model.isTimeToTriggerAlarm(alarm, now));
    }
    
    @Test
    public void testSaveAndLoadAlarms() throws IOException {
        File file = new File("test.alarms.ics");
        model.addAlarm(alarm);
        model.saveAlarmsFile(file);
        
        Model loadedModel = new Model();
        loadedModel.loadAlarmsFromFile(file);
        assertEquals(alarm.toICalendar(), loadedModel.getNextAlarm().toICalendar());
        
        file.delete();
    }
}