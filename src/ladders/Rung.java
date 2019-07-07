package ladders;


/**
 * This is an immutable type. 
 * Rungs are the basic components of a ladder. Every Rung of a ladder 
 * has its index, which is the unique integer to distinguish itself from
 * other Rungs.
 */
public class Rung {
  private final int rungIndex; //index in the order of "L -> R".
  
  /*
   * Abstraction Function:
   * AF(Rung) = A Rung with index number.
   * Representation Invariant:
   * RI(rungIndex) = a positive integer which represents the index of 
   *                 this Rung.
   * checkRep:
   * the index of Rung must be a positive integer.
   * Safety from Exposure:
   * An immutable class. Use "final" and "private" to ensure the filed 
   * won't be changed after the class is created. The only method is 
   * the observer which won't be a threat to the safety of this class.
   * 
   * Thread Safety:
   * This class is immutable and no other mutator method is used, which
   * proves the safety of this class in multi-threads.
   */
  
  
  /**
   * Constructor.
   * @param index - the index of this Rung.
   */
  public Rung(int index) {
    assert index > 0;
    this.rungIndex = index;
  }
  
  /**
   * Get the index of this Rung. The order is from the left shore
   * to the right shore.
   * @return rungIndex - the index of this Rung.
   */
  public int getRungIndex() {
    return rungIndex;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Rung)) {
      return false;
    }
    Rung rung = (Rung)obj;
    return rung.rungIndex == rungIndex;
  }
  
  @Override
  public int hashCode() {
    int result = 17;
    result = result * 31 + rungIndex;
    return result;
  }
}
