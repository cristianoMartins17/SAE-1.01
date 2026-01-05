import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class Analyse {

    public static void main(String[] args) throws IOException {
        if (args.length<2) {
            System.out.println("Analyse <image_entrée> <méthode>");
            System.exit(1);
        }
        String pathEntree=args[0];
        String methode=args[1];
        if (!estUneMethode(methode)) {
            System.out.println("La méthode saisie n'existe pas.");
            System.exit(2);
        }
        BufferedImage imageEntree=ImageIO.read(new File(pathEntree));
        int hauteur=imageEntree.getHeight();
        int longueur=imageEntree.getWidth();
        int cle=Profiler.analyseBreakKey(imageEntree, methode);
        System.out.println("cle : " +cle);
        long nombreCalcul=calculerNbCalculs(methode, hauteur, longueur);
        System.out.println("temps total de débrouillage : "+(Profiler.formaterUnitTemps(Profiler.globalTime)));
        System.out.println("Nombre de calculs : "+nombreCalcul);
        System.out.println("Nombre de calculs avec pusisance de 10 : "+convertisseurNorme(nombreCalcul));
        
    }

    public static boolean estUneMethode(String methode) {
        switch (methode) {
            case "Euclid", "EuclidOpti","Pearson","PearsonOpti","Hybrid","Auto":
                return true;
            default:
                return false;
        }
    }

    public static long calculerNbCalculs(String methode, int hauteur, int longueur) {
        if (methode.equals("Auto")) {
            methode=(hauteur>512) ? "Hybrid" : "PearsonOpti";
        }
        switch (methode) {
            case "Euclid":
                return 32768L*(hauteur-1)*(longueur*3L+2L);
            case "EuclidOpti":
                return 32768L*(hauteur-1)*(longueur*3+1L);
            case "Pearson":
                return 32768L*(hauteur-1)*(longueur*12L+7L);
            case "PearsonOpti":
                return 32768L*(hauteur-1)*(longueur*4L+4L)+hauteur*(longueur+1)+hauteur*longueur*3;
            case "Hybrid":
                return 256L*(hauteur-1)*(longueur*4L+4L)+hauteur*(longueur+1)+hauteur*longueur*3+128L*(hauteur-1L)*(longueur*3L-1L);
            default:
                return -1;
        }
    }

    public static String convertisseurNorme(long nombre) {
        if (nombre==0) { return "0";}
        int puissance=0;

        while (nombre/(long)(Math.pow(10, puissance+3))!=0) {
            puissance+=3;
        }
        String exposant=(puissance>0) ?  "x 10^"+puissance : "";
        String resultat=String.format("%.4f %s",nombre/(Math.pow(10, puissance)),exposant);
        return resultat;

    }
    
}
