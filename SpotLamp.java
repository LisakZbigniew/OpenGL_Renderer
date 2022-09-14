//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021

import com.jogamp.opengl.GL3;

import data.*;
import gmaths.*;
import light.*;
import model.*;
import sgGraph.*;
import util.*;


public class SpotLamp extends NameNode {
    
    TransformNode bulbPivot;
    Light spotLight;
    public SpotLamp(GL3 gl, SceneMeta scene){
        super("SpotLamp root", scene);

        //Entities
        Shader shader = new Shader(gl,"lamp.vs","lamp.fs");
        Material material = new Material(Material.DEFAULT_AMBIENT, Material.DEFAULT_DIFFUSE, new Vec3(0.8f, 0.8f, 0.8f), 128);
        Mat4 modelMatrix = new Mat4(1);
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Model cube = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh);
        
        ModelNode base = new ModelNode("Base node", cube);
        ModelNode stem = new ModelNode("Stem node", cube);
        ModelNode bulbReach = new ModelNode("Bulb reach node", cube);
        ModelNode bulbSocket = new ModelNode("Bulb socket ndoe", cube);

        spotLight = new SpotLight(gl, scene.getCamera(), scene, new Vec2(3, 5)); 

        LightNode lightBulb = new LightNode("LightBulb", spotLight);

        //Transformations

        Mat4 t = Mat4.multiply(Mat4Transform.translate(0,0.125f,0),Mat4Transform.scale(3, 0.25f, 3));
        TransformNode baseTransform = new TransformNode("base transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0,24.25f,0),Mat4Transform.scale(0.1f, 48, 0.1f));
        TransformNode stemTransform = new TransformNode("stem transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(-4.5f,0.5f,0),Mat4Transform.scale(10, 0.02f, 1));
        TransformNode reachTransform = new TransformNode("reach transform node", t);
        
        t = Mat4.multiply(Mat4Transform.translate(-0.5f,0,0),Mat4Transform.scale(0.18f, 2f, 2f));
        TransformNode socketTransform = new TransformNode("socket transform node", t);
        bulbPivot = socketTransform;
        t = Mat4.multiply(Mat4Transform.scale(0.5f, 0.5f, 0.5f), Mat4Transform.rotateAroundX(90));
        t = Mat4.multiply(Mat4Transform.translate(0,-0.3f,0),t);
        TransformNode bulbTransform = new TransformNode("lightbulb transform node", t);


        this.addChild(baseTransform);
            baseTransform.addChild(base);
                base.addChild(stemTransform);
                    stemTransform.addChild(stem);
                        stem.addChild(reachTransform);
                            reachTransform.addChild(bulbReach);
                                bulbReach.addChild(socketTransform);
                                    socketTransform.addChild(bulbSocket);
                                        bulbSocket.addChild(bulbTransform);
                                            bulbTransform.addChild(lightBulb);


    }

    public void updateLamp(){
        float elapsedTime = scene.getElapsedTime();
        Mat4 t = Mat4.multiply(Mat4Transform.scale(0.18f, 2f, 2f),Mat4Transform.rotateAroundX((float)Math.sin(elapsedTime * 0.001)*20));
        t = Mat4.multiply(Mat4Transform.translate(-0.5f,0,0), t);
        bulbPivot.setTransform(t);
        bulbPivot.update(false);
    }

    public void setLampState(boolean p){
        spotLight.setIsOn(p);
    }
    public boolean getLampState(){
        return spotLight.isOn();
    }

    public SpotLamp(GL3 gl){
        this(gl, null);
    }

}
