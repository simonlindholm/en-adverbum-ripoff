import org.codehaus.jackson.annotate.*;

@JsonTypeName("go")
public class GoCommand extends SimpleCommand {
	public String to;
	public void perform(Game game) {
		game.goRoom(to);
	}
}
