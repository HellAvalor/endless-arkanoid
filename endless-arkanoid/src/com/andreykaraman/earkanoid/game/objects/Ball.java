package com.andreykaraman.earkanoid.game.objects;

import com.andreykaraman.earkanoid.game.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.graphics.g2d.Animation;


public class Ball extends AbstractGameObject {
	public static final String TAG = Ball.class.getName();

	private Animation animNormal;



	public Ball() {
		init();
	}

	public void init() {

		dimension.set(0.5f, 0.5f);
		animNormal = Assets.instance.ball.animNormal;
		// animCopterTransform = Assets.instance.bunny.animCopterTransform;
		// animCopterTransformBack =
		// Assets.instance.bunny.animCopterTransformBack;
		// animCopterRotate = Assets.instance.bunny.animCopterRotate;
		setAnimation(animNormal);
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// Bounding box for collision detection
	//	bounds.set(0, 0, dimension.x, dimension.y);
		// Set physics values
		terminalVelocity.set(10f, 10f);
		friction.set(0.0f, 0.0f);
		acceleration.set(0f, 0f);
		velocity.set(0.0f, 0.0f);
	};


	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	protected void updateMotionY(float deltaTime) {

		super.updateMotionY(deltaTime);
	}

	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;

		float dimCorrectionX = 0;
		float dimCorrectionY = 0;

		// Draw image
		reg = animation.getKeyFrame(stateTime, true);
		batch.draw(reg.getTexture(), position.x- dimension.x/2, position.y - dimension.y/2, origin.x,
				origin.y, dimension.x + dimCorrectionX, dimension.y
						+ dimCorrectionY, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
		// Reset color to white
		batch.setColor(1, 1, 1, 1);
	}

}