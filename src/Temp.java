import java.io.File;

public class Temp {
    File location = new File("C:\\Users\\Colleen\\Desktop\\Java\\WebScraper\\images");

	public void main(String[] args) {
        // Delete all files in the images folder
        File[] files = location.listFiles();
        for (File file : files) {
            file.delete();
        }
    }
}