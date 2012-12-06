import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.*;
import java.util.*;
import java.io.*;

public class Game {
	private static class Item {
		@JsonProperty
		public String desc;
		@JsonProperty
		public int points;
	}
	private static class JsonData {
		@JsonProperty
		public String start, initialText, magicText, giveupText;
		@JsonProperty
		public Map<String, Room> rooms;
		@JsonProperty
		public Map<String, Item> items;
	}

	private Map<String, Room> rooms;
	private Map<String, Item> items;
	private String magicText, giveupText;

	// Game state
	private boolean shouldExit = false;

	// Player state
	private Room currentRoom;
	private ArrayList<String> inventory;
	private int score;
	private Set<String> wonConditions;
	private Set<String> globalState, roomState;

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
		this.roomState = new HashSet<String>();
		this.currentRoom = this.rooms.get(room);
		this.currentRoom.enter(this);
	}

	public void goRandomOtherRoom() {
		List<String> roomKeys = new ArrayList<String>(this.rooms.keySet());

		for (;;) {
			String roomKey = roomKeys.get((int)(Math.random() * roomKeys.size()));
			if (this.rooms.get(roomKey) != this.currentRoom) {
				goRoom(roomKey);
				return;
			}
		}
	}

	public void describeCurrentRoom() {
		this.currentRoom.describe(this);
	}

	public boolean currentRoomHasItem(String item) {
		return this.currentRoom.hasItem(item);
	}

	public boolean inventoryHasItem(String item) {
		return this.inventory.contains(item);
	}

	public void dropEverything() {
		for (String item : this.inventory) {
			this.currentRoom.addItem(item);
		}
		this.inventory.clear();
	}

	public boolean tryDropItem(String item) {
		if (!this.inventoryHasItem(item)) {
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

	public void examineItem(String item) {
		System.out.println(this.items.get(item).desc);
	}

	public boolean tryWinCondition(String name) {
		return this.wonConditions.add(name);
	}

	public int tryItemWinCondition(String item) {
		Item it = this.items.get(item);
		if (it.points != 0 && this.tryWinCondition("picked-" + item)) {
			return it.points;
		}
		return 0;
	}

	public void increaseScore(int points) {
		this.score += points;
	}

	public boolean hasState(boolean global, String name) {
		Set<String> state = (global ? this.globalState : this.roomState);
		return state.contains(name);
	}
	public void setState(boolean global, String name, boolean value) {
		Set<String> state = (global ? this.globalState : this.roomState);
		if (value) {
			state.add(name);
		} else {
			state.remove(name);
		}
	}


	void run() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
			mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_FIELDS, false);
			JsonData d = mapper.readValue(new File("game.json"), JsonData.class);

			this.rooms = d.rooms;
			this.magicText = d.magicText;
			this.giveupText = d.giveupText;
			this.items = d.items;

			this.inventory = new ArrayList<String>();
			this.wonConditions = new HashSet<String>();
			this.globalState = new HashSet<String>();
			this.score = 0;

			// (Hopefully) clear the screen, and print an introduction.
			System.out.println("\033[2J\033[1;1H");
			System.out.println(d.initialText);
			System.out.println("\n---------------");

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
