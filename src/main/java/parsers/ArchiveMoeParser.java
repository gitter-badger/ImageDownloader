package parsers;

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

import gui.GUI;

public class ArchiveMoeParser extends ImageParser {

	private String url;
	private int mode;
	private String board;
	private int limit;
	private String URL;
	
	public ArchiveMoeParser(String tag, int delay, GUI g, int i) {
		imagesDownloaded = 0;
		this.url = tag;
		this.gui = g;
		this.delay = delay;
		this.mode = i;
		
	}
	
	public ArchiveMoeParser(String tag, int delay, GUI g, int i, int pages) {
		this.board = tag;
		this.gui = g;
		this.delay = delay;
		this.mode = i;
		this.limit=pages;
	}

	public void run(){
		gui.appendLog("Parser for archive.moe started.", true);
		if(mode == 1){
			parseImagesFromBoard();
		}else{
			if(url.endsWith("/"))
				url=url.substring(0, url.length()-1);
			int temp = url.lastIndexOf('/');
			String threadNr = url.substring(temp+1,url.length());
			temp = url.lastIndexOf("/thread/");
			this.URL=url;
			url = url.substring(0,temp);
			temp = url.lastIndexOf('/');
			board = url.substring(temp+1,url.length());
			File dir = new File(board);
			dir.mkdir();
			gui.appendLog("Folder '"+board+"' created",true);
			parseImagesfromThread(Long.parseLong(threadNr));
		}
		
		gui.appendLog("archive.moe done. Downloaded "+imagesDownloaded+" pictures ",true);
		gui.reportback();
	}
	
	private void parseImagesFromBoard() {
		File dir = new File(board);
		dir.mkdir();
		gui.appendLog("Folder '"+board+"' created", true);
		
		for(int i = 1; i <= limit; i++){
			Document doc = getDoc("https://archive.moe/"+board+"/gallery/"+i+"/");
			Elements threadLinks = doc.select("a[class=thread_image_link]");
			
			for(Element thread:threadLinks){
				URL = thread.absUrl("href");
				String url=URL;
				if(url.endsWith("/"))
					url=url.substring(0, url.length()-1);
				int temp = url.lastIndexOf('/');
				String threadNr = url.substring(temp+1,url.length());
				parseImagesfromThread(Long.parseLong(threadNr));
				
			}
		}
		
	}

	private void parseImagesfromThread(long tnr){
		
		String path = board+"/"+tnr;
		File dir = new File(path);
		dir.mkdir();
		gui.appendLog("Folder '"+path+"' created",true);
		gui.appendLog("now parsing thread "+tnr+" on board "+board, true);
		
		Document doc = getDoc(URL);
		Elements picLinks = doc.select("a[class=thread_image_link]");
		int c = 0;
		
		for(Element pic:picLinks){
			String picUrl = pic.absUrl("href");
			String picName = picUrl.substring(picUrl.lastIndexOf("/")+1, picUrl.length());
			downloadPicture(pic.absUrl("href"), path+"/"+picName);
			c++;
			imagesDownloaded++;
		}
		gui.appendLog(c+" images found in thread", true);
		
		
	}
	
protected void downloadPicture(String src,String folder){
		
		gui.appendLog("downloading: "+src,true);
		
		File f = new File(folder);
		if(f.exists()){
			gui.appendLog("File already downloaded.", true);	
		}else{
			try {	
				Thread.sleep(delay);		
			} catch (InterruptedException e) {
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
