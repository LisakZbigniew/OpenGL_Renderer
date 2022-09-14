package light;

//Code adapted form exercise solution
//Own code noted with comments Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

import gmaths.*;
import data.LightCube;
import util.*;

public abstract class Light{

    //Own code
    public enum Type {
        DIRECTIONAL,
        POINT,
        SPOT
    }
    //End of own code
    private Material material;
    private static Shader shader;
    protected Mat4 modelMatrix;
    private Camera camera;

    //Own code
    private Material initMaterial;
    protected SceneMeta scene;
    private float size;
    private Type type;
    private Boolean isOn;

    public Light(GL3 gl, Camera camera, SceneMeta scene, Type type,float size) {
        this.camera = camera;
        this.initMaterial = new Material();
        this.material = this.initMaterial;
        this.type = type;
        this.size = size;
        this.scene = scene;
        this.modelMatrix = Mat4Transform.scale(size,size,size);
        isOn = false;
        if(shader == null) initShader(gl);
        if(vertexArrayId[0] == -1) fillBuffers(gl);
        scene.addLight(this);
    }

    public Light(GL3 gl, Camera camera, SceneMeta scene, Type type){
        this(gl,camera,scene,type,0.1f);
    }

    public void initShader(GL3 gl){
        shader = new Shader(gl, "light.vs", "light.fs");
    }

    public void updatePosition(Mat4 t){
        modelMatrix = Mat4.multiply(t, Mat4Transform.scale(size,size,size));
    }

    public void setIsOn(boolean p){
        isOn = p;
    }

    public boolean isOn(){
        return isOn;
    }


    public Vec3 getPosition() {
        return Mat4.multiply(modelMatrix, new Vec4(0, 0, 0, 1)).toVec3();
    }

    public Vec3 getDirection(){
        return Mat4.multiply(modelMatrix, new Vec4(0, 0, 1, 0)).toVec3();
    }

    public Type getType(){
        return type;
    }

    //End of own code

    public void setMaterial(Material m){
        this.initMaterial = m;
        this.material = m;
    }

    public Material getMaterial(){
        return material;
    }

    //Own code
    public void dimLight(float dimLevel){
        this.material = Material.multiply(initMaterial,dimLevel);
    }
    //End of own code

    public void render(GL3 gl, Mat4 modelMatrix) {

        Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));

        shader.use(gl);
        shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
        //Own code
        Vec3 avglight = Vec3.add(material.getAmbient(), Vec3.add(material.getDiffuse(),material.getSpecular()));
        // devided by 1.5 not 3 to exaggerate the effect
        Vec3 lightColor = isOn ? new Vec3 (avglight.x/1.5f, avglight.y/1.5f,avglight.z/1.5f) : new Vec3(); 
        shader.setFloat(gl, "lightColor", lightColor.x, lightColor.y, lightColor.z);
        //End of own code

        gl.glBindVertexArray(vertexArrayId[0]);
        gl.glDrawElements(GL.GL_TRIANGLES, LightCube.indices.length, GL.GL_UNSIGNED_INT, 0);
        gl.glBindVertexArray(0);
    }
    //Own code
    public void render(GL3 gl){
        render(gl,modelMatrix);
    }
    //End of own code

    public void dispose(GL3 gl) {
        gl.glDeleteBuffers(1, vertexBufferId, 0);
        gl.glDeleteVertexArrays(1, vertexArrayId, 0);
        gl.glDeleteBuffers(1, elementBufferId, 0);
    }

    // ***************************************************
    /*
     * THE LIGHT BUFFERS
     */

    private static int[] vertexBufferId = {-1};
    private static int[] vertexArrayId = {-1};
    private static int[] elementBufferId = {-1};

    private void fillBuffers(GL3 gl) {
        gl.glGenVertexArrays(1, vertexArrayId, 0);
        gl.glBindVertexArray(vertexArrayId[0]);
        gl.glGenBuffers(1, vertexBufferId, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferId[0]);
        FloatBuffer fb = Buffers.newDirectFloatBuffer(LightCube.vertices);

        gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * LightCube.vertices.length, fb, GL.GL_STATIC_DRAW);

        int stride = LightCube.vertexStride;
        int numXYZFloats = LightCube.vertexXYZFloats;
        int offset = 0;
        gl.glVertexAttribPointer(0, numXYZFloats, GL.GL_FLOAT, false, stride * Float.BYTES, offset);
        gl.glEnableVertexAttribArray(0);

        gl.glGenBuffers(1, elementBufferId, 0);
        IntBuffer ib = Buffers.newDirectIntBuffer(LightCube.indices);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, elementBufferId[0]);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, Integer.BYTES * LightCube.indices.length, ib, GL.GL_STATIC_DRAW);
        gl.glBindVertexArray(0);
    }

}