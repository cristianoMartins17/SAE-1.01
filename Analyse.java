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
        if (methode.equals("Euclid") || methode.equals("EuclidOpti") || 
        methode.equals("Pearson") || methode.equals("PearsonOpti") || 
        methode.equals("Hybrid") || methode.equals("Auto")
        || methode.equals("Manathan")) {
            return true;
        }
        else {
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
                return 32768L*(hauteur-1)*(longueur*4L+4L)+hauteur*(longueur+1)+hauteur*longueur*3;
            case "Hybrid":
                return 256L*(hauteur-1)*(longueur*4L+4L)+hauteur*(longueur+1)+hauteur*longueur*3+128L*(hauteur-1L)*(longueur*3L-1L);
            case "Manathan":
                return 384*(hauteur-1)*(longueur*3L+1L);
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
        String grandeur="";
        switch (puissance) {
            case 0:
                break;
            case 3:
                grandeur="k";
                break;
            case 6:
                grandeur="millions";
                break;
            case 9:
                grandeur="milliards";
                break;
            default:
                grandeur="x 10^"+puissance;
                break;
        }

        String resultat=String.format("%.4g %s",nombre/(Math.pow(10, puissance)),grandeur);
        return resultat;

    }
    
}
