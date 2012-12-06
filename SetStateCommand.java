import org.codehaus.jackson.annotate.*;

@JsonTypeName("set-state")
public class SetStateCommand extends SimpleCommand {
	@JsonProperty
	private String state;
	@JsonProperty
	private boolean value, global;

	public void perform(Game game) {
		game.setState(global, state, value);
	}
}
