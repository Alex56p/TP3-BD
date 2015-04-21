package InfoClg;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;

/**
 * Created by 201332037 on 2015-04-13.
 */
public class Adherents{
    private JButton BTN_Ajouter;
    private JButton BTN_Modifier;
    private JList Liste_Adherents;
    private JButton BTN_Supprimer;
    public JPanel Panel1;
    public JPanel Panel2;
    private static Connection conn;

    public Adherents() throws Exception{
        Connexion();
        AfficherAdherents();
        Bouttons();
    }

    public void Connexion() throws Exception
    {
        String user1 ="parental";
        String mdep ="ORACLE1";
        String url="jdbc:oracle:thin:@205.237.244.251:1521:orcl";
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //déclarer un objet OracledataSource
        OracleDataSource ods = new OracleDataSource();
        // définir les paramètres de connexion pour l’objet OracleDataSourse ods
        ods.setURL(url);
        ods.setUser(user1);
        ods.setPassword(mdep);
        // Appel de la méthode getConnection pour obtenir une connexion
        conn = ods.getConnection();
    }

    public void AfficherAdherents() throws Exception
    {
        // Commande SQL
        String SQL = "SELECT Prenom_Adherent, Nom_Adherent FROM Adherent";
        PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rst = stm.executeQuery(SQL);

        // Créer une liste pour mettre les enregistrements
        DefaultListModel liste = new DefaultListModel();

        // Mettre les enregistrements
        while(rst.next())
        {
            liste.addElement(rst.getString("Prenom_Adherent") + " " + rst.getString("Nom_Adherent"));
        }

        // Mettre la liste dans le form
        Liste_Adherents.setModel(liste);
    }

    public void Bouttons() throws Exception
    {
        BTN_Ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // on crée une fenêtre dont le titre est "Bonjour"
                JFrame frame = new JFrame("Ajouter_Adherent");
                // on ajoute le contenu du Panne1
                frame.setContentPane(new Ajouter_Adherent().Panel1);
                //la fenêtre se ferme quand on clique sur la croix rouge
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //on attribue la taille minimale au frame
                frame.pack();
                // on rend le frame visible
                frame.setVisible(true);

            }
        });
    }
}
