import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TelaCadastroProdutos extends JFrame {
    private JTextField descricaoField;
    private JTextField precoField;
    private JTextField quantidadeField;

    public TelaCadastroProdutos() {
        // Configurações da janela
        setTitle("Cadastro de Produto");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        // Componentes da janela
        JLabel descricaoLabel = new JLabel("Descrição:");
        descricaoField = new JTextField();
        JLabel precoLabel = new JLabel("Preço:");
        precoField = new JTextField();
        JLabel quantidadeLabel = new JLabel("Quantidade:");
        quantidadeField = new JTextField();
        JButton cadastrarButton = new JButton("Cadastrar");

        // Adiciona os componentes ao painel
        panel.add(descricaoLabel);
        panel.add(descricaoField);
        panel.add(precoLabel);
        panel.add(precoField);
        panel.add(quantidadeLabel);
        panel.add(quantidadeField);
        panel.add(new JLabel()); // Espaço em branco
        panel.add(cadastrarButton);

        // Define a ação do botão cadastrar
        cadastrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarProduto();
            }
        });

        // Adiciona o painel à janela
        add(panel);
    }

    private void cadastrarProduto() {

        // Obtenha os valores dos campos de entrada
        String descricao = descricaoField.getText();
        double preco = Double.parseDouble(precoField.getText());
        int quantidade = Integer.parseInt(quantidadeField.getText());

        // Crie um objeto Cliente
        Produto produto = new Produto(descricao,preco,quantidade);

        try (Connection connection = DatabaseConfig.getConnection()) {
            // Crie a instrução SQL para inserir o produto na tabela de produtos
            String sql = "INSERT INTO produtos (descricao, preco, quantidade) VALUES (?, ?, ?)";

            // Crie um objeto PreparedStatement para executar a instrução SQL
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Configure os parâmetros da instrução SQL
                preparedStatement.setString(1, produto.getDescricao());
                preparedStatement.setDouble(2, produto.getPreco());
                preparedStatement.setInt(3, produto.getQuantidade());

                // Execute a instrução SQL para inserir o produto
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Sucesso
                    JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
                } else {
                    // O produto não pôde ser cadastrado
                    JOptionPane.showMessageDialog(this, "Falha ao cadastrar o produto.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar o produto.");
        }

        // Limpa os campos de entrada
        descricaoField.setText("");
        precoField.setText("");
        quantidadeField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaCadastroProdutos().setVisible(true);
            }
        });
    }
}