import org.codehaus.jackson.annotate.*;

@JsonTypeName("exit")
public class ExitCommand extends Command {
	public void perform(Game game) {
		game.signalExit();
	}
}
