import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.SpringLayout.Constraints;
import javax.swing.border.Border;

public class GUIMain {


    public static void main(String[] args) {
        Color couleurFond=new Color(201, 201, 199);
        JFrame fenetre = new JFrame("SAE");
        fenetre.setSize(500,300);
        fenetre.setResizable(true);
        GUIBrouillage brouillageInterface= new GUIBrouillage();
        GUIDebrouillage debrouillageGraphique = new GUIDebrouillage();

        Color couleurFondBtns=new Color(214, 214, 214);


        JPanel menuMain = new JPanel(new BorderLayout());
        JPanel conteneurMain = new JPanel();
        JPanel brouillageMain = brouillageInterface.mainBrouillage;
        JPanel haut = new JPanel();
        JPanel debrouillageMain=debrouillageGraphique.mainDebrouillage;
        JPanel menu = creerMenu(couleurFond);

        haut.setBackground(new Color(74, 74, 74));

        CardLayout cardLayout = new CardLayout();

        conteneurMain.setLayout(cardLayout);
        conteneurMain.add(menu,"menu");
        conteneurMain.add(brouillageMain, "brouillage");
        conteneurMain.add(debrouillageMain,"debrouillage");

        JButton btnBrouillimg = new JButton("brouillage d'image");
        btnBrouillimg.setFocusPainted(false);

        btnBrouillimg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteneurMain, "brouillage");
            }
        });

        btnBrouillimg.setBackground(couleurFondBtns);

        JButton btnDebrouillage = new JButton("debrouillage d'image");
        btnDebrouillage.setFocusPainted(false);
        btnDebrouillage.setBackground(couleurFondBtns);

        btnDebrouillage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(conteneurMain, "debrouillage");
            }
        });



        JButton btnMenu = new JButton("Menu");
        btnMenu.setBackground(couleurFondBtns);

        btnMenu.setFocusPainted(false);

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


    public static JPanel creerMenu(Color couleurFond) {
        JPanel menu = new JPanel();
        menu.setBackground(couleurFond);

        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        JLabel titre = new JLabel("Bienvenue sur cette interface graphique");
        JTextArea texte = new JTextArea("Cette interface graphique a été réalisée avec Swing afin de pouvroir brouiller et débrouiller des images directement sur une interface. On utilise BuilderProcess pour pouvoir obtenir les chemins d'images du dossier image et il n'y a que Windows, Linux et MacOS supportés.");
        texte.setBackground(couleurFond);
        texte.setLineWrap(true);
        texte.setWrapStyleWord(true);
        texte.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(titre);
        menu.add(texte);
        return menu;

    }




    
}