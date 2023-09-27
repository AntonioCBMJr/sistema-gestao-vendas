import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {
    public MenuPrincipal() {
        // Configurar a janela principal
        setTitle("Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);

        // Layout
        JPanel panel = new JPanel(new GridLayout(2, 2));

        // Botão para abrir tela de cadastro de produtos
        JButton btnCadastroProdutos = new JButton("Cadastro de Produtos");
        btnCadastroProdutos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verificar se a tela de cadastro de produtos já está visível
                if (!new TelaCadastroProdutos().isVisible()) {
                    // Se não estiver visível, torná-la visível
                    new TelaCadastroProdutos().setVisible(true);
                }
            }
        });
        panel.add(btnCadastroProdutos);

        // Botão para abrir tela de cadastro de clientes
        JButton btnCadastroClientes = new JButton("Cadastro de Clientes");
        btnCadastroClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verificar se a tela de cadastro de clientes já está visível
                if (!new TelaCadastroClientes().isVisible()) {
                    // Se não estiver visível, torná-la visível
                    new TelaCadastroClientes().setVisible(true);
                }
            }
        });
        panel.add(btnCadastroClientes);

        // Botão para abrir tela de vendas
        JButton btnVendas = new JButton("Vendas");
        btnVendas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verificar se a tela de vendas já está visível
                if (!new TelaPesquisaVendas().isVisible()) {
                    // Se não estiver visível, torná-la visível
                    new TelaPesquisaVendas().setVisible(true);
                }
            }
        });
        panel.add(btnVendas);

        // Botão para abrir tela de consulta de vendas
        JButton btnConsultaVendas = new JButton("Consulta de Vendas");
        btnConsultaVendas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verificar se a tela de cadastro de produtos já está visível
                if (!new TelaConsultaVendas().isVisible()) {
                    // Se não estiver visível, torná-la visível
                    new TelaConsultaVendas().setVisible(true);
                }
            }
        });
        panel.add(btnConsultaVendas);

        getContentPane().add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
            }
        });
    }
}