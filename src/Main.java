import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
	public static void main(String[] args) throws IOException {
		String link = "https://www.wikihow.com/Breathe";
		LocalDateTime now = LocalDateTime.now();

		// Initialize the file writer
		try (FileWriter fr = new FileWriter("log.txt", true);
		     BufferedWriter myWriter = new BufferedWriter(fr)) {
			myWriter.write("\n\n" + now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
			System.out.println(now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

			Document doc;
			try {
				// Connect to the website using the Jsoup library
				doc = Jsoup.connect(link).get();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			
			String folderName = link.replaceAll("(:)|(\\/)", "_");

			// Create a folder for the images and delete all files in it
			File location = new File("images\\" + folderName + "\\");
			location.mkdirs();
			cleanFolder(location, true);

			// Get all image elements on the page
			Elements images = doc.select("img[src]");
			for (Element image : images) {
				// Get the image source URL and log to the text file
				String src = image.attr("src");
				src = src.startsWith("/")?(link+src):src; // if src starts with "/" then prepend "https://www.w3schools.com"

				// System.out.println("Image URL: " + src);
				myWriter.write("Image URL: " + src + "\n");

				// Get the image name from the URL
				String imageName = src.substring(src.lastIndexOf("/") + 1);
				// System.out.println("Image Name: " + imageName);

				// Download the image
				URL url = new URL(src);
				try (BufferedInputStream bis = new BufferedInputStream(url.openStream());
				FileOutputStream fis = new FileOutputStream(location.getPath() + "\\" + imageName);) {
					byte[] buffer = new byte[1024];
					int count = 0;
					while ((count = bis.read(buffer, 0, 1024)) != -1) {
						fis.write(buffer, 0, count);
					}
					fis.close();
					bis.close();
				} catch (IOException ex) {

				}
			}

			// Get all divs in the selected website and print out all of their attributes to a log file
			Elements divs = doc.select("div");
			for (Element div : divs) {
				assert div != null;
				String className = div.attr("class");
				assert className != null;
				// System.out.println("Div Classname: " + className);

				div.attributes().forEach(System.out::println);
				div.attributes().forEach(x -> {
					assert x != null;
					try {
						myWriter.write("Div Attributes: " + x + "\n");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void cleanFolder(File folder, boolean recursive) {
		File[] files = folder.listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					if (recursive) {
						cleanFolder(file, recursive);
						file.delete();
					}
				}
				else {
					file.delete();
				}
			}
		}
	}

	// Overload the cleanFolder method to allow for an optional recursive parameter where the default value is false
	public static void cleanFolder(File folder) {
		cleanFolder(folder, false);
	}
}