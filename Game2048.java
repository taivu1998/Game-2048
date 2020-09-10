/*
 * File: Game2048.java
 * --------------------------
 * 
 * This file implements the game of 2048. The player plays them game on a 4x4 grid, and the main task is 
 * to move numbered tiles and combine them to get bigger numbers. If two squares with identical numbers 
 * collide, they will merge into a new square with the sum of the two values. The player win if he or she 
 * reaches 2048, although the user can continue to play after attaining that target. The players loses when 
 * there is no way to move the tiles, which means their are no more empty spaces and no pairs of adjacent 
 * squares with the same value.
 */

import acm.program.*;
import acm.util.RandomGenerator;
import acm.graphics.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game2048 extends GraphicsProgram implements Game2048Constants {

    /** Instance variable containing all the numbers displayed on the tiles */
    private int[][] gridNum;

    /** Instance variable containing all the tiles */
    private GCompound[][] gridSquare;

    /** Instance variable serving as a random-number generator */
    private RandomGenerator rgen = RandomGenerator.getInstance();

    /** Player's maximum number displayed */
    private int max;

    /** Label displaying player's maximum number */
    private GLabel labelMax;

    /* Method: run() */
    /**
     * Sets up the game of 2048 and allows the user to play it.
     */
    public void run() {
        setup();
        play();
    }

    /* Method: setup() */
    /**
     * Sets up the display of the game and the grids used to keep track of the numbers and the graphics.
     */
    private void setup() {
        setupWindow();
        setupArrays();
    }

    /* Method: setupWindow() */
    /**
     * Set up the symbol 2048 and add interactive buttons to the south region of the window.
     */
    private void setupWindow() {
        logo2048();
        add(new JButton ("UP"), SOUTH);
        add(new JButton ("DOWN"), SOUTH);
        add(new JButton ("LEFT"), SOUTH);
        add(new JButton ("RIGHT"), SOUTH);
    }

    /* Method: setupArrays() */
    /**
     * Initiates the grids keeping track of the numbers and the tiles in the game.
     */
    private void setupArrays() {
        gridNum = new int[DIMENSION][DIMENSION];
        gridSquare = new GCompound[DIMENSION][DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                gridSquare[i][j] = squareWithNumber((j+1)*SPACE + (j)*SQUARE_SIZE, 
                                                     OFFSET - APPLICATION_SOUTH + (i+1)*SPACE + (i)*SQUARE_SIZE, 
                                                     gridNum[i][j]);
                add(gridSquare[i][j]);
            }
        }   
    }

    /* Method: play() */
    /**
     * Starts the game by displaying new numbers and adding action listeners that allows the user to interact with the game.
     */
    private void play() {
        addNewNumbers();
        updateGraphics();
        max = maxNum();
        displayMax();
        addActionListeners();
    }

    /* Method: actionPerformed() */
    /**
     * Listens when the player clicks the buttons and responds to it accordingly.
     * @param e: action event.
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("UP")) {
            moveUp();
        } else if (cmd.equals("DOWN")) {
            moveDown();
        } else if (cmd.equals("LEFT")) {
            moveLeft();
        } else if (cmd.equals("RIGHT")) {
            moveRight();
        }
        addNewNumbers();
        updateGraphics();
    }

    /* Method: moveUp() */
    /**
     * Moves all the tiles upwards and combines adjacent identical numbers.
     */
    private void moveUp() {
        for (int j = 0; j < DIMENSION; j++) {
            int[] column_j = new int[DIMENSION];
            for (int i = 0; i < DIMENSION; i++) {
                column_j[i] = gridNum[i][j];
            }
            updateColumn(j, combineArraytoBeginning(column_j));
        }
        update();
    }

    /* Method: moveDown() */
    /**
     * Moves all the tiles downwards and combines adjacent identical numbers.
     */
    private void moveDown() {
        for (int j = 0; j < DIMENSION; j++) {
            int[] column_j = new int[DIMENSION];
            for (int i = 0; i < DIMENSION; i++) {
                column_j[i] = gridNum[i][j];
            }
            updateColumn(j, combineArraytoEnd(column_j));
        }
        update();
    }

    /* Method: moveLeft() */
    /**
     * Moves all the tiles to the left and combines adjacent identical numbers.
     */
    private void moveLeft() {
        for (int i = 0; i < DIMENSION; i++) {
            updateRow(i, combineArraytoBeginning(gridNum[i]));
        }
        update();
    }

    /* Method: moveRight() */
    /**
     * Moves all the tiles to the right and combines adjacent identical numbers.
     */
    private void moveRight() {
        for (int i = 0; i < DIMENSION; i++) {
            updateRow(i, combineArraytoEnd(gridNum[i]));
        }
        update();
    }

    /* Method: update() */
    /**
     * Updates the graphics, edits the maximum number displayed and checks if the player wins or loses the game.
     */
    private void update() {
        updateGraphics();
        max = maxNum();
        remove(labelMax);
        displayMax();
        if (max >= WINNING_NUMBER) {
            win();
        } else if (gameOver()) {
            lose();
        }
    }

    /* Method: gameOver() */
    /**
     * Check if their are no more empty spaces and no pairs of adjacent squares with the same value.
     */
    private boolean gameOver() {
        int product1 = 1;
        int product2 = 1;
        int product3 = 1;

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                product1 *= gridNum[i][j];
            }
        }

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION - 1; j++) {
                product2 *= (gridNum[i][j] - gridNum[i][j + 1]);
            }
        }

        for (int j = 0; j < DIMENSION; j++) {
            for (int i = 0; i < DIMENSION - 1; i++) {
                product3 *= (gridNum[i][j] - gridNum[i + 1][j]);
            }
        }

        return (product1 * product2 * product3 != 0);
    }

    /* Method: addNewNumbers() */
    /**
     * Display new numbers in the set of tiles.
     */
    private void addNewNumbers() {
        if (hasEmptySpace()) {
            while (true) {
                int i = rgen.nextInt(0, DIMENSION-1);
                int j = rgen.nextInt(0, DIMENSION-1);
                if (gridNum[i][j] == 0) {
                    gridNum[i][j] = 2;
                    break;
                }
            }
        }
    }

    /* Method: hasEmptySpace() */
    /**
     * Checks if there are any empty spaces.
     */
    private boolean hasEmptySpace() {
        int product = 1;
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                product *= gridNum[i][j];
            }
        }
        return (product == 0);
    }

    /* Method: combineArraytoBeginning() */
    /**
     * Moves nonzero numbers towards the beginning of the array and merges adjacent identical numbers.
     * @param array: the array containing integers.
     */
    private int[] combineArraytoBeginning(int[] array) {
        int[] array1 = moveZerosToEnd(array);
        int[] array2 = addIdenticalNumbers(array1);
        int[] array3 = moveZerosToEnd(array2);
        return array3;
    }

    /* Method: combineArraytoEnd() */
    /**
     * Moves nonzero numbers towards the end of the array and merges adjacent identical numbers.
     * @param array: the array containing integers.
     */
    private int[] combineArraytoEnd(int[] array) {
        int[] arrayReversed = reverseArray(array);
        int[] arrayCombined = combineArraytoBeginning(arrayReversed);
        array = reverseArray(arrayCombined);
        return array;
    }

    /* Method: moveZerosToEnd() */
    /**
     * Move all the zeros towards the end of the array.
     * @param array: the array containing integers.
     */
    private int[] moveZerosToEnd(int[] array) {
        int length = array.length;
        for (int i = length - 1; i >= 0; i--) {
            if (array[i] == 0) {
                for (int j = i; j < length - 1; j++) {
                    array[j] = array[j+1];
                }
                array[length-1] = 0;
            }
        }
        return array;
    }

    /* Method: addIdenticalNumbers() */
    /**
     * Adds adjacent identical numbers.
     * @param array: the array containing integers.
     */
    private int[] addIdenticalNumbers(int[] array) {
        int length = array.length;
        for (int i = 0; i < length - 1; i++) {
            if (array[i] == array[i+1]) {
                array[i] *= 2;
                array[i+1] = 0;
            }
        }
        return array;
    }

    /* Method: reverseArray() */
    /**
     * Reverses the order of the numbers in an array.
     * @param array: the array containing integers.
     */
    private int[] reverseArray(int[] array) {
        int length = array.length;
        int[] arrayCopy = new int[length];
        for (int i = 0; i < length; i++) {
            arrayCopy[i] = array[length - 1 - i];
        }
        return arrayCopy;
    }

    /* Method: log2() */
    /**
     * Calculates the logarithm base 2 of a number.
     * @param num: an integer.
     */
    private int log2(int num) {
        return (int) (Math.log(num) / Math.log(2));
    }

    /* Method: updateRow() */
    /**
     * Updates the values in a row with a new array.
     * @param array: the array containing integers.
     */
    private void updateRow(int i, int[] array) {
        for (int j = 0; j < DIMENSION; j++) {
            gridNum[i][j] = array[j];
        }
    }

    /* Method: updateColumn() */
    /**
     * Updates the values in a column with a new array.
     * @param array: the array containing integers.
     */
    private void updateColumn(int j, int[] array) {
        for (int i = 0; i < DIMENSION; i++) {
            gridNum[i][j] = array[i];
        }
    }

    /* Method: updateGraphics() */
    /**
     * Updates the tiles after each time the player clicks the button.
     */
    private void updateGraphics() {
        removeAllSquares();
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                gridSquare[i][j] = squareWithNumber((j+1)*SPACE + (j)*SQUARE_SIZE, 
                                                     OFFSET - APPLICATION_SOUTH + (i+1)*SPACE + (i)*SQUARE_SIZE, 
                                                     gridNum[i][j]);
                add(gridSquare[i][j]);
            }
        }
    }

    /* Method: squareWithNumber() */
    /**
     * Draws the tiles containing numbers.
     * @param x: the x-coordinate of the tile.
     * @param y: the y-coordinate of the tile.
     * @param num: the number displayed in the tile.
     */
    private GCompound squareWithNumber(double x, double y, int num) {
        GCompound compound = new GCompound();
        GRect square = new GRect (x, y, SQUARE_SIZE, SQUARE_SIZE);
        square.setFilled(true);

        GLabel label = new GLabel (Integer.toString(num));
        label.setFont("SansSerif-40");
        double m = x + (SQUARE_SIZE - label.getWidth()) / 2;
        double n = y + (SQUARE_SIZE + label.getAscent()) / 2;
        label.setLocation(m, n);
        label.setColor(Color.BLACK);

        if (num > 0) {
            switch (log2(num) %6) {
            case 1: square.setColor(Color.YELLOW); break;
            case 2: square.setColor(Color.GREEN); break;
            case 3: square.setColor(Color.PINK); break;
            case 4: square.setColor(Color.CYAN); break;
            case 5: square.setColor(Color.ORANGE); break;
            default: square.setColor(Color.MAGENTA); break;
            }
        } else {
            square.setColor(Color.LIGHT_GRAY);
        }

        compound.add(square);
        if (num != 0) {
            compound.add(label);
        }
        return compound;
    }

    /* Method: removeAllSquares() */
    /**
     * Removes all the squares.
     */
    private void removeAllSquares() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                remove(gridSquare[i][j]);
            }
        }
    }

    /* Method: maxNum() */
    /**
     * Computes the maximum number the player has reached.
     */
    private int maxNum() {
        int max = 0;
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if (gridNum[i][j] > max) {
                    max = gridNum[i][j];
                }
            }
        }
        return max;
    }

    /* Method: displayMax() */
    /**
     * Displays the maximum number the player has reached.
     */
    private void displayMax() {
        labelMax = new GLabel ("Max: " + max);
        labelMax.setFont("SansSerif-50");
        labelMax.setColor(Color.MAGENTA);
        double x = getWidth()/2 + (getWidth()/2 - labelMax.getWidth()) / 2;
        double y = ((OFFSET - APPLICATION_SOUTH)/2 + labelMax.getAscent()) / 2;
        labelMax.setLocation(x, y);
        add(labelMax);
    }

    /* Method: lose() */
    /**
     * Prints the label GAME OVER on the screen.
     */
    private void lose() {
        GObject object = getElementAt(5*getWidth()/8, 3*(OFFSET - APPLICATION_SOUTH)/4);
        if (object != null) {
            remove(object);
        }

        GLabel label = new GLabel ("Game Over");
        label.setFont("SansSerif-50");
        label.setColor(Color.RED);
        double x = getWidth()/4 + (3*getWidth()/4 - labelMax.getWidth()) / 2;
        double y = (OFFSET - APPLICATION_SOUTH)/2 + ((OFFSET - APPLICATION_SOUTH)/2 + labelMax.getAscent()) / 2;
        label.setLocation(x, y);
        add(label);
    }

    /* Method: logo2048() */
    /**
     * Draws the symbol 2048.
     */
    private void logo2048() {
        GRect rect = new GRect (LOGO_SIZE, LOGO_SIZE);
        rect.setFilled(true);
        rect.setColor(Color.GREEN);
        double x1 = (getWidth()/2 - LOGO_SIZE) / 2;
        double y1 = ((OFFSET - APPLICATION_SOUTH) - LOGO_SIZE) / 2;
        rect.setLocation(x1, y1);
        add(rect);

        GLabel label = new GLabel ("2048");
        label.setFont("SansSerif-BOLD-40");
        label.setColor(Color.RED);
        double x2 = (getWidth()/2 - label.getWidth()) / 2;
        double y2 = ((OFFSET - APPLICATION_SOUTH) + label.getAscent()) / 2;
        label.setLocation(x2, y2);
        add(label);
    }

    /* Method: win() */
    /**
     * Prints the label YOU'VE ACED 2048 on the screen.
     */
    private void win() {
        GLabel label = new GLabel ("You've aced 2048!");
        label.setFont("SansSerif-30");
        label.setColor(Color.BLUE);
        double x = getWidth()/4 + (3*getWidth()/4 - labelMax.getWidth()) / 2;
        double y = (OFFSET - APPLICATION_SOUTH)/2 + ((OFFSET - APPLICATION_SOUTH)/2 + labelMax.getAscent()) / 2;
        label.setLocation(x, y);
        add(label);
    }

}
