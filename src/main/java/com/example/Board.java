package com.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, MouseMotionListener {

    private static final String path = "/src/main/java/com/example/Pictures/";
    private static final String RESOURCES_WBISHOP_PNG = path+"wbishop.png";
    private static final String RESOURCES_BBISHOP_PNG = path+"bbishop.png";
    private static final String RESOURCES_WKNIGHT_PNG = path+"wknight.png";
    private static final String RESOURCES_BKNIGHT_PNG = path+"bknight.png";
    private static final String RESOURCES_WROOK_PNG = path+"wrook.png";
    private static final String RESOURCES_BROOK_PNG = path+"brook.png";
    private static final String RESOURCES_WKING_PNG = path+"wking.png";
    private static final String RESOURCES_BKING_PNG = path+"bking.png";
    private static final String RESOURCES_BQUEEN_PNG = path+"bqueen.png";
    private static final String RESOURCES_WQUEEN_PNG = path+"wqueen.png";
    private static final String RESOURCES_WPAWN_PNG = path+"wpawn.png";
    private static final String RESOURCES_BPAWN_PNG = path+"bpawn.png";
    private static final String RESOURCES_BELEPHANT_PNG = path+"belephant.png";
    private static final String RESOURCES_WELEPHANT_PNG = path+"welephant.png";

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

        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                if ((i + j) % 2 == 0){
                    board[i][j] = new Square(this, true, i, j);
                    this.add(board[i][j]);
                }
                else{
                    board[i][j] = new Square(this, false, i, j);
                    this.add(board[i][j]);
                }
            }
        }

        initializePieces();

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;
    }

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
        Image backgroundImage = null; 
        URL imageUrl = null;
        if (currPiece != null) {
            imageUrl = getClass().getResource("/src/main/java/com/example/"+currPiece.getImage());
        }

        if (imageUrl != null) {
            backgroundImage = Toolkit.getDefaultToolkit().createImage(imageUrl);
        } else {
            System.err.println("Image resource not found. Check path: /src/main/java/com/example/Pictures/");
        }

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[x][y];
                if(sq == fromMoveSquare)
                    sq.setBorder(BorderFactory.createLineBorder(Color.blue));
                sq.paintComponent(g);
                System.out.println("Painting square at " + x + ", " + y);   
            }
        }

        if (currPiece != null) {
            if ((currPiece.getColor() && whiteTurn)
                    || (!currPiece.getColor()&& !whiteTurn)) {
                final Image img = currPiece.getImage();
                g.drawImage(img, currX, currY, null);
            }
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
            if (currPiece.getColor() != whiteTurn)
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