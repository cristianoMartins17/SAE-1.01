import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class CompareImages {

    public static void main(String[] args) throws IOException {
        if (args.length<2) {
            System.out.println( "Usage : java CompareImages <image1> <image2> [image_comparaison]");
            System.exit(1);

        }
        String image1Path = args[0];
        String image2Path = args[1];
        BufferedImage image1=ImageIO.read(new File(image1Path));
        BufferedImage image2=ImageIO.read(new File(image2Path));
        if (image1 == null || image2==null) {
            throw new IOException("Format d’image non reconnu: ");
        }
        if (image1.getHeight()!=image2.getHeight() || image2.getWidth() != image1.getWidth()) {
            System.out.println("les images ne sont pas de même taille");
            System.exit(2); // Si les deux images sont pas de même taille, ca sert à rien de comparer
        }

        if (args.length==3) {
            String outChemin = args[2];
            BufferedImage outImage=imageDifferences(image1, image2);
            ImageIO.write(outImage, "png", new File(outChemin));
            System.out.println("les pixels différents entre les deux images sont en blanc et ceux qui sont égaux sont noirs.");

        }

        System.out.println("nombre de pixels différents entre les 2 images : "+comparerDeuxImages(image1, image2));
    }

        public static int comparerDeuxImages(BufferedImage image1, BufferedImage image2) {
            int compteur=0;
            for (int y = 0; y < image1.getHeight(); y++) {
                for (int x= 0; x < image2.getWidth(); x++) {
                    int rgb1=image1.getRGB(x, y);
                    int rgb2=image2.getRGB(x, y);
                    if (rgb1!=rgb2) {
                        compteur++;
                    }
                }
            }

            return compteur;
        }

        public static BufferedImage imageDifferences(BufferedImage image1, BufferedImage image2) {
            BufferedImage resultat= new BufferedImage(image1.getWidth(), image2.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < image1.getHeight(); y++) {
                for (int x = 0; x < image2.getWidth(); x++) {
                    int rgb1=image1.getRGB(x, y);
                    int rgb2=image2.getRGB(x, y);
                    if (rgb1!=rgb2) {
                        resultat.setRGB(x, y, 0xFFFFFFFF);
                    }
                    else {
                        resultat.setRGB(x, y, 0xFF000000);
                    }
                }
            }
            return resultat;
        }
    
}
