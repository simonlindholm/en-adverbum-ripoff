import java.util.*;
import java.io.*;
import org.codehaus.jackson.JsonParser.*;
import org.codehaus.jackson.map.ObjectMapper;

public class Game {
	private static class Data {
		public String start;
		public String initialText, magicText, giveupText;
		public Map<String, Room> rooms;
	}

	private Map<String, Room> rooms;
	private Room currentRoom;
	private String magicText, giveupText;

	private boolean shouldExit = false;
	public void signalExit() {
		shouldExit = true;
	}

	public String getMagicText(String word) {
		return String.format(this.magicText, word);
	}
	public String getGiveupText() {
		return this.giveupText;
	}

	public void goRoom(String room) {
		this.currentRoom = this.rooms.get(room);
		this.currentRoom.enter(this);
	}

	public void dropRoomThings() {
		// TODO
	}

	void run() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(Feature.ALLOW_COMMENTS, true);
			Data d = mapper.readValue(new File("game.json"), Data.class);

			System.out.println("");
			System.out.println(d.initialText);
			System.out.println("\n---------------");

			this.rooms = d.rooms;
			this.magicText = d.magicText;
			this.giveupText = d.giveupText;

			this.goRoom(d.start);

			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			while (true) {
				System.out.print("> ");
				String input = in.readLine();
				if (input == null) {
					// Newline-terminate the prompt and exit on Ctrl+D.
					System.out.println("");
					break;
				}
				input = input.toLowerCase();

				this.currentRoom.handleInput(this, input);
				if (this.shouldExit)
					break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		Game g = new Game();
		g.run();
	}
}
