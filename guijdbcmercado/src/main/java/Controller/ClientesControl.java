package Controller;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Model.Clientes;

public class ClientesControl {
    private DefaultTableModel tableModel;
    private ClientesDAO clientesDAO;
    private List<Clientes> clientes;

    public ClientesControl(List<Clientes> clientes) {
        this.clientes = clientes;
        this.clientesDAO = new ClientesDAO();

        // Inicialize o tableModel se for nulo
        if (this.tableModel == null) {
            this.tableModel = new DefaultTableModel();
            // Adicione as colunas necessárias ao tableModel, se necessário
            this.tableModel.addColumn("Nome");
            this.tableModel.addColumn("CPF");
        }

        this.clientesDAO = new ClientesDAO();
    }

    private void atualizarTabela() {
        if (tableModel != null) {
            tableModel.setRowCount(0);
            for (Clientes cliente : clientesDAO.listarTodos()) {
                tableModel.addRow(new Object[]{cliente.getNome(), cliente.getCpf()});
            }
        }
    }

    public boolean cadastrarUsuario(String nome, String cpf) {
        // Validar o CPF antes de cadastrar
        if (clientesDAO.isCpfValido(cpf)) {
            boolean cadastroSucesso = clientesDAO.cadastrarUsuario(nome, cpf);

            if (cadastroSucesso) {
                atualizarTabela();
            }

            return cadastroSucesso;
        } else {
            showMessage("CPF inválido. Não foi possível cadastrar o usuário.", "Erro de Cadastro");
            return false;
        }
    }
    public Clientes obterClientePorCPF(String cpf) {
        try {
            return clientesDAO.obterClientePorCpf(cpf);
        } catch (RuntimeException e) {
            showMessage("Erro ao obter cliente por CPF: " + e.getMessage(), "Erro");
            return null;
        }
    }

    public void atualizarCliente(String nome, String cpf) {
        clientesDAO.atualizar(nome, cpf);
        atualizarTabela();
    }

    public void apagarCliente(String cpf) {
        clientesDAO.apagar(cpf);
        atualizarTabela();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
