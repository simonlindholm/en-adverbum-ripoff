import java.util.regex.*;
import org.codehaus.jackson.annotate.*;

public abstract class TargettedCommand extends Command {
	public int group;
	public String thing;

	public abstract boolean maybePerformWith(Game game, String target);

	public boolean maybePerform(String input, Game game) {
		assert (this.group != 0) != (this.thing != null) : "Must have exactly one target.";
		assert !(this.group != 0 && this.regex == null) : "Can't use capture groups without regex.";

		String target = this.thing;
		if (this.regex != null) {
			Pattern p = Pattern.compile(this.regex);
			Matcher m = p.matcher(input);
			boolean found = m.find();
			assert found;
			if (this.group != 0) {
				assert this.group <= m.groupCount();
				target = m.group(this.group);
			}
		}
		return this.maybePerformWith(game, target);
	}
}
