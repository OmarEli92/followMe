package modelTests;

import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.util.ObjectGenerator;
import it.unicam.cs.followme.util.SuppliersForGeneration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;

public class ObjectGeneratorTest {
    ObjectGenerator gen = new ObjectGenerator();
    SuppliersForGeneration suppliers = new SuppliersForGeneration();

    @Test
    void robotGeneration(){
        Map<Robot, Point> robots = gen.generateRobots(suppliers.getRobot(),suppliers.getPosition(),300);
        assertFalse(robots.isEmpty());
        assertEquals(300, robots.size());
    }

    @Test
    void CircleGeneration(){
        Set<Circle> circles = gen.generateCircles(suppliers.getCircle(),600);
        assertFalse(circles.isEmpty());
        assertTrue(circles.size() == 600);
    }

    @Test
    void RectangleGeneration(){
        Set<Rectangle> rectangles = gen.generateRectangles(suppliers.getRectangle(),600);
        assertFalse(rectangles.isEmpty());
        assertTrue(rectangles.size() == 600);
    }

}
