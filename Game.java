import java.util.*;
import java.io.*;
import org.codehaus.jackson.JsonParser.*;
import org.codehaus.jackson.map.ObjectMapper;

public class Game {
	private static class Data {
		public String start;
		public String initialText, magicText, giveupText;
		public Map<String, Room> rooms;
		public Map<String, String> items;
	}

	private Map<String, Room> rooms;
	private Map<String, String> itemDescs;
	private String magicText, giveupText;

	// Player state
	private Room currentRoom;
	private ArrayList<String> inventory;

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

	public void dropEverything() {
		for (String item : this.inventory) {
			this.currentRoom.addItem(item);
		}
		this.inventory.clear();
	}

	public boolean currentRoomHasItem(String item) {
		return this.currentRoom.hasItem(item);
	}

	public boolean tryDropItem(String item) {
		if (!this.inventory.contains(item)) {
			return false;
		}
		this.inventory.remove(item);
		this.currentRoom.addItem(item);
		return true;
	}

	public boolean tryPickItem(String item) {
		if (!this.currentRoomHasItem(item)) {
			return false;
		}
		this.currentRoom.removeItem(item);
		this.inventory.add(item);
		return true;
	}

	public boolean tryExamineItem(String item) {
		if (!this.currentRoomHasItem(item) && !this.inventory.contains(item)) {
			return false;
		}
		System.out.println(this.itemDescs.get(item));
		return true;
	}

	void run() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(Feature.ALLOW_COMMENTS, true);
			Data d = mapper.readValue(new File("game.json"), Data.class);

			System.out.println();
			System.out.println(d.initialText);
			System.out.println("\n---------------");

			this.rooms = d.rooms;
			this.magicText = d.magicText;
			this.giveupText = d.giveupText;
			this.itemDescs = d.items;

			this.inventory = new ArrayList<String>();
			this.goRoom(d.start);

			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			while (true) {
				System.out.println();
				System.out.print("> ");
				String input = in.readLine();
				if (input == null) {
					// Newline-terminate the prompt and exit on Ctrl+D.
					System.out.println();
					break;
				}
				input = input.toLowerCase();

				this.currentRoom.handleInput(this, input);
				if (this.shouldExit) {
					break;
				}
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
