import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.security.*;
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

	private static final byte[] ENCRYPTION_KEY = "ouC0QowpgXL02jGS".getBytes();
	private static final byte[] IV = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	private static class PlayerState {
		@JsonProperty
		public String currentRoomName;
		public Room currentRoom;
		@JsonProperty
		public int score;
		@JsonProperty
		public Set<String> inventory, wonConditions, globalState, roomState;

		public PlayerState() {
			this.inventory = new HashSet<String>();
			this.wonConditions = new HashSet<String>();
			this.globalState = new HashSet<String>();
			this.score = 0;
		}

		public static PlayerState fromJSON(Game game, String json) throws Exception {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_FIELDS, false);
			PlayerState s = mapper.readValue(json, PlayerState.class);
			s.currentRoom = game.rooms.get(s.currentRoomName);
			return s;
		}
	}
	private PlayerState state;

	public void signalExit() {
		this.shouldExit = true;
	}

	public String getMagicText(String word) {
		return String.format(this.magicText, word);
	}
	public String getGiveupText() {
		return this.giveupText;
	}

	public void goRoom(String room) {
		this.state.roomState = new HashSet<String>();
		this.state.currentRoom = this.rooms.get(room);
		this.state.currentRoomName = room;
		this.state.currentRoom.enter(this);
	}

	public void goRandomOtherRoom() {
		List<String> roomKeys = new ArrayList<String>(this.rooms.keySet());

		for (;;) {
			String roomKey = roomKeys.get((int)(Math.random() * roomKeys.size()));
			if (roomKey != this.state.currentRoomName) {
				goRoom(roomKey);
				return;
			}
		}
	}

	public void describeCurrentRoom() {
		this.state.currentRoom.describe(this);
	}

	public boolean currentRoomHasItem(String item) {
		return this.state.currentRoom.hasItem(item);
	}

	public boolean inventoryHasItem(String item) {
		return this.state.inventory.contains(item);
	}

	public void dropEverything() {
		for (String item : this.state.inventory) {
			this.state.currentRoom.addItem(item);
		}
		this.state.inventory.clear();
	}

	public boolean tryDropItem(String item) {
		if (!this.inventoryHasItem(item)) {
			return false;
		}
		this.state.inventory.remove(item);
		this.state.currentRoom.addItem(item);
		return true;
	}

	public boolean tryPickItem(String item) {
		if (!this.currentRoomHasItem(item)) {
			return false;
		}
		this.state.currentRoom.removeItem(item);
		this.state.inventory.add(item);
		return true;
	}

	public boolean tryRemoveItem(String item, boolean fromInventory) {
		if (fromInventory) {
			return this.state.inventory.remove(item);
		} else {
			if (this.currentRoomHasItem(item)) {
				this.state.currentRoom.removeItem(item);
				return true;
			} else {
				return false;
			}
		}
	}

	public void examineItem(String item) {
		System.out.println(this.items.get(item).desc);
	}

	public boolean tryWinCondition(String name) {
		return this.state.wonConditions.add(name);
	}

	public int tryItemWinCondition(String item) {
		Item it = this.items.get(item);
		if (it.points != 0 && this.tryWinCondition("picked-" + item)) {
			return it.points;
		}
		return 0;
	}

	public void increaseScore(int points) {
		this.state.score += points;
	}

	private Set<String> getStateHolder(boolean global) {
		return (global ? this.state.globalState : this.state.roomState);
	}
	public boolean hasState(boolean global, String name) {
		return this.getStateHolder(global).contains(name);
	}
	public void setState(boolean global, String name, boolean value) {
		Set<String> state = this.getStateHolder(global);
		if (value) {
			state.add(name);
		} else {
			state.remove(name);
		}
	}

	public void saveGame(String saveName) throws Exception {
		String fileName = saveName + ".save";
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, false);
		String saveStr = mapper.writeValueAsString(this.state);

		// Scramble the save file a bit.
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(ENCRYPTION_KEY, "AES"), new IvParameterSpec(IV));
		byte[] input = saveStr.getBytes("UTF-8");
		byte[] output = cipher.doFinal(input);

		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
		try {
			bos.write(output);
		} finally {
			bos.close();
		}
	}

	public void loadGame(String saveName) throws Exception {
		String fileName = saveName + ".save";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = new FileInputStream(fileName);
		try {
			byte buffer[] = new byte[2048];
			while (true) {
				int read = is.read(buffer);
				if (read < 0) {
					break;
				}
				baos.write(buffer, 0, read);
			}
		} finally {
			is.close();
		}

		// Unscramble the file.
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(ENCRYPTION_KEY, "AES"), new IvParameterSpec(IV));
		byte[] input = baos.toByteArray();
		byte[] output = cipher.doFinal(input);

		// Set up the new state...
		this.state = PlayerState.fromJSON(this, new String(output, "UTF-8"));

		// ... and throw the player directly into the saved game, by providing the room description.
		this.state.currentRoom.describe(this);
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

			this.state = new PlayerState();

			// (Hopefully) clear the screen, and print an introduction. Termcap
			// detection is too hard to attempt here.
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
				input = input.toLowerCase().trim();

				this.state.currentRoom.handleInput(this, input);
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
