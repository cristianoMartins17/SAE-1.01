import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUIDebrouillage {
    public JPanel mainDebrouillage;
    public JComboBox<String> inputEntree;

    GUIDebrouillage() {
        String[] options = {"Euclid","Pearson","Hybrid","Manhattan"};


        mainDebrouillage=new JPanel();

        JLabel labelEntree=new JLabel("Sélectionnez un chemin d'image à débrouiller : ");
        initialiserChemins();
        
        JComboBox<String> inputMethode = new JComboBox<>(options);
        JLabel labelMethode = new JLabel("Sélectionnez une méthode : ");

        JLabel labelSortie=new JLabel("Entrez le nom une potentielle image de sortie : ");
        JTextArea inputSortie=new JTextArea(20,1);
        



        mainDebrouillage.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets= new Insets(10, 5, 0, 5);


        gbc.weightx=0;
        gbc.fill=GridBagConstraints.NONE;



        gbc.gridx=0;
        gbc.gridy=0;
        mainDebrouillage.add(labelEntree,gbc);



        gbc.weightx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=1;
        gbc.gridy=0;
        mainDebrouillage.add(inputEntree,gbc);



        gbc.weightx=0;
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=0;
        gbc.gridy=1;
        mainDebrouillage.add(labelMethode,gbc);

        gbc.weightx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=1;
        gbc.gridy=1;
        mainDebrouillage.add(inputMethode,gbc);

        gbc.weightx=0;
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=0;
        gbc.gridy=2;
        mainDebrouillage.add(labelSortie,gbc);

        gbc.weightx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=1;
        gbc.gridy=2;
        mainDebrouillage.add(inputSortie,gbc);




        gbc.gridx=0;
        gbc.gridy=3;
        gbc.gridwidth=2;


        JButton btnEnvoyer = new JButton("Envoyer");
        btnEnvoyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e ) {
                String entree =(String) "images/"+inputEntree.getSelectedItem();
                String methode = (String) inputMethode.getSelectedItem();
                String sortie = inputSortie.getText();
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

        mainDebrouillage.add(btnEnvoyer,gbc);




        


    }


    public void initialiserChemins() {
        inputEntree = new JComboBox<>(ImagesReader.getImages());
        mainDebrouillage.revalidate();
        mainDebrouillage.repaint();
    }
    
}
