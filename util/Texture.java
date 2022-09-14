package util;

//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 01.12.2021

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

public class Texture {

    private int id;
    private int type;

    public Texture(int id, int type){
        this.id = id;
        this.type = type;
    }

    public void setTexture(GL3 gl, int unitId){
        gl.glActiveTexture(GL.GL_TEXTURE0 + unitId);
        gl.glBindTexture(type, id);
    }

    public void dispose(GL3 gl){
        int[] id = {this.id};
        gl.glDeleteBuffers(1, id, 0);
    }

}
