package monkeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import monkeys.Monkey.Direction;
import static run.Client.*;

/**
 * Produce many monkeys.
 */
public class MonkeyGenerator {

  /**
   * Constructor.
   */
  public MonkeyGenerator() {
    
  }
  
  /**
   * Give birth to monkey and start the thread.
   * @param totalNumber - the total number of monkeys that will be produced.
   * @param cnt - the count number. All the monkeys' ID are greater than this cnt.
   * @param maxVelocity - the maximum velocity.
   */
  public synchronized void monkeyBorn(int totalNumber, final int cnt, int maxVelocity){
    int count = cnt;
    for (int i = 0;i < totalNumber; i++) {
      Monkey m = new Monkey(count++, randomDirection(), randomVelocity(maxVelocity));
      monkeys.add(m);
      new Thread(m).start();  //Start the thread.
    }
    
  }
  
  /**
   * Produce a random Direction. Use ran.nextInt(bound) to produce a number ranging
   * from 0 to 1, that is [0,1]. If the number is 0, return the Direction "LtoR".
   * Else the Direction is "RtoL"
   * @return Direction - random direction.
   */
  private Direction randomDirection() {
    Random ran = new Random();
    int dirNumber = ran.nextInt(2);  // Random number for [0,1]
    if (dirNumber == 0) {
      return Direction.LtoR; //If the number is 0, the direction is "LtoR".
    }
    return Direction.RtoL;  // Else, the direction is "RtoL"
  }
  
  /**
   * Produce velocity ranging from [1,maxVelocity].
   * @param maxVelocity - the maximum velocity that given by the file.
   * @return random velocity
   */
  private int randomVelocity(int maxVelocity) {
    Random ran = new Random();
    int velocity = ran.nextInt(maxVelocity) + 1;
    return velocity;
  }
}
