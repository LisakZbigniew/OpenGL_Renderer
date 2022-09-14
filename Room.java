//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021

import com.jogamp.opengl.GL3;

import data.TwoTriangles;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import model.Mesh;
import model.Model;
import sgGraph.ModelNode;
import sgGraph.NameNode;
import sgGraph.TransformNode;
import util.*;

public class Room extends NameNode{
    
    
    /**
     * 
     * @param scene - scene meta information
     * @param textureIds - array of textures to use in the room listed here:
     *                   0 - floor texture
     *                   1 - wall texture
     *                   2 - door texture
     *                   3 - 
     */
    public Room(GL3 gl, SceneMeta scene, Texture[] textures){
        super("Room root node", scene);
        if(textures.length < 4) return;
        //ENTITIES

        Shader shader = new Shader(gl, "oneTexture.vs", "oneTexture.fs");
        Material material = new Material();
        Mat4 modelMatrix = new Mat4(1);
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        Model floorModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh, textures[0]);
        ModelNode floor = new ModelNode("Floor node", floorModel);
        
        material = new Material(); 
        Model frontWallModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh, textures[1]);
        ModelNode frontWall = new ModelNode("FrontWall node", frontWallModel);
        
        Model sideWallModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh, textures[1]); 
        ModelNode sideWall = new ModelNode("SideWall node", sideWallModel);
        
        material = new Material();
        Model doorModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh, textures[2]);
        ModelNode door = new ModelNode("Door node", doorModel);
        
        shader = new Shader(gl, "window.vs","window.fs");
        material = new Material();
        Model windowModel = new Model(gl, scene.getCamera(), scene, shader, material, modelMatrix, mesh, textures[3]){
            protected void setUniforms(GL3 gl, Mat4 matrix){
                super.setUniforms(gl,matrix);
                shader.setFloat(gl, "tint", scene.getTint());
                shader.setVec4(gl, "tintColor", scene.getTintColor());

            }
        };
        ModelNode window = new ModelNode("Window node", windowModel);

        //TRANSFORMATIONS

        Mat4 t = new Mat4(1);
        TransformNode floorTransformNode = new TransformNode("floorTransformNode", t);
        
        t = Mat4.multiply(Mat4Transform.translate(0.0f, 0.5f, 0.0f), Mat4Transform.rotateAroundX(90));
        t = Mat4.multiply(Mat4Transform.scale(1,0.75f,1),t);
        t = Mat4.multiply(Mat4Transform.rotateAroundY(90),t);
        t = Mat4.multiply(Mat4Transform.translate(-0.5f,0,0),t);
        TransformNode sideWallTransform = new TransformNode("sideWallTransform node", t);
        
            t = Mat4.multiply(Mat4Transform.translate(0, 0.001f, 0.15f), Mat4Transform.scale(0.7f, 1f, 0.4f));
            TransformNode windowTransform = new TransformNode("windowTransform node", t); 

        t = Mat4.multiply(Mat4Transform.translate(0.0f, 0.5f, 0.0f), Mat4Transform.rotateAroundX(90));
        t = Mat4.multiply(Mat4Transform.scale(1,0.75f,1),t);
        t = Mat4.multiply(Mat4Transform.translate(0,0,-0.5f),t);
        TransformNode frontWallTransform = new TransformNode("frontWallTransform node", t);

            t = Mat4.multiply(Mat4Transform.translate(-0.25f, 0.001f, 0.2f),Mat4Transform.scale(0.3f,1,0.6f));  //to the corner - half width of door - distance form wall
            TransformNode doorTransform = new TransformNode("doorTransformNode", t);
        
        this.addChild(floorTransformNode);
            floorTransformNode.addChild(floor);
        this.addChild(sideWallTransform);
            sideWallTransform.addChild(sideWall);
                sideWall.addChild(windowTransform);
                    windowTransform.addChild(window);
        this.addChild(frontWallTransform);
            frontWallTransform.addChild(frontWall);
                frontWall.addChild(doorTransform);
                    doorTransform.addChild(door);  
        
    }

    public Room(GL3 gl, Texture[] textures){
        this(gl, null, textures);
    }

    
}
