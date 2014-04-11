package com.andreykaraman.earkanoid.game;

import com.andreykaraman.earkanoid.game.objects.Brick;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;

public class MyContactListener implements ContactListener {
	World world;
	private static final String TAG = WorldController.class.getName();

	public MyContactListener(World world) {
		super();
		this.world = world;
	}

	@Override
	public void endContact(Contact contact) {
		Body body = null;

		if (contact.getFixtureA() != null
				&& contact.getFixtureA().getUserData() != null
				&& contact.getFixtureA().getUserData().equals("brick")) {
			body = contact.getFixtureA().getBody();
			Gdx.app.debug(TAG, "brick collision2 ");
		}
		if (contact.getFixtureB() != null
				&& contact.getFixtureB().getUserData() != null
				&& contact.getFixtureB().getUserData().equals("brick")) {
			body = contact.getFixtureB().getBody();
			Gdx.app.debug(TAG, "brick collision3");
		}

		if (body != null) {
			// Brick brick = (Brick) body.getUserData();
			// Gdx.app.debug(TAG, "Brick "+brick);
			body.getFixtureList().get(0).setUserData("delete");
			WorldController.deleteBodyArray.add(body);
			// body.setActive(false);
			// world.destroyBody(body);

			Gdx.app.debug(TAG, "Destroy body");
		}

	}

	@Override
	public void beginContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		/*
		 * String p =""; WorldManifold manifold = contact.getWorldManifold();
		 * for(int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
		 * p+="["+
		 * Float.toString(manifold.getPoints()[j].x)+";"+Float.toString(manifold
		 * .getPoints()[j].y)+"]";
		 * 
		 * 
		 * 
		 * } Log.e("contact",p);
		 */
		/*
		 * WorldManifold manifold = contact.getWorldManifold();
		 * for (int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
		 * 
		 * if (contact.getFixtureA().getUserData() != null
		 * && contact.getFixtureA().getUserData().equals("ball")) {
		 * //contact.setEnabled(false);
		 * Gdx.app.debug(TAG, "Ball collision");
		 * }
		 * 
		 * if (contact.getFixtureB().getUserData() != null
		 * && contact.getFixtureB().getUserData().equals("ball")) {
		 * 
		 * // contact.setEnabled(false);
		 * Gdx.app.debug(TAG, "Ball collision");
		 * }
		 * }
		 */
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// Gdx.app.debug(TAG, "Ball collision " +
		// contact.getFixtureA().getUserData());

	}

}
