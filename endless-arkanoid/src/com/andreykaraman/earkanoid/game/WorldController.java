package com.andreykaraman.earkanoid.game;

import java.util.ArrayList;

import com.andreykaraman.earkanoid.game.objects.Ball;
import com.andreykaraman.earkanoid.game.objects.Brick;
import com.andreykaraman.earkanoid.game.screens.DirectedGame;
import com.andreykaraman.earkanoid.util.BodyEditorLoader;
import com.andreykaraman.earkanoid.util.CameraHelper;
import com.andreykaraman.earkanoid.util.Constants;
import com.andreykaraman.earkanoid.util.Constants.BRICK_TYPE;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class WorldController extends InputAdapter implements Disposable {
	public Level level;
	public int lives;
	public int score;
	private static final String TAG = WorldController.class.getName();
	public CameraHelper cameraHelper;
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Circle r2 = new Circle();
	private Rectangle r3 = new Rectangle();
	private float timeLeftGameOverDelay;
	private float speed = 5f;
	private float platformSpeed = 100f;
	public float livesVisual;
	public float scoreVisual;
	public static ArrayList<Body> deleteBodyArray;
	private DirectedGame game;
	private boolean goalReached;

	private float phisicsDensityX;
	private float phisicsDensityY;

	public World b2world;

	public WorldController(DirectedGame game) {
		this.game = game;
		init();
	}

	private void init() {
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
	}

	private void initLevel() {
		score = 0;
		scoreVisual = score;
		goalReached = false;
		level = new Level(Constants.LEVEL_01);
		// cameraHelper.setTarget(level.platform);
		initPhysics();
	}

	private void initPhysics() {
		if (b2world != null)
			b2world.dispose();
		deleteBodyArray = new ArrayList<Body>();
		b2world = new World(new Vector2(0, 0f), true);
		Vector2 origin = new Vector2();
		// Platform physics
		BodyDef bodyDef = new BodyDef();

		// 0. Create a loader for the file saved from the editor.
		BodyEditorLoader loader = new BodyEditorLoader(
				Gdx.files.internal("assets-raw/data/platform.json"));

		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(level.platform.position);
		Body body = b2world.createBody(bodyDef);
		body.setFixedRotation(true);
		level.platform.body = body;
		PolygonShape polygonShape = new PolygonShape();
		origin.x = level.platform.dimension.x / 2.0f;
		origin.y = level.platform.dimension.y / 2.0f;
		polygonShape.setAsBox(level.platform.dimension.x / 2.0f,
				level.platform.dimension.y / 2.0f, origin, 0);
		FixtureDef fixtureDef = new FixtureDef();
		// fixtureDef.shape = polygonShape;
		fixtureDef.density = 1000;

		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(body, "platform.png", fixtureDef, origin.x * 2);

		// level.platform.body.setLinearVelocity(50, 0);
		// level.platform.body.createFixture(fixtureDef);
		level.platform.body.getFixtureList().get(0).setUserData("platform");
		polygonShape.dispose();

		for (Ball ballForPhysics : level.balls) {

			setBall(ballForPhysics, ballForPhysics.position);
		}

		for (Brick brickForPhysics : level.bricks) {
			bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set(brickForPhysics.position);
			body = b2world.createBody(bodyDef);

			brickForPhysics.body = body;

			polygonShape = new PolygonShape();
			origin.x = brickForPhysics.dimension.x / 2.0f;
			origin.y = brickForPhysics.dimension.y / 2.0f;
			polygonShape.setAsBox(brickForPhysics.dimension.x / 2.0f,
					brickForPhysics.dimension.y / 2.0f, origin, 0);
			fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;

			brickForPhysics.body.createFixture(fixtureDef);
			if (brickForPhysics.type == BRICK_TYPE.UNDESTR) {
				brickForPhysics.body.getFixtureList().get(0)
						.setUserData("und_brick");
			} else {
				brickForPhysics.body.getFixtureList().get(0)
						.setUserData("brick");
			}
			polygonShape.dispose();
		}

		b2world.setContactListener(new MyContactListener(b2world));

	}

	public void update(float deltaTime) {
		handleDebugInput(deltaTime);

		if (isGameOver() || goalReached) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0)
				;// backToMenu();
		} else {
			handleInputGame(deltaTime);
		}

		// testCollisions();

		b2world.step(deltaTime, 8, 3);
		level.update(deltaTime);

		for (int i = 0; i < level.balls.size; i++) {

			if (level.balls.get(i).position.y < -0.5) {

				deleteBodyArray.add(level.balls.get(i).body);
				level.balls.removeIndex(i);
				i--;
				Gdx.app.debug(TAG, "Delete obj " + level.bricks.size + " " + i);
			}
		}

		for (int i = 0; i < level.bricks.size; i++) {

			if (level.bricks.get(i).body.getFixtureList().get(0).getUserData()
					.equals("delete")) {
				level.bricks.removeIndex(i);
				i--;
				Gdx.app.debug(TAG, "Delete obj " + level.bricks.size + " " + i);
			}
		}

		for (Body body : deleteBodyArray) {
			b2world.destroyBody(body);
		}

		if (deleteBodyArray != null)
			deleteBodyArray.clear();

		cameraHelper.update(deltaTime);

		if (!isGameOver() && level.balls.size == 0) {
			// AudioManager.instance.play(Assets.instance.sounds.liveLost);
			Gdx.app.debug(TAG, "lives " + lives);
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}

		if (livesVisual > lives)
			livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
		if (scoreVisual < score)
			scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);
	}

	private void handleInputGame(float deltaTime) {
		// if (cameraHelper.hasTarget(level.platform)) {
		// Player Movement
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {

			platformLeft();
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {

			platformRight();
		} else if (Gdx.input.isTouched()) {
			ChangeNavigation2(Gdx.input.getX(), Gdx.input.getY());
		} else {
			platformStop();
		}

	}

	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop)
			return;
		// Selected Sprite Controls
		if (!cameraHelper.hasTarget(level.platform)) {
			// Camera Controls (move)
			float camMoveSpeed = 50 * deltaTime;
			float camMoveSpeedAccelerationFactor = 50;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP))
				moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN))
				moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
				cameraHelper.setPosition(0, 0);
		}
		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD))
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH))
			cameraHelper.setZoom(1);
	}

	public boolean keyUp(int keycode) {
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		// Toggle camera follow

		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null
					: level.platform);
			Gdx.app.debug(TAG,
					"Camera follow enabled: " + cameraHelper.hasTarget());
		}

		// Back to Menu
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			// backToMenu();
		}

		else if (keycode == Keys.SPACE) {
			addBall(level.balls.get(0));
		}
		return false;
	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	public boolean isGameOver() {
		return lives < 0;

	}

	@Override
	public void dispose() {
		if (b2world != null)
			b2world.dispose();
	}

	private void ChangeNavigation2(int x, int y) {
		float touchX = x * (phisicsDensityX / Gdx.graphics.getWidth())
				- (phisicsDensityX - Constants.VIEWPORT_WIDTH) / 2;
		float delta = Math.abs(touchX - level.platform.body.getPosition().x);

		Gdx.app.debug(TAG, "-------------ChangeNavigation2-----------------");
		Gdx.app.debug(TAG, "platfrom x " + level.platform.body.getPosition().x
				+ " toch x " + touchX + " x " + x);
		Gdx.app.debug(TAG,
				" Gdx.graphics.getWidth() " + Gdx.graphics.getWidth()
						+ " phisicsDensityX " + phisicsDensityX + " delta "
						+ delta);
		touchX = Math.max(1f + level.platform.dimension.x / 2, touchX);
		touchX = (float) Math.min(Constants.VIEWPORT_WIDTH - 1
				- level.platform.dimension.x / 2, touchX);
		// если левее выше перса, то указываем, чтобы игрок двигался влево
		/*
		 * if (touchX < level.platform.body.getPosition().x) {
		 * platformLeft(delta);
		 * }
		 * // если кликнули правее перса, то указываем, чтобы игрок двигался
		 * вправо
		 * if (touchX > level.platform.body.getPosition().x) {
		 * platformRight(delta);
		 * }
		 */
		platformPositionSet(touchX);
		// float delta = Math.abs(touchX - level.platform.body.getPosition().x);
	}



	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {

		if (!Gdx.app.getType().equals(ApplicationType.Android))
			return false;
		ChangeNavigation2(x, y);
		Gdx.app.debug(TAG, "-------------touchDown-----------------");
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (!Gdx.app.getType().equals(ApplicationType.Android))
			return false;

		platformStop();
		Gdx.app.debug(TAG, "-------------touchUp-----------------");
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// ChangeNavigation(x, y);
		ChangeNavigation2(x, y);
		Gdx.app.debug(TAG, "-------------touchDragged-----------------");
		return false;
	}

	private void platformLeft() {
		float plSpeed = Math.max(
				level.platform.body.getLinearVelocity().x - 1f, -platformSpeed);
		level.platform.body.setLinearVelocity(plSpeed, 0);
	}

	private void platformRight() {
		float plSpeed = Math.min(
				level.platform.body.getLinearVelocity().x + 1f, platformSpeed);
		level.platform.body.setLinearVelocity(plSpeed, 0);
	}

	private void platformLeft(float delta) {
		delta = delta / Constants.VIEWPORT_WIDTH;
		float plSpeed = Math.max(
				level.platform.body.getLinearVelocity().x - 1f, -delta
						* platformSpeed);
		level.platform.body.setLinearVelocity(plSpeed, 0);
	}

	private void platformRight(float delta) {
		delta = delta / Constants.VIEWPORT_WIDTH;
		float plSpeed = Math.min(
				level.platform.body.getLinearVelocity().x + 1f, delta
						* platformSpeed);
		level.platform.body.setLinearVelocity(plSpeed, 0);
	}

	private void platformStop() {
		level.platform.body.setLinearVelocity(0, 0);
	}

	private void platformPositionSet(float x) {
		level.platform.body.setTransform(x - level.platform.dimension.x / 2, 0,
				0);
	}

	public void setPhisicsDensityX(float phisicsDensityX) {
		this.phisicsDensityX = phisicsDensityX;
	}

	public void setPhisicsDensityY(float phisicsDensityY) {
		this.phisicsDensityY = phisicsDensityY;
	}

	private void addBall(Ball copyBall) {

		Ball ball = new Ball();
		setBall(ball, copyBall.position);
		level.balls.add(ball);
	}

	private void setBall(Ball ball, Vector2 postition) {

		ball.position.set(postition);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(ball.position);
		bodyDef.angle = 0f;

		Body body = b2world.createBody(bodyDef);

		ball.body = body;
		ball.body.setLinearVelocity(new Vector2(speed, speed));
		ball.body.setActive(true);
		CircleShape circleShape = new CircleShape();

		circleShape.setRadius(ball.dimension.x / 2);
		FixtureDef fixtureDef = new FixtureDef();

		fixtureDef.shape = circleShape;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f;
		fixtureDef.density = 0.1f;
		// body.getFixtureList().get(0).setSensor(true);

		// body.getFixtureList().get(0).setUserData("ball");
		// ballForPhysics.body.getFixtureList().get(0).setUserData("ball");
		ball.body.createFixture(fixtureDef);
		ball.body.getFixtureList().get(0).setUserData("ball");
		circleShape.dispose();
	}

}
