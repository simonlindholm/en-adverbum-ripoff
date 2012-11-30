import org.codehaus.jackson.annotate.*;

@JsonTypeName("exit")
public class ExitCommand extends SimpleCommand {
	public void perform(Game game) {
		game.signalExit();
	}
}
