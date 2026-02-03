import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.Flow;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIBrouillage {
    JPanel mainBrouillage;


    GUIBrouillage() {
        mainBrouillage = new JPanel();
        JLabel labelChemin = new JLabel("Entrez le chemin de l'image à brouiller");
        JTextField inputChemin = new JTextField("",10);


        JLabel labelCle = new JLabel("Entrez une clé");
        JTextField inputCle= new JTextField("",10);


        JLabel labelSortie = new JLabel("Entrez une image de sortie");
        JTextField inputSortie= new JTextField("",10);



        JLabel labelProcess = new JLabel("Entrez un processus");
        String[] process = new String[]{"scramble","unscramble"};
        JComboBox<String> inputProcess= new JComboBox<>(process);

        JButton btnEnvoyer = new JButton("envoyer");
        btnEnvoyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chemin = inputChemin.getText();
                String cle = inputCle.getText();
                String sortie = inputSortie.getText();
                String process =(String) inputProcess.getSelectedItem();
                ArrayList<String> arguments= new ArrayList<>();
                switch (process) {
                    case "":
                        arguments.add(chemin);
                        arguments.add(cle);
                        arguments.add(process);
                        break;
                
                    default:
                        arguments.add(chemin);
                        arguments.add(cle);
                        arguments.add(sortie);
                        arguments.add(process);
                        break;
                }
                String[] argsTab = new String[arguments.size()];
                for (int i = 0; i < argsTab.length; i++) {
                    argsTab[i]=arguments.get(i);
                }
                try {
                    Brouillimg.main(argsTab);
                    
                } catch (IOException exception) {
                    System.err.println("impossible");
                };
            }
            

        });

        JPanel panelBtn = new JPanel();
        panelBtn.add(btnEnvoyer);
        mainBrouillage.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
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
 

    
}
