package model;

//Code supplied from exercises with some refactoring
//Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
import gmaths.*;
import util.*;

import com.jogamp.opengl.*;

public class Model {
  
  private Mesh mesh;
  private Texture textureId1; 
  private Texture textureId2; 
  protected Material material;
  protected Shader shader;
  protected Mat4 modelMatrix;
  protected Camera camera;
  protected SceneMeta scene;//New field
  
  public Model(GL3 gl, Camera camera, SceneMeta scene, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, Texture textureId1, Texture textureId2) {
    this.mesh = mesh;
    this.material = material;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.camera = camera;
    this.scene = scene;
    this.textureId1 = textureId1;
    this.textureId2 = textureId2;
  }
  
  public Model(GL3 gl, Camera camera, SceneMeta scene, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, Texture textureId1) {
    this(gl, camera, scene, shader, material, modelMatrix, mesh, textureId1, null);
  }
  
  public Model(GL3 gl, Camera camera, SceneMeta scene, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh) {
    this(gl, camera, scene, shader, material, modelMatrix, mesh, null, null);
  }
  
  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }

  public void render(GL3 gl, Mat4 modelMatrix) {
    
    setUniforms(gl, modelMatrix);

    mesh.render(gl);
  } 

  //New function refactored
  protected void setUniforms(GL3 gl, Mat4 modelMatrix ){
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
    shader.use(gl);
    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
    shader.setVec3(gl, "viewPos", camera.getPosition());

    //Light uniforms refactored to SceneMeta class
    scene.setLightUniforms(shader);

    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());  

    if (textureId1!=null) {
      shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      textureId1.setTexture(gl, 0);
    }
    if (textureId2!=null) {
      shader.setInt(gl, "second_texture", 1);
      textureId2.setTexture(gl, 1);
    }
  }
  
  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }
  
  public void dispose(GL3 gl) {
    mesh.dispose(gl);
    if (textureId1!=null) textureId1.dispose(gl);
    if (textureId2!=null) textureId2.dispose(gl);
  }
  
}