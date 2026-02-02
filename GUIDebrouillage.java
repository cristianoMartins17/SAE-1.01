import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIDebrouillage {
    public JPanel panelMain;

    GUIDebrouillage() {
        String[] options = {"Euclid","Pearson","Hybrid","Manhattan"};

        panelMain=new JPanel();


        JLabel labelHaut= new JLabel("Debrouillage d'image");
        

        JPanel panelEntree = new JPanel();
        JLabel labelEntree = new JLabel("Entrez une image à débrouiller");
        JTextField fieldEntree= new JTextField(10);
        panelEntree.add(labelEntree);
        panelEntree.add(fieldEntree);

        JPanel panelMethode = new JPanel();
        JLabel labelMethode = new JLabel("Sélectionnez un processus");



        JComboBox<String> listeDeroulante = new JComboBox<>(options);
        panelMethode.add(labelMethode);
        panelMethode.add(listeDeroulante);

        BoxLayout boxLayout = new BoxLayout(panelMain, BoxLayout.Y_AXIS);

        JPanel panelSortie=new JPanel();
        JLabel labelSortie = new JLabel("Entrez une image de sortie (optionnel) ");
        JTextField fieldSortie = new JTextField(10);

        JButton btnEnvoyer = new JButton("Envoyer");
        btnEnvoyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e ) {
                String entree = fieldEntree.getText();
                String methode = (String) listeDeroulante.getSelectedItem();
                String sortie = fieldSortie.getText();
                String[] arguments=null;
                if (sortie.equals("")) {
                    arguments = new String[2];
                    arguments[0]=entree;
                    arguments[1]=methode;
                }
                else {
                    arguments= new String[3];
                    arguments[0]=entree;
                    arguments[1]=methode;
                    arguments[2]=sortie;
                }

                try {
                    Debrouillage.main(arguments);
                } catch (IOException exception) {
                    System.out.println("impossible");
                }

            }
        });

        panelSortie.add(labelSortie);
        panelSortie.add(fieldSortie);

        panelMain.setLayout(boxLayout);
        panelMain.add(labelHaut);
        panelMain.add(panelEntree);
        panelMain.add(panelMethode);
        panelMain.add(panelSortie);
        panelMain.add(btnEnvoyer);

        


    }
    
}
