package myform;

import org.jblas.DoubleMatrix;
import java.io.IOException;
import java.util.Random;

public class Layer {
	private String name;
	private int nodesInLastLayer,noOfNodes;
	private Layer nextLayer,previousLayer;
	Random random =new Random();
	//cdelta accumulates the error in one iteration of training set
	private double[][] theta,gradient,cDelta;
	private double[] input,z,output,bias,delta;        //bias is created to append 1 to "Input" Vector
												 //check FeedForward Function;	
	private DoubleMatrix Theta,Input,Z,Output,Bias,Delta,Gradient,CDelta;
	
	public Layer(int nodesInLastLayer,int noOfNodes,Layer nextLayer,String name){
		this.name =name;
		bias = new double[1];
		bias[0] =1; 
		previousLayer=null;
		this.nextLayer = nextLayer;
		this.nodesInLastLayer = nodesInLastLayer;
		this.noOfNodes = noOfNodes;
		
		theta = new double[nodesInLastLayer+1][noOfNodes];  //take care of this +1(bias)
		gradient = new double[nodesInLastLayer+1][noOfNodes];
		cDelta = new double[nodesInLastLayer+1][noOfNodes];
		input = new double[nodesInLastLayer+1];
		z=new double[noOfNodes];
		delta = new double[noOfNodes];
		output = new double[noOfNodes];
		double maxRange=2/Math.sqrt(nodesInLastLayer);
		for(int i=0;i<noOfNodes;i++)
			for(int j=0;j<nodesInLastLayer+1;j++){
				cDelta[j][i]=0;
				theta[j][i]=random.nextDouble()*maxRange-maxRange/2;
				
			}
		Theta = new DoubleMatrix(theta);
		Input = new DoubleMatrix(input);
		Output = new DoubleMatrix(output);
		Z = new DoubleMatrix(z);
		Bias  = new DoubleMatrix(bias);
		Gradient = new DoubleMatrix(gradient);
		Delta = new DoubleMatrix(delta);
		CDelta = new DoubleMatrix(cDelta);
	}
	public void feedForward(DoubleMatrix InputFromPreviousLayer){
		this.Input = InputFromPreviousLayer;
		this.Input = DoubleMatrix.concatVertically(Bias,InputFromPreviousLayer); //adding The bias Term
		Z = Theta.transpose().mmul(this.Input);
//		if(nextLayer==null)
//		this.Input.print();
//		Z.print();
		Output = Sigmodial(Z.transpose()).transpose();
		if(nextLayer!=null){
			nextLayer.feedForward(Output);
		}
	}
	
	public void backpropogate(DoubleMatrix expectedOutput) {
		if(nextLayer==null){
			Delta = Output.sub(expectedOutput);
			CDelta = CDelta.add(Input.mmul(Delta.transpose()));
//			Delta.print();
			previousLayer.backpropogate(Delta);
			return;
		}

		//expected output is nothing but delta from nextLayer
		DoubleMatrix temp = DoubleMatrix.concatVertically(DoubleMatrix.ones(1),Output.mul(Output.mul(-1).add(1)));
		Delta = nextLayer.getTheta().mmul(expectedOutput).mul(temp);
//		removeBiasTerm(Delta).print();
		CDelta = CDelta.add(Input.mmul(removeBiasTerm(Delta).transpose()));
		if(previousLayer!=null)
			previousLayer.backpropogate(removeBiasTerm(Delta));
		return;
	}
	
	public void calculateGradient(int noOfTrainingSets){
		Gradient = CDelta.mul(1.0/noOfTrainingSets);
		if(nextLayer!=null)
			nextLayer.calculateGradient(noOfTrainingSets);
		return ;
	}
	
	public void updateWeights(){
		Theta = Theta.sub(Gradient.mul(DigitReader.LEARNING_RATE));
		if(nextLayer!=null){
			nextLayer.updateWeights();
		}
	}
	
	void outBiasTerms(){
		Theta.getRow(0).print();
	}
	
	public Layer getNextLayer() {
		return nextLayer;
	}
	public void setNextLayer(Layer nextLayer) {
		this.nextLayer = nextLayer;
	}
	
	static DoubleMatrix Sigmodial(DoubleMatrix row){
		DoubleMatrix temp  = new DoubleMatrix();
		temp.copy(row);
		for(int i=0;i<row.getLength();i++){
			temp.put(0,i,sigma(temp.get(0,i)));
		}
		return temp;
	}
	
	static double sigma(double x){
		x = 1/(1+Math.exp(-x));
		return x;
	}
	public void setPreviousLayer(Layer previousLayer){
		this.previousLayer = previousLayer;
	}
	
	public DoubleMatrix getDelta() {
		return Delta;
	}
	public DoubleMatrix getTheta() {
		return Theta;
	}
	public DoubleMatrix getOutput() {
		return Output;
	}
	
	private DoubleMatrix removeBiasTerm(DoubleMatrix vector){
		double[] vec = new double[vector.rows-1];
		for(int i=1;i < vector.rows;i++){
			vec[i-1] = vector.get(i, 0);
		}
		return new DoubleMatrix(vec);
	}
	
	public DoubleMatrix getCDelta(){
		return CDelta;
	}
	
	 public void save(){
		try {
			Theta.save(name+".yic");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(nextLayer!=null)
			nextLayer.save();
	}
	
	 public void load(){
			try {
				Theta.load(name+".yic");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(nextLayer!=null)
				nextLayer.load();
	}
		 
	 
	public void clearCDeltas(){
		CDelta = DoubleMatrix.zeros(nodesInLastLayer+1, noOfNodes);
		if(nextLayer!=null){
			nextLayer.clearCDeltas();
		}
	}
	
}
