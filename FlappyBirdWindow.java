import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBirdWindow extends JFrame {
    private Bird bird;
    private ArrayList<Pipe> pipes;
    private Random random;
    private int score = 0;
    private boolean gameOver = false;
    private JLabel scoreLabel;
    private JLabel gameOverLabel;
    private final int PIPE_WIDTH = 60;
    private Timer gameLoop;

    public FlappyBirdWindow() {
        initializeGame();
    }

    private void initializeGame() {
        setSize(360, 640);
        setResizable(false);
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BackgroundPanel background = new BackgroundPanel();
        setContentPane(background);
        background.setLayout(null);

        bird = new Bird();
        pipes = new ArrayList<>();
        random = new Random();
        background.add(bird);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setBounds(10, 10, 150, 30);
        background.add(scoreLabel);

        gameOverLabel = new JLabel("", SwingConstants.CENTER);
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gameOverLabel.setBounds(0, 200, 360, 100);
        gameOverLabel.setVisible(false);
        background.add(gameOverLabel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!gameOver) {
                        bird.jump();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        restartGame();
                    }
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();

        if (gameLoop != null) {
            gameLoop.stop();
        }
        gameLoop = new Timer(20, e -> {
            if (!gameOver) {
                bird.fall();
                movePipes();
                spawnPipes();
                checkCollision();
                updateScore();
            } else {
                gameOverLabel.setText("<html>Game Over!<br>Score: " + score + "<br>Press Enter to Restart</html>");
                gameOverLabel.setVisible(true);
            }
            repaint();
        });
        gameLoop.start();
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new ImageIcon("flappybirdbg.png").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    class Bird extends JLabel {
        private int yPosition = 300;
        private int velocity = 0;
        private final int GRAVITY = 1;
        private final int JUMP = -10; // Giảm từ -15 xuống -10 để nảy nhẹ hơn
        private final int BIRD_WIDTH = 40;
        private final int BIRD_HEIGHT = 30;

        public Bird() {
            ImageIcon originalIcon = new ImageIcon("flappybird.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(BIRD_WIDTH, BIRD_HEIGHT, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledImage));
            setBounds(100, yPosition, BIRD_WIDTH, BIRD_HEIGHT);
        }

        public void jump() {
            velocity = JUMP;
        }

        public void fall() {
            velocity += GRAVITY;
            yPosition += velocity;
            if (yPosition > getParent().getHeight() - BIRD_HEIGHT) {
                yPosition = getParent().getHeight() - BIRD_HEIGHT;
                velocity = 0;
                gameOver = true;
            } else if (yPosition < 0) {
                yPosition = 0;
                velocity = 0;
            }
            setLocation(100, yPosition);
        }

        public Rectangle getBounds() {
            return new Rectangle(100, yPosition, BIRD_WIDTH, BIRD_HEIGHT);
        }
    }

    class Pipe extends JLabel {
        private int xPosition;
        private boolean isTopPipe;
        private boolean passed = false;
        private final int SPEED = 3;

        public Pipe(boolean isTop, int height) {
            isTopPipe = isTop;
            String imagePath = isTop ? "toppipe.png" : "bottompipe.png";
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(PIPE_WIDTH, height, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledImage));
            xPosition = 360;
            if (isTop) {
                setBounds(xPosition, 0, PIPE_WIDTH, height);
            } else {
                setBounds(xPosition, 640 - height, PIPE_WIDTH, height);
            }
        }

        public void move() {
            xPosition -= SPEED;
            setLocation(xPosition, getY());
        }

        public boolean isOffScreen() {
            return xPosition + PIPE_WIDTH < 0;
        }

        public Rectangle getBounds() {
            return new Rectangle(xPosition, getY(), PIPE_WIDTH, getHeight());
        }

        public boolean isPassed() {
            return passed;
        }

        public void setPassed(boolean passed) {
            this.passed = passed;
        }

        public boolean isTopPipe() {
            return isTopPipe;
        }
    }

    private void movePipes() {
        ArrayList<Pipe> pipesToRemove = new ArrayList<>();
        for (Pipe pipe : pipes) {
            pipe.move();
            if (pipe.isOffScreen()) {
                pipesToRemove.add(pipe);
            }
        }
        pipes.removeAll(pipesToRemove);
        for (Pipe pipe : pipesToRemove) {
            getContentPane().remove(pipe);
        }
    }

    private void spawnPipes() {
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).xPosition < 200) {
            int gap = 150;
            int minHeight = 100;
            int maxHeight = 400;
            int topHeight = random.nextInt(maxHeight - minHeight) + minHeight;
            int bottomHeight = 640 - topHeight - gap;

            Pipe topPipe = new Pipe(true, topHeight);
            Pipe bottomPipe = new Pipe(false, bottomHeight);
            pipes.add(topPipe);
            pipes.add(bottomPipe);
            getContentPane().add(topPipe);
            getContentPane().add(bottomPipe);
        }
    }

    private void updateScore() {
        for (Pipe pipe : pipes) {
            if (!pipe.isTopPipe() && !pipe.isPassed() && pipe.xPosition + PIPE_WIDTH < 100) {
                pipe.setPassed(true);
                score++;
                scoreLabel.setText("Score: " + score);
            }
        }
    }

    private void checkCollision() {
        Rectangle birdBounds = bird.getBounds();
        for (Pipe pipe : pipes) {
            if (birdBounds.intersects(pipe.getBounds())) {
                gameOver = true;
                break;
            }
        }
    }

    private void restartGame() {
        gameOver = false;
        score = 0;
        scoreLabel.setText("Score: " + score);
        gameOverLabel.setVisible(false);
        pipes.clear();
        getContentPane().removeAll();
        initializeGame();
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlappyBirdWindow window = new FlappyBirdWindow();
            window.setVisible(true);
        });
    }
}