package util;

//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021

import java.util.ArrayList;
import java.util.List;
import com.jogamp.opengl.GL3;

import gmaths.Vec4;
import light.*;

public class SceneMeta {

    private List<DirectionalLight> directionalLights = new ArrayList<>();
    private List<PointLight> pointLights = new ArrayList<>();
    private List<SpotLight> spotLights = new ArrayList<>();

    private GL3 gl;
    private Camera camera;
    private int count = 0;
    double startTime;
    float tint;
    Vec4 tintColor;

    public SceneMeta(GL3 gl, Camera camera) {
        this.gl = gl;
        this.camera = camera;
        this.tint = 0.0f;
        this.tintColor = new Vec4(0.4f, 0.4f, 0.6f, 1);
        startTime = getSeconds();
    }

    public Camera getCamera() {
        return camera;
    }

    public void addLight(Light l) {
        if (count == 8)
            return;
        switch (l.getType()) {
            case DIRECTIONAL:
                directionalLights.add((DirectionalLight) l);
                break;
            case POINT:
                pointLights.add((PointLight) l);
                break;
            case SPOT:
                spotLights.add((SpotLight) l);
                break;
        }
        count++;
    }

    public boolean removeLight(Light l) {
        count--;
        return directionalLights.remove(l) || pointLights.remove(l) || spotLights.remove(l);
    }

    public void setTint(float f) {
        if (f < 0.0f || f > 1.0f)
            return;
        tint = f;
    }

    public float getTint() {
        return tint;
    }

    public Vec4 getTintColor() {
        return tintColor;
    }

    public void setLightUniforms(Shader shader) {
        int count = 0;
        for (int i = 0; i < directionalLights.size(); i++) {
            DirectionalLight light = directionalLights.get(i);
            if (light.isOn()) {
                count++;
                shader.setVec3(gl, "directLight[" + i + "].direction", light.getDirection());
                shader.setVec3(gl, "directLight[" + i + "].ambient", light.getMaterial().getAmbient());
                shader.setVec3(gl, "directLight[" + i + "].diffuse", light.getMaterial().getDiffuse());
                shader.setVec3(gl, "directLight[" + i + "].specular", light.getMaterial().getSpecular());
            }
        }
        shader.setInt(gl, "numOfDirectLights", count);
        count = 0;
        for (int i = 0; i < pointLights.size(); i++) {
            PointLight light = pointLights.get(i);
            if (light.isOn()) {
                count++;

                shader.setVec3(gl, "pointLight[" + i + "].position", light.getPosition());
                shader.setVec3(gl, "pointLight[" + i + "].ambient", light.getMaterial().getAmbient());
                shader.setVec3(gl, "pointLight[" + i + "].diffuse", light.getMaterial().getDiffuse());
                shader.setVec3(gl, "pointLight[" + i + "].specular", light.getMaterial().getSpecular());
                shader.setFloat(gl, "pointLight[" + i + "].constant", light.getAttenuation().x);
                shader.setFloat(gl, "pointLight[" + i + "].linear", light.getAttenuation().y);
                shader.setFloat(gl, "pointLight[" + i + "].quadratic", light.getAttenuation().z);
            }
        }
        shader.setInt(gl, "numOfPointLights", count);
        count = 0;
        for (int i = 0; i < spotLights.size(); i++) {
            SpotLight light = spotLights.get(i);
            if (light.isOn()) {
                count++;
                shader.setVec3(gl, "spotLight[" + i + "].position", light.getPosition());
                shader.setVec3(gl, "spotLight[" + i + "].direction", light.getDirection());
                shader.setVec3(gl, "spotLight[" + i + "].ambient", light.getMaterial().getAmbient());
                shader.setVec3(gl, "spotLight[" + i + "].diffuse", light.getMaterial().getDiffuse());
                shader.setVec3(gl, "spotLight[" + i + "].specular", light.getMaterial().getSpecular());
                shader.setFloat(gl, "spotLight[" + i + "].innerAngleCos", light.getInnerAngleCos());
                shader.setFloat(gl, "spotLight[" + i + "].outerAngleCos", light.getOuterAngleCos());
            }
        }
        shader.setInt(gl, "numOfSpotLights", count);

    }

    public void dispose(GL3 gl2) {
        for (Light light : directionalLights) {
            light.dispose(gl);
        }
        for (Light light : pointLights) {
            light.dispose(gl);
        }
        for (Light light : spotLights) {
            light.dispose(gl);
        }
    }

    public double getSeconds() {
        return System.currentTimeMillis();
    }

    public float getElapsedTime() {
        return (float) (getSeconds() - startTime);
    }

}
