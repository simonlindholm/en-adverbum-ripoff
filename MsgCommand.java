import org.codehaus.jackson.annotate.*;

@JsonTypeName("msg")
public class MsgCommand extends SimpleCommand {
	@JsonProperty
	private String text;

	public void perform(Game game) {
		System.out.println(this.text);
	}
}
