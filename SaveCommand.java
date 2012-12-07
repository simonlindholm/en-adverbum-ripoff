import org.codehaus.jackson.annotate.*;

@JsonTypeName("save")
public class SaveCommand extends TargettedCommand {
	@JsonProperty
	private String success, failure, invalidName;

	public boolean maybePerformWith(Game game, String target) {
		assert target != null;
		if (!target.matches("^[a-zA-Z0-9-_]+$")) {
			System.out.println(this.invalidName);
		} else {
			try {
				game.saveGame(target);
				System.out.println(this.success);
			} catch (Exception e) {
				System.out.println(String.format(this.failure, e.getMessage()));
			}
		}
		return true;
	}
}
