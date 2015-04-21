package InfoClg;

import oracle.jdbc.pool.OracleDataSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Alexis on 2015-04-20.
 */
public class Ajouter_Adherent {
    private JTextField TB_Prenom;
    private JTextField TB_Nom;
    private JButton BTN_Cancel;
    private JButton BTN_Ajouter;
    public JPanel Panel1;
    Connection conn;

    public Ajouter_Adherent()
    {
        Connexion();
        Boutons();
    }

    public void Connexion()
    {
        try
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
        catch(SQLException e)
        {

        }
        catch(ClassNotFoundException e)
        {

        }

    }

    public void Boutons()
    {

            BTN_Ajouter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    AjouterAdherent();
                }
            });


    }

    public void AjouterAdherent()
    {
        try
        {
            if(TB_Nom.getText() != null && TB_Prenom.getText() != null)
            {
                String SQL = "INSERT INTO Adherent values(Adherents_seq.Nextval, ?, ?)";
                PreparedStatement Ajout = conn.prepareStatement(SQL);
                Ajout.setString(1, TB_Prenom.getText());
                Ajout.setString(2, TB_Nom.getText());
                int n = Ajout.executeUpdate();
            }
            else
            {
                javax.swing.JOptionPane.showMessageDialog(null,"Erreur, veuillez entrer le nom et le prénom!");
            }
        }
        catch(SQLException e)
        {

        }


    }
}
