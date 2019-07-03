package strategy;

import java.util.List;
import ladders.Ladder;
import monkeys.Monkey;

/**
 * This is the third strategy. The empty ladder will be chosen.
 * If there is no such ladder, return null and the monkey waits for
 * a proper ladder.
 */
public class StrategyThree implements Strategy {

  @Override
  public synchronized Ladder getLadder(List<Ladder> ladders, Monkey monkey) {
    for (Ladder ladder : ladders) {
      if (ladder.isNoMonkey()) {
        return ladder;
      }
    }
    return null;
  }
  
}
