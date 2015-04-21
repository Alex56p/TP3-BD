package InfoClg;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.transform.Result;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

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
    private JTable Table_Prets;
    private JButton BTN_Ajouter_Pret;
    private static Connection conn;
    public ArrayList<Integer> Num_Adherents = new ArrayList<Integer>();

    public ResultSet rst2;

    public Adherents() throws Exception{
        //Adherents
        Connexion();
        AfficherAdherents();
        Boutons();

        //Livres
        AfficherLivres();

        // Prêts
        AjouterRows();
        //AfficherPrets();
    }

    private void AfficherLivres() throws Exception{
        String SQL = "SELECT NUM_LIVRE, TITRE_LIVRE,CODE_GENRE, AUTEUR_LIVRE,ANNEE_LIVRE FROM LIVRE";
        PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rst2 = stm.executeQuery();
        if(rst2.first()) {
            TB_Numero.setText(rst2.getString("NUM_LIVRE"));
            TB_Titre.setText(rst2.getString("TITRE_LIVRE"));
            TB_Genre.setText(rst2.getString("CODE_GENRE"));
            TB_Auteur.setText(rst2.getString("AUTEUR_LIVRE"));
            TB_Date.setText(rst2.getString("ANNEE_LIVRE"));
        }
    }

    private void AfficherLivreSuivant() throws Exception {
        if (rst2.next()) {
            TB_Numero.setText(rst2.getString("NUM_LIVRE"));
            TB_Titre.setText(rst2.getString("TITRE_LIVRE"));
            TB_Genre.setText(rst2.getString("CODE_GENRE"));
            TB_Auteur.setText(rst2.getString("AUTEUR_LIVRE"));
            TB_Date.setText(rst2.getString("ANNEE_LIVRE"));
        }
    }
    private void AfficherLivrePrecedent() throws Exception {
        if(rst2.previous()) {
            TB_Numero.setText(rst2.getString("NUM_LIVRE"));
            TB_Titre.setText(rst2.getString("TITRE_LIVRE"));
            TB_Genre.setText(rst2.getString("CODE_GENRE"));
            TB_Auteur.setText(rst2.getString("AUTEUR_LIVRE"));
            TB_Date.setText(rst2.getString("ANNEE_LIVRE"));
        }
    }

    public void Connexion() throws Exception
    {
        String user1 ="parental";
        String mdep ="ORACLE1";
        String url="jdbc:oracle:thin:@205.237.244.251:1521:orcl";
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //dï¿½clarer un objet OracledataSource
        OracleDataSource ods = new OracleDataSource();
        // dï¿½finir les paramï¿½tres de connexion pour lï¿½objet OracleDataSourse ods
        ods.setURL(url);
        ods.setUser(user1);
        ods.setPassword(mdep);
        // Appel de la mï¿½thode getConnection pour obtenir une connexion
        conn = ods.getConnection();
    }


    public void AfficherAdherents() throws Exception
    {
        // Commande SQL
        String SQL = "SELECT Num_Adherent, Prenom_Adherent, Nom_Adherent FROM Adherent";
        PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rst = stm.executeQuery(SQL);

        // Crï¿½er une liste pour mettre les enregistrements
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
                                              // on crï¿½e une fenï¿½tre dont le titre est "Bonjour"
                                              JFrame frame = new JFrame("Ajouter_Adherent");
                                              // on ajoute le contenu du Panne1
                                              frame.setContentPane(new Ajouter_Adherent().Panel1);
                                              //la fenï¿½tre se ferme quand on clique sur la croix rouge
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
                    JOptionPane.showMessageDialog(f, "Veuillez sélectionner un adhérent.");
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

                    // on crï¿½e une fenï¿½tre dont le titre est "Bonjour"
                    JFrame frame = new JFrame("Ajouter_Adherent");
                    // on ajoute le contenu du Panne1
                    frame.setContentPane(new Ajouter_Adherent(Nom, Prenom, Num).Panel1);
                    //la fenï¿½tre se ferme quand on clique sur la croix rouge
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
                    JOptionPane.showMessageDialog(f, "Veuillez sélectionner un adhérent.");
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
                // on crï¿½e une fenï¿½tre dont le titre est "Bonjour"
                JFrame frame = new JFrame("Ajouter_Pret");
                // on ajoute le contenu du Panne1
                frame.setContentPane(new Ajouter_Pret().Panel1);
                //la fenï¿½tre se ferme quand on clique sur la croix rouge
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //on attribue la taille minimale au frame
                frame.pack();
                // on rend le frame visible
                frame.setVisible(true);
            }
        });
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

    public void AjouterRows()
    {

    }
}
