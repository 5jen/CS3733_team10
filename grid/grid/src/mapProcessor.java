import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

public class mapProcessor extends Component {

	private void parseImage (BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		int node = 0;
		
		int gridSize = 12;
		//number of rows and columns for grids
		int row = (int)Math.ceil((double)h/gridSize);
		int col = (int)Math.ceil((double)w/gridSize);
		
		boolean wall = false, path = false;
		
		PrintWriter map;
		
		try {
			map = new PrintWriter ("map.txt", "UTF-8");
			
			for(int r = 0; r < row; r++){
				for(int c = 0; c < col; c++){
					for (int i = gridSize*r; i < gridSize*(r+1)&& i < h; i+= 1) {
						for (int j = gridSize*c; j < gridSize*(c+1) && j < w; j+= 1) {
							
							int pixel = image.getRGB(j, i);
							if(pixel == -1) path = true;			//white
							if(pixel == -16777216) {				//black
								wall = true;
								//go to next grid
								j = gridSize*(c+1);
								i = gridSize*(r+1);
							}
						}//each row of a grid evaluated!!!
					}//each grid evaluated!!!	
					node++;
					if (wall == true) map.print(0);
					else if(path == true) map.print(1);
					path = false;
					wall = false;
				}//each row of grids evaluated!!
				map.println("");
			}
			
			map.close();
			System.out.print(node);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void parse() {
		try {
			
			BufferedImage image = 
			ImageIO.read(new File("/Users/wuzhizhen/Documents/cs3733/grid/test.png"));
			parseImage(image);

			} 
		
		catch (IOException e) {
			System.err.println(e.getMessage());

			}
	}
	
	public static void main (String[] args){
		new mapProcessor().parse();
		//System.out.print("hello");

	}
	
}
