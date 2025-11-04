
/*
 * PacMan Game
 * https://www.youtube.com/watch?v=lB_J-VNMVpE&list=PLnKe36F30Y4Y1XQOqNsL9Fgg_p6nYhcng
 * Pick back up at 37:18 / 1:08:00
 */

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame("Pac Man");
        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
	    //frame.setResizable(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// create new instance of PacMan
        PacMan pacmanGame = new PacMan();
		// add panel onto window
        frame.add(pacmanGame);
		// get full size of JPanel within window
        frame.pack();
		// makes sure Pacman key listener acting on pacman object
        pacmanGame.requestFocus();
        frame.setVisible(true);

    }
}

