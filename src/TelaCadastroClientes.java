import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TelaCadastroClientes extends JFrame {
    private JTextField nomeField;
    private JButton cadastrarButton;

    public TelaCadastroClientes() {
        // Configurações da janela
        setTitle("Cadastro de Cliente");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        // Componentes da janela
        JLabel nomeLabel = new JLabel("Nome:");
        nomeField = new JTextField();
        cadastrarButton = new JButton("Cadastrar");

        // Adiciona os componentes ao painel
        panel.add(nomeLabel);
        panel.add(nomeField);
        panel.add(new JLabel()); // Espaço em branco
        panel.add(cadastrarButton);

        // Define a ação do botão cadastrar
        cadastrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarCliente();
            }
        });

        // Adiciona o painel à janela
        add(panel);
    }

    private void cadastrarCliente() {
        // Obtenha o valor do campo de nome
        String nome = nomeField.getText();

        // Crie um objeto Cliente
        Cliente cliente = new Cliente(nome);

        try (Connection connection = DatabaseConfig.getConnection()) {
            // Crie a instrução SQL para inserir o cliente na tabela de clientes
            String sql = "INSERT INTO clientes (nome) VALUES (?)";

            // Crie um objeto PreparedStatement para executar a instrução SQL
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Configure os parâmetros da instrução SQL
                preparedStatement.setString(1, cliente.getNome());

                // Execute a instrução SQL para inserir o cliente
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Sucesso
                    JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
                } else {
                    // O cliente não pôde ser cadastrado
                    JOptionPane.showMessageDialog(this, "Falha ao cadastrar o cliente.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar o cliente.");
        }

        // Limpa o campo de nome
        nomeField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaCadastroClientes().setVisible(true);
            }
        });
    }
}