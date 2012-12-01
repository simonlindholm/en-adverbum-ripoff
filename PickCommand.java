import org.codehaus.jackson.annotate.*;

@JsonTypeName("pick")
public class PickCommand extends TargettedCommand {
	@JsonProperty
	private String success, failure;

	public boolean maybePerformWith(Game game, String target) {
		boolean worked = game.tryPickItem(target);
		System.out.println(worked ? this.success : this.failure);
		return true;
	}
}
