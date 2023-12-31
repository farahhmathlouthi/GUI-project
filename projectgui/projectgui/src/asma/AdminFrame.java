package asma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AdminFrame extends JFrame {
    public JPanel panel1;
    private JRadioButton specialiteRadioButton;
    private JRadioButton clubRadioButton;
    private JComboBox<String> optionsComboBox;
    private JTextArea infoTextArea;

    // Added components for the table
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private static final String cDB = "jdbc:mysql://localhost:3306/finalp";
    private final String USERNAME = "root";
    final String PASSWORD = "";
    private static final String TABLE_NAME = "client";

    public AdminFrame() {
        setTitle("HERE YOU GO ADMIN");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel1 = new JPanel(new GridLayout(3, 2));
        //creation des buttons pour choisir de filter selon la specialité ou bien le club
        specialiteRadioButton = new JRadioButton("Show Speciality");
        specialiteRadioButton.setBackground(new Color(173, 216, 230));
        specialiteRadioButton.setPreferredSize(new Dimension(150, 25));
        //creation du bouton pour le club
        clubRadioButton = new JRadioButton("Show Club");
        clubRadioButton.setBackground(new Color(173, 216, 230));
        clubRadioButton.setPreferredSize(new Dimension(100, 25));
        //ajout des boutons à un ButtonGroup pour assurer la désactivation automatique si on sélecte un autre
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(specialiteRadioButton);
        buttonGroup.add(clubRadioButton);
        // le combobox qui va contenir les choix du specialité ou le choix du club
        optionsComboBox = new JComboBox<>();
        optionsComboBox.setPreferredSize(new Dimension(300, 30));
        Font largerFont = optionsComboBox.getFont().deriveFont(13f);
        optionsComboBox.setFont(largerFont);

        infoTextArea = new JTextArea();
        infoTextArea.setBackground(new Color(170, 220, 216, 255));
        infoTextArea.setFont(largerFont);
        infoTextArea.setEditable(false);

        // Initialisation des composants du table
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setPreferredSize(new Dimension(300, 100));
        //groupement des 2 boutons et combobox dans le meme panel
        JPanel radioPanel = new JPanel();
        radioPanel.setPreferredSize(new Dimension(300, 200));
        radioPanel.setBackground(new Color(173, 216, 230));
        radioPanel.add(specialiteRadioButton);
        radioPanel.add(clubRadioButton);
        radioPanel.add(optionsComboBox);

        panel1.add(radioPanel, BorderLayout.CENTER);
        panel1.add(new JScrollPane(infoTextArea), BorderLayout.SOUTH);
        panel1.add(tableScrollPane, BorderLayout.WEST); // Added the table on the left side
        //ajout des actionListener
        specialiteRadioButton.addActionListener(
                e -> updateComboBox(true));
        clubRadioButton.addActionListener(
                e -> updateComboBox(false));
        optionsComboBox.addActionListener(e -> {
            updateInfoTextArea();
            updateTable((String) optionsComboBox.getSelectedItem());
        });
        getContentPane().add(panel1);
        setVisible(true);
    }

    private void updateTable(String selectedOption) {
        tableModel.setRowCount(0); // Clear previous data
        //la connection avec la base de donnée nommée CDB
        try (Connection connection = DriverManager.getConnection(cDB, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE club = ?";
            if (specialiteRadioButton.isSelected()) {
                query = "SELECT * FROM " + TABLE_NAME + " WHERE specialite = ?";
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, selectedOption);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    // Ajout des noms de colonnes au table
                    Vector<String> columnNames = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(metaData.getColumnName(i));
                    }
                    tableModel.setColumnIdentifiers(columnNames);
                    // ajout des lignes au table
                    while (resultSet.next()) {
                        Vector<Object> rowData = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            rowData.add(resultSet.getObject(i));
                        }
                        tableModel.addRow(rowData);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //définition des choix dans les combobox
    private void updateComboBox(boolean showSpecialite) {
        optionsComboBox.removeAllItems();
        if (showSpecialite) {
            for (String option : new String[]{"---","IM","BD","MIME","CM","AV","COCO JV","COCO 3D"}) {
                optionsComboBox.addItem(option);
            }
        } else {
            for (String option : new String[]{"---", "jeunes ingénieurs", "Orenda", "Robotique", "MCI" ,"Microsoft Club","LOG","Spark","Boubli"}) {
                optionsComboBox.addItem(option);
            }
        }
    }
    //pour l'affichage d'un quote/message suite à la sélection
    private void updateInfoTextArea() {
        String selectedOption = (String) optionsComboBox.getSelectedItem();
        if (selectedOption == null || selectedOption.isEmpty()) {
            infoTextArea.setText(""); // Clear the text area
            return;
        }
        String infoText;
        if (specialiteRadioButton.isSelected()) {
            infoText = getInfoForSpecialite(selectedOption);
        } else {
            infoText = getInfoForClub(selectedOption);
        }
        infoTextArea.setText(infoText);
    }
    //affichage du message pour les spécilalités
    private String getInfoForSpecialite(String specialite) {
        switch (specialite) {
            case "IM":
                return " Informatique et Multimédia : Programmation et Developpement ";
            case "AV":
                return " Audiovisuel : Cinema et Audiovisuel (CAV) :\n Writing and Realisation\n" +
                        " Video Editing\n" +
                        " Image\n" +
                        " Sound ";
            case "COCO 3D":
                return " COCO 3D : 3D Animation and Film making  ";
            case "CM":
                return " Communication Multimédia :\n Design and Multimedia Communication : Marketing and Digital Communication. ";
            case "BD":
                return " Big Data : Handling and Analyzing complex datasets ";
            case "MIME":
                return " Micro-informatique et machine embarquée : IoT ,\n Programmation des systèmes embarqués,\n Les objets connectés (OS temps réel) ";
            case "COCO JV":
                return " COCO Jeux Video : Développement de jeux vidéos ";
            default:
                return " Unknown Speciality!!! ";
        }
    }
    //affichage des messages pour les clubs
    private String getInfoForClub(String club) {
        switch (club) {
            case "Orenda":
                return " Orenda : Junior Entreprise! ";
            case "Microsoft Club":
                return " Microsoft : Development Lovers! ";
            case "Spark":
                return "Spark : Cyber Security Lovers! ";
            case "jeunes ingénieurs":
                return " Jeunes ingénieurs : Engineering Lovers! ";
            case "LOG":
                return " Life Of Gamers : Gaming Lovers! ";
            case "Boubli":
                return " Boubli : Content Creating Lovers!";
            case "MCI":
                return " Music Club Isamm : Music Lovers and Artists!";
            case "Robotique":
                return " Robotique : Arduino and IoT Lovers!";
            default:
                return "Unknown Club";
        }
    }
    public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            AdminFrame admin = new AdminFrame();
        }
    });
    }
}