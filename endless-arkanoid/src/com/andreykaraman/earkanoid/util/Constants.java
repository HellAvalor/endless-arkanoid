package com.andreykaraman.earkanoid.util;

public class Constants {
	// Visible game world is 25 meters wide
	public static final float BASE_WIDTH = 320.0f;
	// Visible game world is 25 meters tall
	public static final float BASE_HEIGHT = 480.0f;
	// Visible game world is 25 meters wide
	public static final float VIEWPORT_WIDTH = 25.0f;
	// Visible game world is 25 meters tall
	public static final float VIEWPORT_HEIGHT = 37.5f;
	public static final float DEF_SCREEN_PROP = VIEWPORT_HEIGHT/VIEWPORT_WIDTH;
		// GUI Width
	public static final float VIEWPORT_GUI_WIDTH = 320.0f;
	// GUI Height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	// Amount of extra lives at level start
	public static final int LIVES_START = 3;
	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/earkanoid.pack";
	public static final String LEVEL_01 = "levels/level_01.png";
	// Delay after game over
	public static final float TIME_DELAY_GAME_OVER = 3;

	public enum BRICK_TYPE {
		UNDESTR, SIMPLE, DOUBLE, BONUS
	}
}
