import java.util.*;

public class Room {
	public String name, desc, magic;
	public ArrayList<Command> commands;

	private boolean hasEntered;
	public void enter(Game game) {
		System.out.println("");
		if (this.magic != null) {
			System.out.println(game.getMagicText(this.magic));
		}
		System.out.printf("\033[1m%s\033[0m\n", this.name);
		System.out.println(this.desc);
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
