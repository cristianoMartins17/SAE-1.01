import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

public class GUIMain {


    public static void main(String[] args) {
        Color couleurFond=new Color(201, 201, 199);
        JFrame fenetre = new JFrame("SAE");
        fenetre.setSize(500,300);
        fenetre.setResizable(false);
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
        JLabel texte = new JLabel("Bienvenue sur cette interface graphique");
        texte.setBackground(couleurFond);
        menu.add(texte);
        return menu;

    }




    
}