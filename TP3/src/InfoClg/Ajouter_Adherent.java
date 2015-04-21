package InfoClg;

import oracle.jdbc.pool.OracleDataSource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            //d�clarer un objet OracledataSource
            OracleDataSource ods = new OracleDataSource();
            // d�finir les param�tres de connexion pour l�objet OracleDataSourse ods
            ods.setURL(url);
            ods.setUser(user1);
            ods.setPassword(mdep);
            // Appel de la m�thode getConnection pour obtenir une connexion
            conn = ods.getConnection();
        }
        catch(SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Connexion impossible.");
        }
        catch(ClassNotFoundException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Classe non-trouvée.");
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
            BTN_Cancel.addActionListener((new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            }));


    }

    public void AjouterAdherent()
    {
        try
        {
            JFrame frame = new JFrame();
            if(!TB_Nom.getText().trim().isEmpty() && !TB_Prenom.getText().trim().isEmpty())
            {
                String SQL = "INSERT INTO Adherent values(Adherents_seq.Nextval, ?, ?)";
                PreparedStatement Ajout = conn.prepareStatement(SQL);
                Ajout.setString(1, TB_Prenom.getText());
                Ajout.setString(2, TB_Nom.getText());
                int n = Ajout.executeUpdate();
                javax.swing.JOptionPane.showMessageDialog(frame,"Adhérent ajouté!");
            }
            else
            {
                javax.swing.JOptionPane.showMessageDialog(frame,"Erreur, veuillez entrer le nom et le prenom!");
            }
        }
        catch(SQLException e)
        {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Impossible d'ajouter.");
        }
    }
}
