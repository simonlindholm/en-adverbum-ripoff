import org.codehaus.jackson.annotate.*;

@JsonTypeName("examine")
public class ExamineCommand extends TargettedCommand {
	public String failure;

	public boolean maybePerformWith(Game game, String target) {
		if (!game.tryExamineItem(target)) {
			System.out.println(this.failure);
		}
		return true;
	}
}
