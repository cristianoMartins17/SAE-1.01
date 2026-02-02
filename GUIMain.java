import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

public class GUIMain {


    public static void main(String[] args) {
        JFrame fenetre = new JFrame("SAE");
        fenetre.setSize(700,800);
        GUIBrouillage brouillageInterface= new GUIBrouillage();
        GUIDebrouillage debrouillageGraphique = new GUIDebrouillage();


        JPanel menuMain = new JPanel(new BorderLayout());
        JPanel conteneurMain = new JPanel();
        JPanel brouillageMain = brouillageInterface.mainBrouillage;
        JPanel haut = new JPanel();
        JPanel debrouillageMain=debrouillageGraphique.panelMain;
        JPanel menu = creerMenu();

        CardLayout cardLayout = new CardLayout();

        conteneurMain.setLayout(cardLayout);
        conteneurMain.add(menu,"menu");
        conteneurMain.add(brouillageMain, "brouillage");
        conteneurMain.add(debrouillageMain,"debrouillage");

        JButton btnBrouillimg = new JButton("brouillage d'image");

        btnBrouillimg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteneurMain, "brouillage");
            }
        });
        JButton btnDebrouillage = new JButton("debrouillage d'image");

        btnDebrouillage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteneurMain, "debrouillage");
            }
        });

        JButton btnMenu = new JButton("Menu");

        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteneurMain, "menu");
            }
        });



        haut.add(btnBrouillimg);
        haut.add(btnMenu);
        haut.add(btnDebrouillage);
        menuMain.add(haut, BorderLayout.NORTH);
        menuMain.add(conteneurMain, BorderLayout.CENTER);
        fenetre.add(menuMain);
        fenetre.setVisible(true);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    public static JPanel creerMenu() {
        JPanel menu = new JPanel();
        JLabel texte = new JLabel("Bienvenue sur cette interface graphique");
        menu.add(texte);
        return menu;

    }



    
}
