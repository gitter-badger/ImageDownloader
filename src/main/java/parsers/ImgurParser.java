package parsers;

import gui.GUI;

import java.io.File;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImgurParser extends ImageParser {
	
	private String albumName;
	
	public ImgurParser(String album, int delay, GUI gui){
		this.gui = gui;
		this.albumName = album;
	}
	
	public void run(){
		gui.appendLog("ImgurParser started!", true);
		

		File dir = new File("Imgur-"+albumName);
		dir.mkdir();
		gui.appendLog("Folder 'Imgur-"+albumName+"' created.", true);	
		parseImagesFromList();
		gui.appendLog("ImgurParser stopped! Media downloaded: "+imagesDownloaded, true);
		gui.reportback();
	}
	
	
	
	public void parseImagesFromList(){
		
		Document doc = getDoc("http://imgur.com/a/"+albumName+"/layout/blog");
		
		Elements images = doc.getElementsByTag("img");
		//download ALL the images
		for(Element elem : images){
				if(elem.absUrl("data-src").equals(""))continue;
				downloadPicture(removeThumb(elem.absUrl("data-src")),"Imgur-"+albumName);
				imagesDownloaded++;
		
			
			
		}
	}
	
private static String removeThumb(String s){
		
		s = s.substring(s.lastIndexOf("/")+1, s.length());
		String p[] = s.split("\\.");
		p[0] = p[0].substring(0, p[0].length()-1);
		return "http://i.imgur.com/"+p[0]+"."+p[1];
	}

}

