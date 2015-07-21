package parsers;






import gui.GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public abstract class ImageParser extends Thread {
	

	protected int imagesDownloaded;
	protected GUI gui;
	protected String tag;
	protected int pages;
	protected int delay;
	ReadableByteChannel rbc;
	FileOutputStream fos;
	
	
	public void setup(String tag,int pages,GUI gui,int delay){
		this.tag = tag;
		this.pages = pages;
		this.gui = gui;
		this.delay = delay;
	}
	@SuppressWarnings("deprecation")
	public void diePlease(){
		
		gui.reportback();
		gui.appendLog("\n--PARSER STOPPED --",true);
		this.stop();
	}
	
	
	protected void downloadPicture(String src, String folder){
	
		String path = folder+"/"+getPictureName(src);
		gui.appendLog("downloading: "+src,true);
		File f = new File(path);
		if(f.exists()){
			gui.appendLog("File already downloaded", true);
		}else{
			try {	
				Thread.sleep(delay);		
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
			
			URL url=null;
			try {
				url = new URL(src);
			}
			catch (MalformedURLException e) {
				gui.appendLog(src+ "is a invalid url", true);
			}
			
			try {
				rbc = Channels.newChannel(url.openStream());
			} catch (IOException e) {
				gui.appendLog("openstream error "+url, true);
			}
			
			try {
				fos = new FileOutputStream(path);
			} catch (FileNotFoundException e) {
				gui.appendLog("FileNotFoundException "+folder+"/"+getPictureName(src), true);
			}
			try {
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				rbc.close();
				fos.close();
			} catch (IOException e) {
				gui.appendLog("cound not transfer from: "+url,true);
			}
		}
	}
	
	protected Document getDoc(String src){
		Document doc;
		try {
			Thread.sleep(delay);	
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	
		gui.appendLog("parsing "+src,true);
		try {
			doc = Jsoup.connect(src).get();
			return doc;
		} catch (IOException e1) {
			gui.appendLog("ERROR: could not get document from "+src,true);
			return null;
		}
		
	}

		
	
	protected void pl(String txt){
		System.out.println(txt);
	}
	protected void p(String txt){
		System.out.print(txt);
	}
	protected String getPictureName(String src){
		return imagesDownloaded+src.substring(src.lastIndexOf("."),src.length());
	}
	
}
