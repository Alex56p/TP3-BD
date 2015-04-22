package InfoClg;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.transform.Result;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.util.ArrayList;

import com.sun.org.apache.regexp.internal.RESyntaxException;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
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
    private JTabbedPane tabbedPane1;
    private JPanel Panel_Adherent;
    private JButton suivantButton;
    private JButton precedentButton;
    private JTextField TB_Numero;
    private JTextField TB_Titre;
    private JTextField TB_Auteur;
    private JTextField TB_Genre;
    private JTextField TB_Date;
    private JTextField TB_Maison;
    private JButton BTN_Ajouter_Pret;
    private JList Liste_Pret;
    private JList Liste_Recherche;
    private JTextField TB_Auteur_Recherche;
    private JTextField TB_Titre_Recherche;
    private JButton Rechercher_Auteur;
    private JButton Rechercher_Titre;
    private static Connection conn;
    public ArrayList<Integer> Num_Adherents = new ArrayList<Integer>();
    public ResultSet rst2;

    public Adherents() throws Exception
    {
        //Adherents
        Connexion();
        AfficherAdherents();
        Boutons();

        //Livres
        AfficherLivres();

        // Pr�ts
        AfficherPrets();
    }

    public void AfficherPrets()
    {
        try
        {
            String SQL = "SELECT Num_Exemplaire, Num_Adherent, Date_Emprunt, DateRetour_Emprunt FROM Emprunt ORDER BY Num_Adherent";
            PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery();

            // Cr�er une liste pour mettre les enregistrements
            DefaultListModel liste = new DefaultListModel();

            while(rst.next())
            {
                int Num_Exemplaire = rst.getInt("Num_Exemplaire");
                int Num_Adherent = rst.getInt("Num_Adherent");
                liste.addElement( "Titre Livre : " + GetTitre(Num_Exemplaire) + " | Genre Livre : " + GetGenre(Num_Exemplaire) + " | Date du Pret : " + rst.getDate("Date_Emprunt") + " | Date de retour : " + rst.getDate("DateRetour_Emprunt") + " | Nom Adherent : " + GetPrenom(Num_Adherent) + " " + GetNom(Num_Adherent));
            }

            // Mettre la liste dans le form
            Liste_Pret.setModel(liste);

            stm.clearParameters();
        }
        catch(SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "AfficherPrets");
        }


    }

    public String GetGenre(int Num_Exemplaire)
    {
        String Res = "";

        try
        {
            String SQL = "Select Nom_Genre FROM GENRE WHERE Code_Genre = ?";
            int CodeGenre = GetCodeGenre(Num_Exemplaire);
            PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, CodeGenre);
            ResultSet rst = stm.executeQuery();

            if(rst.next())
            {
                Res = rst.getString("Nom_Genre");
            }
            stm.clearParameters();
        }
        catch(SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "GetGenre");
        }

        return Res;
    }

    public int GetCodeGenre(int Num_Exemplaire)
    {
        int Res = 0;

        try
        {
            String SQL = "SELECT Code_Genre FROM Livre WHERE Num_Livre = ?";
            int numLivre = GetNumLivre(Num_Exemplaire);
            PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, numLivre);
            ResultSet rst = stm.executeQuery();

            if(rst.next())
            {
                Res = rst.getInt("Code_Genre");
            }
            stm.clearParameters();
        }
        catch(SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "GetCodeGenre");
        }

        return Res;
    }

    public String GetTitre(int Num_Exemplaire)
    {
        String Res = "";
        try
        {
            String SQL = "SELECT Titre_Livre FROM Livre WHERE Num_Livre = ?";
            int Num = GetNumLivre(Num_Exemplaire);
            PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, Num);
            ResultSet rst = stm.executeQuery();

            if(rst.next())
            {
                Res = rst.getString("Titre_Livre");
            }
            stm.clearParameters();
        }
        catch(SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "GetTitre");
        }

        return Res;
    }

    public int GetNumLivre(int Num_Exemplaire)
    {
        int num = 0;
        try
        {
            String SQL = "SELECT Num_Livre FROM Exemplaire WHERE Num_Exemplaire = ?";
            PreparedStatement stm = conn.prepareStatement(SQL);
            stm.setInt(1, Num_Exemplaire);
            ResultSet rst = stm.executeQuery();

            if(rst.next())
            {
                num = rst.getInt("Num_Livre");
            }
            stm.clearParameters();
        }
        catch (SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "GetNumLivre");
        }

        return num;
    }

    private void AfficherLivres() throws Exception
    {
        String SQL = "SELECT NUM_LIVRE, TITRE_LIVRE,CODE_GENRE, AUTEUR_LIVRE,ANNEE_LIVRE FROM LIVRE ORDER BY CODE_GENRE";
        PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rst2 = stm.executeQuery();
        if(rst2.first()) {
            TB_Numero.setText(rst2.getString("NUM_LIVRE"));
            TB_Titre.setText(rst2.getString("TITRE_LIVRE"));
            TB_Genre.setText(GetGenre(rst2.getString("CODE_GENRE")));
            TB_Auteur.setText(rst2.getString("AUTEUR_LIVRE"));
            TB_Date.setText(rst2.getString("ANNEE_LIVRE"));
        }
    }

    private void AfficherLivreSuivant() throws Exception
    {
        if (rst2.next()) {
            TB_Numero.setText(rst2.getString("NUM_LIVRE"));
            TB_Titre.setText(rst2.getString("TITRE_LIVRE"));
            TB_Genre.setText(GetGenre(rst2.getString("CODE_GENRE")));
            TB_Auteur.setText(rst2.getString("AUTEUR_LIVRE"));
            TB_Date.setText(rst2.getString("ANNEE_LIVRE"));
        }
    }

    private void AfficherLivrePrecedent() throws Exception
    {
        if(rst2.previous()) {
            TB_Numero.setText(rst2.getString("NUM_LIVRE"));
            TB_Titre.setText(rst2.getString("TITRE_LIVRE"));
            TB_Genre.setText(GetGenre(rst2.getString("CODE_GENRE")));
            TB_Auteur.setText(rst2.getString("AUTEUR_LIVRE"));
            TB_Date.setText(rst2.getString("ANNEE_LIVRE"));
        }
    }

    public String GetGenre(String CodeGenre)
    {
        String Res = "";

        try
        {
            String SQL = "SELECT Nom_Genre FROM Genre WHERE Code_Genre = ?";
            PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setString(1, CodeGenre);
            ResultSet rst = stm.executeQuery();

            if(rst.next())
            {
                Res = rst.getString("Nom_Genre");
            }

            stm.clearParameters();
        }
        catch(SQLException e)
        {

        }

        return Res;
    }

    public void Connexion() throws Exception
    {
        String user1 ="parental";
        String mdep ="ORACLE1";
        String url="jdbc:oracle:thin:@205.237.244.251:1521:orcl";
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //d�clarer un objet OracledataSource
        OracleDataSource ods = new OracleDataSource();
        // d�finir les param�tres de connexion pour l�objet OracleDataSourse ods
        ods.setURL(url);
        ods.setUser(user1);
        ods.setPassword(mdep);
        // Appel de la m�thode getConnection pour obtenir une connexion
        conn = ods.getConnection();
    }

    public void AfficherAdherents() throws Exception
    {
        // Commande SQL
        String SQL = "SELECT Num_Adherent, Prenom_Adherent, Nom_Adherent FROM Adherent";
        PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rst = stm.executeQuery(SQL);

        // Cr�er une liste pour mettre les enregistrements
        DefaultListModel liste = new DefaultListModel();

        // Mettre les enregistrements
        while(rst.next())
        {
            liste.addElement(rst.getString("Prenom_Adherent") + " " + rst.getString("Nom_Adherent"));
            Num_Adherents.add(rst.getInt("Num_Adherent"));
        }

        // Mettre la liste dans le form
        Liste_Adherents.setModel(liste);

        stm.clearParameters();
    }

    public void Boutons() throws Exception
    {
        BTN_Ajouter.addActionListener(new ActionListener() {
                                          @Override
                                          public void actionPerformed(ActionEvent actionEvent) {
                                              // on cr�e une fen�tre dont le titre est "Bonjour"
                                              JFrame frame = new JFrame("Ajouter_Adherent");
                                              // on ajoute le contenu du Panne1
                                              frame.setContentPane(new Ajouter_Adherent().Panel1);
                                              //la fen�tre se ferme quand on clique sur la croix rouge
                                              frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                              //on attribue la taille minimale au frame
                                              frame.pack();
                                              // on rend le frame visible
                                              frame.setVisible(true);


                                              frame.addWindowListener(new WindowAdapter() {

                                                  @Override
                                                  public void windowClosing(WindowEvent windowEvent) {
                                                      try {
                                                          AfficherAdherents();
                                                      } catch (Exception a) {

                                                      }
                                                  }
                                              });
                                          }
                                      });


        BTN_Supprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!Liste_Adherents.isSelectionEmpty())
                    SupprimerAdherent();
                else
                {
                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f, "Veuillez s�lectionner un adh�rent.");
                }

            }
        });

        BTN_Modifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!Liste_Adherents.isSelectionEmpty()) {
                    String Nom = GetNom();
                    String Prenom = GetPrenom();
                    Integer Nom1 = Liste_Adherents.getSelectedIndex();
                    Integer Num = Num_Adherents.get(Nom1);

                    // on cr�e une fen�tre dont le titre est "Bonjour"
                    JFrame frame = new JFrame("Ajouter_Adherent");
                    // on ajoute le contenu du Panne1
                    frame.setContentPane(new Ajouter_Adherent(Nom, Prenom, Num).Panel1);
                    //la fen�tre se ferme quand on clique sur la croix rouge
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    //on attribue la taille minimale au frame
                    frame.pack();
                    // on rend le frame visible
                    frame.setVisible(true);

                    frame.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosing(WindowEvent windowEvent) {
                            try {
                                AfficherAdherents();
                            } catch (Exception a) {

                            }
                        }
                    });
                }
                else
                {
                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f, "Veuillez s�lectionner un adh�rent.");
                }
            }
        });

        suivantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    AfficherLivreSuivant();
                } catch (Exception e) {

                }
            }
        });

        precedentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    AfficherLivrePrecedent();
                } catch (Exception e) {

                }
            }
        });

        BTN_Ajouter_Pret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // on cr�e une fen�tre dont le titre est "Bonjour"
                JFrame frame = new JFrame("Ajouter_Pret");
                // on ajoute le contenu du Panne1
                frame.setContentPane(new Ajouter_Pret().Panel1);
                //la fen�tre se ferme quand on clique sur la croix rouge
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //on attribue la taille minimale au frame
                frame.pack();
                // on rend le frame visible
                frame.setVisible(true);

                frame.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent windowEvent) {
                        try {
                            AfficherPrets();
                        } catch (Exception a) {

                        }
                    }
                });
            }
        });

        Rechercher_Auteur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                RechercherAuteur();
            }
        });

        Rechercher_Titre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                RechercherTitre();
            }
        });
    }

    public void RechercherAuteur()
    {
        try
        {
            String SQL = "SELECT Titre_Livre FROM Livre WHERE Auteur_Livre = ?";
            PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setString(1, TB_Auteur_Recherche.getText());
            ResultSet rst = stm.executeQuery();

            // Cr�er une liste pour mettre les enregistrements
            DefaultListModel liste = new DefaultListModel();


            while(rst.next())
            {
                liste.addElement(rst.getString("Titre_Livre"));
            }

            // Mettre la liste dans le form
            Liste_Recherche.setModel(liste);

            stm.clearParameters();

        }
        catch(SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "RechercherAuteur");
        }

    }

    public void RechercherTitre()
    {
        try
        {
            String SQL = "SELECT Titre_Livre FROM Livre WHERE Titre_Livre LIKE '" + TB_Titre_Recherche.getText() + "%'";
            PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery();

            // Cr�er une liste pour mettre les enregistrements
            DefaultListModel liste = new DefaultListModel();


            while(rst.next())
            {
                liste.addElement(rst.getString("Titre_Livre"));
            }

            // Mettre la liste dans le form
            Liste_Recherche.setModel(liste);

            stm.clearParameters();

        }
        catch(SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "RechercherTitre");
        }
    }

    public String GetNom()
    {
        try
        {
            Integer Nom = Liste_Adherents.getSelectedIndex();
            Integer Num = Num_Adherents.get(Nom);

            String SQL = "SELECT Nom_Adherent FROM Adherent WHERE Num_Adherent = ?";
            PreparedStatement PS= conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            PS.setInt(1, Num);
            ResultSet rst = PS.executeQuery();

            if(rst.first())
            {
                return rst.getString("Nom_Adherent");
            }

            PS.clearParameters();
        }
        catch(SQLException e)
        {

        }

        return "";
    }

    public String GetNom(int Num_Adherent)
    {
        try
        {
            String SQL = "SELECT Nom_Adherent FROM Adherent WHERE Num_Adherent = ?";
            PreparedStatement PS= conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            PS.setInt(1, Num_Adherent);
            ResultSet rst = PS.executeQuery();

            if(rst.first())
            {
                return rst.getString("Nom_Adherent");
            }

            PS.clearParameters();
        }
        catch(SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "GetNomAdherent");
        }

        return "";
    }

    public String GetPrenom(int Num_Adherent)
    {
        try
        {
            String SQL = "SELECT Prenom_Adherent FROM Adherent WHERE Num_Adherent = ?";
            PreparedStatement PS= conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            PS.setInt(1, Num_Adherent);
            ResultSet rst = PS.executeQuery();

            if(rst.first())
            {
                return rst.getString("Prenom_Adherent");
            }

            PS.clearParameters();
        }
        catch(SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "GetPrenomAdherent");
        }

        return "";
    }

    public String GetPrenom()
    {
        try
        {
            Integer Nom = Liste_Adherents.getSelectedIndex();
            Integer Num = Num_Adherents.get(Nom);

            String SQL = "SELECT Prenom_Adherent FROM Adherent WHERE Num_Adherent = ?";
            PreparedStatement PS= conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            PS.setInt(1, Num);
            ResultSet rst = PS.executeQuery();

            if(rst.first())
            {
                return rst.getString("Prenom_Adherent");
            }

            PS.clearParameters();
        }
        catch(SQLException e)
        {

        }

        return "";
    }

    public void SupprimerAdherent()
    {
        try
        {
            Integer Nom = Liste_Adherents.getSelectedIndex();

            Integer Num = Num_Adherents.get(Nom);

            String SQL = "DELETE FROM Adherent WHERE Num_Adherent = ?";

            PreparedStatement declaration2= conn.prepareStatement(SQL);
            declaration2.setInt(1, Num);
            int n = declaration2.executeUpdate();
            AfficherAdherents();
            declaration2.clearParameters();
        }
        catch(SQLException ew)
        {

        }
        catch(Exception e)
        {

        }
    }
}
