import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelaPesquisaVendas extends JFrame {
    private JTable tabelaVendas;
    private DefaultTableModel modeloTabela;
    private JButton incluirButton;
    private JButton excluirButton;
    private JButton efetivarButton;
    private JButton extornarButton;

    public TelaPesquisaVendas() {
        // Configurações da janela
        setTitle("Pesquisa de Vendas");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Criação do painel superior (menu)
        JPanel painelSuperior = new JPanel();

        // Botão "Incluir" no menu superior
        incluirButton = new JButton("Incluir");
        painelSuperior.add(incluirButton);

        // Botão "Excluir" no menu superior
        excluirButton = new JButton("Excluir");
        painelSuperior.add(excluirButton);

        // Botão "Efetivar" no menu superior
        efetivarButton = new JButton("Efetivar");
        painelSuperior.add(efetivarButton);

        // Botão "Extornar" no menu superior
        extornarButton = new JButton("Extornar");
        painelSuperior.add(extornarButton);

        // Adicione o painel superior (menu) ao painel principal
        panel.add(painelSuperior, BorderLayout.NORTH);

        // Criação da tabela de vendas
        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID Venda");
        modeloTabela.addColumn("Data");
        modeloTabela.addColumn("Parceiro");
        modeloTabela.addColumn("Valor");
        modeloTabela.addColumn("Status");
        tabelaVendas = new JTable(modeloTabela);

        // Adiciona a tabela a um JScrollPane para rolagem
        JScrollPane scrollPane = new JScrollPane(tabelaVendas);

        // Adiciona os componentes ao painel principal
        panel.add(scrollPane, BorderLayout.CENTER);

        // Adicionar ação ao botão "Incluir"
        incluirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para abrir a tela de vendas para inclusão
                abrirTelaVendasParaInclusao();
            }
        });

        // Adicionar ação ao botão "Excluir"
        excluirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para excluir a venda selecionada
                excluirVendaSelecionada();
            }
        });

        // Adicionar ação ao botão "Efetivar"
        efetivarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para efetivar a venda selecionada
                efetivarVendaSelecionada();
            }
        });

        // Adicionar ação ao botão "Extornar"
        extornarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para extornar a venda selecionada
                extornarVendaSelecionada();
            }
        });

        // Adicione o painel principal à janela
        add(panel);

        // Carregue as informações das vendas do banco de dados
        carregarVendas();
    }

    // Restante da classe com métodos existentes

    // Método para abrir a tela de vendas para inclusão
    private void abrirTelaVendasParaInclusao() {
        // Crie uma instância da TelaVendas
        TelaVendas telaVendas = new TelaVendas();

        // Configure a visibilidade da tela de vendas
        telaVendas.setVisible(true);
    }

    // Método para excluir a venda selecionada
    private void excluirVendaSelecionada() {
        // Obtém a linha selecionada na tabela
        int selectedRow = tabelaVendas.getSelectedRow();

        // Verifica se uma linha está selecionada
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda para excluir.");
            return;
        }

        // Obtém o ID da venda a partir da tabela
        int idVenda = (int) tabelaVendas.getValueAt(selectedRow, 0);

        // Pergunta ao usuário se deseja realmente excluir
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir esta venda?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            // Se o usuário confirmar, execute a exclusão
            excluirVenda(idVenda);
        }
    }

    // Método para executar a exclusão da venda
    private void excluirVenda(int idVenda) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            // Primeiro, exclua os itens da venda (substitua "itens_venda" pelo nome real da tabela)
            String deleteItensQuery = "DELETE FROM itens_venda WHERE venda_id = ?";
            try (PreparedStatement deleteItensStatement = connection.prepareStatement(deleteItensQuery)) {
                deleteItensStatement.setInt(1, idVenda);
                deleteItensStatement.executeUpdate();
            }

            // Em seguida, exclua a venda (substitua "vendas" pelo nome real da tabela)
            String deleteVendaQuery = "DELETE FROM vendas WHERE id = ?";
            try (PreparedStatement deleteVendaStatement = connection.prepareStatement(deleteVendaQuery)) {
                deleteVendaStatement.setInt(1, idVenda);
                deleteVendaStatement.executeUpdate();
            }

            // Recarregue as informações das vendas após a exclusão
            carregarVendas();

            JOptionPane.showMessageDialog(this, "Venda excluída com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao excluir a venda: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para efetivar a venda selecionada
    private void efetivarVendaSelecionada() {
        // Obtém a linha selecionada na tabela
        int selectedRow = tabelaVendas.getSelectedRow();

        // Verifica se uma linha está selecionada
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda para efetivar.");
            return;
        }

        // Obtém o ID da venda a partir da tabela
        int idVenda = (int) tabelaVendas.getValueAt(selectedRow, 0);

        // Pergunta ao usuário se deseja realmente efetivar a venda
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente efetivar esta venda?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            // Se o usuário confirmar, execute a efetivação da venda
            efetivarVenda(idVenda);
        }
    }

    // Método para efetivar a venda no banco de dados
    private void efetivarVenda(int idVenda) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            // Execute uma atualização para definir o status como "Efetivada" (substitua "vendas" pelo nome real da tabela)
            String updateQuery = "UPDATE vendas SET status = 'Efetivada' WHERE id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, idVenda);
                updateStatement.executeUpdate();
            }

            // Recarregue as informações das vendas após a efetivação
            carregarVendas();

            JOptionPane.showMessageDialog(this, "Venda efetivada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao efetivar a venda: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para extornar a venda selecionada
    private void extornarVendaSelecionada() {
        // Obtém a linha selecionada na tabela
        int selectedRow = tabelaVendas.getSelectedRow();

        // Verifica se uma linha está selecionada
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda para extornar.");
            return;
        }

        // Obtém o ID da venda a partir da tabela
        int idVenda = (int) tabelaVendas.getValueAt(selectedRow, 0);

        // Pergunta ao usuário se deseja realmente extornar a venda
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente extornar esta venda?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            // Se o usuário confirmar, execute o extorno da venda
            extornarVenda(idVenda);
        }
    }

    // Método para extornar a venda no banco de dados
    private void extornarVenda(int idVenda) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            // Execute uma atualização para definir o status como "Digitando" (substitua "vendas" pelo nome real da tabela)
            String updateQuery = "UPDATE vendas SET status = 'Digitando' WHERE id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, idVenda);
                updateStatement.executeUpdate();
            }

            // Recarregue as informações das vendas após o extorno
            carregarVendas();

            JOptionPane.showMessageDialog(this, "Venda extornada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao extornar a venda: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para carregar as vendas do banco de dados e atualizar a tabela
    private void carregarVendas() {
        DefaultTableModel model = (DefaultTableModel) tabelaVendas.getModel();
        model.setRowCount(0); // Limpa todas as linhas da tabela

        try (Connection connection = DatabaseConfig.getConnection()) {
            String query = "SELECT id, data, cliente_id, valor_total, status FROM vendas";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int idVenda = resultSet.getInt("id");
                        String data = resultSet.getString("data");
                        String parceiro = resultSet.getString("cliente_id");
                        double valor = resultSet.getDouble("valor_total");
                        String status = resultSet.getString("status");

                        // Adicione uma linha à tabela com os dados da venda
                        model.addRow(new Object[]{idVenda, data, parceiro, valor, status});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar as vendas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaPesquisaVendas().setVisible(true);
            }
        });
    }
}