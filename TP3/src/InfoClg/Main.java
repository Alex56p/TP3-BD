package InfoClg;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Created by Alexis on 2015-04-20.
 */
public class Main {
    public static void main (String args[]) throws Exception
    {
        JFrame frame = new JFrame("Adherents");
        // on ajoute le contenu du Panne1
        frame.setContentPane(new Adherents().Panel1);
        //la fen�tre se ferme quand on clique sur la croix rouge
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //on attribue la taille minimale au frame
        frame.pack();
        // on rend le frame visible
        frame.setVisible(true);
    }
}
