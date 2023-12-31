package asma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ClientFrame extends JFrame {
    private JTextField nomField;
    private JTextField prenomField;
    private JComboBox<String> specialiteComboBox;
    private JComboBox<String> clubComboBox;
    //private JCheckBox musicCheckBox;
    //private JCheckBox infoCheckBox;
    //private JCheckBox artCheckBox;
    private JButton submitButton;
    private JButton annulerButton;
    private JPanel panel;

    public ClientFrame() {
        setTitle(" HERE YOU GO CLIENT ");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(136, 193, 213));

        //création des labels pour le nom, prénom, spécialité et club
        JLabel nomLabel = new JLabel("Nom:");
        nomLabel.setBounds(20, 30, 80, 25);

        nomField = new JTextField(20);
        nomField.setBounds(120, 30, 200, 25);
        panel.add(nomField);

        JLabel prenomLabel = new JLabel("Prénom:");
        prenomLabel.setBounds(20, 70, 80, 25);

        prenomField = new JTextField(20);
        prenomField.setBounds(120, 70, 200, 25);

        JLabel specialiteLabel = new JLabel("Spécialité:");
        specialiteLabel.setBounds(20, 110, 80, 25);
        //liste des spécialités possibles
        String[] specialiteOptions = {"---","IM","BD","MIME","CM","AV","COCO JV","COCO 3D"};
        specialiteComboBox = new JComboBox<>(specialiteOptions);
        specialiteComboBox.setBounds(120, 110, 200, 25);

        JLabel clubLabel = new JLabel("Club:");
        clubLabel.setBounds(20, 150, 80, 25);
        // liste des clubs possibles
        String[] clubOptions = {"---", "jeunes ingénieurs", "Orenda", "Robotique", "MCI" ,"Microsoft Club","LOG","Spark","Boubli"};
        clubComboBox = new JComboBox<>(clubOptions);
        clubComboBox.setBounds(120, 150, 200, 25);
        //ajout des boutons
        submitButton = new JButton("Soumettre");
        submitButton.setBounds(150, 210, 100, 30);
        submitButton.setBackground(new Color(5, 150, 168));

        annulerButton = new JButton("Annuler");
        annulerButton.setBounds(270, 210, 100, 30);
        annulerButton.setBackground(new Color(6, 104, 136, 255));
        //ajout des composants au panel
        panel.add(nomLabel);
        panel.add(prenomLabel);
        panel.add(prenomField);
        panel.add(specialiteLabel);
        panel.add(specialiteComboBox);
        panel.add(clubLabel);
        panel.add(clubComboBox);
        panel.add(submitButton);
        panel.add(annulerButton);
        //ajout des actionListener
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                handleSubmit();
            }
        });

        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                handleAnnuler();
            }
        });

        getContentPane().add(panel);
        setVisible(true);
    }
    private void handleSubmit() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String specialite = (String) specialiteComboBox.getSelectedItem();
        String club = (String) clubComboBox.getSelectedItem() ;
        //pour assurer la présence de tous les champs nécessaires pour l'opération de l'ajout du client ( client peut etre sans club)
        if (nom.isEmpty() || prenom.isEmpty() || specialite.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("Formulaire du Client Soumis:");
        System.out.println("Nom: " + nom);
        System.out.println("Prénom: " + prenom);
        System.out.println("Spécialité: " + specialite);
        System.out.println("Club: " + club);
        user = addClientToDB(nom,prenom,specialite,club);
        if (user != null){
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register a new Client",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void handleAnnuler() {

        dispose();
    }
    //fonction qui retourne un "user" pour ajouter le client à la BD
    public User user;
    private User addClientToDB(String nom, String prenom, String specialite, String club) {
        User user = null;
        //établissement de la connection avec la BD
        final String cDB = "jdbc:mysql://localhost:3306/finalp";
        final String USERNAME = "root";
        final String PASSWORD = "";
        try {
            Connection connect = DriverManager.getConnection(cDB, USERNAME, PASSWORD);
            Statement st = connect.createStatement();
            String sql = "INSERT INTO client(nom, prenom ,specialite ,club) " +
                    "VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, specialite);
            preparedStatement.setString(4, club);
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.nom = nom;
                user.prenom = prenom;
                user.specialite = specialite;
                user.club = club;
            }
            JOptionPane.showMessageDialog(this, "Successful add of " + nom + " " + prenom,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            //fermer la connection
            st.close();
            connect.close();
        } catch (Exception n) {
            //throw new RuntimeException(e);
            n.printStackTrace();
        }
        return user;
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ClientFrame myForm = new ClientFrame();
                User user = myForm.user ;
            }
        });
    }
}
