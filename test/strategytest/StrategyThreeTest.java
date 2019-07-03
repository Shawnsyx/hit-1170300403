package strategytest;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ladders.Ladder;
import monkeys.Monkey;
import monkeys.Monkey.Direction;
import strategy.StrategyThree;

public class StrategyThreeTest {
  List<Ladder> ladders = new ArrayList<Ladder>();
  
  public void initialize() {
    ladders.clear();
    Ladder l1 = new Ladder(1);
    Ladder l2 = new Ladder(2);
    Ladder l3 = new Ladder(3);
    Ladder l4 = new Ladder(4);
    ladders.add(l1);
    ladders.add(l2);
    ladders.add(l3);
    ladders.add(l4);
  }
  
  @Test
  public void test1() {
    Ladder l1 = new Ladder(1);
    Ladder l2 = new Ladder(2);
    Ladder l3 = new Ladder(3);
    ladders.add(l1);
    ladders.add(l2);
    ladders.add(l3);
    Monkey m1 = new Monkey(1, Direction.LtoR, 3);
    Monkey m2 = new Monkey(2, Direction.RtoL, 4);
    Monkey m3 = new Monkey(3, Direction.RtoL, 2);
    l1.jumpOn(m1);
    l2.jumpOn(m2);
    /*Only l3 is empty*/
    Ladder m3Ladder = new StrategyThree().getLadder(ladders, m3);
    assertEquals("expected ladder l3", l3,m3Ladder);
  }
  
  @Test
  public void test2() {
    ladders.clear();
    Ladder l1 = new Ladder(1);
    Ladder l2 = new Ladder(2);
    Ladder l3 = new Ladder(3);
    ladders.add(l1);
    ladders.add(l2);
    ladders.add(l3);
    Monkey m1 = new Monkey(1, Direction.LtoR, 3);
    Monkey m2 = new Monkey(2, Direction.RtoL, 4);
    Monkey m3 = new Monkey(3, Direction.RtoL, 2);
    Monkey m4 = new Monkey(4, Direction.RtoL, 5);
    l1.jumpOn(m1);
    l2.jumpOn(m2);
    l3.jumpOn(m4);
    /*All the Ladders are occupied. So null is expected*/
    Ladder m3Ladder = new StrategyThree().getLadder(ladders, m3);
    assertEquals("expected null", null,m3Ladder);
  }
}
