import org.codehaus.jackson.annotate.*;

@JsonTypeName("giveup")
public class GiveupCommand extends Command {
	public String to;
	public void perform(Game game) {
		System.out.println(game.getGiveupText());
		game.dropRoomThings();
		game.goRoom(this.to);
	}
}
