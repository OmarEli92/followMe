package modelTests;
import it.unicam.cs.followme.model.Circle;
import it.unicam.cs.followme.model.Point;
import it.unicam.cs.followme.model.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class ShapesTest {
    private Circle c1 = new Circle(new Point(2,2),2,"ABCD123");
    private Circle c2 = new Circle(new Point(70,21),9,"A121");
    private Circle c3 = new Circle(new Point(0,22),7,"AB123");
    private Circle c4 = new Circle(new Point(2,2),2,"ABCD123");
    private Circle c5 = new Circle(new Point(0,22),2,"E123");
    private Rectangle r1 = new Rectangle(20,12,new Point(31,14),"qqq111");
    private Rectangle r2 = new Rectangle(14,3,new Point(20,15),"der10");
    private Rectangle r3 = new Rectangle(300,90,new Point(200,200),"das10");
    private Rectangle r4 = new Rectangle(53,43,new Point(33,33),"dos10");
    private Rectangle r5 = new Rectangle(300,34,new Point(90,89),"dis10");
    private Rectangle r6 = new Rectangle(89,120,new Point(13,14),"dis10");
    private Point p1 = new Point(2,2);
    private Point p2 = new Point(8,25);
    private Point p3 = new Point(15,7);
    private Point p4 = new Point(25,9);
    private Point p5 = new Point(201,199);

    @Test
    final void CenterOfTheCircleTest(){
        assertEquals(c1.getCenter(), new Point(2, 2));
        assertEquals(c2.getCenter(), new Point(70, 21));
        assertEquals(c3.getCenter(), new Point(0, 22));


    }

    @Test
    final void CircleRadiusTest(){
        assertEquals(2, c1.getRadius());
        assertEquals(9, c2.getRadius());
        assertEquals(7, c3.getRadius());
    }

    @Test
    final void circleLabelTest() {
        assertEquals("ABCD123", c1.getCondition());
        assertEquals("A121", c2.getCondition());
        assertEquals("AB123", c3.getCondition());
    }

    @Test
    final void equalityOfTwoCircles(){
        assertEquals(c1, c4);
        assertNotEquals(c3, c5);
    }

    @Test
    final void IsPointInsideTest(){
        assertTrue(c1.isPointInside(p1));
        assertFalse(c2.isPointInside(p2));
        assertFalse(c3.isPointInside(p3));
        assertTrue(r1.isPointInside(p4));
        assertFalse(r3.isPointInside(p1));
        assertTrue(r3.isPointInside(p5));
    }

    @Test
    final void equalityOfTwoRectangles(){
        assertFalse(r1.equals(r2));
        assertFalse(r3.equals(r4));
        assertFalse(r5.equals(r6));
    }

}
