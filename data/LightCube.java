package data;
//Code form supplied exercises extracted to its own class
public final class LightCube {
    public static final float[] vertices = new float[] { // x,y,z
            -0.5f, -0.5f, -0.5f, // 0
            -0.5f, -0.5f, 0.5f, // 1
            -0.5f, 0.5f, -0.5f, // 2
            -0.5f, 0.5f, 0.5f, // 3
            0.5f, -0.5f, -0.5f, // 4
            0.5f, -0.5f, 0.5f, // 5
            0.5f, 0.5f, -0.5f, // 6
            0.5f, 0.5f, 0.5f // 7
    };

    public static final int[] indices = new int[] { 0, 1, 3, // x -ve
            3, 2, 0, // x -ve
            4, 6, 7, // x +ve
            7, 5, 4, // x +ve
            1, 5, 7, // z +ve
            7, 3, 1, // z +ve
            6, 4, 0, // z -ve
            0, 2, 6, // z -ve
            0, 4, 5, // y -ve
            5, 1, 0, // y -ve
            2, 3, 7, // y +ve
            7, 6, 2 // y +ve
    };

    public static final int vertexStride = 3;
    public static final int vertexXYZFloats = 3;
}
