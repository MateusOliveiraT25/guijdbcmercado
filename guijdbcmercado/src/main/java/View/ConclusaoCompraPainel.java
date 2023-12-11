package View;

import javax.swing.*;

import Controller.EstoqueControll;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ConclusaoCompraPainel extends JPanel {

    private DefaultListModel<String> detalhesCompraModel;
    private JList<String> detalhesCompraList;
    private JLabel totalCompraLabel;
    private JComboBox<String> opcoesPagamentoComboBox;
    private JButton finalizarCompraButton, imprimirCupomButton;
    private double total; // Adiciona um campo para armazenar o total dos produtos
    private EstoqueControll estoqueControll; // Adiciona uma referência ao EstoqueControll

    public ConclusaoCompraPainel(EstoqueControll estoqueControll) {
        this.estoqueControll = estoqueControll; // Inicializa o EstoqueControll
        setLayout(new BorderLayout());

        // Lista final dos produtos, quantidades e preços
        detalhesCompraModel = new DefaultListModel<>();
        detalhesCompraList = new JList<>(detalhesCompraModel);
        JScrollPane detalhesCompraScrollPane = new JScrollPane(detalhesCompraList);

        // Rótulo para exibir o total da compra
        totalCompraLabel = new JLabel("Total da Compra: R$ 0.00");

        // Opções de pagamento
        String[] opcoesPagamento = {"Dinheiro", "Cartão de Crédito", "Cartão de Débito", "Pix"};
        opcoesPagamentoComboBox = new JComboBox<>(opcoesPagamento);

        // Botão para finalizar a compra
        finalizarCompraButton = new JButton("Finalizar Compra");

        // Botão opcional para imprimir o relatório de vendas (Cupom Fiscal)
        imprimirCupomButton = new JButton("Imprimir Cupom Fiscal");

        // Adicionando componentes ao painel de conclusão
        JPanel botoesPanel = new JPanel();
        botoesPanel.add(finalizarCompraButton);
        botoesPanel.add(imprimirCupomButton);

        add(detalhesCompraScrollPane, BorderLayout.CENTER);
        add(totalCompraLabel, BorderLayout.SOUTH);
        add(opcoesPagamentoComboBox, BorderLayout.NORTH);
        add(botoesPanel, BorderLayout.EAST);

        // Adicionando ação ao botão "Finalizar Compra"
        finalizarCompraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarCompra();
            }
        });

        // Adicionando ação ao botão "Imprimir Cupom Fiscal"
        imprimirCupomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imprimirCupomFiscal();
            }
        });
    }
    public DefaultListModel<String> getDetalhesCompraModel() {
        return detalhesCompraModel;
    }
    

    private void finalizarCompra() {
        // Lógica para finalizar a compra
        // Atualize conforme necessário
        JOptionPane.showMessageDialog(this, "Compra finalizada com sucesso!");
    
        // Deduz a quantidade do estoque para cada produto na lista
        for (int i = 0; i < detalhesCompraModel.size(); i++) {
            String produtoTexto = detalhesCompraModel.getElementAt(i);
            String codigoBarras = extrairCodigoBarrasDoTexto(produtoTexto);
            int quantidadeComprada = extrairQuantidadeDoTexto(produtoTexto);
    
            // Debugging para verificar os valores obtidos
            System.out.println("Produto: " + codigoBarras + ", Quantidade: " + quantidadeComprada);
    
            estoqueControll.deduzirQuantidadeDoEstoque(codigoBarras, quantidadeComprada);
        }
    }
    

    // Métodos auxiliares para extrair informações do texto do produto
    private String extrairCodigoBarrasDoTexto(String textoProduto) {
        // Verifica se o textoProduto está no formato esperado
        if (textoProduto.matches("\\d+ - .*")) {
            // Usa expressão regular para extrair o código de barras (sequência de dígitos)
            return textoProduto.split(" - ")[0];
        } else {
            // Se o formato não corresponder ao esperado, retorna uma string vazia ou lança uma exceção, conforme necessário
            return "";
        }
    }
    

    private int extrairQuantidadeDoTexto(String textoProduto) {
        try {
            // Encontrar a última ocorrência de espaço em branco
            int ultimoEspaco = textoProduto.lastIndexOf(" ");
    
            // Extrair a parte da string após o último espaço em branco (supondo que seja a quantidade)
            String quantidadeTexto = textoProduto.substring(ultimoEspaco + 1);
    
            // Remover caracteres não numéricos (manter apenas os dígitos)
            quantidadeTexto = quantidadeTexto.replaceAll("\\D+", "");
    
            // Tentar converter a quantidade para inteiro
            return Integer.parseInt(quantidadeTexto);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            // Se ocorrer um erro ao converter, tratar ou imprimir a exceção
            e.printStackTrace();
            return 0; // Ou retornar um valor padrão
        }
    }
    

    private void imprimirCupomFiscal() {
        // Obtém a data e hora atuais
        String dataHoraAtual = java.time.LocalDateTime.now().toString();
    
        // Chama o método no EstoqueControll passando as informações necessárias
        estoqueControll.imprimirCupomFiscal(total, dataHoraAtual, this);
    }
    
    

    public void setProdutos(List<String> produtos) {
        detalhesCompraModel.clear(); // Limpa a lista existente
        for (String produto : produtos) {
            detalhesCompraModel.addElement(produto); // Adiciona os novos produtos
        }
    }

    public void setTotal(double total) {
        this.total = total;
        totalCompraLabel.setText("Total da Compra: R$" + String.format("%.2f", total));
    }
}
