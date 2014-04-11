package com.andreykaraman.earkanoid.game;

import com.andreykaraman.earkanoid.game.objects.AbstractGameObject;
import com.andreykaraman.earkanoid.game.objects.Ball;
import com.andreykaraman.earkanoid.game.objects.Brick;
import com.andreykaraman.earkanoid.game.objects.Platform;
import com.andreykaraman.earkanoid.util.Constants.BRICK_TYPE;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class Level {
	public static final String TAG = Level.class.getName();
	public Platform platform;
	public Array<Ball> balls;
	public Array<Brick> bricks;

	// public Array<GoldCoin> goldcoins;
	// public Array<Feather> feathers;
	// public Array<Carrot> carrots;
	// public Goal goal;
	// objects
	// public Array<Rock> rocks;
	// decoration
	// public Clouds clouds;
	// public Mountains mountains;
	// public WaterOverlay waterOverlay;

	public enum BLOCK_TYPE {
		BALL(255, 0, 0), // red
		EMPTY(0, 0, 0), // black
		PLATFORM(255, 255, 255), // white
		UNDESTR_BRICK(255, 0, 255), // pink
		SIMPLE_BRICK(0, 255, 0), // green
		DOUBLE_BRICK(0, 0, 255), // blue ??
		BONUS_BRICK(255, 255, 0); // Yellow ??

		private int color;

		private BLOCK_TYPE(int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}

		public boolean sameColor(int color) {
			return this.color == color;
		}

		public int getColor() {
			return color;
		}
	}

	public Level(String filename) {
		init(filename);
	}

	private void init(String filename) {
		// player character
		platform = null;
		// objects
		balls = new Array<Ball>();
		bricks = new Array<Brick>();
		// goldcoins = new Array<GoldCoin>();
		// feathers = new Array<Feather>();
		// carrots = new Array<Carrot>();
		// load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		// scan pixels from top-left to bottom-right
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				float heightIncreaseFactor = -1f;
				// height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;

				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				// find matching color value to identify block type at (x,y)
				// point and create the corresponding game object if there is
				// a match
				// empty space
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					// do nothing
				}

				// platform spawn point
				else if (BLOCK_TYPE.PLATFORM.sameColor(currentPixel)) {
					obj = new Platform();

					obj.position.set(pixelX,
							(baseHeight + heightIncreaseFactor) / 2);
					platform = (Platform) obj;
				}
				// ball
				else if (BLOCK_TYPE.BALL.sameColor(currentPixel)) {
					obj = new Ball();

					// offsetHeight = -2.5f;
					obj.position.set(pixelX,
							(baseHeight + heightIncreaseFactor) / 2);
					balls.add((Ball) obj);

				} else if (BLOCK_TYPE.UNDESTR_BRICK.sameColor(currentPixel)) {
					obj = new Brick(BRICK_TYPE.UNDESTR);
			
				//	offsetHeight = -1f;
					obj.position.set(pixelX,
							(baseHeight + heightIncreaseFactor) / 2);
					bricks.add((Brick) obj);

				} else if (BLOCK_TYPE.SIMPLE_BRICK.sameColor(currentPixel)) {
					obj = new Brick(BRICK_TYPE.SIMPLE);

					// offsetHeight = -2.5f;
					obj.position.set(pixelX,
							(baseHeight + heightIncreaseFactor) / 2);
					bricks.add((Brick) obj);

				} else if (BLOCK_TYPE.DOUBLE_BRICK.sameColor(currentPixel)) {
					obj = new Brick(BRICK_TYPE.DOUBLE);
					
					//offsetHeight = -1f;
					obj.position.set(pixelX,
							(baseHeight + heightIncreaseFactor) / 2);
					bricks.add((Brick) obj);

				} else if (BLOCK_TYPE.BONUS_BRICK.sameColor(currentPixel)) {
					obj = new Brick(BRICK_TYPE.BONUS);

					// offsetHeight = -2.5f;
					obj.position.set(pixelX,
							(baseHeight + heightIncreaseFactor) / 2);
					bricks.add((Brick) obj);

				}

				// unknown object/pixel color
				else {
					int r = 0xff & (currentPixel >>> 24); // red color channel
					int g = 0xff & (currentPixel >>> 16); // green color channel
					int b = 0xff & (currentPixel >>> 8); // blue color channel
					int a = 0xff & currentPixel; // alpha channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
							+ pixelY + ">: r<" + r + "> g<" + g + "> b<" + b
							+ "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}
		// decoration
		/*
		 * clouds = new Clouds(pixmap.getWidth()); clouds.position.set(0, 2);
		 * mountains = new Mountains(pixmap.getWidth());
		 * mountains.position.set(-1, -1); waterOverlay = new
		 * WaterOverlay(pixmap.getWidth()); waterOverlay.position.set(0,
		 * -3.75f);
		 */// free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}

	public void render(SpriteBatch batch) {

		// Draw Player Character
		platform.render(batch);
		for (Ball ball : balls)
			ball.render(batch);
		for (Brick brick : bricks)
			brick.render(batch);

	}

	public void update(float deltaTime) {
		platform.update(deltaTime);
		for (Ball ball : balls)
			ball.update(deltaTime);
		for (Brick brick : bricks)
			brick.update(deltaTime);

	}

}