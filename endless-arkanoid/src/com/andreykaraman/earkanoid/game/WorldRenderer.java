package com.andreykaraman.earkanoid.game;

import com.andreykaraman.earkanoid.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class WorldRenderer implements Disposable {
	private static final String TAG = WorldRenderer.class.getName();
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private OrthographicCamera cameraGUI;
	private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;
	private Box2DDebugRenderer b2debugRenderer;

	private float phisicsDensityX;
	private float phisicsDensityY;

	private float deltaX;
	private float deltaY;

	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}

	private void init() {
		recalcPhisDensity();
		batch = new SpriteBatch();
		// worldController.setPpuX( (float)Gdx.graphics.getWidth() /
		// Constants.BASE_WIDTH);
		// worldController.setPpuY( (float)Gdx.graphics.getHeight()
		// /Constants.BASE_HEIGHT);
		// Инициализация камеры и отрисовщика спрайтов.
		// camera = new OrthographicCamera(1, h/w);

		camera = new OrthographicCamera(phisicsDensityX, phisicsDensityY);
		camera.position.set(deltaX, deltaY, 0);
		// camera.position.set(0, 0, 0);
		camera.update();
		// cameraGUI = new OrthographicCamera(1, h/w);
		// cameraGUI = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
		// Constants.VIEWPORT_HEIGHT);
		// cameraGUI.position.set(w / 2, h / 2, 0);
		// cameraGUI.setToOrtho(true); // flip y-axis
		// cameraGUI.update();
		b2debugRenderer = new Box2DDebugRenderer();

	}

	public void render() {
		renderWorld(batch);
		// renderGui(batch);
	}

	private void renderWorld(SpriteBatch batch) {

		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// if (GamePreferences.instance.useMonochromeShader) {
		// batch.setShader(shaderMonochrome);
		// shaderMonochrome.setUniformf("u_amount", 1.0f);
		// }
		worldController.level.render(batch);
		batch.setShader(null);
		batch.end();
		if (DEBUG_DRAW_BOX2D_WORLD) {
			b2debugRenderer.render(worldController.b2world, camera.combined);
		}
	}

	@Override
	public void dispose() {
		batch.dispose();

	}

	public void resize(int width, int height) {

		recalcPhisDensity();
		camera.viewportHeight = phisicsDensityY;
		camera.viewportWidth = phisicsDensityX;
		camera.position.set(deltaX, deltaY, 0);
		camera.update();
		
		// camera.viewportWidth = width;
		// camera.viewportHeight = height;
		// camera.position.set(deltaX, deltaY, 0);
		// float w = Gdx.graphics.getWidth();
		// float h = Gdx.graphics.getHeight();
		//camera.viewportHeight = phisicsDensityX;
		//camera.viewportWidth = phisicsDensityY;
		// camera.viewportWidth = (h / height) * width;

		// cameraGUI.viewportHeight = h;
		// cameraGUI.viewportWidth = (h / (float) height)
		// * (float) width;
		// cameraGUI.viewportHeight = Constants.VIEWPORT_HEIGHT;
		// cameraGUI.viewportWidth = (Constants.VIEWPORT_HEIGHT / (float)
		// height)
		// * (float) width;
		// cameraGUI.position.set(cameraGUI.viewportWidth / 2,
		// cameraGUI.viewportHeight / 2, 0);
		// cameraGUI.update();
	}

	private void recalcPhisDensity() {

		float screenProp = ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics
				.getWidth());

		if (screenProp> Constants.DEF_SCREEN_PROP) {
			phisicsDensityX = Constants.VIEWPORT_WIDTH;
			phisicsDensityY = phisicsDensityX*screenProp;
			deltaX = phisicsDensityX / 2;
			deltaY = phisicsDensityY / 2
					- (phisicsDensityY - Constants.VIEWPORT_HEIGHT) / 2;
		} else if (screenProp < Constants.DEF_SCREEN_PROP) {
			phisicsDensityY = Constants.VIEWPORT_HEIGHT;
			phisicsDensityX = phisicsDensityY/screenProp;
			deltaX = phisicsDensityX / 2
					- (phisicsDensityX - Constants.VIEWPORT_WIDTH) / 2;
			deltaY = phisicsDensityY / 2;
		} else {
			phisicsDensityY = Constants.VIEWPORT_HEIGHT;
			phisicsDensityX = Constants.VIEWPORT_WIDTH;
			deltaX = phisicsDensityX / 2;
			deltaY = phisicsDensityY / 2;
		}

		worldController.setPhisicsDensityX(phisicsDensityX);
		worldController.setPhisicsDensityY(phisicsDensityY);
		
		Gdx.app.debug(TAG, "Gdx.graphics.getWidth() " + Gdx.graphics.getWidth()
				+ " Gdx.graphics.getHeight() " + Gdx.graphics.getHeight()
				+ " screenProp " + screenProp);
		Gdx.app.debug(TAG, "phisicsDensityX " + phisicsDensityX
				+ " phisicsDensityY " + phisicsDensityY + " deltaX " + deltaX
				+ " deltaY " + deltaY);

	}
}
