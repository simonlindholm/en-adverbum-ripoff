import org.codehaus.jackson.annotate.*;

@JsonTypeName("condition")
public class ConditionCommand extends Command {
	public Command then, otherwise;
	public String roomHasItem;

	private Command decide(Game game) {
		if (this.roomHasItem != null && !game.currentRoomHasItem(this.roomHasItem)) {
			return otherwise;
		}
		return then;
	}

	public boolean maybePerform(String input, Game game) {
		Command c = this.decide(game);
		return (c != null && c.matches(input) && c.maybePerform(input, game));
	}
}
