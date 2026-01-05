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
        BufferedImage imageEntree=ImageIO.read(new File(pathEntree));
        int cle=Profiler.analyseBreakKey(imageEntree, methode);
        System.out.println("cle : " +cle);
        
    }
    
}
