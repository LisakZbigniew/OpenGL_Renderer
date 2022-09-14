package sgGraph;
//Code adapted form exercise solution
//Own code noted with comments Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
import com.jogamp.opengl.*;

import model.Model;

public class ModelNode extends SGNode {

    protected Model model;

    public ModelNode(String name, Model m) {
        super(name, null);
        model = m;
    }

    public void draw(GL3 gl) {
        model.render(gl, worldTransform);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).draw(gl);
        }
    }

    public void dispose(GL3 gl) {
        model.dispose(gl);
        for (SGNode node : children) {
            node.dispose(gl);
        }
    }

}