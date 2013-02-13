import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;


public class Main implements ClipboardOwner {
	public void captureScreen(final String sFilename) throws AWTException, IOException
	  {
	      // Determine current screen size
	      Rectangle size = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	 
	      Robot robot = new Robot();
	      BufferedImage oImage = robot.createScreenCapture(size);
	      ImageIO.write(oImage, "png", new File(sFilename));
	  }
	
	public String inBase64(final String filename) throws IOException {
		return Base64.encodeBase64String(FileUtils.readFileToByteArray(new File(filename)));
	}
	
	public void putOnClipboard(String text) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringselection = new StringSelection(text);
		clipboard.setContents(stringselection, this);
	}
	
	/**
	 * with thanks to http://stackoverflow.com/questions/1067655/how-to-upload-a-file-using-java-httpclient-library-working-with-php-strange-pr
	 * @param filename
	 * @param url
	 * @throws IOException 
	 * @throws  
	 */
	public void upload(final String filename, final String url) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		
	    HttpPost httppost = new HttpPost(url);
	    File file = new File(filename);

	    MultipartEntity mpEntity = new MultipartEntity();
	    ContentBody cbFile = new FileBody(file, "image/png");
	    mpEntity.addPart("userfile", cbFile);

	    httppost.setEntity(mpEntity);
	    System.out.println("executing request " + httppost.getRequestLine());
	    HttpResponse response = httpclient.execute(httppost);
	    HttpEntity resEntity = response.getEntity();

	    System.out.println(response.getStatusLine());
	    if (resEntity != null) {
	      System.out.println(EntityUtils.toString(resEntity));
	    }
	    if (resEntity != null) {
	      resEntity.consumeContent();
	    }

	    httpclient.getConnectionManager().shutdown();
	}
	
	public static void main(String[] main) {
		Main m = new Main();
//		m.captureScreen("test.png");
	}

	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// whatever.
	}
}
