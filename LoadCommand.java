import org.codehaus.jackson.annotate.*;

@JsonTypeName("load")
public class LoadCommand extends TargettedCommand {
	@JsonProperty
	private String notFound, failure, invalidName;

	public boolean maybePerformWith(Game game, String target) {
		assert target != null;
		if (!target.matches("^[a-zA-Z0-9-_]+$")) {
			System.out.println(this.invalidName);
		} else {
			try {
				game.loadGame(target);
			} catch (java.io.FileNotFoundException e) {
				System.out.println(this.notFound);
			} catch (Exception e) {
				System.out.println(String.format(this.failure, e.getMessage()));
			}
		}
		return true;
	}
}
