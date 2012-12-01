import org.codehaus.jackson.annotate.*;

@JsonTypeName("drop")
public class DropCommand extends TargettedCommand {
	@JsonProperty
	private String success, failure;

	public boolean maybePerformWith(Game game, String target) {
		boolean worked = game.tryDropItem(target);
		System.out.println(worked ? this.success : this.failure);
		return true;
	}
}
