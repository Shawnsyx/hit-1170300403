package strategy;

import static run.Client.*;

import java.util.List;
import ladders.Ladder;
import monkeys.Monkey;

/**
 * This interface is for different strategies.
 */
public interface Strategy {
  public Ladder getLadder(List<Ladder> ladders, Monkey monkey);
}
