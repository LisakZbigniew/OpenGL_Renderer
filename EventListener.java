//Code adapted form exercise solution M04_GLEventListener
//Own code noted with comments Zbigniew Lisak (zjlisak1@sheffield.ac.uk)

import gmaths.*;
import light.*;
import sgGraph.*;
import util.*;


import com.jogamp.opengl.*;



public class EventListener implements GLEventListener {

    private static final boolean DISPLAY_SHADERS = false;

    public EventListener(Camera camera) {
        this.camera = camera;
        this.camera.setPosition(new Vec3(14, 20, 30));
    }

    // ***************************************************
    /*
     * METHODS DEFINED BY GLEventListener
     */

    /* Initialisation */
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LESS);
        gl.glFrontFace(GL.GL_CCW); // default is 'CCW'
        gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
        gl.glCullFace(GL.GL_BACK); // default is 'back', assuming CCW
        initialise(gl);
    }

    /* Called to indicate the drawing surface has been moved and/or resized */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(x, y, width, height);
        float aspect = (float) width / (float) height;
        camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
    }

    /* Draw */
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        //Own code
        lamp.updateLamp();
        render(gl);
        //End of own code
    }

    /* Clean up memory, if necessary */
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        //Own code
        scene.dispose(gl);
        root.dispose(gl);
        //End of own code
    }

    // ***************************************************
    /*
     * INTERACTION
     *
     *
     */

    //Own code

    
    Pose[] poses = new Pose[5];

    public void setPose(int i) {
        Pose p = poses[i];
        Mat4 t = Mat4.multiply(Mat4Transform.translate(p.posX, 0, p.posZ), Mat4Transform.rotateAroundY(p.rotY));
        robotTransform.setTransform(t);
        robotTransform.update(false);
        robot.updatePose(p.bodyPitch, p.bodyRoll, p.headPitch, p.headYaw, p.leftEarAngle, p.rightEarAngle);
    }

    public void changeSpotLightState() {
        lamp.setLampState(!lamp.getLampState());
    }


    public void setLight1(float lightIntensity) {
        light1.dimLight(lightIntensity);
    }

    public void setLight2(float lightIntensity) {
        light2.dimLight(lightIntensity);
    }

    public void setDayTime(float dayTime) {
        scene.setTint(dayTime);
    }
    //End of own code




    // ***************************************************
    /*
     * THE SCENE Now define all the methods to handle the scene. This will be added
     * to in later examples.
     */

    private Camera camera;

    //Own Code
    private SGNode root;
    private SceneMeta scene;
    private SpotLamp lamp;
    private PointLight light1,light2;
    Robot robot;
    TransformNode robotTransform;

    private void initialise(GL3 gl){
        buildScene(gl);
        setPoses();
        setPose(0);
    }

    private void buildScene(GL3 gl) {
        Texture textureFloor =       TextureLibrary.loadTexture(gl, "textures/WoodFloor024_1K_Color.jpg");
        Texture textureWall =        TextureLibrary.loadTexture(gl, "textures/Plaster001_1K_Color.jpg");
        Texture textureDoor =        TextureLibrary.loadTexture(gl, "textures/doors_wood_doors_0217_01_s.jpg");
        Texture textureEgg =         TextureLibrary.loadTexture(gl, "textures/Tiles_048_basecolor.jpg");
        Texture textureEggSpecular = TextureLibrary.loadTexture(gl, "textures/Tiles_048_metallic.jpg");
        Texture textureScreen =      TextureLibrary.loadTexture(gl, "textures/phone_screen.jpg");

        String[] envFaces = {"textures/environment/posx.jpg",
                             "textures/environment/negx.jpg",
                             "textures/environment/negy.jpg",
                             "textures/environment/posy.jpg",
                             "textures/environment/posz.jpg",
                             "textures/environment/negz.jpg"};
        Texture environmentTexture = TextureLibrary.loadCubemap(gl, envFaces);

        scene = new SceneMeta(gl,camera);

        light1 = new PointLight(gl, camera, scene, new Vec3(1.0f, 0.0045f, 0.00075f), 0.5f);
        light1.setMaterial(Material.multiply(new Material(), 0.8f));

        light2 = new PointLight(gl, camera, scene, new Vec3(1.0f, 0.0045f, 0.00075f), 0.5f);
        light2.setMaterial(Material.multiply(new Material(), 0.8f));

        LightNode light1Node = new LightNode("Main Light1", light1);
        LightNode light2Node = new LightNode("Main Light2", light2);

        TransformNode light1TransformNode = new TransformNode("light transform node", Mat4Transform.translate(8, 20, -1));
        TransformNode light2TransformNode = new TransformNode("light transform node", Mat4Transform.translate(-8, 20, 8));
        
        root = new NameNode("root",scene);

        Texture[] textureIds = {textureFloor,textureWall,textureDoor, environmentTexture};
        Room roomNode = new Room(gl, scene, textureIds);
        TransformNode transformRoom = new TransformNode("scale room", Mat4Transform.scale(32, 32, 32));

        lamp = new SpotLamp(gl,scene);
        TransformNode lampTransform = new TransformNode("move Lamp", Mat4Transform.translate(12, 0, 8));

        Phone phone = new Phone(gl, scene, textureScreen);
        Mat4 t = Mat4.multiply(Mat4Transform.translate(10, 0, -10), Mat4Transform.rotateAroundY(-45));
        TransformNode phoneTransform = new TransformNode("move Phone", t);

        LargeEgg egg = new LargeEgg(gl, scene, textureEgg, textureEggSpecular);

        robot = new Robot(gl, scene);
        robotTransform = new TransformNode("robot position", Mat4Transform.translate(0,0,0));
        

        root.addChild(light1TransformNode);
            light1TransformNode.addChild(light1Node);
        root.addChild(light2TransformNode);
            light2TransformNode.addChild(light2Node);
        root.addChild(transformRoom);
            transformRoom.addChild(roomNode);
        root.addChild(lampTransform);
            lampTransform.addChild(lamp);
        root.addChild(phoneTransform);
            phoneTransform.addChild(phone);
        root.addChild(egg);
        root.addChild(robotTransform);
            robotTransform.addChild(robot);


        root.update(true);

        //root.print(0, false);
        //System.exit(0);
    }

    private void setPoses(){
        //Pose 1 Entry
        poses[0] = new Pose(-6, -10, 20, 10, 0, -10, 0, 15, -15);
        //Pose 2 Phone
        poses[1] = new Pose(6, -8, 110, 0, 20, 0, 10, -5, -35);
        //Pose 3 SpotLamp
        poses[2] = new Pose(4, 4, 70, 15, 0, -70, 0, 15, -15);
        //Pose 4 Egg
        poses[3] = new Pose(-4, 1, 100, -40, 0, 40, 0, 65, -65);
        //Pose 5 Window
        poses[4] = new Pose(-15, 13, -90, 0, 40, 0, 0, 0, 0);
    }


    private void render(GL3 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        root.draw(gl);
        
    }
    //End of own code

}