//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021

import com.jogamp.opengl.GL3;

import util.*;
import sgGraph.*;
import model.*;
import gmaths.*;
import data.*;

public class Robot extends NameNode{
    
    TransformNode bodyTransform, headTransform,leftEarTransform,rightEarTransform;

    public Robot(GL3 gl, SceneMeta scene){
        super("Robot root", scene);

        //Entities
        Shader shader = new Shader(gl,"lamp.vs","lamp.fs");
        Material material = new Material();
        material.setSpecular(new Vec3(0, 0, 0));
        Mat4 modelMatrix = new Mat4(1);
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Model sphereGray = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh);

        material = new Material(new Vec3(0,0.23f,0.55f), new Vec3(0,0.4f,0.9f), new Vec3(0.74f,0.84f,1), 32);
        Model sphereBlue = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh);

        material = new Material(new Vec3(0.68f,0.36f,0.68f), new Vec3(1,0.5f,1f), new Vec3(1f,0.8f,1), 32);
        Model spherePink = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh);
        
        ModelNode foot = new ModelNode("Foot", sphereGray);
        ModelNode body = new ModelNode("Body", sphereGray);
        ModelNode neck = new ModelNode("Neck", sphereGray);
        ModelNode head = new ModelNode("Head", sphereGray);
        ModelNode rightEar = new ModelNode("RightEar", sphereGray);
        ModelNode leftEar = new ModelNode("LeftEar", sphereGray);
        ModelNode rightInnerEar = new ModelNode("RightInnerEar", spherePink);
        ModelNode leftInnerEar = new ModelNode("LeftInnerEar", spherePink);
        
        ModelNode visor = new ModelNode("Visor", sphereBlue);


        //Transformations

        Mat4 t = Mat4.multiply(Mat4Transform.translate(0,0.5f,0),Mat4Transform.scale(1, 1, 1));
        TransformNode footTransform = new TransformNode("foot transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0,2,0),Mat4Transform.scale(1, 3, 1));
        bodyTransform = new TransformNode("body transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0,0.52f,0),Mat4Transform.scale(0.2f, 0.06f, 0.2f));
        TransformNode neckTransform = new TransformNode("neck transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0,2.9f,0),Mat4Transform.scale(6, 5, 8));
        headTransform = new TransformNode("head transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0,0.2f,0.1f),Mat4Transform.scale(0.9f, 0.2f, 0.9f));
        TransformNode visorTransform = new TransformNode("visor transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(-0.2f,0.8f,0), Mat4Transform.scale(0.4f, 1.5f, 0.1f));
        t = Mat4.multiply(Mat4Transform.rotateAroundZ(15),t);
        leftEarTransform = new TransformNode("left ear transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0.2f,0.8f,0), Mat4Transform.scale(0.4f, 1.5f, 0.1f));
        t = Mat4.multiply(Mat4Transform.rotateAroundZ(-15),t);
        rightEarTransform = new TransformNode("right ear transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0,0,0.1f),Mat4Transform.scale(0.8f, 0.8f, 0.9f));
        TransformNode leftInnerEarTransform = new TransformNode("left inner ear transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0,0,0.1f),Mat4Transform.scale(0.8f, 0.8f, 0.9f));
        TransformNode rightInnerEarTransform = new TransformNode("right inner ear transform node", t);

        this.addChild(footTransform);
            footTransform.addChild(foot);
                foot.addChild(bodyTransform);
                    bodyTransform.addChild(body);
                        body.addChild(neckTransform);
                            neckTransform.addChild(neck);
                                neck.addChild(headTransform);
                                    headTransform.addChild(head);
                                        head.addChild(visorTransform);
                                            visorTransform.addChild(visor);
                                        head.addChild(leftEarTransform);
                                            leftEarTransform.addChild(leftEar);
                                                leftEar.addChild(leftInnerEarTransform);
                                                    leftInnerEarTransform.addChild(leftInnerEar);
                                        head.addChild(rightEarTransform);
                                            rightEarTransform.addChild(rightEar);
                                                rightEar.addChild(rightInnerEarTransform);
                                                    rightInnerEarTransform.addChild(rightInnerEar);

    }


    public void updatePose(float bodyPitch,float bodyRoll, float headPitch, float headYaw, float leftEarAngle, float rightEarAngle){
        
        Mat4 t = Mat4.multiply(Mat4Transform.translate(0,2,0),Mat4Transform.scale(1, 3, 1));
        t = Mat4.multiply(Mat4Transform.rotateAroundZ(bodyRoll), t);
        t = Mat4.multiply(Mat4Transform.rotateAroundX(bodyPitch), t);
        bodyTransform.setTransform(t);
        bodyTransform.update(false);

        t = Mat4.multiply(Mat4Transform.translate(0,2.9f,0),Mat4Transform.scale(6, 5, 8));
        t = Mat4.multiply(Mat4Transform.rotateAroundY(headYaw), t);
        t = Mat4.multiply(Mat4Transform.rotateAroundX(headPitch), t);
        headTransform.setTransform(t);
        headTransform.update(false);

        t = Mat4.multiply(Mat4Transform.translate(-0.2f,0.8f,0), Mat4Transform.scale(0.4f, 1.5f, 0.1f));
        t = Mat4.multiply(Mat4Transform.rotateAroundZ(leftEarAngle),t);
        leftEarTransform.setTransform(t);
        leftEarTransform.update(false);

        t = Mat4.multiply(Mat4Transform.translate(0.2f,0.8f,0), Mat4Transform.scale(0.4f, 1.5f, 0.1f));
        t = Mat4.multiply(Mat4Transform.rotateAroundZ(rightEarAngle),t);
        rightEarTransform.setTransform(t);
        rightEarTransform.update(false);

    }

}
