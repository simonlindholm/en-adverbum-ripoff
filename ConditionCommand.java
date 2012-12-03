import org.codehaus.jackson.annotate.*;

@JsonTypeName("condition")
public class ConditionCommand extends Command {
	@JsonProperty
	private Command then, otherwise;
	@JsonProperty
	private String roomHasItem;
	@JsonProperty
	private String state;

	private Command decide(Game game) {
		if (this.roomHasItem != null && !game.currentRoomHasItem(this.roomHasItem)) {
			return otherwise;
		}
		if (this.state != null && !game.hasState(this.state)) {
			return otherwise;
		}
		return then;
	}

	public boolean maybePerform(String input, Game game) {
		Command c = this.decide(game);
		return (c != null && c.matches(input) && c.maybePerform(input, game));
	}
}
