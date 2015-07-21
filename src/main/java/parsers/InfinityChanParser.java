package parsers;

import gui.GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;






import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InfinityChanParser extends ImageParser {
	
	private String url;
	private int mode;
	private int limit;
	private String board;
	
	public InfinityChanParser(String url, int delay, GUI gui, int mode){
		imagesDownloaded = 0;
		this.url = url;
		this.gui = gui;
		this.delay = delay;
		this.mode = mode;
	}
	public InfinityChanParser(String board, int delay, GUI gui, int mode, int limit){
		
		imagesDownloaded=0;
		this.delay=delay;
		this.mode=mode;
		
		this.gui=gui;

		this.limit=limit;
		this.board=board;
	}
	
	
	public void run(){
		gui.appendLog("Parser for 8chan started.", true);
		

		if(mode==1){
			parseImagesfromBoard();
			
		}else{
			
			int temp = url.lastIndexOf(".net");
			String temp1 = url.substring(temp+4);
			System.out.println("temp1; "+temp1);
			temp = temp1.indexOf("/res/");
			temp1= temp1.substring(1, temp);
			File dir = new File(temp1);
			dir.mkdir();
			gui.appendLog("Folder '"+temp1+"'created'", true);
			board=temp1;
		    parseImagesfromThread(url);
		}
		
		gui.appendLog("InfinityChan-Parser done. Downloaded "+imagesDownloaded+" pictures ", true);
		gui.reportback();
	}
	
	public void parseImagesfromBoard(){
		//threadids rausfinden, url+board benutzen, nicht das limit vergessen
		File dir = new File(board);
		dir.mkdir();
		gui.appendLog("Folder '"+board+"' created", true);
		
		Document doc = getDoc("http://8ch.net/"+board+"/catalog.html");
		
		Elements threadDivs = doc.select("a[href*=/res/]");
		
		int count = 0;
		for(Element thedDiv:threadDivs){
			
			if(count==limit)break;
			count++;
			String threadUrl = thedDiv.absUrl("href");
			parseImagesfromThread(threadUrl);
		}
	}
	
	public void parseImagesfromThread(String url){
		
		//threadnummer aus der url rausbekommen
		int temp = url.lastIndexOf('/');
		String temp1 = url.substring(temp+1);
		temp = temp1.lastIndexOf('.');
		temp1 = temp1.substring(0, temp);
		
		String path=board+"/"+temp1;
		File dir = new File(path);
		dir.mkdir();
		gui.appendLog("Folder '"+path+"' created",true);
		gui.appendLog("now parsing thread "+temp1+" on board "+board, true);
		
		//image urls finden
		Document doc = getDoc(url);
		Elements picDivs = doc.select("[class=fileinfo]");
		int c = 0;
		for(Element pic:picDivs){
			String dl = pic.child(0).absUrl("href");
			temp = dl.lastIndexOf("/");
			temp1 = dl.substring(temp+1);
			if(dl.startsWith("http://8ch.net/") || dl.startsWith("https://media.8ch.net/")){
				downloadPicture(dl,path+"/"+temp1);
				imagesDownloaded++;
				c++;
			}		
		}
		gui.appendLog(c+" images found in thread", true);
	}
	
	public void downloadPicture(String src, String folder){
		gui.appendLog("downloading: "+src, true);
		File f = new File(folder);
		if(f.exists()){
			gui.appendLog("File already downloaded.", true);
		}else{
			try{
				Thread.sleep(delay);
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
			}
			
			URL url = null;
			try {
				url = new URL(src);
			} catch (MalformedURLException e1) {
				gui.appendLog("MalformedURLException "+src,true);
			}
			 
			try {
				rbc = Channels.newChannel(url.openStream());
			} catch (IOException e1) {
				gui.appendLog("OpenStream error "+src,true);
			}
			
				
			try {
				fos = new FileOutputStream(folder);
			} catch (FileNotFoundException e1) {
					gui.appendLog("FileNotFoundException "+folder,true);
			}
			
			try {	
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			rbc.close();
			fos.close();
			}
			catch (IOException e) {
				gui.appendLog("could not download "+src , true);
			}
			
			
		}
	}
}
