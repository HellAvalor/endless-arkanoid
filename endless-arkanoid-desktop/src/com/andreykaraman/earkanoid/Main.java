package com.andreykaraman.earkanoid;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = true;

	public static void main(String[] args) {

		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.debug = drawDebugOutline;
			TexturePacker2.process(settings, "assets-raw/images",
					"../endless-arkanoid-android/assets/images", "earkanoid.pack");
			// TexturePacker2.process(settings, "assets-raw/images-ui",
			// "../CanyonBunny-android/assets/images",
			// "canyonbunny-ui.pack");
		}
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "endless-arkanoid";
		cfg.useGL20 = true;
		cfg.width = 480;
		cfg.height = 600;

		new LwjglApplication(new ArkanoidGame(), cfg);
	}
}
