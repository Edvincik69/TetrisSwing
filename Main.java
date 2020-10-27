import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class Main extends JPanel {

    private int gameStatus = 1; // 1 - start, -1 - stop, 0 - playing
    private final int numberOfPieces = 7;
    private final int startX = 4;
    private final int startY = 0;
    private Point currentPiecePosition = new Point(startX, startY);
    private int currentPieceID = -1;
    private int currentPieceRotation = 0;
    private final int boardWidth = 12;
    private final int boardHeight = 22;
    private Color[][] board = new Color[boardWidth][boardHeight];

    private final Color[] tetriminosColors = {
            Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.magenta, Color.red
    };

    private final Point[][][] tetriminos = {
            // I-Piece
            {
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
            },

            // L-Piece
            {
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) }
            },

            // J-Piece
            {
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
            },

            // O-Piece
            {
                    { new Point(1, 0), new Point(1, 1), new Point(2, 0), new Point(2, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(2, 0), new Point(2, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(2, 0), new Point(2, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(2, 0), new Point(2, 1) }
            },

            // S-Piece
            {
                    { new Point(2, 0), new Point(3, 0), new Point(1, 1), new Point(2, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
                    { new Point(2, 0), new Point(3, 0), new Point(1, 1), new Point(2, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) }
            },

            // T-Piece
            {
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
            },

            // Z-Piece
            {
                    { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
                    { new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
                    { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
                    { new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
            }
    };

    private void fillBoard() {
        for(int i = 0; i < boardWidth; i++) {
            for(int j = 0; j < boardHeight; j++) {
                if(i == 0 || i == boardWidth - 1 || j == boardHeight - 1) {
                    board[i][j] = Color.gray;
                }
                else {
                    board[i][j] = Color.black;
                }
            }
        }
    }

    private void createNewPiece() {
        currentPiecePosition = new Point(startX, startY);
        currentPieceRotation = 0;
        currentPieceID = getRandomNumber(0, numberOfPieces);
        if(!isMovable(currentPiecePosition.x, currentPiecePosition.y, currentPieceRotation)) {
            gameStatus = -1;
            currentPieceID = -1;
        }
        repaint();
    }

    private boolean isMovable(int x, int y, int rotation) {
        for (Point p : tetriminos[currentPieceID][rotation]) {
            if (board[p.x + x][p.y + y] != Color.black) {
                return false;
            }
        }
        return true;
    }

    // 0 - down, -1 - left, 1 - right
    private void moveInDirection(int direction) {
        if(direction != 0) {
            if(isMovable(currentPiecePosition.x + direction, currentPiecePosition.y, currentPieceRotation)) {
                currentPiecePosition.x += direction;
            }
        }
        else {
            if(isMovable(currentPiecePosition.x, currentPiecePosition.y + 1, currentPieceRotation)) {
                currentPiecePosition.y += 1;
            }
            else {
                placePieceOnBoard();
            }
        }
        repaint();
    }

    private void rotateClockwise() {
        int newPieceRotation = currentPieceRotation + 1;
        if(newPieceRotation > 3) {
            newPieceRotation = 0;
        }
        if(isMovable(currentPiecePosition.x, currentPiecePosition.y, newPieceRotation)) {
            currentPieceRotation = newPieceRotation;
        }
        repaint();
    }

    private void placePieceOnBoard() {
        for (Point p : tetriminos[currentPieceID][currentPieceRotation]) {
            board[p.x + currentPiecePosition.x][p.y + currentPiecePosition.y] = tetriminosColors[currentPieceID];
        }
        checkFilledRows();
        createNewPiece();
    }

    private void checkFilledRows() {
        for(int i = boardHeight - 2; i > 0; i--) {
            if(isRowFilled(i)) {
                deleteFilledRow(i);
                i += 1;
            }
        }
    }

    private boolean isRowFilled(int rowNum) {
        for(int j = 1; j < boardWidth - 1; j++) {
            if(board[j][rowNum] == Color.black) {
                return false;
            }
        }
        return true;
    }

    private void deleteFilledRow(int rowNum) {
        for (int i = rowNum - 1; i > 0; i--) {
            for (int j = 1; j < boardWidth - 1; j++) {
                board[j][i + 1] = board[j][i];
            }
        }
    }

    // [min; max)
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void drawPiece(Graphics g) {
        if(currentPieceID != -1) {
            g.setColor(tetriminosColors[currentPieceID]);
            for (Point p : tetriminos[currentPieceID][currentPieceRotation]) {
                g.fillRect(25 * (p.x + currentPiecePosition.x), 25 * (p.y + currentPiecePosition.y), 23, 23);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        g.fillRect(0, 0, 25 * boardWidth, 25 * boardHeight);
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                g.setColor(board[i][j]);
                if(board[i][j] != Color.black) {
                    g.fillRect(25 * i, 25 * j, 23, 23);
                }
                else {
                    g.fillRect(25 * i, 25 * j, 25, 25);
                }
            }
        }
        if(gameStatus == 1) {
            displayText(g, "Press Enter to start", 25, 250, 29);
        }
        if(gameStatus == -1) {
            displayText(g, "Game Over !", 40, 250, 40);
            displayText(g, "Press Enter to start", 25, 280, 29);
        }
        drawPiece(g);
    }

    private void displayText(Graphics g, String text, int x, int y, int size) {
        g.setFont(new Font("TimesRoman", Font.PLAIN, size));
        g.setColor(Color.black);
        g.drawString(text,x + 2,y - 2);
        g.drawString(text,x + 2,y + 2);
        g.drawString(text,x - 2,y - 2);
        g.drawString(text,x - 2,y + 2);
        g.setColor(Color.white);
        g.drawString(text,x,y);
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Tetris");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(315, 588);
        jFrame.setVisible(true);
        final Main game = new Main();
        game.fillBoard();
        jFrame.add(game);

        int moveDelay = 1000;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(game.getGameStatus() == 0) {
                    game.moveInDirection(0);
                }
            }
        };
        new Timer(moveDelay, taskPerformer).start();

        jFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(game.getGameStatus() == 1) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                            game.createNewPiece();
                            game.setGameStatus(0);
                            break;
                    }
                }
                if(game.getGameStatus() == 0) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            game.rotateClockwise();
                            break;
                        case KeyEvent.VK_DOWN:
                            game.moveInDirection(0);
                            break;
                        case KeyEvent.VK_LEFT:
                            game.moveInDirection(-1);
                            break;
                        case KeyEvent.VK_RIGHT:
                            game.moveInDirection(1);
                            break;
                    }
                }
                if(game.getGameStatus() == -1) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                            game.fillBoard();
                            game.createNewPiece();
                            game.setGameStatus(0);
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
}
