package com.andreykaraman.earkanoid;

import com.andreykaraman.earkanoid.game.Assets;
import com.andreykaraman.earkanoid.game.screens.DirectedGame;
import com.andreykaraman.earkanoid.game.screens.GameScreen;
import com.andreykaraman.earkanoid.game.screens.transitions.ScreenTransition;
import com.andreykaraman.earkanoid.game.screens.transitions.ScreenTransitionFade;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

public class ArkanoidGame extends DirectedGame {
	@Override
	public void create() {
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());

		// Load preferences for audio settings and start playing music
		// GamePreferences.instance.load();
		// AudioManager.instance.play(Assets.instance.music.song01);

		// Start game at menu screen
		// ScreenTransition transition = ScreenTransitionSlice.init(2,
		// ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);

		ScreenTransition transition = ScreenTransitionFade.init(0.75f);
		setScreen(new GameScreen(this), transition);
	}

}
