/*http://alvinalexander.com/blog/post/java/getting-rgb-values-for-each-pixel-in-image-using-java-bufferedi*/
/*http://www.javamex.com/tutorials/graphics/bufferedimage_setrgb.shtml*/
/*http://www.mkyong.com/java/how-to-write-an-image-to-file-imageio/*/
package storm.vstorm.bolt;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import com.twitter.chill.Base64.InputStream;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class IMGBolt extends BaseRichBolt{
	OutputCollector _collector;
	String rootPath = "/Users/lexu/Documents/workspace/VStorm-Topologies/images/";

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      _collector = collector;
    }
    @Override
    public void execute(Tuple tuple) {
    	Object imgobj = tuple.getValueByField("image");
    	
    	//BufferedImage img = (BufferedImage)imgobj;
    	ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = null;
		try {
			//o = new ObjectOutputStream(b);
			//o.writeObject(imgobj);
			//byte[] bytearr = b.toByteArray();
			byte[] bytearr = (byte[]) imgobj;
			ByteArrayInputStream in = new ByteArrayInputStream(bytearr);
			BufferedImage img = ImageIO.read(in);
			int pixel = 0;
	    	int w = img.getWidth();
	        int h = img.getHeight();
	        //System.out.println("width, height: " + w + ", " + h);
	        for (int i = 0; i < h; i++) {
	          for (int j = 0; j < w; j++) {
	            pixel = img.getRGB(j, i);
	            int alpha = (pixel >> 24) & 0xff;
	            int red = (pixel >> 16) & 0xff;
	            int green = (pixel >> 8) & 0xff;
	            int blue = (pixel) & 0xff;
	            int color = (alpha << 24) | (blue << 16) | (red << 8) | green;
	            img.setRGB(j, i, color);
	          }
	        }
	    	long timestamp = System.currentTimeMillis();
	    	String str_timestamp = String.valueOf(timestamp);
			ImageIO.write(img, "jpg", new File(rootPath+"lalalalalalala"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
       
    	/*String rootPath = "/Users/lexu/Documents/workspace/VStorm-Topologies/images/";
    	Random rand = new Random();
		String[] filenames = new String[]{"DSC00249.jpg","DSC00557.jpg", "DSC00630.jpg", "DSC00790.jpg", "DSC01025.jpg"};
		String filename = filenames[rand.nextInt(filenames.length)];
		//filename =rootPath+filename;
		//BufferedImage image;
		try {
			BufferedImage image = ImageIO.read(new File(rootPath+"failed.png"));
			ImageIO.write(image, "jpg", new File(rootPath+"lalalalalalala"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	/*int pixel = 0;
    	int w = img.getWidth();
        int h = img.getHeight();
        //System.out.println("width, height: " + w + ", " + h);
        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
            pixel = img.getRGB(j, i);
            int alpha = (pixel >> 24) & 0xff;
            int red = (pixel >> 16) & 0xff;
            int green = (pixel >> 8) & 0xff;
            int blue = (pixel) & 0xff;
            int color = (alpha << 24) | (blue << 16) | (red << 8) | green;
            img.setRGB(j, i, color);
          }
        }
    	long timestamp = System.currentTimeMillis();
    	String str_timestamp = String.valueOf(timestamp);*/
    	/*try {
			ImageIO.write(image, "jpg", new File(rootPath+"lalalalalalala"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

			
      _collector.emit(tuple, new Values(tuple.getFields()));
      //_collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
      //declarer.declare(new Fields("word"));
    }
}