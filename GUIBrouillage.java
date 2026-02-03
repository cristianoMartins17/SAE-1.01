import java.io.IOException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIBrouillage {
    JPanel mainBrouillage;
    JComboBox<String> inputChemin=new JComboBox<>();


    GUIBrouillage() {

        mainBrouillage = new JPanel();
        JLabel labelChemin = new JLabel("Sélectionnez le chemin de l'image à brouiller : ");
        initialiserChemins();


        JLabel labelCle = new JLabel("Entrez une clé : ");
        JTextField inputCle= new JTextField("",10);


        JLabel labelSortie = new JLabel("Entrez une image de sortie : ");
        JTextField inputSortie= new JTextField("",10);



        JLabel labelProcess = new JLabel("Sélectionnez un processus : ");
        String[] process = new String[]{"scramble","unscramble"};
        JComboBox<String> inputProcess= new JComboBox<>(process);

        JButton btnEnvoyer = new JButton("envoyer");
        btnEnvoyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chemin = "images/"+(String) inputChemin.getSelectedItem();
                String cle = inputCle.getText();
                String sortie =inputSortie.getText().equals("") ? "images/out.png" : "images/"+inputSortie.getText();
                String process =(String) inputProcess.getSelectedItem();

                String[] arguments= {chemin,cle,sortie,process};
                System.out.println(sortie);
                System.out.println(arguments);
                try {
                    Brouillimg.main(arguments);
                    initialiserChemins();
                    
                } catch (IOException exception) {
                    System.err.println("impossible");
                };
            }
            

        });

        JPanel panelBtn = new JPanel();
        panelBtn.add(btnEnvoyer);
        mainBrouillage.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(10, 5, 0, 5);


        gbc.weightx=0;
        gbc.fill=GridBagConstraints.NONE;

        gbc.gridx=0;
        gbc.gridy=0;
        mainBrouillage.add(labelChemin,gbc);

        gbc.weightx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=1;
        gbc.gridy=0;
        mainBrouillage.add(inputChemin,gbc);

        gbc.weightx=0;
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=0;
        gbc.gridy=1;
        mainBrouillage.add(labelCle,gbc);

        gbc.weightx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=1;
        gbc.gridy=1;
        mainBrouillage.add(inputCle,gbc);

        gbc.weightx=0;
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=0;
        gbc.gridy=2;
        mainBrouillage.add(labelSortie,gbc);

        gbc.weightx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=1;
        gbc.gridy=2;
        mainBrouillage.add(inputSortie,gbc);

        gbc.weightx=0;
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=0;
        gbc.gridy=3;
        mainBrouillage.add(labelProcess,gbc);

        gbc.weightx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=1;
        gbc.gridy=3;
        mainBrouillage.add(inputProcess,gbc);


        gbc.gridx=0;
        gbc.gridy=4;
        gbc.gridwidth=2;

        mainBrouillage.add(panelBtn,gbc);

    }

    public void initialiserChemins() {
        inputChemin.removeAllItems();
        String[] images = ImagesReader.getImages();
        for (int i=0;i<images.length;i++) {
            inputChemin.addItem(images[i]);
        }
        mainBrouillage.revalidate();
        mainBrouillage.repaint();
    }


 

    
}
