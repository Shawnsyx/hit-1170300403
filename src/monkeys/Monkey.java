package monkeys;

import static run.Client.manyLadders;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import ladders.Ladder;
import strategy.StrategyOne;
import strategy.StrategyThree;
import strategy.StrategyTwo;
import static run.Client.logger;
import static run.Client.*;

/**
 * This is the class Monkey, an immutable class.
 * A Monkey has ID(as name), direction and velocity.
 */
public class Monkey implements Runnable {
  private final int ID;
  private final Direction direction;  
  private final int velocity;
  private Ladder monkeyLadder;  
  private long bornTime;
  private long endTime;
  
  /*
   * Abstraction Function:
   * AF(Monkey) = a monkey with ID, moving direction and velocity.
   * AF(thisLadder) = The ladder that this monkey is on.
   * Representation Invariant:
   * RI(ID) = a positive number indicating the ID of this Monkey.
   * RI(direction) = LtoR or RtoL
   * RI(velocity) = a positive integer.
   * Safety from Exposure:
   * 1.All fields are private and use final to guarantee the immutability.
   * 2.Only observer methods and no mutators are used. 
   * 
   * Thread Safety:
   * 1. All the fields are private and final , thus won't be changed by the client.
   * 2. Use lock to guarantee the operation is atomic. 
   */
  
  /*
   * This is the an type - Direction, for Monkey and Ladder.
   */
  public static enum Direction {
    LtoR,RtoL
  }

  /**
   * Constructor.
   * @param number - the Monkey's ID number
   * @param direc - the direction of Monkey. Must be "L->R" or "R->L".
   * @param v - the velocity of the Monkey, a positive integer.
   */
  public Monkey (int number, Direction direc, int v) {
    /*CheckRep*/
    assert number > 0;
    assert v > 0;
  
    this.ID = number;
    this.direction = direc;
    this.velocity = v;
    this.bornTime = System.currentTimeMillis();
    
  }
  

  @Override
  public void run() {
    /* First Step -> Get a Ladder so that the monkey will be able to cross the river. */
    logger.info(
        "A monkey is born. " + "ID:" + ID + " Direction:" + direction + " Velocity: " + velocity);
    while (monkeyLadder == null) {
      synchronized (manyLadders) {
        // ladderDistribute();
        monkeyLadder = new StrategyOne().getLadder(manyLadders, this);
        if (monkeyLadder != null) {
          monkeyLadder.jumpOn(this);
          logger.info("Monkey: " + ID + "\tget on this Ladder:" + monkeyLadder.getLadderIndex());
        }
      } // DEBUG. When choose a Ladder, must guarantee the thread safety.
      try {
        Thread.sleep(1000); // Wait for a second until this monkey can get a Ladder.
      } catch (InterruptedException e) {
        return;
      }
    }
    /* According to direction. */
    /* Now get on this ladder */
    if (direction == Direction.LtoR) {
      leftToRight();
    } else if (direction == Direction.RtoL) {
      rightToLeft();
    } else {
      logger.info("Monkey: " + ID + "\tkeeps waiting for "
          + (System.currentTimeMillis() - bornTime) / 1000 + "s");
    }
  }
  
  
  /**
   * If the direction is L->R.
   */
  private void leftToRight() {
    /* In Ladder, keeps moving. The length is 20. */
    boolean isEnd = false;
    while (monkeyLadder.currentPosition(this) <= 20 && (!isEnd)) {
      int curPosition = monkeyLadder.currentPosition(this);
      int reachPosition = monkeyLadder.currentPosition(this) + 1;// The position can be reached.
      for (; reachPosition <= 20 && monkeyLadder.getRungLtoR(reachPosition); reachPosition++) {
      } // get the reachable position
      reachPosition--; // !!! Added. and reachPosition == 20. Or it will use god's perspective.
      if (reachPosition == 20 && (monkeyLadder.currentPosition(this) + velocity > 20)) {
        // jump off the ladder because v > h. Takes 1 second.
        logger.info(this.toString() + " cross the river after the borntime: "
            + (System.currentTimeMillis() - bornTime) / 1000 + "s");
        System.out.println(this.toString() + " cross the river after the borntime: "
            + (System.currentTimeMillis() - bornTime) / 1000 + "s");
        endTime = System.currentTimeMillis();
        monkeyLadder.jumpOff(this);
        isEnd = true;
        downLatch.countDown();
        totalNumber --;
        break;
      } else if (monkeyLadder.getRungLtoR(curPosition + 1)) { // rung after the current position is
                                                              // empty.
        if (reachPosition > (velocity + curPosition)) { // farther than it should be.
          reachPosition = velocity + curPosition; // far from the the reach.
        }
        monkeyLadder.monkeyMove(this, reachPosition);
        logger.info(this.toString() + " Ladder:" + monkeyLadder.getLadderIndex() + " get the "
            + reachPosition + "th Rung" + " after the born time: "
            + (System.currentTimeMillis() - bornTime) / 1000 + "s");
        System.out.println(this.toString() + " Ladder:" + monkeyLadder.getLadderIndex() + " get the "
            + reachPosition + "th Rung" + " after the born time: "
            + (System.currentTimeMillis() - bornTime) / 1000 + "s");
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        return;
      }

    }
    /* End for this leftToRight method */
  }
  
