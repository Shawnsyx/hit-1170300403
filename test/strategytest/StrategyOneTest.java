package strategytest;

import static org.junit.Assert.assertEquals;
import static run.Client.manyLadders;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ladders.Ladder;
import monkeys.Monkey;
import monkeys.Monkey.Direction;
import strategy.StrategyOne;
import run.Client;

public class StrategyOneTest {
  List<Ladder> ladders = new ArrayList<Ladder>();
  public void initialize() {
    Ladder l1 = new Ladder(1);
    Ladder l2 = new Ladder(2);
    Ladder l3 = new Ladder(3);
    ladders.add(l1);
    ladders.add(l2);
    ladders.add(l3);
  }
  
  /*
   * Ladder 1 -> occupied by monkey1 Dir:LtoR
   * Ladder 2 -> occupied by monkey2 Dir:RtoL
   * Ladder 3 -> empty
   */
  @Test
  public void ladderTest1() {
    initialize();
    Ladder l1 = ladders.get(0);
    Ladder l2 = ladders.get(1);
    Ladder l3 = ladders.get(2);
    Monkey m1 = new Monkey(1, Direction.LtoR, 3);
    Monkey m2 = new Monkey(2, Direction.RtoL, 4);
    Monkey m3 = new Monkey(3, Direction.RtoL, 2);
    l1.jumpOn(m1);
    l2.jumpOn(m2);
    Ladder m3Ladder = new StrategyOne().getLadder(ladders, m3);
    assertEquals("expected l3", l3, m3Ladder);
    l1.jumpOff(m1);
    l2.jumpOff(m2);
  }
  
  /*
   * choose l2 or l3.
   * Because of the same direction 
   */
  @Test
  public void ladderTest2() {
    initialize();
    Ladder l1 = ladders.get(0);
    Ladder l2 = ladders.get(1);
    Ladder l3 = ladders.get(2);
    Monkey m1 = new Monkey(1, Direction.LtoR, 3);
    Monkey m2 = new Monkey(2, Direction.RtoL, 4);
    Monkey m3 = new Monkey(3, Direction.RtoL, 2);
    Monkey m4 = new Monkey(4, Direction.RtoL, 5);
    l1.jumpOn(m1);
    l2.jumpOn(m2);
    l3.jumpOn(m4);
    l2.setDirection(Direction.RtoL);
    l3.setDirection(Direction.RtoL);
    
    Ladder m3Ladder = new StrategyOne().getLadder(ladders, m3);
    //System.out.println(m3Ladder.getLadderIndex());
    
  }
  
  @Test
  public void ladderTest3() {
    Ladder l5 = new Ladder(5);
    Ladder l6 = new Ladder(6);
    Monkey m1 = new Monkey(5, Direction.RtoL, 4);
    Monkey m2 = new Monkey(6, Direction.LtoR, 7);
    //Client.printLadders();
    Client.manyLadders.add(l5);
    Client.manyLadders.add(l6);
    //Client.printLadders();  
    /* Use the Client.printLadders to see the size of the "Client.manyLadders".
     * Check whether the operation has changed the static fields or not. */
    l5.jumpOn(m1);
    Ladder m2Ladder = new StrategyOne().getLadder(Client.manyLadders, m2);
    assertEquals("expected l6", l6,m2Ladder);
  }
}
