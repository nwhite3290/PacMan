
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;


public class PacMan extends JPanel implements ActionListener, KeyListener {
	class Block {
		int x;
		int y;
		int width;
		int height;
		Image image;

		int startX;
		int startY;

		Block(Image image, int x, int y, int width, int height) {
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.startX = x;
			this.startY = y;
		}

	}

	private int rowCount = 21;
	private int columnCount = 19;
	private int tileSize = 32;
	private int boardWidth = columnCount * tileSize;
	private int boardHeight = rowCount * tileSize;

	// create member images to store the images
	private Image wallImage;
	private Image blueGhostImage;
	private Image orangeGhostImage;
	private Image pinkGhostImage;
	private Image redGhostImage;

	private Image pacmanUpImage;
	private Image pacmanDownImage;
	private Image pacmanLeftImage;
	private Image pacmanRightImage;

	//X = wall, O = skip, P = pac man, ' ' = food
	//Ghosts: b = blue, o = orange, p = pink, r = red
	private String[] tileMap = {
			"XXXXXXXXXXXXXXXXXXX",
			"X        X        X",
			"X XX XXX X XXX XX X",
			"X                 X",
			"X XX X XXXXX X XX X",
			"X    X       X    X",
			"XXXX XXXX XXXX XXXX",
			"OOOX X       X XOOO",
			"XXXX X XXrXX X XXXX",
			"O       bpo       O",
			"XXXX X XXXXX X XXXX",
			"OOOX X       X XOOO",
			"XXXX X XXXXX X XXXX",
			"X        X        X",
			"X XX XXX X XXX XX X",
			"X  X     P     X  X",
			"XX X X XXXXX X X XX",
			"X    X   X   X    X",
			"X XXXXXX X XXXXXX X",
			"X                 X",
			"XXXXXXXXXXXXXXXXXXX"
	};

	HashSet<Block> walls;
	HashSet<Block> foods;
	HashSet<Block> ghosts;
	Block pacman;

	Timer gameLoop;

	// constructor
	PacMan() {
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setBackground(Color.BLACK);
		// don't need to create separate KeyListener object
		// just need to reference pacman
		addKeyListener(this);
		// make sure JPanel is th one listening for key presses
		// in App.java: add pacmanGame.requestFocus();
		setFocusable(true);

		// load images
		wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
		blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
		orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
		pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
		redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

		pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
		pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
		pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
		pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

		loadMap();
		//System.out.println(walls.size());
		//System.out.println(foods.size());
		//System.out.println(ghosts.size());

		// pacman implements (this) Action Listener
		// how long it takes to start timer, milliseconds gone between frames
		// call repaint every 50ms
		gameLoop = new Timer(50, this); // (1000ms/50ms) = 20fps
		// start gameLoop timer
		gameLoop.start(); // now add KeyListeners to move pacman (up,down,left,right)



	}

	public void loadMap() {
		walls = new HashSet<Block>();
		foods = new HashSet<Block>();
		ghosts = new HashSet<Block>();

		for (int r = 0; r < rowCount; r++) {
			for (int c = 0; c < columnCount; c++) {
				String row = tileMap[r];
				char tileMapChar = row.charAt(c);

				int x = c  * tileSize;
				int y = r * tileSize;

				if (tileMapChar == 'X') { // block wall
					Block wall = new Block(wallImage, x, y, tileSize, tileSize);
					walls.add(wall);
				}
				// blue ghost
				else if (tileMapChar == 'b') {
					Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				}
				// orange ghost
				else if (tileMapChar == 'o') {
					Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				}
				// pink ghost
				else if (tileMapChar == 'p') {
					Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				}
				// red ghost
				else if (tileMapChar == 'r') {
					Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				}
				// pacman
				else if (tileMapChar == 'P') {
					pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
				}
				// food = 4px X 4px
				else if (tileMapChar == ' ') {
					// 32px - 4px = 28; 28 / 2 = 14
					Block food = new Block(null, x + 14, y + 14, 4, 4);
					foods.add(food);
				}
			}
		}
	}

	// problem paintComponent only is drawn one time
	// create game loop - implement Action Listener
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	// fill in the graphics images
	public void draw(Graphics g) {
		//g.fillRect(pacman.x, pacman.y, pacman.width, pacman.height);
		g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
		// draw ghosts
		for (Block ghost : ghosts) {
			g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
		}
		// draw walls
		for (Block wall : walls) {
			g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
		}
		// dont have food images
		g.setColor(Color.WHITE);
		for (Block food : foods) {
			g.fillRect(food.x, food.y, food.width, food.height);
		}

		//score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else {
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
	}
	 public void move() {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;
        //check wall collisions
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
        //check ghost collisions
        for (Block ghost : ghosts) {
            if (collision(ghost, pacman)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            if (ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall : walls) {
                if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }
        //check food collision
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);
        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }




	
	// repaint game loop
	// calls paintComponent again
	// still need a timer
	@Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

	// performs action when key typed
	// using arrow keys, so don't need this
	@Override
	public void keyTyped(KeyEvent e) { }

	// performs action when key is held pressed
	// don't need this
	@Override
	public void keyPressed(KeyEvent e) { }

	// performs action when key is released
	@Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }

}

