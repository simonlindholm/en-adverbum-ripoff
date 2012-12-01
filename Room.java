import java.util.*;

public class Room {
	public String name, desc, magic;
	public ArrayList<Command> commands;
	public ArrayList<String> items;

	public void enter(Game game) {
		if (this.magic != null) {
			System.out.println(game.getMagicText(this.magic));
		}
		this.describe(game);
	}

	public void describe(Game game) {
		// Print with room name with either weird decoration or in bold (both work ok).
		System.out.println();
		System.out.printf("\033[1m%s\033[0m\n", this.name);
		System.out.println(this.desc);
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
