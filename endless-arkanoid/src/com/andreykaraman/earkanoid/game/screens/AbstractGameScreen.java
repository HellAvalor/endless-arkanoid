package com.andreykaraman.earkanoid.game.screens;

import com.andreykaraman.earkanoid.game.Assets;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.InputProcessor;

public abstract class AbstractGameScreen implements Screen {

	protected DirectedGame game;

	public abstract InputProcessor getInputProcessor();

	public AbstractGameScreen(DirectedGame game) {
		this.game = game;
	}

	public abstract void render(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void show();

	public abstract void hide();

	public abstract void pause();

	public void resume() {
		Assets.instance.init(new AssetManager());
	}

	public void dispose() {
		Assets.instance.dispose();
	}
}
