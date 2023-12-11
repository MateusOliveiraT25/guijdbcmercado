package View;

import Controller.EstoqueControll;
import Model.Produto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VendasPainel extends JPanel {

    private JTextField codigoBarrasField;
    private DefaultListModel<String> produtosListModel;
    private JList<String> produtosList;
    private JCheckBox clienteVipCheckBox;
    private JButton adicionarProdutoButton, removerProdutoButton;
    private EstoqueControll estoqueControll;
    private JButton avancarButton;
    private JLabel totalLabel; // Adiciona um campo para exibir o total
    private double total; // Adiciona um campo para armazenar o total dos produtos
    private ConclusaoCompraPainel conclusaoCompraPainel; // Adiciona um campo para o ConclusaoCompraPainel

    public VendasPainel(EstoqueControll estoqueControll, ConclusaoCompraPainel conclusaoCompraPainel) {
        this.estoqueControll = estoqueControll;
        this.conclusaoCompraPainel = conclusaoCompraPainel; // Inicializa o campo com o ConclusaoCompraPainel
        setLayout(new BorderLayout());

        codigoBarrasField = new JTextField();
        adicionarProdutoButton = new JButton("Adicionar Produto");

        produtosListModel = new DefaultListModel<>();
        produtosList = new JList<>(produtosListModel);
        JScrollPane produtosScrollPane = new JScrollPane(produtosList);

        removerProdutoButton = new JButton("Remover Produto");
        clienteVipCheckBox = new JCheckBox("Cliente VIP");

        avancarButton = new JButton("Pagamento");
        totalLabel = new JLabel("Total: R$ 0.00"); // Inicializa o rótulo de total

        JPanel codigoBarrasPanel = new JPanel(new BorderLayout());
        codigoBarrasPanel.add(new JLabel("Código de Barras: "), BorderLayout.WEST);
        codigoBarrasPanel.add(codigoBarrasField, BorderLayout.CENTER);
        codigoBarrasPanel.add(adicionarProdutoButton, BorderLayout.EAST);

        JPanel botoesPanel = new JPanel();
        botoesPanel.add(removerProdutoButton);
        botoesPanel.add(clienteVipCheckBox);
        botoesPanel.add(avancarButton);
        botoesPanel.add(totalLabel); // Adiciona o rótulo de total aos botõesPanel

        add(codigoBarrasPanel, BorderLayout.NORTH);
        add(produtosScrollPane, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);

        adicionarProdutoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarProduto();
            }
        });

        removerProdutoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerProduto();
            }
        });

        avancarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para avançar para a tela de conclusão
                JTabbedPane jTPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, VendasPainel.this);

                // Supondo que "Tela de Conclusão de Compras" está na terceira posição (índice 2)
                jTPane.setSelectedIndex(2);

                // Obtém a referência para o ConclusaoCompraPainel
                ConclusaoCompraPainel conclusaoCompraPainel = (ConclusaoCompraPainel) jTPane.getComponentAt(2);

                // Obtém os produtos do VendasPainel
                List<String> produtos = obterProdutosDoVendasPainel();

                // Passa os produtos e o total para o ConclusaoCompraPainel
                conclusaoCompraPainel.setProdutos(produtos);
                conclusaoCompraPainel.setTotal(total);
            }
        });
    }

    private List<String> obterProdutosDoVendasPainel() {
        // Lógica para obter a lista de produtos do VendasPainel
        List<String> produtos = new ArrayList<>();
        for (int i = 0; i < produtosListModel.size(); i++) {
            produtos.add(produtosListModel.getElementAt(i));
        }
        return produtos;
    }

    private void adicionarProduto() {
        String codigoBarras = codigoBarrasField.getText();
        if (!codigoBarras.isEmpty()) {
            Produto produto = estoqueControll.obterProdutoPorCodigoBarras(codigoBarras);

            if (produto != null) {
                double precoProduto = produto.getPreco();

                // Aplica desconto se o cliente for VIP
                if (clienteVipCheckBox.isSelected()) {
                    precoProduto *= 0.95; // Desconto de 5%
                }

                produtosListModel.addElement(produto.getNome() + " - Preço: R$" + precoProduto);
                codigoBarrasField.setText("");

                // Atualiza o total ao adicionar um produto
                total += precoProduto;
                atualizarTotalLabel();
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado no estoque", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerProduto() {
        int selectedIndex = produtosList.getSelectedIndex();
        if (selectedIndex != -1) {
            // Obtém o texto do item selecionado para extrair o preço
            String selectedProduct = produtosListModel.getElementAt(selectedIndex);
            double preco = extrairPrecoDoTexto(selectedProduct);

            // Atualiza o total ao remover um produto
            total -= preco;
            atualizarTotalLabel();

            // Remove o produto da lista
            produtosListModel.remove(selectedIndex);
        }
    }

    private double extrairPrecoDoTexto(String textoProduto) {
        // Lógica para extrair o preço do texto formatado
        // Implemente de acordo com o formato real do texto
        // Neste exemplo, assume-se que o preço está no final do texto após "Preço: R$"
        String precoTexto = textoProduto.substring(textoProduto.lastIndexOf("R$") + 3).trim();
        return Double.parseDouble(precoTexto);
    }

    private void atualizarTotalLabel() {
        totalLabel.setText("Total: R$" + String.format("%.2f", total));
    }
}
