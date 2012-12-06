import org.codehaus.jackson.annotate.*;

@JsonTypeName("teleport")
public class TeleportCommand extends SimpleCommand {
	public void perform(Game game) {
		game.goRandomOtherRoom();
	}
}
