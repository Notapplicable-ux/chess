//Name: Anand Raj
//Date: 2/25/26
//Description: This class represents a chess piece on the board. 
package com.example;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Piece {

    private final boolean color;
    private BufferedImage img;

    public Piece(boolean isWhite, String img_file) {

        this.color = isWhite;

        try {
            if (this.img == null) {
                this.img = ImageIO.read(new File(img_file));
            }
        }
        catch (IOException e) {
            System.out.println("File not found: " + img_file);
        }
    }

    public boolean getColor() {
        return color;
    }

    public Image getImage() {
        return img;
    }

    public void draw(Graphics g, Square currentSquare) {

        int x = currentSquare.getX();
        int y = currentSquare.getY();

        g.drawImage(this.img, x, y, null);
    }

    // returns squares this piece could capture
    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {

        ArrayList<Square> controlled = new ArrayList<>();

        int row = start.getRow();
        int col = start.getCol();

        int[][] moves = {
                {3,2},{3,-2},{-3,2},{-3,-2},
                {2,3},{2,-3},{-2,3},{-2,-3}
        };

        for(int[] m : moves){

            int newRow = row + m[0];
            int newCol = col + m[1];

            if(newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8){
                controlled.add(board[newRow][newCol]);
            }
        }

        return controlled;
    }

    // returns legal movement squares
    public ArrayList<Square> getLegalMoves(Board b, Square start){

        ArrayList<Square> legalMoves = new ArrayList<>();

        Square[][] board = b.getSquareArray();

        int row = start.getRow();
        int col = start.getCol();

        int[][] moves = {
                {3,2},{3,-2},{-3,2},{-3,-2},
                {2,3},{2,-3},{-2,3},{-2,-3}
        };

        for(int[] m : moves){

            int newRow = row + m[0];
            int newCol = col + m[1];

            if(newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8){

                Square target = board[newRow][newCol];

                if(!target.isOccupied()){
                    legalMoves.add(target);
                }
                else if(target.getOccupyingPiece().getColor() != this.color){
                    legalMoves.add(target);
                }
            }
        }

        return legalMoves;
    }
}