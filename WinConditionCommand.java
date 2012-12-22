import org.codehaus.jackson.annotate.*;

@JsonTypeName("win-condition")
public class WinConditionCommand extends SimpleCommand {
	@JsonProperty
	private String name, success, failure;
	@JsonProperty
	private int points;

	public void perform(Game game) {
		if (game.tryWinCondition(this.name)) {
			if (this.success == null) {
				// Print the [+N] as a separate paragraph (it's not related to anything).
				System.out.println();
			} else {
				System.out.print(this.success + " ");
			}
			System.out.println("[+" + this.points + "]");
			game.increaseScore(this.points);
		} else if (this.failure != null) {
			System.out.println(this.failure);
		}
	}
}
