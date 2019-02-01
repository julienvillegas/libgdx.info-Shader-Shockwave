package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

public class ShockWave extends Group {


  private FrameBuffer fbo;
  private String vertexShader ;
  private String fragmentShader ;
  private ShaderProgram shaderProgram;
  private float time;

  private boolean disabled;

  private float shockWavePositionX;
  private float shockWavePositionY;

  static private ShockWave shockWave;

  static public ShockWave getInstance(){
    if(shockWave==null){
      shockWave=new ShockWave();
    }
    return shockWave;
  }

  private ShockWave(){
    disabled = true;
    time = 0;
    vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
    fragmentShader = Gdx.files.internal("shaders/fragment.glsl").readString();
    shaderProgram = new ShaderProgram(vertexShader,fragmentShader);
    shaderProgram.pedantic = false;

    fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), true);
  }

  public void start(float posX,float posY){
    this.shockWavePositionX = posX;
    this.shockWavePositionY = posY;
    RunnableAction enable = new RunnableAction();
    enable.setRunnable(new Runnable() {
      @Override
      public void run() {
        disabled = true;
      }
    });
    this.addAction(Actions.delay(1,enable));
    disabled=false;
    time = 0;

  }

  @Override
  public void act(float delta) {
    super.act(delta);
    time+=delta;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    if(disabled){
      super.draw(batch, parentAlpha);
    }
    else {
      batch.end();
      batch.flush();
      fbo.begin();
      batch.begin();
      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
      super.draw(batch, parentAlpha);
      batch.end();
      batch.flush();
      fbo.end();
      batch.begin();
      batch.setShader(shaderProgram);
      Vector2 v = new Vector2();
      v = new Vector2(shockWavePositionX, shockWavePositionY);
      v.x = v.x / Gdx.graphics.getWidth();
      v.y = v.y / Gdx.graphics.getHeight();
      shaderProgram.setUniformf("time", time);
      shaderProgram.setUniformf("center", v);
      Texture texture = fbo.getColorBufferTexture();
      TextureRegion textureRegion = new TextureRegion(texture);
      // and.... FLIP!  V (vertical) only
      textureRegion.flip(false, true);
      batch.draw(textureRegion, 0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      batch.setShader(null);
    }
  }
}
