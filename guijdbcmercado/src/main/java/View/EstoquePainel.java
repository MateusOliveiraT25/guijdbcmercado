package View;

import Controller.EstoqueControll;
import Model.Produto;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class EstoquePainel extends JPanel {
    private EstoqueControll gerenciadorEstoque;

    public EstoquePainel(EstoqueControll gerenciadorEstoque) {
        this.gerenciadorEstoque = gerenciadorEstoque;

        JButton listarProdutosButton = new JButton("Listar Produtos");
        listarProdutosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarProdutos();
            }
        });

        JButton adicionarProdutoButton = new JButton("Adicionar Produto");
        adicionarProdutoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarProduto();
            }
        });

        add(listarProdutosButton);
        add(adicionarProdutoButton);
    }

    public void setGerenciadorEstoque(EstoqueControll gerenciadorEstoque) {
        this.gerenciadorEstoque = gerenciadorEstoque;
    }

    private void listarProdutos() {
        List<Produto> produtos = gerenciadorEstoque.listarProdutosDoBanco();
        StringBuilder mensagem = new StringBuilder("Produtos em Estoque:\n");

        for (Produto produto : produtos) {
            mensagem.append(produto.getNome()).append(" - Quantidade: ").append(produto.getQuantidade()).append("\n");
        }

        JOptionPane.showMessageDialog(this, mensagem.toString(), "Produtos em Estoque", JOptionPane.INFORMATION_MESSAGE);
    }

    private void adicionarProduto() {
    // Criação do JDialog
    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Adicionar Produto", true);
    dialog.setLayout(new GridLayout(5, 2));

    // Adiciona campos de entrada e rótulos ao JDialog
    JTextField codigoBarraField = new JTextField();
    JTextField nomeField = new JTextField();
    JTextField quantidadeField = new JTextField();
    JTextField precoField = new JTextField();

    dialog.add(new JLabel("Código de Barras:"));
    dialog.add(codigoBarraField);
    dialog.add(new JLabel("Nome:"));
    dialog.add(nomeField);
    dialog.add(new JLabel("Quantidade:"));
    dialog.add(quantidadeField);
    dialog.add(new JLabel("Preço:"));
    dialog.add(precoField);

    // Adiciona botões de OK e Cancelar ao JDialog
    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("Cancelar");

    dialog.add(okButton);
    dialog.add(cancelButton);

    // Adiciona ação ao botão OK
    okButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Obtem os valores dos campos
                String codigoBarra = codigoBarraField.getText();
                String nome = nomeField.getText();
                int quantidade = Integer.parseInt(quantidadeField.getText());
                double preco = Double.parseDouble(precoField.getText());

                // Chamada do método do controlador para adicionar o produto
                gerenciadorEstoque.adicionarProduto(codigoBarra, nome, quantidade, preco);

                // Exibição de mensagem de sucesso em uma caixa de diálogo
                JOptionPane.showMessageDialog(EstoquePainel.this, "Produto adicionado com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                // Fecha o JDialog
                dialog.dispose();
            } catch (NumberFormatException ex) {
                // Tratamento de erro se a conversão falhar
                JOptionPane.showMessageDialog(dialog, "Erro ao converter valores para números.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    // Adiciona ação ao botão Cancelar
    cancelButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Fecha o JDialog sem fazer alterações
            dialog.dispose();
        }
    });

    // Configurações finais do JDialog
    dialog.setSize(300, 200);
    dialog.setLocationRelativeTo(null); // Centraliza na tela
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    // Exibe o JDialog
    dialog.setVisible(true);
}
}