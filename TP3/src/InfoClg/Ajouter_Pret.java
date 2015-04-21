package InfoClg;

import InfoClg.Ajouter_Adherent;
import oracle.jdbc.pool.OracleDataSource;
import sun.jdbc.odbc.JdbcOdbcPreparedStatement;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by Alexis on 2015-04-21.
 */
public class Ajouter_Pret {
    public JPanel Panel1;
    private JComboBox CB_Client;
    private JComboBox CB_Livre;
    private JButton BTN_Ajouter;
    public ArrayList<Integer> Num_Adherents = new ArrayList<Integer>();
    public ArrayList<Integer> Num_Livres = new ArrayList<Integer>();

    private static Connection conn;

    public Ajouter_Pret()
    {
        Connexion();
        AfficherClients();
        AfficherLivres();
        Bouton();
    }

    public void Connexion()
    {
        try
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
        catch(SQLException e)
        {

        }
        catch(Exception e)
        {

        }

    }

    public void AfficherClients()
    {
        try
        {
            // Commande SQL
            String SQL = "SELECT Num_Adherent, Prenom_Adherent, Nom_Adherent FROM Adherent";
            PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery(SQL);

            // Mettre les enregistrements
            while(rst.next())
            {
                CB_Client.addItem(rst.getString("Prenom_Adherent") + " " + rst.getString("Nom_Adherent"));
                Num_Adherents.add(rst.getInt("Num_Adherent"));
            }



            stm.clearParameters();
        }
        catch(SQLException e)
        {

        }

    }

    public void AfficherLivres()
    {
        try
        {
            // Commande SQL
            String SQL = "SELECT Num_Livre, Titre_Livre FROM Livre";
            PreparedStatement stm = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery(SQL);

            // Mettre les enregistrements
            while(rst.next())
            {
                CB_Livre.addItem(rst.getString("Titre_Livre"));
                Num_Livres.add(rst.getInt("Num_Livre"));
            }

            stm.clearParameters();
        }
        catch(SQLException e)
        {

        }
    }

    public void Bouton()
    {
        BTN_Ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AjouterPret();
            }
        });
    }

    public void AjouterPret()
    {
        try
        {
             //Prendre le num_Adherent
            Integer NomC = CB_Client.getSelectedIndex();
            Integer NumC = Num_Adherents.get(NomC);
            // Prendre le num_Livre
            Integer NomL = CB_Livre.getSelectedIndex();
            Integer NumL = Num_Adherents.get(NomL);
            int NumE = RechercherExemplaire(NumL);

            // Date
            Calendar calendar = Calendar.getInstance();
            java.util.Date currentDate = calendar.getTime();
            java.sql.Date date = new java.sql.Date(currentDate.getTime());

            calendar.add(Calendar.MONTH, 1);
            currentDate = calendar.getTime();
            java.sql.Date date2 = new java.sql.Date(currentDate.getTime());

            // Requête
            String SQL = "INSERT INTO EMPRUNT VALUES(?, ?, ?, ?)";
            PreparedStatement PS = conn.prepareStatement(SQL);

            PS.setInt(1, NumE);
            PS.setInt(2, NumC);
            PS.setDate(3, date);
            PS.setDate(4, date2);

            int n = PS.executeUpdate();
            PS.clearParameters();
        }
        catch(SQLException e)
        {
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f, e.getMessage());
        }
    }

    public int RechercherExemplaire(int Num_Livre)
    {
        int res = 0;
        try
        {
            String SQL = "SELECT Num_Exemplaire FROM EXEMPLAIRE WHERE Num_Livre = ? AND DISPONIBLE = 0";
            PreparedStatement PS = conn.prepareStatement(SQL);

            PS.setInt(1, Num_Livre);

            ResultSet rst = PS.executeQuery();
            if(rst.next())
            {
                res = rst.getInt(1);
            }
            PS.clearParameters();
        }
        catch(SQLException e)
        {

        }

        return res;
    }
}
