/*
 * File: GraphicsContest.java
 * --------------------------
 */

import acm.program.*;
import acm.graphics.*;
import java.awt.event.*;

public interface Game2048Constants {

    /** The width of the application window */
    public static final int APPLICATION_WIDTH = 600;

    /** The height of the application window */
    public static final int APPLICATION_HEIGHT = 800;

    /** The dimension of the set of tiles */
    public static final int DIMENSION = 4;

    /** The target that the player needs to reach in order to win the game */
    public static final int WINNING_NUMBER = 2048;

    /** The offset above the set of tiles */
    public static final double OFFSET = APPLICATION_HEIGHT - APPLICATION_WIDTH;

    /** The space between squares */
    public static final double SPACE = 10;

    /** The size of the south region of the window */
    public static final int APPLICATION_SOUTH = 50;

    /** The size of each square */
    public static final double SQUARE_SIZE = (APPLICATION_WIDTH - (DIMENSION + 1)*SPACE) / DIMENSION;

    /** The size of the logo 2048 */
    public static final double LOGO_SIZE = 120;

}
