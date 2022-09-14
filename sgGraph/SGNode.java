package sgGraph;

//Code adapted form exercise solution
//Own code noted with comments Zbigniew Lisak (zjlisak1@sheffield.ac.uk)

import gmaths.*;

import java.util.ArrayList;
import com.jogamp.opengl.*;

import util.*;

public class SGNode {

  protected String name;
  protected ArrayList<SGNode> children;
  protected Mat4 worldTransform;
  protected SceneMeta scene; //New field

  public SGNode(String name,SceneMeta scene) {
    children = new ArrayList<SGNode>();
    this.scene = scene;
    this.name = name;
    worldTransform = new Mat4(1);
  }

  public void addChild(SGNode child) {
    children.add(child);
  }
  
  public void update(boolean init) {
    update(worldTransform,scene,init);
  }
  
  //added propagation of scene and init variable (for LightNode class)
  protected void update(Mat4 t,SceneMeta s, boolean init) {
    worldTransform = t;
    scene = s;
    for (int i=0; i<children.size(); i++) {
      children.get(i).update(t,s,init);
    }
  }

  public void dispose(GL3 gl) {
    for (SGNode node : children) {
        node.dispose(gl);
    }
  }

  protected String getIndentString(int indent) {
    String s = ""+indent+" ";
    for (int i=0; i<indent; ++i) {
      s+="  ";
    }
    return s;
  }
  
  public void print(int indent, boolean inFull) {
    System.out.println(getIndentString(indent)+"Name: "+name);
    if (inFull) {
      System.out.println("worldTransform");
      System.out.println(worldTransform);
    }
    for (int i=0; i<children.size(); i++) {
      children.get(i).print(indent+1, inFull);
    }
  }
  
  public void draw(GL3 gl) {
    for (int i=0; i<children.size(); i++) {
      children.get(i).draw(gl);
    }
  }

}