package Controller;
import java.util.List;
import java.util.Optional;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import java.sql.SQLException;
import java.util.List;
import Model.Estoque;
import Model.Produto;
import View.ConclusaoCompraPainel;
import Controller.ProdutoDAO;

public class EstoqueControll {
    private Estoque estoque;
    private ProdutoDAO produtoDAO; // Adicione um campo para o ProdutoDAO

    public EstoqueControll() {
        this.estoque = new Estoque();
        this.produtoDAO = new ProdutoDAO(); // Inicialize o ProdutoDAO
     // Adicione alguns produtos ao estoque para teste
  
 }
    /**
     * Adiciona um novo produto ao estoque e atualiza o banco de dados.
     *
     * @param codigoBarra Código de barras do produto.
     * @param nome        Nome do produto.
     * @param quantidade  Quantidade do produto.
     * @param preco       Preço do produto.
     */
    public void adicionarProduto(String codigoBarra, String nome, int quantidade, double preco) {
        try {
            // Cria um novo produto
            Produto novoProduto = new Produto(codigoBarra, nome, quantidade, preco);

            // Adiciona o produto ao estoque
            estoque.adicionarProduto(novoProduto);
            System.out.println("Produto adicionado com sucesso!");

            // Atualiza a tabela do banco de dados
            atualizarTabelaBancoDados();
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao adicionar produto: " + e.getMessage());
        }
    }

    /**
     * Remove um produto do estoque e atualiza o banco de dados.
     *
     * @param produto Produto a ser removido.
     */
    public void removerProduto(Produto produto) {
        try {
            // Remove o produto do estoque
            estoque.removerProduto(produto);
            System.out.println("Produto removido com sucesso!");

            // Atualiza a tabela do banco de dados
            atualizarTabelaBancoDados();
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao remover produto: " + e.getMessage());
        }
    }

    /**
     * Lista todos os produtos do estoque.
     *
     * @return Lista de produtos no estoque.
     */
    public List<Produto> listarProdutosDoBanco() {
        try {
            return produtoDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar produtos do banco de dados: " + e.getMessage());
            return null;
        }
    }
    public Produto obterProdutoPorCodigoBarras(String codigoBarras) {
        try {
            Optional<Produto> produtoBanco = produtoDAO.listarTodos().stream()
                    .filter(p -> codigoBarras.equals(p.getCodigoBarra()))
                    .findFirst();

            if (produtoBanco.isPresent()) {
                return produtoBanco.get();
            }

            Optional<Produto> produtoEstoque = estoque.listarProdutos().stream()
                    .filter(p -> codigoBarras.equals(p.getCodigoBarra()))
                    .findFirst();

            return produtoEstoque.orElse(null);
        } catch (Exception e) {
            System.err.println("Erro ao obter produto por código de barras: " + e.getMessage());
            return null;
        }
    }

    // Método para deduzir a quantidade do estoque
    
   // Método para deduzir a quantidade do estoque
public void deduzirQuantidadeDoEstoque(String codigoBarras, int quantidade) {
    // Verifica se o código de barras e a quantidade são válidos
    if (codigoBarras != null && !codigoBarras.isEmpty() && quantidade > 0) {
        // Obtém o produto do estoque usando o código de barras
        Produto produto = estoque.obterProdutoPorCodigoBarras(codigoBarras);

        if (produto != null) {
            int quantidadeAtual = produto.getQuantidade();

            if (quantidadeAtual >= quantidade) {
                int novaQuantidade = quantidadeAtual - quantidade;
                produto.setQuantidade(novaQuantidade);
                System.out.println("Quantidade deduzida com sucesso. Novo estoque: " + novaQuantidade);

                // Atualizar a lista do estoque após a dedução
                atualizarTabelaBancoDados();
            } else {
                System.err.println("Quantidade insuficiente em estoque para dedução. Quantidade atual: " + quantidadeAtual);
            }
        } else {
            System.err.println("Produto não encontrado. Código de Barras: " + codigoBarras);
        }
    } else {
        System.err.println("Código de barras ou quantidade inválida.");
    }
}

    
public void imprimirCupomFiscal(double totalCompra, String dataHoraAtual, ConclusaoCompraPainel conclusaoCompraPainel) {
    DefaultListModel<String> detalhesCompraModel = conclusaoCompraPainel.getDetalhesCompraModel();
    
    // Obtém o valor total da compra
    String valorTotal = String.format("%.2f", totalCompra);

    // Obtém a quantidade de produtos (assumindo que detalhesCompraModel seja um campo da classe)
    int quantidadeProdutos = detalhesCompraModel.size();

   

    // Constrói o conteúdo do cupom fiscal
    StringBuilder cupomFiscal = new StringBuilder();
    cupomFiscal.append("Data/Hora: ").append(dataHoraAtual).append("\n");
    cupomFiscal.append("Valor Total: R$").append(valorTotal).append("\n");
    cupomFiscal.append("Quantidade de Produtos: ").append(quantidadeProdutos).append("\n");
    cupomFiscal.append("\nDetalhes da Compra:\n");

    // Adiciona detalhes de cada produto (assumindo que detalhesCompraModel seja um campo da classe)
    for (int i = 0; i < detalhesCompraModel.size(); i++) {
        cupomFiscal.append(detalhesCompraModel.getElementAt(i)).append("\n");
    }

    // Lógica para imprimir o Cupom Fiscal
    // Substitua esta linha com a lógica real de impressão
    System.out.println("Cupom Fiscal impresso com sucesso:\n" + cupomFiscal.toString());
    System.out.println("Cupom Fiscal impresso com sucesso:\n" + cupomFiscal.toString());
    JOptionPane.showMessageDialog(null, "Cupom Fiscal impresso com sucesso:\n" + cupomFiscal.toString());
}

public boolean codigoDeBarrasExiste(String codigoBarra) {
    try {
        return produtoDAO.registroExiste(codigoBarra);
    } catch (SQLException e) {
        // Tratar exceção, se necessário
        e.printStackTrace();
        return false;
    }
}


    /**
     * Atualiza a tabela do banco de dados com os produtos do estoque.
     */
    private void atualizarTabelaBancoDados() {
        // Obtém a lista de produtos do estoque
        List<Produto> produtos = estoque.listarProdutos();

        // Atualiza a tabela no banco de dados
        produtoDAO.atualizarTabelaBancoDados(produtos);
    }

}