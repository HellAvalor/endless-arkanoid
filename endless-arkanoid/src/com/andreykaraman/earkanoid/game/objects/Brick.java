package com.andreykaraman.earkanoid.game.objects;

import com.andreykaraman.earkanoid.game.Assets;
import com.andreykaraman.earkanoid.util.Constants;
import com.andreykaraman.earkanoid.util.Constants.BRICK_TYPE;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Brick extends AbstractGameObject {
	private TextureRegion regBrick;
	public boolean ruined;
	public int brickLife;
	public BRICK_TYPE type;

	public Brick(BRICK_TYPE type) {
		init(type);
	}

	private void init(BRICK_TYPE type) {
		ruined = false;
		dimension.set(1f, 0.5f);
		switch (type) {
		case UNDESTR:
			this.type = BRICK_TYPE.UNDESTR;
			brickLife = -1;
			regBrick = getRegBrick(type);
			break;

		case SIMPLE:
			this.type = BRICK_TYPE.SIMPLE;
			brickLife = 1;
			regBrick = getRegBrick(type);
			break;

		case DOUBLE:
			this.type = BRICK_TYPE.DOUBLE;
			brickLife = 2;
			regBrick = getRegBrick(type);
			break;

		case BONUS:
			this.type = BRICK_TYPE.BONUS;
			brickLife = 1;
			regBrick = getRegBrick(type);
			break;

		default:
			break;
		}

		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
	}

	public TextureRegion getRegBrick(BRICK_TYPE type) {
		switch (type) {
		case UNDESTR:
			return Assets.instance.bricks.undestrBrick;
	
		case SIMPLE:
			return Assets.instance.bricks.simpleBrick;
		
		case DOUBLE:
			return Assets.instance.bricks.doubleBrick;
		
		case BONUS:
			return Assets.instance.bricks.bonusBrick;

		default:
			return Assets.instance.bricks.undestrBrick;
		}

	}

	public BRICK_TYPE getType() {
		return type;
	}

	public void setType(BRICK_TYPE type) {
		this.type = type;
		this.regBrick = getRegBrick(type);
	}

	public void render(SpriteBatch batch) {
		if (ruined)
			return;
		TextureRegion reg = null;
		reg = regBrick;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
	}

	public int getScore() {
		return 250;
	}
}