import org.codehaus.jackson.annotate.*;

public abstract class SimpleCommand extends Command {
	public boolean maybePerform(String input, Game game) {
		this.perform(game);
		return true;
	}
	public abstract void perform(Game game);
}
