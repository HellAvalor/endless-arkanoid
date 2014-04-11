package com.andreykaraman.earkanoid.game;

import com.andreykaraman.earkanoid.util.Constants;
import com.andreykaraman.earkanoid.util.Constants.BRICK_TYPE;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	public AssetPlatform platform;
	public AssetBall ball;
	public AssetBricks bricks;

	// singleton: prevent instantiation from other classes
	private Assets() {
	}

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

		// load sounds
		// assetManager.load("sounds/jump.wav", Sound.class);
		// assetManager.load("sounds/jump_with_feather.wav", Sound.class);
		// assetManager.load("sounds/pickup_coin.wav", Sound.class);
		// assetManager.load("sounds/pickup_feather.wav", Sound.class);
		// assetManager.load("sounds/live_lost.wav", Sound.class);
		// load music
		// assetManager.load("music/keith303_-_brand_new_highscore.mp3",
		// Music.class);
		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG,
				"# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// create game resource objects

		platform = new AssetPlatform(atlas);
		ball = new AssetBall(atlas);
		bricks = new AssetBricks(atlas);
		// rock = new AssetRock(atlas);
		// goldCoin = new AssetGoldCoin(atlas);
		// fonts = new AssetFonts();
		// levelDecoration = new AssetLevelDecoration(atlas);
		// sounds = new AssetSounds(assetManager);
		// music = new AssetMusic(assetManager);
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		// TODO Auto-generated method stub
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.toString() + "'",
				(Exception) throwable);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	public class AssetPlatform {
		public final AtlasRegion head;
		public final Animation animNormal;

		public AssetPlatform(TextureAtlas atlas) {
			head = atlas.findRegion("platform");
			Array<AtlasRegion> regions = null;
			AtlasRegion region = null; // Animation: Bunny Normal
			regions = atlas.findRegions("platform");
			animNormal = new Animation(1.0f / 10.0f, regions,
					Animation.LOOP_PINGPONG); // Animation: Bunny Copter - knot
												// ears
			/*
			 * regions = atlas.findRegions("anim_bunny_copter");
			 * animCopterTransform = new Animation(1.0f / 10.0f, regions); //
			 * Animation: // Bunny // Copter // - // unknot // ears regions =
			 * atlas.findRegions("anim_bunny_copter"); animCopterTransformBack =
			 * new Animation(1.0f / 10.0f, regions, Animation.REVERSED); //
			 * Animation: Bunny Copter - rotate // ears regions = new
			 * Array<AtlasRegion>();
			 * regions.add(atlas.findRegion("anim_bunny_copter", 4));
			 * regions.add(atlas.findRegion("anim_bunny_copter", 5));
			 * animCopterRotate = new Animation(1.0f / 15.0f, regions);
			 */
		}
	}

	public class AssetBall {
		public final AtlasRegion ball;
		public final Animation animNormal;

		public AssetBall(TextureAtlas atlas) {
			ball = atlas.findRegion("ball");
			Array<AtlasRegion> regions = null;
			AtlasRegion region = null; // Animation: Bunny Normal
			regions = atlas.findRegions("ball");
			animNormal = new Animation(1.0f / 10.0f, regions,
					Animation.LOOP_PINGPONG); // Animation: Bunny Copter - knot
												// ears
		}
	}

	public class AssetBricks {
		public AtlasRegion undestrBrick;
		public AtlasRegion simpleBrick;
		public AtlasRegion doubleBrick;
		public AtlasRegion bonusBrick;

		public AssetBricks(TextureAtlas atlas) {

			undestrBrick = null;
			simpleBrick = null;
			for (BRICK_TYPE brickType : BRICK_TYPE.values()) {
				if (brickType == BRICK_TYPE.UNDESTR) {
					undestrBrick = atlas.findRegion("undestr_brick");
				} else if (brickType == BRICK_TYPE.SIMPLE) {
					simpleBrick = atlas.findRegion("simple_brick");
				} else if (brickType == BRICK_TYPE.DOUBLE) {
					doubleBrick = atlas.findRegion("double_brick");
				} else if (brickType == BRICK_TYPE.BONUS) {
					bonusBrick = atlas.findRegion("bonus_brick");
				}

			}

		}
	}
}
