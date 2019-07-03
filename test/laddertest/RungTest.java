package laddertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ladders.Rung;
import org.junit.Test;


/**
 * This is the Test for the class Rung.
 * 
 */
public class RungTest {

  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    assert false; // make sure assertions are enabled with VM argument: -ea
  }

  @Test(expected = AssertionError.class)
  public void checkRep() {
    Rung r1 = new Rung(-1);
    System.out.println(r1.getRungIndex());
  }

  @Test
  public void equalTest() {
    Rung r1 = new Rung(1);
    Rung r2 = new Rung(1);
    assertTrue(r1.equals(r2));
    Set<Rung> rungs = new HashSet<Rung>();
    rungs.add(r1);
    rungs.add(r2);
    assertEquals("the expected size is 1", rungs.size(), 1);
    Map<Rung, String> rungMap = new HashMap<Rung, String>();
    rungMap.put(r1, "this is r1");
    assertEquals("Expected equals", "this is r1",rungMap.get(new Rung(1)));
  }

  @Test
  public void getRungIndexTest() {
    Rung r1 = new Rung(2);
    assertEquals("expected 2", r1.getRungIndex(), 2);
  }
}
