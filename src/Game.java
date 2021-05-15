
import java.util.Scanner; //scanner

public class Game { // driver class

	public static int chartoint(char lettah) { // method used to convert characters entered into integers for the
												// coordinates
		String Letters = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return Letters.indexOf(lettah);
	}

	public static void main(String[] args) {
		boolean computerWin, humanWin;// boolean used to start/finish the game
		computerWin = false;
		humanWin = false;

		Scanner kb = new Scanner(System.in); // scanner

		Element[][] cells = new Element[9][9]; // 2d array of element objects
		for (int i = 1; i < 9; i++) {
			for (int j = 1; j < 9; j++) {
				cells[i][j] = new Element(0, "none", false);
			}
		}

		Grid board = new Grid(cells); // board object

		System.out.println("Hi, lets play Battleship !"); // welcome prompt
		// since columns are given by letters, we will convert them into an integer
		// "colnb" by using the method "chartoint"
		int row;
		char col;
		int colnb;

		// creating 6 prompts for the coordinates of the ships
		for (int i = 1; i <= 6; i++) {

			System.out.print("Enter the coordinates of your ship #" + i + ": ");
			String input = kb.nextLine();
			col = input.charAt(0);
			colnb = chartoint(col);
			row = input.charAt(1) - 48; // the row is given as an ascii value, therefore we convert it to the integers
										// we want
										// by subtracting 48
			//this checks whether the given coordinates are inside our outside the grid, and whether they are duplicates or not
			if (colnb <= 8 && colnb>0 && row <= 8 && row>0 && cells[row][colnb].type != 1) {
				cells[row][colnb].type = 1;
				cells[row][colnb].owner = "human";

			} else if (colnb > 8 || row > 8 || row==0 ) {
				System.out.println("sorry, coordinates outside the grid. try again");
				i--;
			} else if (cells[row][colnb].type == 1) {
				System.out.println("sorry, coordinates already used. try again.");
				i--;

			}
		}
		//exact same thing but for the grenades
		for (int i = 1; i <= 4; i++) {
			System.out.print("Enter the coordinates of your grenade #" + i + ": ");
			String input = kb.nextLine();
			col = input.charAt(0);
			colnb = chartoint(col);
			row = input.charAt(1) - 48;
			if (colnb <= 8 && colnb>0 && row <= 8 && row>0 && cells[row][colnb].type != 2 && cells[row][colnb].type !=1) {
				cells[row][colnb].type = 2;
				cells[row][colnb].owner = "human";
			} else if (colnb > 8 || row > 8 || row == 0 ) {
				System.out.println("sorry, coordinates outside the grid. try again");
				i--;
			} else if (cells[row][colnb].type == 2 || cells[row][colnb].type == 1) {
				System.out.println("sorry, coordinates already used. try again.");
				i--;

			}
		}
		//the computer makes 6 ships and 4 grenades randomly using Math.random
		int nbofcpuShips = 0;

		while (nbofcpuShips != 6) {

			row = (int) (Math.random() * 8 + 1);
			colnb = (int) (Math.random() * 8 + 1);
			//this checks if the coordinates chosen by the computer are empty or not, so that the computer does not put two ships in the same coordinates
			if (cells[row][colnb].type == 0) {
				cells[row][colnb].type = 1;
				cells[row][colnb].owner = "Computer";
				nbofcpuShips++;
			}

		}
		//same thing for the grenades
		int nbofGrenades = 0;
		while (nbofGrenades != 4) {

			row = (int) (Math.random() * 8 + 1);
			colnb = (int) (Math.random() * 8 + 1);
			if (cells[row][colnb].type == 0) {
				cells[row][colnb].type = 2;
				cells[row][colnb].owner = "Computer";
				nbofGrenades++;
			}
		}

		System.out.println("Ok, the computer placed its ships and grenades at random. Let's play.");
		
		//board.printGrid();
		
		//keeps track of the ships left for computer and player
		int nbofshipsleft = 6;
		int nbofcpushipsleft = 6;
		//we use these booleans for the turn mechanics of the game
		//playerturn will either be false or true, when it is false it is always the computer's turn, and when it is true
		//it is always the player's turn
		//the other booleans are pretty self-explanatory, when the player hits a grenade, we make them skip a turn
		//when the computer hits a grenade, it skips a turn as well
		boolean playerturn = true;
		boolean skipturn = false;
		boolean hitgrenade = false;
		boolean cpuskipturn = false;
		boolean cpuhitgrenade = false;

		while (!humanWin && !computerWin) {
			// player's turn
			if (playerturn && !skipturn) {
				if (cpuhitgrenade) {
					cpuskipturn = true;
				} else {
					cpuskipturn = false;
				}
				cpuhitgrenade = false;
				System.out.print("position of your rocket: ");

				String input = kb.nextLine();
				col = input.charAt(0);
				colnb = chartoint(col);
				row = input.charAt(1) - 48;
				//we use an if statement here to make sure that the coordinates are not already called by the player
				if (cells[row][colnb].alrdyCalled != true) {
					if (cells[row][colnb].type == 1) {
						System.out.println("ship hit.");
						if (!cpuskipturn)
							playerturn = false;
						if (cells[row][colnb].owner == "Computer")
							nbofcpushipsleft--;
						else
							nbofshipsleft--;
						cells[row][colnb].alrdyCalled = true;
					} else if (cells[row][colnb].type == 2) {
						System.out.println("boom! grenade.");
						cells[row][colnb].alrdyCalled = true;
						if (!cpuskipturn)
							playerturn = false;
						hitgrenade = true;
					} else {
						System.out.println("nothing.");
						cells[row][colnb].alrdyCalled = true;
						if (!cpuskipturn)
							playerturn = false;
					}
				} else {
					System.out.println("position already called.");
					if (!cpuskipturn)
						playerturn = false;

				}

				board.printGrid();
				// this is what decides if we break the loop to end the game or not
				if (nbofshipsleft == 0 || nbofcpushipsleft == 0) {
					//this for loop is just so we can print the final grid with every ship and grenade
					for (int i = 1; i <= 8; i++) {
						for (int j = 1; j <= 8; j++) {
							if (cells[i][j].type == 0) {
								cells[i][j].alrdyCalled = false;
							} else if (cells[i][j].type == 1) {
								cells[i][j].alrdyCalled = true;
							} else if (cells[i][j].type == 2) {
								cells[i][j].alrdyCalled = true;
							}
						}
					}
					break;
				}

			} else if (!playerturn && !cpuskipturn) {

				// computer's turn
				//everything that happened during the player's turn applies here
				//except we do not use the scanner to get a prompt, but rather the Math.random method
				if (hitgrenade) {

					skipturn = true;
				} else {
					skipturn = false;
				}
				hitgrenade = false;
				System.out.print("position of my rocket: ");
				String Letters = " ABCDEFGH";
				row = (int) (Math.random() * 8 + 1);
				col = Letters.charAt((int) (Math.random() * 8 + 1));
				colnb = chartoint(col);
				System.out.print(col + "" + row);
				System.out.println();
				if (cells[row][colnb].alrdyCalled != true) {

					if (cells[row][colnb].type == 1) {
						System.out.println("ship hit.");
						cells[row][colnb].alrdyCalled = true;
						if (!skipturn)
							playerturn = true;
						if (cells[row][colnb].owner == "Computer")
							nbofcpushipsleft--;
						else
							nbofshipsleft--;
					} else if (cells[row][colnb].type == 2) {
						System.out.println("boom! grenade.");
						cells[row][colnb].alrdyCalled = true;
						if (!skipturn)
							playerturn = true;
						cpuhitgrenade = true;

					} else {
						System.out.println("nothing.");
						cells[row][colnb].alrdyCalled = true;
						if (!skipturn)
							playerturn = true;

					}
				} else {
					System.out.println("position already called.");
					if (!skipturn)
						playerturn = true;
				}

				board.printGrid();

				if (nbofshipsleft == 0 || nbofcpushipsleft == 0) {
					for (int i = 1; i <= 8; i++) {
						for (int j = 0; j <= 8; j++) {
							if (cells[i][j].type == 0) {
								cells[i][j].alrdyCalled = false;
							} else if (cells[i][j].type == 1) {
								cells[i][j].alrdyCalled = true;
							} else if (cells[i][j].type == 2) {
								cells[i][j].alrdyCalled = true;
							}
						}
					}

					break;
				}
			}

		}
		//this decides who won and prints the end grid with all the ships and grenades
		if (nbofshipsleft == 0) {
			System.out.println("Computer Wins!");
			board.printGrid();
		} else if (nbofcpushipsleft == 0) {
			System.out.println("You Win!");
			board.printGrid();
		}
		kb.close();
	}

}
//first class is to create the grid
class Grid {
	private Element[][] grid;

