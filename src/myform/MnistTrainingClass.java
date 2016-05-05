package myform;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MnistTrainingClass {
	 FileInputStream label = null,imageData=null;
	 int mode;
	 String fileName[]={"data0",
                            "data1",
                            "data2",
                            "data3",
                            "data4",
                            "data5",
                            "data6",
                            "data7",
                            "data8",
                            "data9"
			 	};
	 FileInputStream images[] = new FileInputStream[10];
	public MnistTrainingClass(int mode){
		this.mode=mode;
		if(mode==0){
		 int img[] = new int[784];
	        try {
	            label = new FileInputStream("label.idx1-ubyte");
	            for(int i=0;i<10;i++){
	            	images[i]= new FileInputStream(fileName[i]);
	            }
	            int c1,c2;
	        }catch(IOException e){
	        	e.printStackTrace();
	        	System.out.println("Something wrog with file");
	        }
		}else{
			try{
				 label = new FileInputStream("labels.idx1-ubyte");
				 imageData = new FileInputStream("images.idx3-ubyte");
			}
			catch(FileNotFoundException e){
				 e.printStackTrace();
			}
			int i=0;
			int cImage=0,cLabel=0;
	        try {
				while ((cImage = imageData.read()) != -1) {
					if(i++<15)
						continue;
					else break;
				}
				i=0;
	            while ((cLabel = label.read()) != -1) {
	            	if(i++<7){
	            		continue;
	            	}
	            	else break;
	            }				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		}
	       
	}
	
	public double[] nextImage(int j){
		int c;
		double img[] = new double[784];
		if(j==-1)
			return img;	
		for(int i=0;i<784;i++){
			try {
					if((c = images[j].read())!=-1){
						img[i]= c>0?1:0;	
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public int nextLabel(){
		int c = -1;
		try {
			c = label.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
	public double[] nextImage(){
		int c;
		double img[] = new double[784];	
		for(int i=0;i<784;i++){
			try {
					if((c = imageData.read())!=-1){
						img[i]= c>0?1:0;	
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return img;
	}
	public void close(){
		try {
			label.close();
			if(mode==0)
			for(int i=0;i<10;i++)
				images[i].close();
			else{
				imageData.close();
				label.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
