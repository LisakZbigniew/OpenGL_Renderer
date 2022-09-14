package sgGraph;

//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021

import com.jogamp.opengl.GL3;

import util.*;
import gmaths.Mat4;
import light.Light;

public class LightNode extends SGNode {
    protected Light light;

    public LightNode(String name, Light l) {
        super(name, null);
        light = l;
    }

    @Override
    public void update(Mat4 t, SceneMeta s, boolean init) {
        worldTransform = t;
        scene = s;
        if(init)light.setIsOn(true);
        light.updatePosition(t);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).update(t, s,init);
        }
    }

    public void draw(GL3 gl) {
        light.render(gl,worldTransform);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).draw(gl);
        }
    }

    public void dispose(GL3 gl) {
        light.dispose(gl);
        for (SGNode node : children) {
            node.dispose(gl);
        }
    }

}
