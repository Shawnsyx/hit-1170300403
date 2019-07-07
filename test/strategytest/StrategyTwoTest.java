package strategytest;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ladders.Ladder;
import monkeys.Monkey;
import monkeys.Monkey.Direction;
import strategy.StrategyOne;
import strategy.StrategyTwo;

public class StrategyTwoTest {
  private Ladder l1 = new Ladder(1);
  private Ladder l2 = new Ladder(2);
  private Ladder l3 = new Ladder(3);
  
  @Test
  public void ladderTest1() {
    Monkey m1 = new Monkey(1, Direction.LtoR, 3);
    Monkey m3 = new Monkey(3, Direction.RtoL, 2);
    Monkey m4 = new Monkey(4, Direction.RtoL, 5);
    l1.jumpOn(m1);
    l1.setDirection(Direction.LtoR);
    l3.jumpOn(m3);
    l3.setDirection(Direction.RtoL);
    /*m3.v < m1.v but m4's direction is the same as m3, so l3 will be chosen*/
    List<Ladder> inputLadders = new ArrayList<Ladder>();
    inputLadders.add(l1);
    inputLadders.add(l3);
    Ladder m4Ladder = new StrategyTwo().getLadder(inputLadders, m4);
    assertEquals("expected l3", l3, m4Ladder);
  }
  
  @Test
  public void ladderTest2() {
    Monkey m1 = new Monkey(1, Direction.LtoR, 3);
    Monkey m2 = new Monkey(2, Direction.RtoL, 4);
    Monkey m3 = new Monkey(3, Direction.RtoL, 2);
    Monkey m4 = new Monkey(4, Direction.RtoL, 5);
    l2.jumpOn(m2);
    l2.setDirection(Direction.RtoL);
    List<Ladder> inputLadders = new ArrayList<Ladder>();
    inputLadders.add(l1);
    inputLadders.add(l2);
    inputLadders.add(l3);
    Ladder m4Ladder = new StrategyTwo().getLadder(inputLadders, m4);
    assertEquals("expected l2", l2, m4Ladder);
  }
}
