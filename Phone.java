//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021
import util.*;
import sgGraph.*;
import model.*;
import gmaths.*;
import data.*;

import com.jogamp.opengl.GL3;

public class Phone extends NameNode{

    public Phone(GL3 gl, SceneMeta scene, Texture screenTexture){
        super("Phone root", scene);

        //Entities
        Shader shader = new Shader(gl,"lamp.vs","lamp.fs");
        Material material = new Material(new Vec3(0,0.23f,0.55f), new Vec3(0,0.4f,0.9f), new Vec3(0.74f,0.84f,1), 32);
        Mat4 modelMatrix = new Mat4(1);
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Model baseModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh);

        material = new Material(new Vec3(0.13f,0.13f,0.13f), new Vec3(0.28f,0.28f,0.28f), new Vec3(0.9f,0.9f,0.9f), 32);
        Model bodyModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh);

        material = new Material();
        Model buttonModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh);

        shader = new Shader(gl, "oneTexture.vs", "oneTexture.fs");
        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        Model screenModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh, screenTexture);
        
        ModelNode base = new ModelNode("Base", baseModel);
        ModelNode body = new ModelNode("Phone body", bodyModel);
        ModelNode screen = new ModelNode("Phone Screen", screenModel);
        ModelNode button = new ModelNode("Home Button", buttonModel);

        //Transformations

        Mat4 t = Mat4.multiply(Mat4Transform.translate(0,0.333f,0),Mat4Transform.scale(20/3f, 2/3f, 8/3f));
        TransformNode baseTransform = new TransformNode("base transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0,7.5f,0),Mat4Transform.scale(0.9f, 14, 0.5f));
        TransformNode bodyTransform = new TransformNode("body transform node", t);

        t = Mat4.multiply(Mat4Transform.scale(0.9f, 0.8f, 1),Mat4Transform.rotateAroundX(90));
        t = Mat4.multiply(Mat4Transform.translate(0,0.03f,0.501f),t);
        TransformNode screenTransform = new TransformNode("screen transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0,-0.42f,0.05f),Mat4Transform.scale(0.2f, 0.05f, 1));
        TransformNode buttonTransform = new TransformNode("button transform node", t);


        this.addChild(baseTransform);
            baseTransform.addChild(base);
                base.addChild(bodyTransform);
                    bodyTransform.addChild(body);
                        body.addChild(screenTransform);
                            screenTransform.addChild(screen);
                        body.addChild(buttonTransform);
                            buttonTransform.addChild(button);

    }


    
}
