package parsers;


import java.io.File;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GelBooruParser
  extends ImageParser
{
  public GelBooruParser()
  {
    this.imagesDownloaded = 0;
  }
  
  public void run()
  {
    File dir = new File(this.tag);
    this.gui.appendLog("Parser for gelboru.com started.", true);
    this.gui.appendLog("Folder '" + this.tag + "' created", true);
    dir.mkdir();
    

    String url_pre = "http://gelbooru.com/index.php?page=post&s=list&tags=" + this.tag + "&pid=";
    for (int i = 0; i < this.pages; i++) {
      parseImagesfromList(url_pre + i * 42);
    }
    this.gui.appendLog("GelBooru-Parser done. Downloaded " + this.imagesDownloaded + " pictures into folder '" + this.tag + "'", true);
    if (this.imagesDownloaded == 0) {
      this.gui.appendLog("-- NO IMAGES FOUND --", true);
    }
    this.gui.reportback();
  }
  
  private void parseImagesfromList(String url)
  {
    Document doc = null;
    Document show = null;
    



    doc = getDoc(url);
    if (doc != null)
    {
      Elements picSpans = doc.getElementsByClass("thumb");
      
      int count = 0;
      for (Element picSpan : picSpans)
      {
        count++;
        String picShowUrl = picSpan.child(0).absUrl("href");
        String picRealUrl;
        show = getDoc(picShowUrl);
        if (show != null)
        {
          try
          {
            picRealUrl = show.getElementById("image").attr("src");
          }
          catch (NullPointerException e)
          {
            
            count--;
            continue;
          }
          picRealUrl = picRealUrl.substring(0, picRealUrl.lastIndexOf("?"));
          
          downloadPicture(picRealUrl, this.tag);
          this.imagesDownloaded += 1;
        }
        this.gui.appendLog("", true);
      }
      this.gui.appendLog("found " + count + " pictures on page.", true);
    }
  }
}
