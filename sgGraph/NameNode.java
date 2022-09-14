package sgGraph;
//Code adapted form exercise solution
//Own code noted with comments Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
import util.*;

public class NameNode extends SGNode {
  
  public NameNode(String name) {
    this(name,null);
  }
  public NameNode(String name,SceneMeta scene) {
    super(name,scene);
  }
  
}