/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myform;

import javax.swing.JProgressBar;
import org.jblas.DoubleMatrix;

/**
 *
 * @author karan
 */
public class Trainer implements Runnable{
    DigitReader digitReader;
    public Trainer(DigitReader t) {
        this.digitReader =t;
    }
    
    @Override
    public void run() {
         double out[][]={{1,0,0,0,0,0,0,0,0,0},
                        {0,1,0,0,0,0,0,0,0,0},
                        {0,0,1,0,0,0,0,0,0,0},
                        {0,0,0,1,0,0,0,0,0,0},
                        {0,0,0,0,1,0,0,0,0,0},			
                        {0,0,0,0,0,1,0,0,0,0},
                        {0,0,0,0,0,0,1,0,0,0},
                        {0,0,0,0,0,0,0,1,0,0},
                        {0,0,0,0,0,0,0,0,1,0},
                        {0,0,0,0,0,0,0,0,0,1},
                        };
        MnistTrainingClass test = new MnistTrainingClass(1);
        int epoch=200;
        digitReader.prograssBar.setMaximum(epoch);
        digitReader.prograssBar.setMinimum(0);
        digitReader.prograssBar.setStringPainted(true);
//        digitReader.prograssBar.setIndeterminate(true);
        System.out.println(digitReader.isTraining);  
        DigitReader.hiddenLayer.load();
		for(int j=0;j<epoch;j++){
                        digitReader.prograssBar.setValue(j+1);
			System.out.println(j);
			test.close();
			test= new MnistTrainingClass(1);
			for(int i=0;i<60000;i++){
				DigitReader.hiddenLayer.feedForward(new DoubleMatrix(test.nextImage()));
				DigitReader.outputLayer.backpropogate(new DoubleMatrix(out[test.nextLabel()]));
                        }
			DigitReader.hiddenLayer.calculateGradient(60000);
			DigitReader.hiddenLayer.updateWeights();
			DigitReader.hiddenLayer.clearCDeltas();
			DigitReader.hiddenLayer.save();
		}
        digitReader.btTrain.doClick();
        digitReader.prograssBar.setValue(0);
        System.out.println("Training Compelete");        
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   public void start ()
   {
      Thread t=null; 
      System.out.println("Starting Training");
      if (t == null)
      {
         t = new Thread (this, "Train");
         t.start ();
      }
   }
    
}
