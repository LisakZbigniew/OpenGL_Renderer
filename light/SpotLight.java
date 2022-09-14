package light;

//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021

import com.jogamp.opengl.GL3;
import java.lang.Math;
import gmaths.*;
import util.*;


public class SpotLight extends Light{

    
    private Vec2 cutoffAngle;


    /**
     * @param cutOff cut off angles of spotlight (inner ring, width of outer ring)
     */
    public SpotLight(GL3 gl, Camera camera, SceneMeta scene , Vec2 cutOff){
        super(gl, camera, scene, Type.SPOT);
        this.cutoffAngle = cutOff;
    }

    /**
     * @param c cut off angles of spotlight (inner ring, width of outer ring)
     */
    public void setCutOffAngle(Vec2 c){
        cutoffAngle = c;
    }

    public float getInnerAngleCos(){
        return (float)Math.cos(Math.toRadians(cutoffAngle.x));
    }

    public float getOuterAngleCos(){
        return (float)Math.cos(Math.toRadians(cutoffAngle.x+cutoffAngle.y));
    }


}