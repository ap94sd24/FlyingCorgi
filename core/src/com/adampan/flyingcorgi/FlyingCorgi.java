package com.adampan.flyingcorgi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlyingCorgi extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private ShapeRenderer shapeRenderer;

	private Texture[] dogs;
	private int flyState = 0;
	private float dogHeight = 0;
	private float velocity = 0;
	private Circle dogCircle;

	private int gameState = 0;
	private float gravity = 2f;

	private Texture topTube;
	private Texture bottomTube;
	private Rectangle[] topTubeRect;
	private Rectangle[] bottomTubeRect;
	private float maxTubeOffset;
	private Random randomGenerator;
	private float tubeVelocity = 4;
	private int numOfTubes = 4;
	private float[] tubeXCoord = new float[numOfTubes];
	private float[] tubeOffset = new float[numOfTubes];
	private float gap = 400f;
	private float distanceBetweenTubes;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg2.png");
		shapeRenderer  = new ShapeRenderer();
		dogCircle = new Circle();


		dogs = new Texture[2];
		dogs[0] = new Texture("flyingCorgi5.png");
		dogs[1] = new Texture("flyingCorgi6.png");
		dogHeight = Gdx.graphics.getHeight() / 2f - dogs[0].getHeight() / 2f;

		topTube = new Texture("toptube2.png");
		bottomTube = new Texture("bottomtube2.png");

		maxTubeOffset = Gdx.graphics.getHeight() / 2f - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth()  * 3f  / 4f;
		topTubeRect = new Rectangle[numOfTubes];
		bottomTubeRect = new Rectangle[numOfTubes];

		for (int i = 0; i < numOfTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeXCoord[i] = Gdx.graphics.getWidth() / 2f - topTube.getWidth() / 2f  + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRect[i] = new Rectangle();
			bottomTubeRect[i] = new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	    if (gameState != 0) {
            if (Gdx.input.justTouched()) {
                velocity = -25;
            }

			for (int i = 0; i < numOfTubes; i++) {

				// check if it move to end of screen
				if (tubeXCoord[i] < - topTube.getWidth()) {
					tubeXCoord[i] += numOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				} else {
					tubeXCoord[i] -= tubeVelocity;
				}

				batch.draw(topTube, tubeXCoord[i], Gdx.graphics.getHeight() / 2f + gap / 2f + tubeOffset[i]);
				batch.draw(bottomTube, tubeXCoord[i], Gdx.graphics.getHeight() / 2f - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRect[i] = new Rectangle(tubeXCoord[i], Gdx.graphics.getHeight() / 2f + gap / 2f + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRect[i] = new Rectangle(tubeXCoord[i], Gdx.graphics.getHeight() / 2f - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}

            if (dogHeight > 0 || velocity < 0) {
				velocity += gravity;
				dogHeight -= velocity;
			}
		} else {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}

		if (flyState == 0) {
			flyState = 1;
		} else {
			flyState = 0;
		}

		batch.draw(dogs[flyState], Gdx.graphics.getWidth() / 2f - dogs[flyState].getWidth() / 2f, dogHeight);
		batch.end();

		dogCircle.set(Gdx.graphics.getWidth() / 2f, dogHeight + dogs[flyState].getHeight() / 2f, dogs[flyState].getWidth() / 2f);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	    //	shapeRenderer.setColor(Color.RED);
	//	shapeRenderer.circle(dogCircle.x, dogCircle.y, dogCircle.radius);

		for (int i = 0; i < numOfTubes; i++) {

			//shapeRenderer.rect(tubeXCoord[i], Gdx.graphics.getHeight() / 2f + gap / 2f + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeXCoord[i], Gdx.graphics.getHeight() / 2f - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

			if (Intersector.overlaps(dogCircle, topTubeRect[i]) || Intersector.overlaps(dogCircle, bottomTubeRect[i])) {
				Gdx.app.log("Collision: ", "HIT!");
			}

		}

	//	shapeRenderer.end();
	}
}
