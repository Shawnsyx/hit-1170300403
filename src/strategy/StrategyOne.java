package strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ladders.Ladder;
import monkeys.Monkey;
import monkeys.Monkey.Direction;

/**
 * This is the first Strategy. 
 * In this strategy, a Ladder without on monkeys on it will be chosen. If all the 
 * Ladders are occupied by monkeys, then a Ladder with same direction as monkey's has 
 * higher priority, that is , this Ladder will be chosen. 
 * If there are lots of Ladders that meets the requirement, then choose one among them
 * randomly.
 */
public class StrategyOne implements Strategy {

  @Override
  public synchronized Ladder getLadder(List<Ladder> ladders, Monkey monkey) {
    /*Choose a ladder that is not occupied by monkeys.*/
    for (Ladder l : ladders) {
      if (l.isNoMonkey()) {
        return l;
      }
    }
    /*There is no empty Ladder. Choose a ladder that is in the same direction*/
    Direction monkeyDirection = monkey.getDirection();
    for (int i = 0;i < ladders.size(); i++) {
      Ladder l = ladders.get(i);
      if (l.getDirection() == monkeyDirection && l.getDirection() == Direction.RtoL
          && l.getRungLtoR(20)) {
        return l;
      } else if (l.getDirection() == monkeyDirection && l.getDirection() == Direction.LtoR
          && l.getRungLtoR(1)) {
        return l;
      }
    }
       
    return null;
  }

}
