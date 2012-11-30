import org.codehaus.jackson.annotate.*;

@JsonTypeName("msg")
public class MsgCommand extends SimpleCommand {
	public String text, text2;
	private boolean first = true;
	public void perform(Game game) {
		// Alternate between text and text2.
		System.out.println(this.first ? this.text : this.text2);
		if (this.text2 != null) {
			this.first = !this.first;
		}
	}
}
