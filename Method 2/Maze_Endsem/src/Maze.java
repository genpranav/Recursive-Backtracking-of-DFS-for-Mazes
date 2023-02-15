import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

// import java.util.*;
public class Maze extends JFrame implements Runnable {
    public static final int WIDTH = 600, HEIGHT =600;

    private boolean running = false;
    private Thread thread;

    public int w = WIDTH / 50;
    public int rows = HEIGHT / w, cols = WIDTH / w;


    public Cell current;

    public Stack<Cell> visited = new Stack<Cell>();
    public Cell[][] grid = new Cell[rows][cols];

    // region Random Setup Garbo
    public Maze() {
        super("Maze Generator By Batch 7");

      
        setBounds(0, 0, WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        setLayout(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) grid[i][j] = new Cell(i, j, w);
        }

        current = grid[0][0];

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Main.ins.setVisible(true);
            }
        });

        setVisible(true);
        this.start();
    }
    private void start() {
        thread = new Thread(this);
        thread.start();

        running = true;
    }
    private void stop() {
        try {
            thread.join();
            running = false;
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void tick() { }

    public void run() {
        while(running) {
            tick();
            try {
                render();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    // endregion

    private Cell checkCellNeighbors(Cell curr) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>();
        Random rand = new Random();

        // top
        if(curr.row - 1 >= 0 && !grid[curr.row - 1][curr.col].visited)
            neighbors.add(grid[curr.row - 1][curr.col]);
        // right
        if(curr.col + 1 < cols && !grid[curr.row][curr.col + 1].visited)
            neighbors.add(grid[curr.row][curr.col + 1]);
        // bottom
        if(curr.row + 1 < rows && !grid[curr.row + 1][curr.col].visited)
            neighbors.add(grid[curr.row + 1][curr.col]);
        // left
        if(curr.col - 1 >= 0 && !grid[curr.row][curr.col - 1].visited)
            neighbors.add(grid[curr.row][curr.col - 1]);

        // picking random neighbor
        if(neighbors.size() > 0)
            return neighbors.get(rand.nextInt(neighbors.size()));

        return null;
    }

    public void render() throws InterruptedException {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0 , WIDTH, HEIGHT);

        current.visited = true;
        Cell next = this.checkCellNeighbors(current);
        if(next != null) {
            current.destroyWalls(next);
            visited.push(current);
            current = next;
        }
        else if(visited.size() > 0) {
            current = visited.pop();
        }

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++) {
                if(grid[i][j].visited) {
                    g.setColor(Color.WHITE);
                        g.fillRect(grid[i][j].x, grid[i][j].y, w, w);
                }
                g.setColor(Color.BLUE);
                g.fillRect(current.x, current.y, w, w);

                grid[i][j].drawWalls(g);
            }
        }

        g.dispose();
        bs.show();
        try {
//            thread.sleep(0);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
