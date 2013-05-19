import org.codehaus.jackson.annotate.*;

@JsonTypeName("condition")
public class ConditionCommand extends Command {
	@JsonProperty
	private Command then, otherwise;
	@JsonProperty
	private String roomHasItem, hasItem;
	@JsonProperty
	private String globalState, localState;

	private Command decide(Game game) {
		if (this.hasItem != null && !game.inventoryHasItem(this.hasItem)) {
			return otherwise;
		}
		if (this.roomHasItem != null && !game.currentRoomHasItem(this.roomHasItem)) {
			return otherwise;
		}
		if (this.globalState != null && !game.hasState(true, this.globalState)) {
			return otherwise;
		}
		if (this.localState != null && !game.hasState(false, this.localState)) {
			return otherwise;
		}
		return then;
	}

	public boolean maybePerform(String input, Game game) {
		Command c = this.decide(game);
		return (c != null && c.matches(input) && c.maybePerform(input, game));
	}
}
