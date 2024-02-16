import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ConnectBDJava extends JFrame implements ActionListener, ItemListener {
    // Informations de connexion à la base de données
    String urlDatabase = "jdbc:mysql://localhost:3307/ENTREPRISE";
    String login = "root";
    String password = "laflamme";

    // Objets pour la connexion et l'interaction avec la base de données
    Connection conn;
    Statement stmt;
    ResultSet rset;
    ResultSetMetaData rsetMeta;

    // Composants de l'interface graphique
    Button bouton1, bouton_Fer, bouton_Afc;
    java.awt.List listeDeChamps, listeDeRequetes;
    TextArea fenetreRes;
    TextField nomTable;
    Panel p1, p2, p3;
    Label req, res, champ, desc;

    // Requêtes SQL prédéfinies
    String R1, R2, R3, R4, R5, R6;

    public ConnectBDJava() throws SQLException, ClassNotFoundException {
        super("Projet d'utilisation de JDBC dans Access");
        setLayout(new BorderLayout());
        setBounds(0, 0, 400, 400);
        setResizable(true);

        // Initialisation des requêtes SQL
        R1 = "SELECT * FROM emp;";
        R2 = "SELECT * FROM dept;";
        R3 = "SELECT * FROM mission;";
        R4 = "SELECT ename, job, mgr FROM emp WHERE deptno = 30 ORDER BY ename;";
        R5 = "SELECT deptno, AVG(sal) FROM emp GROUP BY deptno;";
        R6 = "SELECT deptno, AVG(sal) FROM emp GROUP BY deptno HAVING AVG(sal) > 2000;";

        // Initialisation des composants de l'interface graphique
        initialiseComponents();

        // Connexion à la base de données
        conn = DriverManager.getConnection(urlDatabase, login, password);
        stmt = conn.createStatement();
    }

    // Méthode pour initialiser les composants de l'interface graphique
    private void initialiseComponents() {
        bouton1 = new Button("AfficheChamps");
        bouton_Afc = new Button("AfficheRequetes");
        bouton_Fer = new Button("Fermer");

        listeDeRequetes = new java.awt.List(10, false);
        listeDeChamps = new java.awt.List(20, false);

        fenetreRes = new TextArea();
        nomTable = new TextField();

        champ = new Label("Champs");
        champ.setAlignment(Label.CENTER);
        req = new Label("Requetes");
        req.setAlignment(Label.CENTER);
        res = new Label("Résultat requete");
        res.setAlignment(Label.CENTER);

        p1 = new Panel();
        p2 = new Panel();
        p3 = new Panel();

        p1.setLayout(new GridLayout(1, 3));
        p2.setLayout(new GridLayout(1, 3));
        p3.setLayout(new GridLayout(1, 4));

        p1.add(champ);
        p1.add(req);
        p1.add(res);

        p2.add(listeDeChamps);
        p2.add(listeDeRequetes);
        p2.add(fenetreRes);

        listeDeRequetes.addItemListener(this);

        bouton1.addActionListener(this);
        bouton_Fer.addActionListener(this);
        bouton_Afc.addActionListener(this);

        p3.add(nomTable);
        p3.add(bouton1);
        p3.add(bouton_Afc);
        p3.add(bouton_Fer);

        // Personnalisation des couleurs des composants
        personnaliserCouleurs();

        add("North", p1);
        add("Center", p2);
        add("South", p3);
    }

    // Méthode pour personnaliser les couleurs des composants
    private void personnaliserCouleurs() {
        bouton1.setBackground(Color.green);
        bouton_Fer.setBackground(Color.yellow);
        bouton_Afc.setBackground(Color.red);
        p1.setBackground(Color.blue);
        champ.setBackground(Color.green);
        req.setBackground(Color.yellow);
        res.setBackground(Color.red);
        listeDeRequetes.setBackground(Color.blue);
        listeDeChamps.setBackground(Color.green);
        fenetreRes.setBackground(Color.yellow);
    }

    // Méthode pour remplir la liste des champs d'une table donnée
    public void remplirListeChamps(String table) throws SQLException {
        if (table == null || table.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom de la table ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérification supplémentaire pour les caractères spéciaux pourrait être ajoutée ici

        String query = "SELECT * FROM `" + table + "`"; // Utilisation de backticks pour gérer les noms de tables spéciaux
        rset = stmt.executeQuery(query);
        rsetMeta = rset.getMetaData();
        int nbColonne = rsetMeta.getColumnCount();
        for (int i = 1; i <= nbColonne; i++) {
            listeDeChamps.add(rsetMeta.getColumnLabel(i));
        }
    }


    // Méthode pour gérer les actions sur les boutons
    public void actionPerformed(ActionEvent evt) {
        if ("Fermer".equals(evt.getActionCommand())) {
            this.dispose();
            System.exit(0);
        } else if ("AfficheChamps".equals(evt.getActionCommand())) {
            try {
                listeDeChamps.removeAll();
                remplirListeChamps(nomTable.getText());
            } catch (SQLException e) {
                e.printStackTrace(); // Afficher l'erreur en cas d'exception
            }
        } else if ("AfficheRequetes".equals(evt.getActionCommand())) {
            listeDeRequetes.removeAll();
            listeDeRequetes.add(R1);
            listeDeRequetes.add(R2);
            listeDeRequetes.add(R3);
            listeDeRequetes.add(R4);
            listeDeRequetes.add(R5);
            listeDeRequetes.add(R6);
        }
    }

    // Méthode pour gérer les changements d'éléments dans les listes
    public void itemStateChanged(ItemEvent evt) {
        if (evt.getSource() == listeDeRequetes) {
            String requeteSelectionnee = listeDeRequetes.getSelectedItem();
            try {
                fenetreRes.setText(""); // Effacer le texte précédent
                rset = stmt.executeQuery(requeteSelectionnee);
                rsetMeta = rset.getMetaData();
                int nbColonne = rsetMeta.getColumnCount();

                for (int i = 1; i <= nbColonne; i++) {
                    fenetreRes.append(rsetMeta.getColumnName(i) + " , ");
                }
                fenetreRes.append("\n");

                while (rset.next()) {
                    for (int i = 1; i <= nbColonne; i++) {
                        fenetreRes.append(rset.getString(i) + " , ");
                    }
                    fenetreRes.append("\n");
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Afficher l'erreur en cas d'exception
            }
        }
    }

    // Méthode main pour exécuter l'application
    public static void main(String[] s) throws SQLException, ClassNotFoundException {
        ConnectBDJava window = new ConnectBDJava();
        window.setVisible(true);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
    }
}
