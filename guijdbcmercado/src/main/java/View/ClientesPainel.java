package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import Controller.ClientesControl;
import Model.Clientes;

public class ClientesPainel extends JPanel {
    private JTextField clienteCPFField, clienteNomeField;
    private DefaultTableModel tableModel;
    private int linhaSelecionada = -1;
    private JLabel resultadoLabel;
    private CardLayout cardLayout;
    private JPanel cards;

    public ClientesPainel(CardLayout cardLayout, JPanel cards) {
        super();
        this.cardLayout = cardLayout;
        this.cards = cards;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Diálogo inicial para verificar se o usuário está cadastrado
        int respostaCadastro = JOptionPane.showConfirmDialog(null, "Você faz parte do clube de membros?", "Verificação de Cadastro", JOptionPane.YES_NO_OPTION);
        if (respostaCadastro == JOptionPane.NO_OPTION) {
            cadastrarUsuario();
        }

        add(new JLabel("Cadastro Clientes"));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));

        inputPanel.add(new JLabel("CPF"));
        clienteCPFField = new JTextField(20);
        clienteCPFField.setEditable(false); // Campo de nome não editável
        inputPanel.add(clienteCPFField);

        inputPanel.add(new JLabel("Nome"));
        clienteNomeField = new JTextField(20);
        clienteNomeField.setEditable(false); // Campo de nome não editável
        inputPanel.add(clienteNomeField);

        add(inputPanel);

        JPanel buttonPanel = new JPanel();
        JButton buscarButton = new JButton("Procurar");
        buttonPanel.add(buscarButton);
        add(buttonPanel);

        resultadoLabel = new JLabel();
        add(resultadoLabel);

        // Ajuste da chamada do construtor do ClientesControl
        ClientesControl operacoes = new ClientesControl(null); // Passe a lista de clientes aqui, se disponível

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpfBusca = JOptionPane.showInputDialog(null, "Digite o CPF do cliente para buscar:", "Busca por CPF", JOptionPane.QUESTION_MESSAGE);
                if (cpfBusca != null && !cpfBusca.isEmpty()) {
                    Clientes clienteEncontrado = operacoes.obterClientePorCPF(cpfBusca);
                    if (clienteEncontrado != null) {
                        clienteCPFField.setText(clienteEncontrado.getCpf());
                        clienteNomeField.setText(clienteEncontrado.getNome());
                        resultadoLabel.setText("Resultado da Busca: CPF - " + clienteEncontrado.getCpf() + ", Nome - " + clienteEncontrado.getNome());
                    } else {
                        JOptionPane.showMessageDialog(null, "Cliente não encontrado", "Erro de Busca", JOptionPane.ERROR_MESSAGE);
                        resultadoLabel.setText("Resultado da Busca: Cliente não encontrado");
                    }
                }
            }
        });
    }

    private void cadastrarUsuario() {
        JTextField nomeField = new JTextField();
        JTextField cpfField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("CPF:"));
        panel.add(cpfField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Cadastro de Usuário", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText();
            String cpf = cpfField.getText();

            // Passe a lista de clientes para o construtor do ClientesControl, se disponível
            ClientesControl operacoes = new ClientesControl(null);
            boolean cadastroSucesso = operacoes.cadastrarUsuario(nome, cpf);

            if (cadastroSucesso) {
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao cadastrar usuário. Verifique os dados e tente novamente.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Usuário cancelou o cadastro
            cardLayout.show(cards, "Cadastro Clientes");
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
