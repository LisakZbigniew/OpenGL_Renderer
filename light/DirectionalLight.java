package light;
//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021

import com.jogamp.opengl.GL3;
import util.SceneMeta;
import gmaths.Vec3;
import util.Camera;

public class DirectionalLight extends Light{

    private Vec3 direction;
    public DirectionalLight(GL3 gl, Camera camera, SceneMeta scene , Vec3 direction){
        super(gl, camera, scene, Type.DIRECTIONAL, 0.0f);
        this.direction = direction;
    }

    public Vec3 getDirection(){
        return direction;
    }

    public void setDirection(Vec3 direction){
        this.direction = direction;
    }
}
