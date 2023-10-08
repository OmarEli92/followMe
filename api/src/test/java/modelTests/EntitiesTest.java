package modelTests;
import it.unicam.cs.followme.model.Point;
import it.unicam.cs.followme.model.Robot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class EntitiesTest {

    private final Robot r1 = new Robot(new Point(0.1,0.3),10);
    private final Robot r2 = new Robot(new Point(0.5,0.1),10);
    private final Robot r3 = new Robot(new Point(1,-1),10);
    private final Robot r4 = new Robot(new Point(0.9,-0.3),10);

    @Test
    final void checkDirection(){
        r1.setDirection(new Point(1,100));
        r2.setDirection(new Point(2,21));
        r3.setDirection(new Point(45,16));
        r4.setDirection(new Point(35,6));
        assertNotEquals(r1.getDirection(),r2.getDirection());
        assertNotEquals(r2.getDirection(),r3.getDirection());
        assertNotEquals(r3.getDirection(),r4.getDirection());
    }

    @Test
    final void checkCondition(){
        r1.setCondition("ABC123");
        r1.setCondition("ABC12");
        r2.setCondition("");
        r3.setCondition("");
        r3.setCondition("ABC12");
        r4.setCondition("ABC123");

        assertEquals("ABC12", r1.getCondition());
        assertEquals("", r2.getCondition());
        assertEquals("ABC12", r3.getCondition());
        assertEquals("ABC123", r4.getCondition());


    }

}
