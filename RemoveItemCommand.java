import org.codehaus.jackson.annotate.*;

@JsonTypeName("remove-item")
public class RemoveItemCommand extends SimpleCommand {
	@JsonProperty
	private String name;
	@JsonProperty
	private boolean held;

	public void perform(Game game) {
		game.tryRemoveItem(name, held);
	}
}
