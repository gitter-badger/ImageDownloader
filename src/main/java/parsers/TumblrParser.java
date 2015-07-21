package parsers;

import java.io.File;




import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TumblrParser extends ImageParser {
	
	
		
	public void run(){
		gui.appendLog("TumblrParser started!", true);
		
		

		File dir = new File(tag);
		dir.mkdir();
		gui.appendLog("Folder '"+tag+"' created.", true);
		
		for(int i = 0 ; i<pages ; i++){
			parseImagesFromList("http://"+tag+".tumblr.com/page/"+(i+1));
		}
		gui.appendLog("TumblrParser stopped! Media downloaded: "+imagesDownloaded, true);
		gui.reportback();
	}
	
	public void parseImagesFromList(String url){
		
		Document doc = getDoc(url);
		
		Elements images = doc.getElementsByTag("img");
		
		Elements links = doc.select("a[href]");
		
		
		//download ALL the images
		for(Element elem : images){
			if(!elem.absUrl("src").equals("")){
			downloadPicture(elem.absUrl("src"),tag);
			imagesDownloaded++;
			}
		}
		
		 
		
		//download ONLY the links to gifs, webms, and images
		for(Element elem : links){
			
			String link = elem.absUrl("href");
			if(link.endsWith(".gif") || link.endsWith(".webm") || link.endsWith(".jpeg") 
					||link.endsWith(".jpg") ||link.endsWith(".png") ||link.endsWith(".bmp")){
				downloadPicture(link,tag);
				imagesDownloaded++;
			}
		}
		
	}
	
}
