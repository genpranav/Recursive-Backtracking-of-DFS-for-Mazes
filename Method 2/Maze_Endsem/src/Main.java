
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame implements Runnable {
    public static final int WIDTH = 400, HEIGHT = 200;

    public static Main ins;

    private Thread thread;
    private boolean running;

    JButton start = new JButton("Begin");
    JButton exit = new JButton("Quit");
 
    public Main() {
        // region Window Settings
        super("Maze Generator by Batch 7");
        
        running = false;

        setBounds(0, 0, WIDTH, HEIGHT);
        setResizable(true);
        setLocationRelativeTo(null);

        setLayout(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //endregion
        
        // region Start Button
        start.setSize(150, 100);
        start.setLocation(WIDTH / 2 - 25, HEIGHT - 175);
        start.setBackground(Color.GREEN);
        start.setForeground(Color.BLACK);
        add(start);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ins.setVisible(false);
                 new Maze();
            }
        });
        //endregion
        
        // region Quit Button
        exit.setSize(150, 100);
        exit.setLocation(WIDTH / 3 - 75, HEIGHT - 175);
        exit.setBackground(Color.RED);
        exit.setForeground(Color.BLACK);
        add(exit);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.out.println("Leaving us!!!");
                System.exit(0);
            }
        });
  
        
 
 
        
        setVisible(true);
        this.start();
    }

    private synchronized void start() {
        thread = new Thread(this);
        thread.start();

        running = true;
    }
    private synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void tick() { }

    private void renderComponent() {
        repaint();
    }

    public void run() {
        long past = System.currentTimeMillis();
        while(running) {
            long now = System.currentTimeMillis();
            tick();
            if((now - past) / 1000 == 60)
                renderComponent();
        }
        stop();
    }
    public static void main(String[] args) {
    	System.out.println("Welcome");
        ins = new Main();
    }
}
