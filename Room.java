import org.codehaus.jackson.annotate.*;
import java.util.*;

public class Room {
	@JsonProperty
	private String name, itemsDesc, magic;
	@JsonProperty
	private Command desc;
	@JsonProperty
	private ArrayList<Command> initial, commands;
	@JsonProperty
	private ArrayList<String> items;

	public void enter(Game game) {
		if (this.magic != null) {
			System.out.println(game.getMagicText(this.magic));
		}
		this.describe(game);

		if (this.initial != null) {
			for (Command c : this.initial) {
				c.maybePerform("", game);
			}
		}
	}

	public void describe(Game game) {
		// Try to print the room name in bold.
		System.out.println();
		System.out.printf("\033[1m%s\033[0m\n", this.name);
		this.desc.maybePerform("", game);
		if (!this.items.isEmpty()) {
			System.out.println();
			System.out.print(this.itemsDesc);
			for (String item : this.items) {
				System.out.print("... " + item);
			}
			System.out.println(".");
		}
	}

	public boolean hasItem(String item) {
		return this.items.contains(item);
	}
	public void addItem(String item) {
		this.items.add(item);
	}
	public void removeItem(String item) {
		this.items.remove(item);
	}

	public void handleInput(Game game, String input) {
		for (Command c : commands) {
			if (c.matches(input) && c.maybePerform(input, game)) {
				return;
			}
		}
		assert false: "At least one command must match.";
	}
}
