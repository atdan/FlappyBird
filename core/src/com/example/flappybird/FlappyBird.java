package com.example.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;


    Texture[] birds;
    int flapState = 0;

    float birdY = 0;
    float velocity = 0;

    int gamestate = 0;
    float gravity = (float) 1.5;

    Texture topTube, bottomTube;

    float gap = 400;

    float maxTubeOffset;

    Random randomGenerator;

    int numberOfTubes = 4;

    float[] tubeOffset = new float[numberOfTubes];

    float tubeVelocity = 4;
    float[] tubeX = new float[numberOfTubes];


    float distanceBetweenTubes;

    Circle birdCircle;

    //ShapeRenderer shapeRenderer;


    Rectangle[] toptubeRectangles;
    Rectangle[] bottomTubeRectangles;

    int score = 0;
    int scoringTube = 0;

    BitmapFont font;
    Texture gameover;

    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        gameover = new Texture("gameover.png");



        //font for score
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);


        toptubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;

        randomGenerator = new Random();

        //half a screen away
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
        birdCircle = new Circle();

        //shapeRenderer = new ShapeRenderer();
        //tubeX = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2;


        startGame();



    }

    public void startGame(){

        birdY = (Gdx.graphics.getHeight() / 2) - birds[0].getHeight() / 2;

        for (int i = 0; i< numberOfTubes; i++){
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + i*distanceBetweenTubes;

            toptubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render() {

//        //called when the screen is tapped
//        if (Gdx.input.justTouched()) {
//
//            Gdx.app.log("Touched", "Screen tapped");
//            gamestate = 1;
//
//        }

        //add background

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.end();
        //when the screen is tapped
        if (gamestate != 0) {



            if (Gdx.input.justTouched()) {

                velocity = -30;



            }

            for (int i = 0; i< numberOfTubes; i++){

                if (tubeX[i] < -topTube.getWidth()){

                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);


                }else {
                    tubeX[i] = tubeX[i] - tubeVelocity;

                    if (tubeX[scoringTube] < Gdx.graphics.getWidth()){

                        score++;
                        Gdx.app.log("Score: ", String.valueOf(score));

                        if (scoringTube < numberOfTubes-1){
                            scoringTube++;
                        }else {
                            scoringTube = 0;
                        }

                    }
                }

                tubeX[i] = tubeX[i] - tubeVelocity;

                batch.begin();
                batch.draw(topTube, tubeX[i],
                        Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i],
                        Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight() + tubeOffset[i]);

                toptubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i],
                         topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight() + tubeOffset[i],
                        bottomTube.getWidth(), bottomTube.getHeight());
                batch.end();
            }


            if (birdY > 0 ){


                velocity = velocity + gravity;
                //effect of gravity
                birdY -= velocity;
            }else {
                gamestate = 2;
            }






        } else if (gamestate == 0){


            //called when the screen is tapped
            if (Gdx.input.justTouched()) {

//				Gdx.app.log("Touched", "Screen tapped");
                gamestate = 1;

            }
        } else if (gamestate ==2){

            batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2,
                    Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);


            if (Gdx.input.justTouched()){

                gamestate = 1;
                startGame();
                score = 0;
                scoringTube = 0;
                velocity = 0;

            }




        }
        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }

        batch.begin();
        //bird appears at tthe screen

        batch.draw(birds[flapState], (Gdx.graphics.getWidth() / 2) - birds[flapState].getWidth() / 2,
                birdY);

        //draw font
        font.draw(batch, String.valueOf(score), 100, 200);
        batch.end();


        birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2,
                birds[flapState].getWidth()/2);

        //shape
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//
//
//        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for (int i = 0; i< numberOfTubes; i++){
//            shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i],
//                    topTube.getWidth(), topTube.getHeight());
//            shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight() + tubeOffset[i],
//                    bottomTube.getWidth(), bottomTube.getHeight());
//

            //chack for collision btw bird and pipe

            if (Intersector.overlaps(birdCircle, toptubeRectangles[i]) ||
                Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){

                Gdx.app.log("Collision", "Yes!!");

                gamestate = 2;
            }
        }
        //shapeRenderer.end();


    }

//	@Override
//	public void dispose () {
//		batch.dispose();
//		img.dispose();
//	}
}
