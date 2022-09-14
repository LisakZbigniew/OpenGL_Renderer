package light;

//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021

import com.jogamp.opengl.GL3;

import gmaths.Vec3;
import util.*;

public class PointLight extends Light {

    private Vec3 attenuation;
    public PointLight(GL3 gl, Camera camera, SceneMeta scene, Vec3 attenuation){
        super(gl, camera, scene,Type.POINT);
        this.attenuation = attenuation;
    }
    public PointLight(GL3 gl, Camera camera, SceneMeta scene, Vec3 attenuation, float size){
        super(gl, camera, scene,Type.POINT,size);
        this.attenuation = attenuation;
    }

    public Vec3 getAttenuation(){
        return attenuation;
    }

    public void setAttenuation(Vec3 attenuation){
        this.attenuation = attenuation;
    }
}
