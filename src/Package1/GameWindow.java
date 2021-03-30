package Package1;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {

    private static long last_frame_time;
    private static GameWindow game_Window;
    private static Image background; // фон
    private static Image game_over;
    private static Image drop; // капля
    private static float drop_left = 200;
    private static float drop_top = -100;
    private static float drop_v = 200; // скорость капли
    private static int score;



    public static void main(String[] args) throws IOException {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        game_Window = new GameWindow();
        game_Window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // при закрытии окна программа завершится
        game_Window.setLocation(200,100); // устанавливаем точку в которой будет появляться наше окно
        game_Window.setSize(906,478); // настроим размер окна
        game_Window.setResizable(false); // запретим изменение нашего окна (например, с помощью мыши)
        last_frame_time = System.nanoTime();
        GameField gameField = new GameField();
        gameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                if (is_drop) {
                    drop_top = -100;
                    drop_left = (int) (Math.random() * (gameField.getWidth() - drop.getWidth(null)));
                    drop_v = drop_v + 20;
                    score++;
                    game_Window.setTitle("Score: " + score);
                }
            }
        });
        game_Window.add(gameField);
        game_Window.setVisible(true); // делаем наше окно видимым (по умолчанию оно невидимое)
    }

    private static void onRepaint(Graphics graphics) { // метод для рисования
        long current_time = System.nanoTime();
        float delta_time = (current_time  - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;

        drop_top = drop_top + drop_v * delta_time;
        graphics.drawImage(background,0,0,null);
        graphics.drawImage(drop,(int) drop_left, (int) drop_top,null);
        if (drop_top > game_Window.getHeight()) {
            graphics.drawImage(game_over,280,120,null);
        }
    }

    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
