import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.annotate.JsonTypeInfo.*;

@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({
	@JsonSubTypes.Type(value=MsgCommand.class, name="msg"),
	@JsonSubTypes.Type(value=GoCommand.class, name="go"),
	@JsonSubTypes.Type(value=AnyCommand.class, name="any"),
	@JsonSubTypes.Type(value=AllCommand.class, name="all"),
	@JsonSubTypes.Type(value=DropCommand.class, name="drop"),
	@JsonSubTypes.Type(value=PickCommand.class, name="pick"),
	@JsonSubTypes.Type(value=GiveupCommand.class, name="giveup"),
	@JsonSubTypes.Type(value=ExamineCommand.class, name="examine"),
	@JsonSubTypes.Type(value=ConditionCommand.class, name="condition"),
	@JsonSubTypes.Type(value=WinConditionCommand.class, name="win-condition"),
	@JsonSubTypes.Type(value=ExitCommand.class, name="exit")
})
public abstract class Command {
	@JsonProperty
	protected String regex;

	public boolean matches(String input) {
		return (this.regex == null || input.matches("^" + this.regex + "$"));
	}
	public abstract boolean maybePerform(String input, Game game);
}
