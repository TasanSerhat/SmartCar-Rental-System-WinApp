import ui.RoleSelectionForm;
import ui.SplashScreen;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Run Splash Screen
        new SplashScreen();

        // Run Role Selection Screen
        SwingUtilities.invokeLater(() -> {
            new RoleSelectionForm();
        });
    }
}
