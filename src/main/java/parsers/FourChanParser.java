package parsers;

import gui.GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class FourChanParser extends ImageParser {
	
	private String url;
	private int mode;
	private String board;
	private int limit;
	
	public FourChanParser(String url, int delay, GUI gui, int mode){
		imagesDownloaded = 0;
		this.url = url;
		this.gui = gui;
		this.delay = delay;
		this.mode = mode;
	}
	
	public FourChanParser(String board, int delay, GUI g, int mode, int limit) {
		imagesDownloaded = 0;
		this.board = board;
		this.gui = g;
		this.delay = delay;
		this.mode = mode;
		this.limit = limit;
	}

	public void run(){
		
		gui.appendLog("Parser for 4chan started.",true);
		if(mode==1){
			
			parseImagesfromBoard();
		} else {
			if(url.endsWith("/"))
				url=url.substring(0, url.length()-1);
			int temp = url.lastIndexOf('/');
			String threadNr = url.substring(temp+1,url.length());
			temp = url.lastIndexOf("/thread/");
			url = url.substring(0,temp);
			temp = url.lastIndexOf('/');
			board = url.substring(temp+1,url.length());
			File dir = new File(board);
			dir.mkdir();
			gui.appendLog("Folder '"+board+"' created",true);
			parseImagesfromThread(Long.parseLong(threadNr));
			
		}

	
		gui.appendLog("4chan-Parser done. Downloaded "+imagesDownloaded+" pictures ",true);
		gui.reportback();
		
	}
	
	private void parseImagesfromBoard() {
		
		File dir = new File(board);
		dir.mkdir();
		gui.appendLog("Folder '"+board+"' created",true);
	
		try {
			InputStream inputstream = new URL("http://a.4cdn.org/"+board+"/threads.json").openStream();
	        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream,"UTF-8"));
	        Gson gson = new Gson();
	        try {	
				Thread.sleep(delay);		
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
	        JsonArray pages = gson.fromJson(bufferedreader,JsonArray.class);
	       
	        JsonObject page;
	        JsonArray threads;
	        String threadNr;
	        int count = 0;
	        boolean done = false;
	        for(int i=0;i<pages.size();i++){
	        	
	        	page = pages.get(i).getAsJsonObject();
	        	threads = page.get("threads").getAsJsonArray();
	        	
	        	for(int j=0;j<threads.size();j++){
	        		 
	        		threadNr = threads.get(j).getAsJsonObject().get("no").getAsString();
	        		parseImagesfromThread(Long.parseLong(threadNr));
	        		count++;
	        		if(count==limit){
	        			done=true;
	        			break;
	        		}
	        	}
	        	if(done)break;
	        }
        
		} catch(IOException e){
			gui.appendLog("Could not get thread-data from 4chan please retry", true);
		}
		
	}

	private void parseImagesfromThread(long tnr){
		
		
		int c = 0;
		String path = board+"/"+tnr;
		File dir = new File(path);
		dir.mkdir();
		gui.appendLog("Folder '"+path+"' created",true);
		gui.appendLog("now parsing thread "+tnr+" on board "+board, true);
		
		try {
			InputStream inputstream = new URL("http://a.4cdn.org/"+board+"/thread/"+tnr+".json").openStream();
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream,"UTF-8"));
			Gson gson = new Gson();
			try {	
				Thread.sleep(delay);		
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			JsonObject thread = gson.fromJson(bufferedreader,JsonObject.class);
			JsonArray posts = thread.get("posts").getAsJsonArray();
			JsonObject post;
			
			for(int i = 0;i<posts.size();i++){
				post = posts.get(i).getAsJsonObject();
				
				//look if there is a image
				if(post.get("filename")!=null){
					downloadPicture("http://i.4cdn.org/"+board+"/src/"+post.get("tim").getAsString()+post.get("ext").getAsString(),path+"/"+post.get("tim").getAsString()+post.get("ext").getAsString());
					imagesDownloaded++;
					c++;
				}
			}
			
			
		} catch(IOException e){
			gui.appendLog("Could not get thread-data from 4chan please retry", true);
		}
		gui.appendLog(c+" images found in parsed thread.",true);
		
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