  /**
   * If the direction is R->L.
   */
  private void rightToLeft() {
    boolean isEnd = false;
    while (monkeyLadder.currentPosition(this) >= 1 && (!isEnd)) {
      /* If the position is in [1,20], keeps moving on. Until reach 0. */
      int curPosition = monkeyLadder.currentPosition(this);
      int reachPosition = monkeyLadder.currentPosition(this) - 1; // The position can be reached.
      for (; reachPosition >= 1 && monkeyLadder.getRungLtoR(reachPosition); reachPosition--) {
      } // get the reachable position
      reachPosition++;
      if (reachPosition == 1 && (monkeyLadder.currentPosition(this) - velocity < 1)) {
        // jump off the ladder because v > h. Takes 1 second.
        logger.info(this.toString() + " cross the river after the borntime: "
            + (System.currentTimeMillis() - bornTime) / 1000 + "s");
        System.out.println(this.toString() + " cross the river after the borntime: "
            + (System.currentTimeMillis() - bornTime) / 1000 + "s");
        endTime = System.currentTimeMillis();
        monkeyLadder.jumpOff(this);
        isEnd = true;
        downLatch.countDown();
        totalNumber --;
        break;
      } else if (monkeyLadder.getRungLtoR(curPosition - 1)) { // rung after the current position is
                                                              // empty.
        if (reachPosition < (curPosition - velocity)) { // farther than it should be.
          reachPosition = curPosition - velocity; // far from the the reach.
        }
        monkeyLadder.monkeyMove(this, reachPosition);
        logger.info(this.toString() + " Ladder:" + monkeyLadder.getLadderIndex() + " get the "
            + reachPosition + "th Rung" + " after the born time: "
            + (System.currentTimeMillis() - bornTime) / 1000 + "s");
        System.out.println(this.toString() + " Ladder:" + monkeyLadder.getLadderIndex() + " get the "
            + reachPosition + "th Rung" + " after the born time: "
            + (System.currentTimeMillis() - bornTime) / 1000 + "s");
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        return; // This can release the lock.
      }
    }
    /* End for this rightToLeft method */
    return;
  }
  /**
   * Randomly choose one strategy.
   * @return Ladder according to the random number.
   */
  public Ladder randomStrategy() {
    Random ran = new Random();
    int number = ran.nextInt(2) + 1;
    if (number == 1) {
      return new StrategyOne().getLadder(manyLadders, this);
    } else if (number == 2) {
      return new StrategyTwo().getLadder(manyLadders, this);
    }
    return new StrategyThree().getLadder(manyLadders, this);
  }
  
  /**
   * Get the velocity of this Monkey.
   * @return velocity - the velocity of this Monkey.
   */
  public int getVelocity() {
    return velocity;
  }
  
  /**
   * Get the direction of this Monkey.
   * @return direction - LtoR or RtoL. 
   */
  public synchronized Direction getDirection() {
    return direction;
  }
  
  /**
   * Get the ID of this monkey.
   * @return ID - the ID number of this monkey.
   */
  public int getID() {
    return ID;
  }
  /**
   * Get the born time of this monkey.
   * @return born time - the born time of this monkey.
   */
  public long getBornTime() {
    return bornTime;
  }
  /**
   * Get the time when the monkey cross the river successfully.
   * @return the time when the monkey cross the river successfully.
   */
  public long getEndTime() {
    return endTime;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Monkey)) {
      return false;
    }
    Monkey monkey = (Monkey) obj;
    return (monkey.direction.equals(this.direction)) && (monkey.ID == this.ID)
        && (monkey.velocity == this.velocity);
  }
   
  @Override
  public int hashCode() {
    int result = 17;
    result = result * 31 + ID;
    result = result * 31 + velocity;
    result = result + direction.hashCode();
    return result;
  }

  @Override
  public String toString() {
    String ans = "";
    ans += "Monkey ID: " + ID + " Direction:" + this.direction + " velocity: " + velocity;
    return ans;
  }

}
