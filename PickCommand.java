import org.codehaus.jackson.annotate.*;

@JsonTypeName("pick")
public class PickCommand extends TargettedCommand {
	@JsonProperty
	private String success, failure;

	public boolean maybePerformWith(Game game, String target) {
		boolean worked = game.tryPickItem(target);
		String output = worked ? this.success : this.failure;
		if (worked) {
			int points = game.tryItemWinCondition(target);
			if (points != 0) {
				output += " [+" + points + "]";
			}
		}
		System.out.println(output);
		return true;
	}
}
