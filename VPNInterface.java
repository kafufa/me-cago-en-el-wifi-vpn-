package vpn;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class VPNInterface extends JFrame {
    private static final String FONT_NAME = "Arial";
    private static final int FONT_SIZE_TITLE = 24;
    private static final int FONT_SIZE_LABEL = 12;
    private static final int FONT_SIZE_VALUE = 18;

    private JTabbedPane tabbedPane;
    private JLabel statusLabel;
    private JButton connectButton;
    private JComboBox<String> serverCombo;
    private boolean connected = false;

    public VPNInterface() {
        setTitle("VPN Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Conexión", createConnectionPanel());
        tabbedPane.addTab("Servidores", createServersPanel());
        tabbedPane.addTab("Configuración", createConfigPanel());
        tabbedPane.addTab("Estadísticas", createStatsPanel());
        tabbedPane.addTab("Logs", createLogsPanel());
        add(tabbedPane);
    }

    private JPanel createConnectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Estado de Conexión"));

        statusLabel = new JLabel("Desconectado");
        statusLabel.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_TITLE));
        statusLabel.setForeground(Color.RED);
        statusPanel.add(statusLabel);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("Seleccionar Servidor:"), gbc);

        String[] servers = {"Estados Unidos - Nueva York", "Reino Unido - Londres", "Alemania - Frankfurt", "Japón - Tokio", "España - Madrid", "Francia - París"};
        serverCombo = new JComboBox<>(servers);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(serverCombo, gbc);

        connectButton = new JButton("Conectar");
        connectButton.setFont(new Font(FONT_NAME, Font.BOLD, 16));
        connectButton.setPreferredSize(new Dimension(200, 50));
        connectButton.addActionListener(e -> toggleConnection());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(connectButton);

        panel.add(statusPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createServersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"País", "Ciudad", "Latencia", "Carga", "Disponible"};
        Object[][] data = {
                {"Estados Unidos", "Nueva York", "45ms", "23%", "Sí"},
                {"Reino Unido", "Londres", "32ms", "67%", "Sí"},
                {"Alemania", "Frankfurt", "28ms", "45%", "Sí"},
                {"Japón", "Tokio", "156ms", "12%", "Sí"},
                {"España", "Madrid", "15ms", "89%", "Sí"},
                {"Francia", "París", "25ms", "34%", "Sí"}
        };

        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(new JLabel("Lista de Servidores Disponibles"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Protocolo
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Protocolo:"), gbc);

        String[] protocols = {"OpenVPN", "WireGuard", "IKEv2", "L2TP"};
        JComboBox<String> protocolCombo = new JComboBox<>(protocols);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(protocolCombo, gbc);

        // Puerto
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Puerto:"), gbc);

        JTextField portField = new JTextField("1194");
        gbc.gridx = 1;
        panel.add(portField, gbc);

        // Auto-conectar
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JCheckBox autoConnect = new JCheckBox("Conectar automáticamente al iniciar");
        panel.add(autoConnect, gbc);

        // Kill switch
        gbc.gridy = 3;
        JCheckBox killSwitch = new JCheckBox("Activar Kill Switch");
        panel.add(killSwitch, gbc);

        // DNS personalizado
        gbc.gridy = 4;
        JCheckBox customDNS = new JCheckBox("Usar DNS personalizado");
        panel.add(customDNS, gbc);

        // Botón guardar
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Guardar Configuración");
        saveButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Configuración guardada correctamente")
        );
        panel.add(saveButton, gbc);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 20, 20));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(createStatBox("Tiempo Conectado", "00:00:00"));
        panel.add(createStatBox("Datos Enviados", "0 MB"));
        panel.add(createStatBox("Datos Recibidos", "0 MB"));
        panel.add(createStatBox("Velocidad Descarga", "0 KB/s"));
        panel.add(createStatBox("Velocidad Subida", "0 KB/s"));
        panel.add(createStatBox("IP Actual", "No conectado"));
        panel.add(createStatBox("Servidor", "Ninguno"));
        panel.add(createStatBox("Protocolo", "N/A"));

        return panel;
    }

    private JPanel createStatBox(String title, String value) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_LABEL));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE_VALUE));

        box.add(titleLabel, BorderLayout.NORTH);
        box.add(valueLabel, BorderLayout.CENTER);

        return box;
    }

    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setText("[" + new Date() + "] Aplicación iniciada\n");
        logArea.append("[" + new Date() + "] Esperando conexión...\n");

        JScrollPane scrollPane = new JScrollPane(logArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton clearButton = new JButton("Limpiar Logs");
        clearButton.addActionListener(e -> logArea.setText(""));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(clearButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void toggleConnection() {
        if (!connected) {
            connectButton.setEnabled(false);
            connectButton.setText("Conectando...");

            // Usar javax.swing.Timer para garantizar que las actualizaciones se realicen en el EDT
            javax.swing.Timer timer = new javax.swing.Timer(2000, e -> {
                connected = true;
                statusLabel.setText("Conectado");
                statusLabel.setForeground(new Color(0, 150, 0));
                connectButton.setText("Desconectar");
                connectButton.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Conectado a " + serverCombo.getSelectedItem(), "Conexión Exitosa", JOptionPane.INFORMATION_MESSAGE);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            connected = false;
            statusLabel.setText("Desconectado");
            statusLabel.setForeground(Color.RED);
            connectButton.setText("Conectar");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new VPNInterface().setVisible(true);
        });
    }
}