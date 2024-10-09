package stocks.commands;

import java.io.IOException;

/**
 * This interface represents a Command.
 * A Command has a distinct function that can be executed,
 * processing user input and returning a calculated output.
 */
public interface Command {

  /**
   * Runs the given command, executing its function.
   *
   * @throws IOException if an error is thrown.
   */
  void run() throws IOException;


}
