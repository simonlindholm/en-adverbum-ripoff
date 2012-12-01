import org.codehaus.jackson.annotate.*;

@JsonTypeName("win-condition")
public class WinConditionCommand extends SimpleCommand {
	public String name;
	public int points;
	public String success, failure;

	public void perform(Game game) {
		assert (this.success == null) == (this.failure == null) :
			"A win condition must be wholly independent and silent or have both initial and repeat messages.";
		boolean optional = (this.success == null);

		boolean worked = game.tryWinCondition(this.name);
		if (worked) {
			if (optional) {
				// Print the [+N] as a separate paragraph (it's not related to anything).
				System.out.println();
			} else {
				System.out.print(this.success + " ");
			}
			System.out.println("[+" + this.points + "]");
			game.increaseScore(this.points);
		} else if (!optional) {
			System.out.println(this.failure);
		}
	}
}
