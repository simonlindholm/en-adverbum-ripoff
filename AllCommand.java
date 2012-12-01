import java.util.*;
import org.codehaus.jackson.annotate.*;

@JsonTypeName("all")
public class AllCommand extends Command {
	public ArrayList<Command> list;

	public boolean maybePerform(String input, Game game) {
		for (Command c : list) {
			if (c.matches(input)) {
				c.maybePerform(input, game);
			}
		}
		return true;
	}
}
