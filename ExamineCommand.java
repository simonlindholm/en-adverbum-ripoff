import org.codehaus.jackson.annotate.*;

@JsonTypeName("examine")
public class ExamineCommand extends TargettedCommand {
	public String failure;

	public boolean maybePerformWith(Game game, String target) {
		if (target == null) {
			game.describeCurrentRoom();
		} else if (this.group == 0 || (game.currentRoomHasItem(target) || game.inventoryHasItem(target))) {
			game.examineItem(target);
		} else {
			System.out.println(this.failure);
		}
		return true;
	}
}
