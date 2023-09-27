import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelaVendas extends JFrame {
    private JComboBox<String> clientesComboBox;
    private JComboBox<String> produtosComboBox;
    private JTextField quantidadeField;
    private JTextField valorUnitarioField; // Campo de valor unitário
    private JButton adicionarButton;
    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;
    private JTextField valorTotalField; // Campo de valor total
    private JButton salvarButton; // Botão de salvar


    public TelaVendas() {
        // Configurações da janela
        setTitle("Tela de Vendas");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Componentes da janela
        JPanel painelSuperior = new JPanel();

        JLabel labelCliente = new JLabel("Cliente:");
        clientesComboBox = new JComboBox<>();

        JLabel labelProduto = new JLabel("Produto:");
        produtosComboBox = new JComboBox<>();
        JLabel labelQuantidade = new JLabel("Quantidade:");
        quantidadeField = new JTextField(5);

        JLabel labelValorUnitario = new JLabel("Valor Unitário:"); // Rótulo para o campo de valor unitário
        valorUnitarioField = new JTextField(5); // Campo de valor unitário
        valorUnitarioField.setEditable(false);

        JLabel labelValorTotal = new JLabel("Valor Total:"); // Rótulo para o campo de valor total
        valorTotalField = new JTextField(5); // Campo de valor total
        valorTotalField.setEditable(false); // Torna o campo apenas para consulta

        adicionarButton = new JButton("Adicionar");

        painelSuperior.add(labelCliente);
        painelSuperior.add(clientesComboBox);
        painelSuperior.add(labelProduto);
        painelSuperior.add(produtosComboBox);
        painelSuperior.add(labelQuantidade);
        painelSuperior.add(quantidadeField);
        painelSuperior.add(labelValorUnitario); // Adiciona o rótulo do valor unitário
        painelSuperior.add(valorUnitarioField); // Adiciona o campo de valor unitário
        //painelSuperior.add(labelValorTotal); // Adiciona o rótulo do valor total
        //painelSuperior.add(valorTotalField); // Adiciona o campo de valor total
        painelSuperior.add(adicionarButton);

        // Criação da tabela de itens da venda
        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("Produto");
        modeloTabela.addColumn("Quantidade");
        modeloTabela.addColumn("Valor Unitário");
        modeloTabela.addColumn("Valor Total");
        tabelaItens = new JTable(modeloTabela);

        // Adiciona a tabela a um JScrollPane para rolagem
        JScrollPane scrollPane = new JScrollPane(tabelaItens);

        // Adiciona os componentes ao painel principal (parte superior)
        panel.add(painelSuperior, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Criação do painel inferior (menu)
        JPanel painelInferior = new JPanel();

        // Campo "Valor Total" no menu inferior
        JLabel labelValorTotalInferior = new JLabel("Valor Total:");
        painelInferior.add(labelValorTotalInferior);
        painelInferior.add(valorTotalField); // Campo de valor total

        // Botão "Salvar" no menu inferior
        salvarButton = new JButton("Salvar");
        painelInferior.add(salvarButton);

        // Define ação do botão "Adicionar"
        adicionarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarItem();
            }
        });

        // Define ação do botão "Salvar"
        salvarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarVenda();
            }
        });

        // Adiciona o painel inferior (menu) ao painel principal
        panel.add(painelInferior, BorderLayout.SOUTH);

        // Define ação quando o item do ComboBox de produtos é alterado
        produtosComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarValorUnitario();
            }
        });

        // Adiciona o painel principal à janela
        add(panel);

        // Carregue informações dos produtos do banco de dados
        carregarProdutos();

        // Carregue informações dos parceiros do banco de dados
        carregarParceiros();
    }

    private void carregarProdutos() {
        DefaultComboBoxModel<String> produtosModel = (DefaultComboBoxModel<String>) produtosComboBox.getModel();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT descricao FROM produtos");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String produto = resultSet.getString("descricao");
                produtosModel.addElement(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarParceiros() {
        DefaultComboBoxModel<String> clientesModel = (DefaultComboBoxModel<String>) clientesComboBox.getModel();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT nome FROM clientes");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String cliente = resultSet.getString("nome");
                clientesModel.addElement(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void atualizarValorUnitario() {
        String produtoSelecionado = (String) produtosComboBox.getSelectedItem();
        if (produtoSelecionado != null) {
            try (Connection connection = DatabaseConfig.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT preco FROM produtos WHERE descricao = ?")) {
                preparedStatement.setString(1, produtoSelecionado);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        double preco = resultSet.getDouble("preco");
                        valorUnitarioField.setText(String.valueOf(preco));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void adicionarItem() {
        // Obtenha o produto selecionado e a quantidade
        String produtoSelecionado = (String) produtosComboBox.getSelectedItem();
        int quantidade = Integer.parseInt(quantidadeField.getText());

        // Verifica se a quantidade é válida (maior que zero)
        if (quantidade <= 0) {
            JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.");
            return;
        }

        // Verificar saldo do produto
        if (!verificarSaldoProduto(produtoSelecionado, quantidade)) {
            JOptionPane.showMessageDialog(this, "Saldo insuficiente para o produto selecionado.");
            return;
        }

        // Obtenha o valor unitário do campo (já foi atualizado no evento do ComboBox)
        double precoUnitario = Double.parseDouble(valorUnitarioField.getText());

        // Verifica se o produto já está na tabela
        boolean produtoJaAdicionado = false;
        int linhaProduto = -1;
        for (int row = 0; row < modeloTabela.getRowCount(); row++) {
            String produtoNaTabela = (String) modeloTabela.getValueAt(row, 0);
            if (produtoNaTabela.equals(produtoSelecionado)) {
                produtoJaAdicionado = true;
                linhaProduto = row;
                break;
            }
        }

        // Se o produto já estiver na tabela, atualize a quantidade e o valor total
        if (produtoJaAdicionado) {
            int quantidadeExistente = (int) modeloTabela.getValueAt(linhaProduto, 1);
            double valorTotalExistente = (double) modeloTabela.getValueAt(linhaProduto, 3);

            quantidadeExistente += quantidade;
            valorTotalExistente = quantidadeExistente * precoUnitario;

            modeloTabela.setValueAt(quantidadeExistente, linhaProduto, 1);
            modeloTabela.setValueAt(valorTotalExistente, linhaProduto, 3);
        } else {
            // Caso contrário, adicione um novo item à tabela
            double valorTotal = precoUnitario * quantidade;
            modeloTabela.addRow(new Object[]{produtoSelecionado, quantidade, precoUnitario, valorTotal});
        }

        // Limpa os campos de entrada
        quantidadeField.setText("");

        // Atualiza o valor total no campo da parte inferior
        atualizarValorTotal();
    }

    private boolean verificarSaldoProduto(String produto, int quantidade) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT quantidade FROM produtos WHERE descricao = ?")) {
            preparedStatement.setString(1, produto);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int saldo = resultSet.getInt("quantidade");
                    return saldo >= quantidade;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    private void atualizarValorTotal() {
        double total = 0.0;
        for (int row = 0; row < modeloTabela.getRowCount(); row++) {
            double valorItem = (double) modeloTabela.getValueAt(row, 3);
            total += valorItem;
        }
        valorTotalField.setText(String.valueOf(total));
    }

    private void salvarVenda() {
        // Obtenha os dados da venda
        String clienteSelecionado = (String) clientesComboBox.getSelectedItem();
        // Obtém a data atual como um java.util.Date
        java.sql.Date dataVenda = new java.sql.Date(new java.util.Date().getTime());

        double valorTotal = Double.parseDouble(valorTotalField.getText());

        // Verifique se todos os campos necessários estão preenchidos
        if (clienteSelecionado.isEmpty() || valorTotal <= 0 || modeloTabela.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos antes de salvar a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConfig.getConnection()) {
            // Insira os dados da venda na tabela "vendas"
            String insertVendaQuery = "INSERT INTO vendas (data, cliente_id, valor_total, status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement vendaStatement = connection.prepareStatement(insertVendaQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                vendaStatement.setDate(1,  dataVenda);
                vendaStatement.setInt(2, getIdCliente(clienteSelecionado));
                vendaStatement.setDouble(3, valorTotal);
                vendaStatement.setString(4, "digitando"); // Status inicial "digitando"

                vendaStatement.executeUpdate();

                // Obtenha o ID da venda recém-inserida
                int vendaId;
                try (ResultSet generatedKeys = vendaStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        vendaId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Erro ao obter o ID da venda.");
                    }
                }

                // Insira os dados dos itens da venda na tabela "itens_venda"
                String insertItemQuery = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco, valor_total) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement itemStatement = connection.prepareStatement(insertItemQuery)) {
                    for (int row = 0; row < modeloTabela.getRowCount(); row++) {
                        String produto = (String) modeloTabela.getValueAt(row, 0);
                        int quantidade = (int) modeloTabela.getValueAt(row, 1);
                        double precoUnitario = (double) modeloTabela.getValueAt(row, 2);
                        double valorItem = (double) modeloTabela.getValueAt(row, 3);

                        itemStatement.setInt(1, vendaId);
                        itemStatement.setInt(2, getIdProduto(produto));
                        itemStatement.setInt(3, quantidade);
                        itemStatement.setDouble(4, precoUnitario);
                        itemStatement.setDouble(5, valorItem);

                        itemStatement.executeUpdate();
                    }
                }


                // Após salvar a venda com sucesso, atualize o saldo dos produtos
                for (int row = 0; row < modeloTabela.getRowCount(); row++) {
                    String produto = (String) modeloTabela.getValueAt(row, 0);
                    int quantidade = (int) modeloTabela.getValueAt(row, 1);
                    atualizarSaldoProduto(produto, quantidade);
                }

                JOptionPane.showMessageDialog(this, "Venda salva com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Limpe os campos e a tabela após salvar a venda
                limparCampos();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar a venda: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarSaldoProduto(String produto, int quantidadeVendida) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            // Obtenha o saldo atual
            int saldoAtual = 0;
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT quantidade FROM produtos WHERE descricao = ?")) {
                preparedStatement.setString(1, produto);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        saldoAtual = resultSet.getInt("quantidade");
                    }
                }
            }

            // Calcule o novo saldo
            int novoSaldo = saldoAtual - quantidadeVendida;

            // Atualize o saldo na tabela de produtos
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE produtos SET quantidade = ? WHERE descricao = ?")) {
                preparedStatement.setInt(1, novoSaldo);
                preparedStatement.setString(2, produto);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        clientesComboBox.setSelectedIndex(0);
        produtosComboBox.setSelectedIndex(0);
        quantidadeField.setText("");
        valorUnitarioField.setText("");
        valorTotalField.setText("");
        modeloTabela.setRowCount(0); // Limpa a tabela de itens
    }

    // Método para obter o ID do cliente com base no nome
    private int getIdCliente(String clienteNome) {
        int clienteId = -1; // Valor padrão para indicar que nenhum cliente foi encontrado

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM clientes WHERE nome = ?")) {
            preparedStatement.setString(1, clienteNome);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    clienteId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clienteId;
    }

    // Método para obter o ID do produto com base no nome
    private int getIdProduto(String produtoNome) {
        int produtoId = -1; // Valor padrão para indicar que nenhum produto foi encontrado

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM produtos WHERE descricao = ?")) {
            preparedStatement.setString(1, produtoNome);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    produtoId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtoId;
    }

    // Método para obter a data atual como um objeto Date
    private Date getCurrentDate() {
        return new Date();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaVendas().setVisible(true);
            }
        });
    }
}