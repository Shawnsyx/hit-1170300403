package laddertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static run.Client.*;
import java.util.Iterator;
import ladders.Ladder;
import monkeys.Monkey;
import monkeys.Monkey.Direction;
import org.junit.Test;

 

/**
 * This is the test class for Ladder.
 */
public class LadderTest {
  private Ladder l1 = new Ladder(1);
  
  /*
   * Testing Strategy: Test different methods and other boundary situation.
   * Partition: 1. Test getRung(L and R)
   *            2. Test the ladderIndex and the length of the Ladder.
   */
  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    /*This is incorrect because the index is -1, which is no greater than 0.*/
    Ladder l2 = new Ladder(-1);
    System.out.println(l2.getRungLength());
  }
  
  @Test 
  public void lengthTest() {
    /* To test if the length is 20 */
    assertEquals("expected the length 20", 20, l1.getRungLength());
  }
  
  @Test
  public void ladderEqualTest() {
    /*
     * If the index of these two Ladders are the same, then they are the same.
     */
    Ladder ladder = new Ladder(1); 
    assertTrue(l1.equals(ladder));
    assertEquals("expected the index is 1", 1, l1.getLadderIndex());
  }
  
  @Test
  public void direcTest() {
    assertEquals("expected null", null, l1.getDirection());
    l1.setDirection(Direction.LtoR);
    assertEquals("expected L->R", Direction.LtoR, l1.getDirection());
    l1.resetDirection();
    assertEquals("expected null", null, l1.getDirection());
  }
  
  @Test(expected = AssertionError.class)
  public void minSpeedTest1() {
    /*
     * The speed is supposed to be a positive integer, so an AssertionError is expected.
     */
    l1.setMinSpeed(-1); 
  }
  
  @Test
  public void minSpeedTest2() {
    assertEquals("expected 2147483647", 2147483647, l1.getMinSpeed());
    l1.setMinSpeed(3);
    assertEquals("expected 3", 3, l1.getMinSpeed());
    l1.setMinSpeed(0);
    assertEquals("expected 0", 0, l1.getMinSpeed());
  }
  
  @Test
  public void getRungTest() {
    assertTrue(l1.getRungLtoR(1));
    assertTrue(l1.getRungLtoR(10));
    assertTrue(l1.getRungLtoR(20));
    assertTrue(l1.getRungRtoL(1));
    assertTrue(l1.getRungRtoL(10));
    assertTrue(l1.getRungRtoL(20));
    assertFalse(l1.getRungLtoR(0));
    assertFalse(l1.getRungLtoR(21));
    assertFalse(l1.getRungRtoL(21));
    assertFalse(l1.getRungRtoL(0));
  }
  
  
  @Test
  public void jumpOnTest1() {
    Monkey m1 = new Monkey(1, Direction.LtoR, 2);
    Monkey m2 = new Monkey(2, Direction.LtoR, 3);
    Monkey m3 = new Monkey(3, Direction.LtoR, 1);
    l1.jumpOn(m1);
    l1.monkeyMove(m1, 3);
    l1.jumpOn(m2);
    assertEquals("expected speed is 2", 2,l1.getMinSpeed());
    l1.jumpOff(m1);
    assertEquals("expected speed is 3", 3,l1.getMinSpeed());
    l1.jumpOn(m3);
    /*Because m2 is on the ladder 1, so m3 can't jump on this ladder unless m2 moves on.*/
    assertEquals("expected speed is 3", 3,l1.getMinSpeed());
    l1.monkeyMove(m2, 5);
    l1.jumpOn(m3);
    /*Now m3 successfully jumps on this Ladder, the minSpeed is 1 now.*/
    assertEquals("expected speed is 1", 1,l1.getMinSpeed());
  }
  
  @Test
  public void jumpOnTest2() {
    Monkey monkey = new Monkey(2, Direction.RtoL, 4);
    Ladder ladder = new Ladder(3);
    ladder.jumpOn(monkey);
    assertEquals("expected direction rtol", Direction.RtoL, ladder.getDirection());
    ladder.jumpOff(monkey);
    assertEquals("expected direction null", null, ladder.getDirection());
    
  }
  
  public void test() {
    Iterator<Ladder> it = manyLadders.iterator();
    while(it.hasNext()) {
      System.out.println(it.next().getLadderIndex());
    }
  }
}
