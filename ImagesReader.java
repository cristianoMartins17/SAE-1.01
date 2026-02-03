import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ImagesReader {

    public static String[] getImages() {
        ArrayList<String> res = new ArrayList<>();
        ProcessBuilder builder;
        String os = getSystem();
        switch (os) {
            case "windows":
                builder = new ProcessBuilder("cmd.exe", "/c", "dir images");     
                break;
            case "linux","mac":
                builder = new ProcessBuilder("bash", "-c", "ls images");   
            default:
                throw new IllegalArgumentException();
        }
        builder.redirectErrorStream(true);
        builder.directory(new File("."));
        try {
            Process processus = builder.start();
            BufferedReader lecteur = new BufferedReader(new InputStreamReader(processus.getInputStream()));
            String ligne = lecteur.readLine();
            while (ligne!=null) {
                String[] tabString=ligne.split(" ");
                for (int i = 0;i<tabString.length;i++) {
                    if (tabString[i].endsWith("png") || tabString[i].endsWith("jpg")) {
                        res.add(tabString[i]);

                    }
                }
                ligne=lecteur.readLine();
            }

        } catch (IOException e) {
            System.out.println(e);
            System.out.println("erreur");
        }
        String[] resTab = new String[res.size()];
        for (int i = 0; i < res.size(); i++) {
            resTab[i]=res.get(i);
        }
        return resTab;
    }

    public static String getSystem() {
        String os = System.getProperty("os.name");
         
        if (os.toLowerCase().startsWith("windows")) {
            return "windows";
        }
        else if (os.toLowerCase().startsWith("linux")) {
            return "linux";
        }
        else if (os.toLowerCase().startsWith("mac")) {
            return "mac";
        }
        return "aucun";
    }
    
}
