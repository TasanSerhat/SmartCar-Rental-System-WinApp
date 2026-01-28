package ui;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.white);

        int width = 450;
        int height = 300;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        JLabel label = new JLabel(new ImageIcon("splash.png"));
        JLabel title = new JLabel("SmartCar Rental System", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        content.add(title, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        content.add(progressBar, BorderLayout.SOUTH);

        Color customColor = new Color(44, 62, 80);
        content.setBorder(BorderFactory.createLineBorder(customColor, 10));

        setVisible(true);

        // Simulate loading
        try {
            for (int i = 0; i <= 100; i++) {
                progressBar.setValue(i);
                Thread.sleep(30); // 3 seconds load time
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setVisible(false);
    }
}
