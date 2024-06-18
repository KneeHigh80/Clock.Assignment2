
package clock;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lukas
 */
public class AlarmTest {
    
    private Alarm alarm;
    
    @Before
    public void setUp() {
        alarm = new Alarm(7, 30, 0, "Test Alarm");
    }
    
    @Test
    public void testGetHours() {
        assertEquals(7, alarm.getHours());
    }

    
    @Test
    public void testSetHours() {
        alarm.setHours(8);
        assertEquals(8, alarm.getHours());
    }

    
    @Test
    public void testGetMinutes() {
        assertEquals(30, alarm.getMinutes());
    }

    
    @Test
    public void testSetMinutes() {
        alarm.setMinutes(30);
        assertEquals(30, alarm.getMinutes());
    }

    /**
     * Test of toICalendar method, of class Alarm.
     */
    @Test
    public void testToICalendar() {
        String expected = "BEGIN:VEVENT\n" +
                          "SUMMARY:Morning Alarm\n" +
                          "DTSTART:073000\n" +
                          "BEGIN:VALARM\n" +
                          "TRIGGER:-PT00H30M00S\n" +
                          "ACTION:DISPLAY\n" +
                          "DESCRIPTION:Morning Alarm\n" +
                          "END:VALARM\n" +
                          "END:VEVENT\n";
        assertEquals(expected, alarm.toICalendar());
    }
    
}
