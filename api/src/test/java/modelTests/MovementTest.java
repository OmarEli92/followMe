package modelTests;
import it.unicam.cs.followme.util.Movement;
import it.unicam.cs.followme.model.Point;
import org.junit.jupiter.api.Test;

public class MovementTest {
    private Movement mov = new Movement();

    @Test
    final void testDirection(){
        Point p1 = new Point(0,0);
        Point p2 = new Point(3,3);
        Point p3 = new Point(2,4);
        Point p4 = new Point(3,9);
        Point p5 = new Point(1,1);
        Point p6 = new Point(-4,4);
        Point dir1 = mov.getDirection(p1,p2);
        Point dir2 = mov.getDirection(p3,p4);
        Point dir3 = mov.getDirection(p4,p5);
        System.out.println(dir1.getX()+","+ dir1.getY());
        System.out.println(dir2.getX()+","+dir2.getY());
        System.out.println(dir3.getX()+ ","+ dir3.getY());
    }

}
