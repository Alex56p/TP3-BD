package InfoClg;

import oracle.jdbc.pool.OracleDataSource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class Ajouter_Adherent {
    private JTextField TB_Prenom;
    private JTextField TB_Nom;
    private JButton BTN_Ajouter;
    public JPanel Panel1;
    private JTextField TB_Adresse;
    private JTextField TB_Telephone;
    Connection conn;
    Boolean Modifier = false;
    int Num;

    public Ajouter_Adherent()
    {
        Connexion();
        Boutons();
        Modifier = false;
    }

    public Ajouter_Adherent(String Nom, String Prenom, int Num_Adherent, String Adresse, String NumTel)
    {
        Connexion();
        Boutons();
        TB_Nom.setText(Nom);
        TB_Prenom.setText(Prenom);
        TB_Adresse.setText(Adresse);
        TB_Telephone.setText(NumTel);
        Modifier = true;
        Num = Num_Adherent;
        BTN_Ajouter.setText("Modifier");
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
    }

    public void AjouterAdherent()
    {
        JFrame frame = new JFrame();
        if(!Modifier)
        {
            try
            {
                if(!TB_Nom.getText().trim().isEmpty() && !TB_Prenom.getText().trim().isEmpty())
                {
                    String SQL = "INSERT INTO Adherent(PRENOM_ADHERENT,NOM_ADHERENT,ADRESSE_ADHERENT,NUMTEL_ADHERENT) values(?, ?, ?, ?)";
                    PreparedStatement Ajout = conn.prepareStatement(SQL);
                    Ajout.setString(1, TB_Prenom.getText());
                    Ajout.setString(2, TB_Nom.getText());
                    Ajout.setString(3, TB_Adresse.getText());
                    Ajout.setString(4, TB_Telephone.getText());
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
                JOptionPane.showMessageDialog(frame, "Impossible d'ajouter.");
            }
        }
        else
        {
            try
            {
                String SQL = "UPDATE Adherent SET Prenom_Adherent = ? , Nom_Adherent = ?, Adresse_Adherent = ? , NumTel_Adherent = ? WHERE Num_Adherent = ?";
                PreparedStatement Update = conn.prepareStatement(SQL);
                Update= conn.prepareStatement(SQL);
                Update.setString(1, TB_Prenom.getText());
                Update.setString(2, TB_Nom.getText());
                Update.setString(3, TB_Adresse.getText());
                Update.setString(4, TB_Telephone.getText());
                Update.setInt(5, Num);
                int n = Update.executeUpdate();
                Update.clearParameters();

                javax.swing.JOptionPane.showMessageDialog(frame, "Adhérent modifi�!");
            }
            catch(SQLException e)
            {
                JOptionPane.showMessageDialog(frame, "Impossible de modifier.");
            }

        }
    }
}
