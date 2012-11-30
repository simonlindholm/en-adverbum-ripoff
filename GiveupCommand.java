import org.codehaus.jackson.annotate.*;

@JsonTypeName("giveup")
public class GiveupCommand extends SimpleCommand {
	public String to;
	public void perform(Game game) {
		System.out.println(game.getGiveupText());
		game.dropEverything();
		game.goRoom(this.to);
	}
}
