//Name: Anand Raj
//Date: 2/25/26
//Description: This class creates the chess board and controls the main gameplay. 
package com.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, MouseMotionListener {

    private static final String path = "src/main/java/com/example/Pictures/";
    private static final String RESOURCES_BELEPHANT_PNG = path+"belephantNormal.png";
    private static final String RESOURCES_WELEPHANT_PNG = path+"welephantNormal.png";

    private final Square[][] board;
    private final GameWindow g;

    private boolean whiteTurn;

    Piece currPiece;
    private Square fromMoveSquare;

    private int currX;
    private int currY;

    public Board(GameWindow g) {
        this.g = g;
        board = new Square[8][8];
        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // create chessboard pattern
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){

                boolean lightSquare = (row + col) % 2 == 0;

                board[row][col] = new Square(this, lightSquare, row, col);
                this.add(board[row][col]);
            }
        }

        initializePieces();

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;
    }

    // places elephant pieces symmetrically for both sides
    void initializePieces() {

        board[0][1].put(new Piece(false, RESOURCES_BELEPHANT_PNG));
        board[0][6].put(new Piece(false, RESOURCES_BELEPHANT_PNG));

        board[7][1].put(new Piece(true, RESOURCES_WELEPHANT_PNG));
        board[7][6].put(new Piece(true, RESOURCES_WELEPHANT_PNG));
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    public Piece getCurrPiece() {
        return this.currPiece;
    }

    @Override
    public void paintComponent(Graphics g) {

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {

                Square sq = board[x][y];

                if(sq == fromMoveSquare)
                    sq.setBorder(BorderFactory.createLineBorder(Color.blue));

                sq.paintComponent(g);
            }
        }

        // draw piece while dragging
        if (currPiece != null) {
            Image img = currPiece.getImage();
            g.drawImage(img, currX, currY, null);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        currX = e.getX();
        currY = e.getY();

        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (sq.isOccupied()) {

            currPiece = sq.getOccupyingPiece();
            fromMoveSquare = sq;

            if(currPiece.getColor() != whiteTurn)
                return;

            sq.setDisplay(false);
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        Square endSquare = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if(fromMoveSquare != null && currPiece != null){

            if(currPiece.getLegalMoves(this, fromMoveSquare).contains(endSquare)){

                endSquare.put(currPiece);
                fromMoveSquare.removePiece();

                // switch turns
                whiteTurn = !whiteTurn;
            }

            fromMoveSquare.setDisplay(true);
        }

        currPiece = null;

        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        currX = e.getX() - 24;
        currY = e.getY() - 24;

        repaint();
    }

    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}