import org.codehaus.jackson.annotate.*;

@JsonTypeName("set-state")
public class SetStateCommand extends SimpleCommand {
	@JsonProperty
	private String state;
	@JsonProperty
	private boolean value;

	public void perform(Game game) {
		game.setState(state, value);
	}
}
