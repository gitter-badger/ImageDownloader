package  parsers;

import java.io.File;





import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class XXXParser extends ImageParser {
	
	public XXXParser(){
		imagesDownloaded = 0;
	}
	
	public void run(){
		
		
		File dir = new File(tag);
		gui.appendLog("Parser for rule34.xxx started.",true);
		gui.appendLog("Folder '"+tag+"' created",true);

		dir.mkdir();
		
		
		String url_pre = "http://rule34.xxx/index.php?page=dapi&s=post&q=index&tags="+tag;
			
		for(int i = 0;i<pages;i++){
			parseImagesfromList(url_pre+"&pid="+i);
			
		}	
		
		gui.appendLog("Paheal-Parser done. Downloaded "+imagesDownloaded+" pictures into folder '"+tag+"'",true);
		gui.reportback();
		
	}
	
	private void parseImagesfromList(String url){
		
	
		
		
		String xml = getDoc(url).toString();
		Document xmlDoc = Jsoup.parse(xml,"",Parser.xmlParser());
		
		for(Element e : xmlDoc.select("post")){
			downloadPicture(e.attr("file_url"),tag);
			imagesDownloaded++;
		}
		
		
		
		
		
	}

}
