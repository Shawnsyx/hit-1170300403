package strategy;

import java.util.List;
import ladders.Ladder;
import monkeys.Monkey;
import monkeys.Monkey.Direction;

/**
 * This is the second strategy. According to this strategy, a Ladder that
 * has the fastest monkey movement will be chosen. By fastest movement, we mean
 * that if all the monkeys' direction(Ladder's direction as well) is the same as 
 * this monkey's, monkeys' minimum velocity is biggest.
 */
public class StrategyTwo implements Strategy{

  @Override
  public synchronized Ladder getLadder(List<Ladder> ladders, Monkey monkey) {
    int minSpeed = Integer.MIN_VALUE;
    int minIndex = -1; // The ladder index in the list ladders.
    Direction monkeyDirection = monkey.getDirection();
    for (int i = 0;i < ladders.size(); i++) {
      Ladder ladder = ladders.get(i);
      /*Same direction and the faster speed.*/
      if (((ladder.getDirection() == monkeyDirection) && ladder.getMinSpeed() >= minSpeed)
          || ((ladder.getDirection() == null) && ladder.getMinSpeed() >= minSpeed)) {
        minSpeed = ladder.getMinSpeed();
        minIndex = i;
      }
    }
    /*The speed is changed, and the best Ladder will be chosen.*/
    if (minIndex >= 0) {
      return ladders.get(minIndex);
    }
    return null;
  }

}
