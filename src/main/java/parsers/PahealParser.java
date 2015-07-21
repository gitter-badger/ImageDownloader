package parsers;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import java.io.*;



public class PahealParser extends ImageParser {
	
	
	

	public PahealParser(){
		imagesDownloaded = 0;
	}
	
	
	public void run(){
		
		
		File dir = new File(tag);
		gui.appendLog("Parser for Paheal Rule34 started, be advised this site has a somehow slow DL-speed.",true);
		gui.appendLog("Folder '"+tag+"' created",true);

		dir.mkdir();
		
		String url_pre = "http://rule34.paheal.net/post/list/"+tag+"/";
			
		for(int i = 1;i<pages+1;i++){
			parseImagesfromList(url_pre+i);
			
		}	
		
		gui.appendLog("Paheal-Parser done. Downloaded "+imagesDownloaded+" pictures into folder '"+tag+"'",true);
		gui.reportback();
		
	}
	
	private void parseImagesfromList(String url){
	
		
		
			
			Document doc = null;
			Elements picDivs;
			String picRealUrl;
			Element picLink;
			
			doc = getDoc(url);
			
				
			if(doc==null){
				
			}else{
				
				
				picDivs = doc.select("[data-tags]");
		
				int count = 0;
				for(Element picDiv:picDivs){
					
					picLink = picDiv.child(2);
					picRealUrl = picLink.attr("href");
					
					downloadPicture(picRealUrl,tag);
					imagesDownloaded++;
					gui.appendLog("",true);
					
				}
				gui.appendLog("found "+count+" images on page",true);
				
			}
	}
	
	
}