	public Grid(Element[][] cells) {
		this.grid = cells;

	}
	//printGrid is to create a 8x8 grid of elements
	public void printGrid() {

		for (int i = 1; i < 9; i++) {
			for (int j = 1; j < 9; j++) {
				System.out.print(grid[i][j] + " ");

			}
			System.out.println();
		}
	}

}
// because the instructions told us to have an object represent a position in the grid
// and to contain the type of element, the owner, and if it has been called,
// I kept it simple and stuck to just those 3 and used an integer for the type, a string for the owner,
// and a boolean to check if it is called or not
class Element {
	int type;
	String owner;
	boolean alrdyCalled;

	public Element() {
		type = 0;
		owner = "none";
		alrdyCalled = false;
	}

	public Element(int type, String Owner, boolean alrdyCalled) {
		this.type = type;
		this.owner = Owner;
		this.alrdyCalled = alrdyCalled;

	}

	public int getType() {
		return type;
	}

	public String getOwner() {
		return owner;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setOwner(String Owner) {
		this.owner = Owner;
	}

	public void setAlrdyCalled(boolean alrdyCalled) {
		this.alrdyCalled = alrdyCalled;
	}
	// the toString is the most important part here
	// it makes sure to return the right symbol depending on the type or if it is already called
	// I used the variable toReturn  so i could have the toUpperCase method depending on whether 
	// it is the computer's piece or the players piece
	public String toString() {
		String toReturn = "";

		if (!alrdyCalled) {
			return "_";
		} else if (type == 0) {
			return "*";
		} else if (type == 1) {
			toReturn = "s";
		} else if (type == 2) {
			toReturn = "g";
		}

		if (owner == "Computer") {
			toReturn = toReturn.toUpperCase();
		}
		return toReturn;

	}

}

