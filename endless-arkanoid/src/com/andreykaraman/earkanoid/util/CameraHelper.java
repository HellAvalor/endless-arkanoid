package com.andreykaraman.earkanoid.util;

import com.andreykaraman.earkanoid.game.objects.AbstractGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraHelper {
	private static final String TAG = CameraHelper.class.getName();
	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;
	private Vector2 position;
	private float zoom;
	private AbstractGameObject target;
	private final float FOLLOW_SPEED = 4.0f;

	public CameraHelper() {
		position = new Vector2();
		zoom = 1.0f;
	}

	public void update(float deltaTime) {
		if (!hasTarget())
			return;
		position.lerp(target.position, FOLLOW_SPEED * deltaTime);

		// Prevent camera from moving down too far
	//	position.y = Math.max(-1f, position.y);
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void addZoom(float amount) {
		setZoom(zoom + amount);
	}

	public void setZoom(float zoom) {
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}

	public float getZoom() {
		return zoom;
	}

	public void setTarget(AbstractGameObject target) {
		this.target = target;
	}

	public AbstractGameObject getTarget() {
		return target;
	}

	public boolean hasTarget() {
		return target != null;
	}

	public boolean hasTarget(AbstractGameObject target) {
		return hasTarget() && this.target.equals(target);
	}

	public void applyTo(OrthographicCamera camera) {
	//	camera.position.x = position.x;
	//	camera.position.y = position.y;
	//	camera.position.set(Constants.VIEWPORT_WIDTH/2,
	//			Constants.VIEWPORT_HEIGHT/2, 0);
	//	camera.position.x = 0;
	//	camera.position.y = 0;
		camera.zoom = zoom;
		camera.update();
	}
	
	
}