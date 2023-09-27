import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelaConsultaVendas extends JFrame {
    private JLabel labelData;
    private JTextField campoData;
    private JButton botaoFiltrar;
    private JRadioButton radioConsolidada;
    private JRadioButton radioDetalhada;
    private JTable tabelaResultados; // Tabela para exibir os resultados

    public TelaConsultaVendas() {
        setTitle("Consulta de Vendas");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelFiltro = new JPanel();
        labelData = new JLabel("Data:");
        campoData = new JTextField(10);
        radioConsolidada = new JRadioButton("Consolidada");
        radioDetalhada = new JRadioButton("Detalhada");
        ButtonGroup grupoRadio = new ButtonGroup();
        grupoRadio.add(radioConsolidada);
        grupoRadio.add(radioDetalhada);
        botaoFiltrar = new JButton("Filtrar");

        painelFiltro.add(labelData);
        painelFiltro.add(campoData);
        painelFiltro.add(radioConsolidada);
        painelFiltro.add(radioDetalhada);
        painelFiltro.add(botaoFiltrar);

        // Crie um modelo de tabela vazio
        DefaultTableModel tableModel = new DefaultTableModel();

        // Defina as colunas da tabela
        tableModel.addColumn("Venda ID");
        tableModel.addColumn("Data");
        tableModel.addColumn("Valor Total da Venda");
        tableModel.addColumn("Cliente");
        tableModel.addColumn("Produto");
        tableModel.addColumn("Preço Unitário");
        tableModel.addColumn("Quantidade");
        tableModel.addColumn("Valor Total do Item");

        // Crie a tabela com o modelo de tabela
        tabelaResultados = new JTable(tableModel);

        // Adicione a tabela a um JScrollPane para rolagem
        JScrollPane scrollPane = new JScrollPane(tabelaResultados);

        // Adicione o JScrollPane à área de resultado
        JPanel painelResultado = new JPanel(new BorderLayout());
        painelResultado.add(scrollPane, BorderLayout.CENTER);

        botaoFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtém a data e os tipos de visualização selecionados
                String data = campoData.getText();
                boolean consolidada = radioConsolidada.isSelected();
                boolean detalhada = radioDetalhada.isSelected();

                // Formate a data para o padrão aceito pelo banco de dados (yyyy-MM-dd)
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dataFormatada;
                try {
                    dataFormatada = dateFormat.parse(data);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Formato de data inválido. Use dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Conecta-se ao banco de dados
                try (Connection connection = DatabaseConfig.getConnection()) {
                    String query;

                    if (detalhada) {
                        // Consulta detalhada com base na data
                        query = "SELECT v.id, v.data, v.valor_total, c.nome, pr.descricao, iv.preco, iv.quantidade, iv.valor_total " +
                                "FROM vendas v " +
                                "INNER JOIN clientes c ON v.cliente_id = c.id " +
                                "INNER JOIN itens_venda iv ON v.id = iv.venda_id " +
                                "INNER JOIN produtos pr ON iv.produto_id = pr.id " +
                                "WHERE v.data = ? " +
                                "ORDER BY v.id, iv.id";
                    } else {
                        // Consulta consolidada com base na data
                        query = "SELECT v.id, v.data, v.valor_total, c.nome " +
                                "FROM vendas v " +
                                "INNER JOIN clientes c ON v.cliente_id = c.id " +
                                "WHERE v.data = ? " +
                                "ORDER BY v.id";
                    }

                    // Prepara a consulta
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setDate(1, new java.sql.Date(dataFormatada.getTime()));

                        // Executa a consulta
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            // Limpa o modelo de tabela antes de adicionar novos dados
                            tableModel.setRowCount(0);

                            while (resultSet.next()) {
                                if (detalhada) {
                                    // Obtenha os valores do resultado
                                    int vendaId = resultSet.getInt("id");
                                    Date dataVenda = resultSet.getDate("data");
                                    double valorTotalVenda = resultSet.getDouble("valor_total");
                                    String nomeCliente = resultSet.getString("nome");
                                    String descricaoProduto = resultSet.getString("descricao");
                                    double precoUnitario = resultSet.getDouble("preco");
                                    int quantidade = resultSet.getInt("quantidade");
                                    double valorTotalItem = resultSet.getDouble("valor_total");

                                    // Adicione os valores ao modelo de tabela
                                    tableModel.addRow(new Object[]{
                                            vendaId,
                                            dataVenda,
                                            valorTotalVenda,
                                            nomeCliente,
                                            descricaoProduto,
                                            precoUnitario,
                                            quantidade,
                                            valorTotalItem
                                    });
                                } else {
                                    // Obtenha os valores do resultado
                                    int vendaId = resultSet.getInt("id");
                                    Date dataVenda = resultSet.getDate("data");
                                    double valorTotalVenda = resultSet.getDouble("valor_total");
                                    String nomeCliente = resultSet.getString("nome");

                                    // Adicione os valores ao modelo de tabela
                                    tableModel.addRow(new Object[]{
                                            vendaId,
                                            dataVenda,
                                            valorTotalVenda,
                                            nomeCliente,
                                            "", // Coluna vazia para Produto, Preço Unitário, Quantidade e Valor Total do Item
                                            0.0,
                                            0,
                                            0.0
                                    });
                                }
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao consultar as vendas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLayout(new BorderLayout());
        add(painelFiltro, BorderLayout.NORTH);
        add(painelResultado, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TelaConsultaVendas tela = new TelaConsultaVendas();
                tela.setVisible(true);
            }
        });
    }
}