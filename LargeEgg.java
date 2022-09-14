//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021
import sgGraph.*;
import data.*;
import gmaths.*;
import model.*;
import util.*;

import com.jogamp.opengl.GL3;

public class LargeEgg extends NameNode{
    
    public LargeEgg(GL3 gl, SceneMeta scene, Texture baseTexture, Texture specularTexture){
        super("Egg root", scene);

        //Entities
        Shader shader = new Shader(gl,"lamp.vs","lamp.fs");
        Material material = new Material(new Vec3(0,0.23f,0.55f), new Vec3(0,0.4f,0.9f), new Vec3(0.74f,0.84f,1), 32);
        Mat4 modelMatrix = new Mat4(1);
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Model baseModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh);

        shader = new Shader(gl, "twoTextures.vs", "twoTextures.fs");
        material = new Material();
        mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Model eggModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh, baseTexture, specularTexture);

        
        
        ModelNode base = new ModelNode("Base", baseModel);
        ModelNode egg = new ModelNode("Phone body", eggModel);

        //Transformations

        Mat4 t = Mat4.multiply(Mat4Transform.translate(0,0.333f,0),Mat4Transform.scale(5, 2/3f, 5));
        TransformNode baseTransform = new TransformNode("base transform node", t);

        t = Mat4.multiply(Mat4Transform.translate(0,6.5f,0),Mat4Transform.scale(0.8f, 12, 0.8f));
        TransformNode eggTransform = new TransformNode("egg transform node", t);


        this.addChild(baseTransform);
            baseTransform.addChild(base);
                base.addChild(eggTransform);
                    eggTransform.addChild(egg);

    }

}
