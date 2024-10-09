package stocks;

import java.io.IOException;

/**
 * This class represents a MockModel that implements the Appendable class.
 * Used to mock user's inputs by keeping track of a log as a StringBuilder,
 * that can be appended to and then checked for its output.
 */
public class MockModel implements Appendable {

  /**
   * The log to append to, as a StringBuilder.
   */
  private StringBuilder log;

  /**
   * Constructs a MockModel with a given StringBuilder.
   *
   * @param sb StringBuilder to become the log.
   */
  public MockModel(StringBuilder sb) {
    this.log = sb;
  }

  /**
   * Appends to the log.
   *
   * @param csq the character sequence to append.  If {@code csq} is
   *            {@code null}, then the four characters {@code "null"} are
   *            appended to this Appendable.
   * @return the model but with new data appended to the log.
   * @throws IOException if an exception is thrown.
   */
  @Override
  public Appendable append(CharSequence csq) throws IOException {
    this.log.append(csq.toString());
    return this;
  }

  /**
   * Not used.
   *
   * @param csq   The character sequence from which a subsequence will be
   *              appended.  If {@code csq} is {@code null}, then characters
   *              will be appended as if {@code csq} contained the four
   *              characters {@code "null"}.
   * @param start The index of the first character in the subsequence
   * @param end   The index of the character following the last character in the
   *              subsequence
   * @return null.
   */
  @Override
  public Appendable append(CharSequence csq, int start, int end) {
    return null;
  }

  /**
   * Not used.
   *
   * @param c The character to append
   * @return null.
   */
  @Override
  public Appendable append(char c) {
    return null;
  }

  /**
   * Returns the log as a String.
   *
   * @return the log as a String.
   */
  @Override
  public String toString() {
    return this.log.toString();
  }
}