import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Testing {
	public static void main(String[] args) {
		cleanImagesFolder();
	}

	public static void downloadImages() throws IOException {
		try {
			// Connect to the website using the Jsoup library
			Document doc = Jsoup.connect("https://www.w3schools.com/tags/default.asp").get();

			// Get all image elements on the page
			Elements images = doc.select("img[src]");
			for (Element image : images) {
				// Get the image source URL and log to the text file
				String src = image.attr("src");
				src = src.startsWith("/") ? "https://www.w3schools.com" + src : src; // if src starts with "/" then prepend "https://www.w3schools.com"

				System.out.println("Image URL: " + src);

				// Get the image name from the URL
				String imageName = src.substring(src.lastIndexOf("/") + 1);
				System.out.println("Image Name: " + imageName);

				// Download the image
				URL url = new URL(src);
				BufferedInputStream bis = new BufferedInputStream(url.openStream());
				FileOutputStream fis = new FileOutputStream("images/" + imageName);
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = bis.read(buffer, 0, 1024)) != -1) {
					fis.write(buffer, 0, count);
				}
				fis.close();
				bis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
	}

	public static void cleanImagesFolder() {
		String link = "https://www.w3schools.com/tags/default.asp";
		String folderName = link.replaceAll("(:)|(\\/)", "_");
		System.out.println(folderName);
		
		// Create a folder for the images
		File location = new File("images\\" + folderName);
		System.out.println(location.getAbsolutePath());

		// Delete all files in the images folder
		location.mkdirs();
		File[] files = location.listFiles();
		for (File file : files) {
			file.delete();
		}
	}
}