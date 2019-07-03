package ladders;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import monkeys.Monkey;
import monkeys.Monkey.Direction;

/**
 * This is a mutable class. 
 * a Ladder is a list of Rungs and different Ladder has different index number.
 */
public class Ladder {
  private final int ladderIndex;
  private final Map<Integer, Monkey> monkeysOnLadder; // document the position.
  private final Set<Monkey> monkeys; //All the monkeys on the Ladder.
  private Direction direction; // The direction of Monkeys which are on this Ladder.
  private int minSpeed;
  
  /*
   * Abstraction Function:
   * AF(Ladder) = a Ladder has unique index number and is a list of Rungs.
   * AF(rungs) = a list of Rungs which are on this Ladder. 
   * AF(monkeys) = a set of monkeys that are on this Ladder.
   * Representation Invariant:
   * RI(ladderIndex) = a positive integer. 
   * RI(rungs) = the size of rungs, according to the manual, it is 20.
   * RI(direction) = null | "L->R" | "R->L".
   * RI(minSpeed) = an integer >= 0.
   * Safety from Exposure: 
   * 1. All the fields are private and used with "final" to guarantee the safety. 
   * 2. Only observer methods are used in this ADT.
   * 3. Defensive copy: use Collection.unmodifiable to guarantee the safety.
   * 
   * Thread Safety: 
   * Use synchronized key word to guarantee the operation are atomic. Method
   * about mutators and observers will be called atomically and risk are lowered
   * in this way.
   */

  /**
   * Constructor.
   * 
   * @param index - the ladderIndex of this Ladder.
   */
  public Ladder(int index) {
    assert index > 0;
    this.monkeysOnLadder = new HashMap<Integer, Monkey>();
    this.monkeys = new HashSet<Monkey>();
    this.direction = null;
    this.minSpeed = Integer.MAX_VALUE;  // The latter speed should be less than this number.
    /*
     * The length of the ladder is 20. h -> 20
     */
    for (int i = 0; i < 20; i++) {
      monkeysOnLadder.put(i + 1, null);
    }
    this.ladderIndex = index;
  }

  /**
   * Get the Rung with the index. The order is from the left shore to the right shore.
   * @param index - the index of the Rung. The order is from the left to the right.
   * @return true - if the Rung with this index is empty, that is, no Monkey on it.
   */
  public synchronized boolean getRungLtoR(int index) {
    if (index > 20 || index < 1) {
      System.out.println("The index is out of boundry. Index:" + index);
      return false;
    }
    
    if (monkeysOnLadder.get(index)==null) {
      return true;
    }
    return false;
  }

  /**
   * Get the Rung with the index. The order is from the right shore to the left shore.
   * @param index - the index of the Rung. The order is from the right to the left.
   * @return true - if the Rung with this index is empty, that is, no Monkey on it.
   */
  public synchronized boolean getRungRtoL(int index) {
    if (index > 20 || index < 1) {
      System.out.println("The index is out of boundry. Index:" + index);
      return false;
    }
    index = 20 - index; //the index starts from 0, so this is the transition formula.
    if (monkeysOnLadder.get(index) != null) {
      return false;
    }
    return true;
  }
  
  /**
   * Get the length of this Ladder, that is, how many Rungs are on the Ladder.
   * @return the number of Rungs on the Ladder.
   */
  public synchronized int getRungLength() {
    return monkeysOnLadder.keySet().size();
  }
  
  /**
   * Get the index of this Ladder.
   * @return the ladderIndex.
   */
  public synchronized int getLadderIndex() {
    return ladderIndex;
  }
  
  /**
   * Set the minimum speed of this Ladder.
   * @param speed - a positive integer, which is 
   */
  public synchronized void setMinSpeed(int speed) {
    assert speed >= 0;
    this.minSpeed = speed;
  }
  
  /**
   * Get the minimum speed among the Monkeys which are on this Ladder.
   * @return - minimum speed.
   */
  public synchronized int getMinSpeed() {
    return this.minSpeed;
  }
  
  /**
   * Set the direction of this Ladder. 
   * The direction is null - no Monkey on this Ladder.
   * If the direction is not null, then it must be "L->R" or "R->L".
   * @param inputDirection - the direction of this Ladder.
   */
  public synchronized void setDirection(Direction inputDirection) {
      this.direction = inputDirection;
  }
  
