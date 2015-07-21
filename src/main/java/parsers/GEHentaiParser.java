package parsers;

import gui.GUI;

import java.io.File;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GEHentaiParser extends ImageParser {
	
	private boolean wholeMode;
	private String foldername;
	
	
	public GEHentaiParser(String tag, int delay, GUI gui, boolean mode, int pages){
		this.gui = gui;
		this.tag = tag;
		this.delay = delay;
		this.pages = pages;
		wholeMode = mode;
	}

	public void run(){
		
		gui.appendLog("GEHentaiParser started!", true);
		this.foldername = getFolderName();
		File dir = new File(foldername);
		
		dir.mkdir();
		gui.appendLog("Folder '"+foldername+"' created.", true);
		if(wholeMode){
			for(int i = 0; i < pages; i++){
				parseImagesFromAlbumPage(tag+"?p="+i);
			}
		}else{
			parseImagesFromAlbumPage(tag);
		}
		
		gui.appendLog("GEHentaiParser stopped! Images downloaded: "+imagesDownloaded, true);
		gui.reportback();
	}
	
	
	private String getFolderName(){
		
		Document temp = getDoc(tag);
		
		Element elem = temp.getElementById("gn");
		
		return elem.text();
	}
	public void parseImagesFromAlbumPage(String url){
		
		
		Document doc = getDoc(url);
		
		Elements divs = doc.select(".gdtm");
		
		for(Element div : divs){
			
			
			Document temp = getDoc(div.select("a").get(0).absUrl("href"));
			
			for(Element elem : temp.select("img[src*=keystamp]")){
				downloadPicture(elem.absUrl("src"),foldername);
				imagesDownloaded++;
			}
			
		}
		
		
		
	}
}
