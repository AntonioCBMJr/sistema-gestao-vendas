import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Chame o método para criar as tabelas
        DatabaseInitializer.createDatabaseAndTables();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
            }
        });
    }
}
