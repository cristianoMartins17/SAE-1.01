import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.Flow;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIBrouillage {
    JPanel mainBrouillage;


    GUIBrouillage() {
        mainBrouillage = new JPanel();
        BoxLayout boxLayout = new BoxLayout(mainBrouillage, BoxLayout.Y_AXIS);
        JPanel panelChemin=new JPanel();
        JLabel labelChemin = new JLabel("Entrez le chemin de l'image à brouiller");
        JTextField inputChemin = new JTextField("",10);
        panelChemin.add(labelChemin);
        panelChemin.add(inputChemin);

        JPanel panelCle= new JPanel();
        JLabel labelCle = new JLabel("Entrez une clé");
        JTextField inputCle= new JTextField("",10);
        panelCle.add(labelCle);
        panelCle.add(inputCle);

        JPanel panelSortie= new JPanel();
        JLabel labelSortie = new JLabel("Entrez une image de sortie");
        JTextField inputSortie= new JTextField("",10);

        panelSortie.add(labelSortie);
        panelSortie.add(inputSortie);

        JPanel panelProcess= new JPanel();
        JLabel labelProcess = new JLabel("Entrez un processus");
        JTextField inputProcess= new JTextField("",10);

        JButton btnEnvoyer = new JButton("envoyer");
        btnEnvoyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chemin = inputChemin.getText();
                String cle = inputCle.getText();
                String sortie = inputSortie.getText();
                String process = inputProcess.getText();
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

        panelProcess.add(labelProcess);
        panelProcess.add(inputProcess);

        mainBrouillage.setLayout(boxLayout);
        
        mainBrouillage.add(panelChemin);
        mainBrouillage.add(panelCle);
        mainBrouillage.add(panelSortie);
        mainBrouillage.add(panelProcess);
        mainBrouillage.add(panelBtn);


        
    }
 

    
}
