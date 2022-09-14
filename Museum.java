//Code adapted form exercise solution M04 
//Own code noted with comments Zbigniew Lisak (zjlisak1@sheffield.ac.uk)

import gmaths.*;
import util.Camera;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Museum extends JFrame implements ActionListener, ChangeListener {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
    private GLCanvas canvas;
    private EventListener glEventListener;
    private final FPSAnimator animator;
    private Camera camera;
    private Map<String,JSlider> sliders;

    public static void main(String[] args) {
        Museum b1 = new Museum("Assignment - Zbigniew Lisak");
        b1.getContentPane().setPreferredSize(dimension);
        b1.pack();
        b1.setVisible(true);
    }

    public Museum(String textForTitleBar) {
        super(textForTitleBar);

        GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
        canvas = new GLCanvas(glcapabilities);
        camera = new Camera(Camera.DEFAULT_POSITION, Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);

        glEventListener = new EventListener(camera);

        canvas.addGLEventListener(glEventListener);
        canvas.addMouseMotionListener(new MyMouseInput(camera));
        canvas.addKeyListener(new MyKeyboardInput(camera));

        getContentPane().add(canvas, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");

        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);

        JPanel p = new JPanel();

        //Own Code
        JButton b = new JButton("Spot Light on/off");
        b.addActionListener(this);
        p.add(b);

        JPanel subp = new JPanel();
        subp.setLayout(new BoxLayout(subp, BoxLayout.Y_AXIS));

        sliders = new HashMap<String,JSlider>();

        JSlider s = new JSlider(0, 100, 100);
        JLabel sl = new JLabel("Light Intensity 1");
        s.addChangeListener(this);
        subp.add(sl);
        subp.add(s);

        sliders.put("Light1", s);

        s = new JSlider(0, 100, 100);
        sl = new JLabel("Light Intensity 2");
        s.addChangeListener(this);
        subp.add(sl);
        subp.add(s);
        p.add(subp);

        sliders.put("Light2", s);

        subp = new JPanel();
        subp.setLayout(new BoxLayout(subp, BoxLayout.Y_AXIS));
        s = new JSlider(0, 100, 0);
        sl = new JLabel("Day time");
        s.addChangeListener(this);
        subp.add(sl);
        subp.add(s);
        p.add(subp);

        sliders.put("DayTime", s);

        //End of own code
        //Here I just relabeled the buttons
        b = new JButton("Pose 1");
        b.addActionListener(this);
        p.add(b);

        b = new JButton("Pose 2");
        b.addActionListener(this);
        p.add(b);

        b = new JButton("Pose 3");
        b.addActionListener(this);
        p.add(b);

        b = new JButton("Pose 4");
        b.addActionListener(this);
        p.add(b);

        b = new JButton("Pose 5");
        b.addActionListener(this);
        p.add(b);

        this.add(p, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                animator.stop();
                remove(canvas);
                dispose();
                System.exit(0);
            }
        });

        animator = new FPSAnimator(canvas, 60);
        animator.start();
    }


    //Adapted the function to new button names and functions
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("Spot Light on/off")) {
            glEventListener.changeSpotLightState();

        } else if (e.getActionCommand().equalsIgnoreCase("Pose 1")) {
            glEventListener.setPose(0);

        } else if (e.getActionCommand().equalsIgnoreCase("Pose 2")) {
            glEventListener.setPose(1);

        } else if (e.getActionCommand().equalsIgnoreCase("Pose 3")) {
            glEventListener.setPose(2);

        } else if (e.getActionCommand().equalsIgnoreCase("Pose 4")) {
            glEventListener.setPose(3);

        } else if (e.getActionCommand().equalsIgnoreCase("Pose 5")) {
            glEventListener.setPose(4);

        } else if (e.getActionCommand().equalsIgnoreCase("quit"))
            System.exit(0);
    }


    //Own code
    @Override
    public void stateChanged(ChangeEvent e) {
        float light1 = sliders.get("Light1").getValue() / 100.0f;
        float light2 = sliders.get("Light2").getValue() / 100.0f;
        float dayTime = sliders.get("DayTime").getValue() / 100.0f;

        glEventListener.setLight1(light1);
        glEventListener.setLight2(light2);
        glEventListener.setDayTime(dayTime);
        
    }
    //End of own code

}

class MyKeyboardInput extends KeyAdapter {
    private Camera camera;

    public MyKeyboardInput(Camera camera) {
        this.camera = camera;
    }

    public void keyPressed(KeyEvent e) {

        Camera.Movement m = Camera.Movement.NO_MOVEMENT;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                m = Camera.Movement.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                m = Camera.Movement.RIGHT;
                break;
            case KeyEvent.VK_UP:
                m = Camera.Movement.UP;
                break;
            case KeyEvent.VK_DOWN:
                m = Camera.Movement.DOWN;
                break;
            case KeyEvent.VK_A:
                m = Camera.Movement.FORWARD;
                break;
            case KeyEvent.VK_Z:
                m = Camera.Movement.BACK;
                break;
        }

        camera.keyboardInput(m);
    }
}

class MyMouseInput extends MouseMotionAdapter {
    private Point lastpoint;
    private Camera camera;

    public MyMouseInput(Camera camera) {
        this.camera = camera;
    }

    /**
     * mouse is used to control camera position
     *
     * @param e instance of MouseEvent
     */
    public void mouseDragged(MouseEvent e) {
        Point ms = e.getPoint();
        float sensitivity = 0.001f;
        float dx = (float) (ms.x - lastpoint.x) * sensitivity;
        float dy = (float) (ms.y - lastpoint.y) * sensitivity;
        // System.out.println("dy,dy: "+dx+","+dy);
        if (e.getModifiers() == MouseEvent.BUTTON1_MASK)
            camera.updateYawPitch(dx, -dy);
        lastpoint = ms;
    }

    /**
     * mouse is used to control camera position
     *
     * @param e instance of MouseEvent
     */
    public void mouseMoved(MouseEvent e) {
        lastpoint = e.getPoint();
    }
}