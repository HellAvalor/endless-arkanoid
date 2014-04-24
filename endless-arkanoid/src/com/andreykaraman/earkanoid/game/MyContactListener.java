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

			body.getFixtureList().get(0).setUserData("toched");
			

			Gdx.app.debug(TAG, "Destroy body");
		}

	}

	@Override
	public void beginContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
