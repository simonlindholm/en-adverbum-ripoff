import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.annotate.JsonTypeInfo.*;

@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({
	@JsonSubTypes.Type(value=MsgCommand.class, name="msg"),
	@JsonSubTypes.Type(value=GoCommand.class, name="go"),
	@JsonSubTypes.Type(value=GiveupCommand.class, name="giveup"),
	@JsonSubTypes.Type(value=ExitCommand.class, name="exit")
})
public abstract class Command {
	public String regex;
	public abstract void perform(Game game);
}
