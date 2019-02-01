package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MyGdxGame extends ApplicationAdapter {
	private Stage stage;

	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		Texture texture = new Texture(Gdx.files.absolute("background.jpg"));




		stage.addActor(ShockWave.getInstance());

		Image image1 = new Image(texture);
		image1.setPosition(0,0);
		image1.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		image1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				ShockWave.getInstance().start(x,y);
			}
		});

		ShockWave.getInstance().addActor(image1);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

	}
}