  /**
   * Get the Monkey movement direction of this Ladder.
   * @return direction - the Monkey's direction
   */
  public synchronized Direction getDirection() {
    return this.direction;
  }
  
  /**
   * Make the direction is null again.
   */
  public synchronized void resetDirection() {
    this.direction = null;
  }
  
  /**
   * A monkey jumped on this Ladder. Renew the minimum speed of this Ladder and check
   * the direction of the monkey's movement.
   * @param monkey - the monkey that will jump on this Ladder.
   * @return true - if jumps on successfully.
   */
  public synchronized boolean jumpOn(Monkey monkey) {
    if (this.direction == null) {
      setDirection(monkey.getDirection());
    } // No direction, set it as the monkey's.
    if (monkey.getDirection() == Direction.LtoR && monkeysOnLadder.get(1) == null) {
      monkeys.add(monkey);
      renewSpeed();
      monkeysOnLadder.put(1, monkey);
      return true;
    } else if (monkey.getDirection() == Direction.RtoL && monkeysOnLadder.get(20) == null) {
      monkeys.add(monkey);
      renewSpeed();
      monkeysOnLadder.put(20, monkey);
      return true;
    }
    return false;
  }
  
  /**
   * Monkey jump off this Ladder.
   * @param monkey - jump off the ladder.
   */
  public synchronized void jumpOff(Monkey monkey) {
    monkeys.remove(monkey);
    renewSpeed();
    int position = currentPosition(monkey); //Get the position before a monkey jumps off.
    monkeysOnLadder.replace(position, null);
    if (isNoMonkey()) {
      resetDirection();
    } // If there is no monkey, the Ladder's Direction is null.
  }
  
  /**
   * Change the position of this monkey. According to its speed.
   * @param monkey - the Monkey that moves on this Ladder.
   * @param position - the destination Rung.
   */
  public synchronized boolean monkeyMove(Monkey monkey, int position) {
    if (position < 1 || position > 20) {
      return false;
    }
    int curPosition = currentPosition(monkey); // Get the current Position and remove the monkey.
    if (monkeysOnLadder.get(position) == null) {
      monkeysOnLadder.put(position, monkey); //put the monkey on this Rung(which is replaced by an integer).  
      monkeysOnLadder.replace(curPosition, null); // The position now is cleared. No monkey on it.    
      return true;
    }
    return false;
  }
  
  /**
   * Get the current position(the index of the Rung that the monkey is on) of this monkey.
   * @param monkey - whose position is asked.
   * @return 0 - if the monkey is not on the Ladder.
   *         currentPosition - if the monkey is on the Ladder and return the index of the
   *         Rung that the monkey is on.
   */
  public synchronized int currentPosition(Monkey monkey) {
    int currentPosition = 0;
    int ladderLength = getRungLength();
    for (int i = 1;i <= ladderLength; i++) {  //Search the index
      if (monkeysOnLadder.get(i) != null && monkeysOnLadder.get(i).equals(monkey)) {
        currentPosition = i;
        //break;
      }
    }
    return currentPosition;
  }
  
  /**
   * Renew the minimum speed of this Ladder.This method will be used
   * when a monkey jumped on or off this Ladder.
   */
  private synchronized void renewSpeed() {
    this.minSpeed = Integer.MAX_VALUE;
    for(Monkey m :monkeys) {
      if (m.getVelocity() <= minSpeed) {
        minSpeed = m.getVelocity();
      }
    }
  }
  
  /**
   * Get the Map of monkeysOnLadder. 
   * @return monkeysOnLadder - the Map. 
   */
  public synchronized Map<Integer, Monkey> getMonkeyCondition(){
    /*Defensive copy in case that the field is changed by the client.*/
    return Collections.unmodifiableMap(monkeysOnLadder);
  }
  /**
   * Check if there is any monkey on this Ladder.
   * @return true - if there is no monkey on this Ladder. False otherwise.
   */
  public synchronized boolean isNoMonkey() {
    if (monkeys.isEmpty()) {
      return true;
    }
    return false;
  }
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Ladder)) {
      return false;
    }
    Ladder ladder = (Ladder) obj;
    return ladder.ladderIndex == this.ladderIndex;
  }
  
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + ladderIndex;
    result = 31 * result + 20;
    return result;
  }
}
