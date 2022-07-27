package mltutorial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import mltutorial.Dataset.DNA;
import mltutorial.MLTutorial.EvaluationDistribution;
import mltutorial.MLTutorial.EvaluationModel;
import mltutorial.MLTutorial.RateParameter;
import mltutorial.exceptions.NullAncestorException;

import cern.jet.stat.Gamma;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;


/**
 * @author Raphael Helaers
 *
 */
public class Likelihood {
	public final int A = Dataset.A;
	public final int C = Dataset.C;
	public final int G = Dataset.G;
	public final int T = Dataset.T;
	
	private final int numStates = Dataset.numStates;
	
	//Parameters
	private final Dataset D;
	private final int numCharComp;
	private final EvaluationModel model;
	private final EvaluationDistribution distribution;
	
	//Tree and sequence data
	private final int numNodes;
	private final double[][][][] sequences; //binary sequences for each node
	private final Map<Node,Integer> nodeIndex; //node indice in sequences
	private Node root;
	
	//Likelihood computation
	private final double[] equiFreq; //equilibrium frequencies : proportion of each nucleotide in the partition
	private double GTRa,GTRb,GTRc,GTRd,GTRe; //GTR rate parameters. 
	private double kappa; //Transition rate parameter in K2P/HKY85
	private Matrix Q; //Instantaneous rate matrix
	private double scaling; //Scaling factor
	private EigenvalueDecomposition Rev; //Rate matrix eigen values and eigen vectors	
	private double likelihoodValue;
	
	//Rate heterogeneity
	private final int numCat;
	private double gammaShape; //Gamma shape parameter
	private final double[] cutpoints;
	private final double[] rates; 
	
	//Invariable sites
	private final double[] invariantSites;
	private double pInv;
	
	public Likelihood (Dataset dataset, EvaluationModel model, EvaluationDistribution distribution, 
			double distributionShape,	double pinv, Map<RateParameter,Double> rateParameters, 
			List<Node> tree, Node root, int numSubsets) {
		D = dataset;
		numCharComp = D.getNChar();
		this.model = model;
		this.distribution = distribution;
		this.pInv = pinv;
		numCat = (distribution == EvaluationDistribution.NONE) ? 1 : numSubsets;
		numNodes = tree.size();
		this.root = root;
		nodeIndex = new HashMap<Node, Integer>();
		//sequences = new double[numCat][numStates][numNodes][numCharComp];
		sequences = new double[numCat][numNodes][numStates][numCharComp];
		int nodeCounter = 0;
		equiFreq = new double[numStates];
		for (Node node : tree){
			nodeIndex.put(node, nodeCounter);
			if (node.isLeaf()){
				for (int k=0 ; k < numCharComp ; k++){
					DNA dna = D.getDNA(node.getLabel(), k);
					for (int cat=0 ; cat < numCat ; cat++){
						sequences[cat][nodeCounter][A][k] = (dna.isA()) ? 1 : 0;
						sequences[cat][nodeCounter][C][k] = (dna.isC()) ? 1 : 0;
						sequences[cat][nodeCounter][G][k] = (dna.isG()) ? 1 : 0;
						sequences[cat][nodeCounter][T][k] = (dna.isT()) ? 1 : 0;
					}
					if (model == EvaluationModel.HKY85 || model == EvaluationModel.GTR){
						equiFreq[A] += (dna.isA()) ? 1.0 / (double)dna.numOfStates() : 0;
						equiFreq[C] += (dna.isC()) ? 1.0 / (double)dna.numOfStates() : 0;
						equiFreq[G] += (dna.isG()) ? 1.0 / (double)dna.numOfStates() : 0;
						equiFreq[T] += (dna.isT()) ? 1.0 / (double)dna.numOfStates() : 0;
					}
				}	
			}
			nodeCounter++;
		}
		if (model == EvaluationModel.HKY85 || model == EvaluationModel.GTR){
			//HKY8, TN93 and GTR use a data estimated probability
			for (int i=0 ; i < equiFreq.length ; i++){
				equiFreq[i] /= D.getNChar()*D.getNTax();
			}
		}else{
			//Other model use an equal probability of 0.25 for each nucleotide
			for (int i=0 ; i < equiFreq.length ; i++){
				equiFreq[i] = 0.25;
			}
		}
		switch(model){
		case GTR:
			GTRa = rateParameters.get(RateParameter.A);
			GTRb = rateParameters.get(RateParameter.B);
			GTRc = rateParameters.get(RateParameter.C);
			GTRd = rateParameters.get(RateParameter.D);
			GTRe = rateParameters.get(RateParameter.E);
			break;
		case K2P:
		case HKY85:
			kappa = rateParameters.get(RateParameter.K);
			break;
		}
		cutpoints = new double[numCat+1];
		cutpoints[0] = 0;
		cutpoints[numCat] = 1000;
		rates = new double[numCat];
		if (numCat == 1) rates[0] = 1.0;
		Q = new Matrix(new double[numStates][numStates]);
		invariantSites = new double[numCharComp];
		for (int site=0 ; site < invariantSites.length ; site++){
			switch(D.getDNA(0, site)){
			case A:
				invariantSites[site] = equiFreq[A];
				break;
			case C:
				invariantSites[site] = equiFreq[C];
				break;
			case G:
				invariantSites[site] = equiFreq[G];
				break;
			case T:
				invariantSites[site] = equiFreq[T];
				break;
			default:
				invariantSites[site] = 0;
			}
			if (invariantSites[site] == 0) break;
			for (int taxa=1 ; taxa < D.getNTax() ; taxa++){
				if (D.getDNA(0, site) != D.getDNA(taxa, site)){
					invariantSites[site] = 0;
					break;
				}
			}			
		}
		if (distribution == EvaluationDistribution.GAMMA){
			updateGammaDistribution(distributionShape);
		}
		updateRateMatrix();
	}
	
	private void updateRateMatrix(){
		double alpha = 1.0;  
		double beta = 1.0;  
		double gamma = 1.0;  
		double delta = 1.0;  
		double epsilon = 1.0;  
		double eta = 1.0; 		
		switch (model) {
		case JC:
			alpha = beta = gamma = delta = epsilon = eta = 1.0;				
			break;
		case K2P:
			alpha = delta = gamma = eta = 1.0;
			beta = epsilon = kappa;				
			break;
		case HKY85:
			alpha = delta = gamma = eta = 1.0;
			beta = epsilon = kappa;
			break;
		case GTR:
			alpha = GTRa;
			beta = GTRb;
			gamma = GTRc;
			delta = GTRd;
			epsilon = GTRe;
			eta = 1.0;
			break;
		}
		//Setting Q values
		Q.set(A, C, alpha * equiFreq[C]);
		Q.set(A, G, beta * equiFreq[G]);
		Q.set(A, T, gamma * equiFreq[T]);
		Q.set(C, A, alpha * equiFreq[A]);
		Q.set(C, G, delta * equiFreq[G]);
		Q.set(C, T, epsilon * equiFreq[T]);
		Q.set(G, A, beta * equiFreq[A]);
		Q.set(G, C, delta * equiFreq[C]);
		Q.set(G, T, eta * equiFreq[T]);
		Q.set(T, A, gamma * equiFreq[A]);
		Q.set(T, C, epsilon * equiFreq[C]);
		Q.set(T, G, eta * equiFreq[G]);
		Q.set(A, A, -( Q.get(A, C) + Q.get(A, G) + Q.get(A, T) ));
		Q.set(C, C, -( Q.get(C, A) + Q.get(C, G) + Q.get(C, T) ));
		Q.set(G, G, -( Q.get(G, A) + Q.get(G, C) + Q.get(G, T) ));
		Q.set(T, T, -( Q.get(T, A) + Q.get(T, C) + Q.get(T, G) ));				
		//Scaling Q
		scaling = 0;
		for (int i=0 ; i < numStates ; i++){
			for (int j=0 ; j < numStates ; j++){
				if (i != j){
					scaling += equiFreq[i] * Q.get(i, j) ;
				}					
			}
		}
		scaling = (1.0/scaling);
		for (int i=0 ; i < numStates ; i++){
			for (int j=0 ; j < numStates ; j++){
				Q.set(i, j, Q.get(i, j) * scaling);
			}
		}
		//Computing eigen values and eigen vector
		if (model == EvaluationModel.GTR){
			Rev = Q.eig();
		}
	}
	
	public void updateRateParameter(RateParameter rateParameter, double newValue){
		switch(rateParameter){
		case A:
			GTRa = newValue;
			break;
		case B:
			GTRb = newValue;
			break;
		case C:
			GTRc = newValue;
			break;
		case D:
			GTRd = newValue;
			break;
		case E:
			GTRe = newValue;
			break;
		case K:
			kappa = newValue;
			break;			
		}
		updateRateMatrix();
	}
	
	public void updateGammaDistribution(double newAlpha){
		gammaShape = newAlpha;
		for (int i=1 ; i < numCat ; i++){
			double k = (double)i/(double)numCat;
			cutpoints[i] = Tools.percentagePointChi2(k,2*gammaShape) / (2*gammaShape);
		}		
		for (int i=0 ; i < numCat ; i++){
			rates[i] = ( Gamma.incompleteGamma(gammaShape+1,cutpoints[i+1]*gammaShape) - Gamma.incompleteGamma(gammaShape+1,cutpoints[i]*gammaShape)) / (1.0/(double)numCat);
		}
		updateRateMatrix();
	}
	
	public void updateInvariant(double newPinv){
		pInv = newPinv;
		if (pInv > 1.0) pInv = 1.0;
		else if (pInv < 0.0) pInv = 0.0;
	}
	
	public void setRoot(Node root){
		this.root = root;
	}
	
	public double getLikelihoodValue() {
		try{
			update(root);
		}catch (NullAncestorException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Cannot compute likelihood", JOptionPane.ERROR_MESSAGE);
			return 0;
		}
		return -likelihoodValue;
	}
	
	public EvaluationModel getModel(){
		return model;
	}
	
	public Map<RateParameter,Double> getRateParameters(){
		Map<RateParameter,Double> map = new HashMap<RateParameter,Double>();
		switch(model){
		case JC:
			break;
		case K2P:
		case HKY85:
			map.put(RateParameter.K, kappa);
			break;
		case GTR:
			map.put(RateParameter.A, GTRa);
			map.put(RateParameter.B, GTRb);
			map.put(RateParameter.C, GTRc);
			map.put(RateParameter.D, GTRd);
			map.put(RateParameter.E, GTRe);
			break;
		}
		return map;
	}
	
	public EvaluationDistribution getDistribution(){
		return distribution;
	}
	
	public int getDistributionSubsets(){
		return numCat;
	}
	
	public double getGammaShape(){
		return gammaShape;
	}
	
	public double getPInv(){
		return pInv;
	}
	
	public double getQ(int row, int col){
		return Q.get(row, col);
	}
	
	public double getScaling(){
		return scaling;
	}
	
	public double getGammaCutPoints(int cat1, int cat2){
		return cutpoints[cat2];
	}
	
	public double getGammaRate(int category){
		return rates[category];
	}
	
	public double getInvariantSite(int site){
		return invariantSites[site];
	}
	
	public double getConditionnalLikelihood(Node node, int category, int site, int base){
		return sequences[category][nodeIndex.get(node)][base][site];
	}
	
	public double getPij(int i, int j, double bl, int cat){
		bl /= (1.0-pInv);
		switch(model){
		case JC :
			double exp = Math.exp(-(bl) * scaling * rates[cat]);
			if (i==j){
				return 0.25 + (0.75 * exp);
			}else{
				return 0.25 - (0.25 * exp);
			}
		case K2P :
			double expa = Math.exp(-(bl) * scaling * rates[cat]);
			double expb = Math.exp(-(bl) * scaling * rates[cat] * ((kappa + 1) / 2));
			if (i == j){
				return 0.25 + (0.25 * expa) + (0.5 * expb);
			}else if ((i == A && j == G) ||	(i == G && j == A) ||
								(i == C && j == T) ||	(i == T && j == C) ){
				return 0.25 + (0.25 * expa) - (0.5 * expb);
			}else{
				return 0.25 - (0.25 * expa);
			}			
		case HKY85 :
			double[] PIj = new double[numStates];
			//Define PIj : sum frequency of A & G if j is purine, C & T if j is pyrimidine
			PIj[A] = PIj[G] = equiFreq[A] + equiFreq[G];
			PIj[C] = PIj[T] = equiFreq[C] + equiFreq[T];
			double expA = Math.exp(-(bl) * scaling * rates[cat]);
			double expB = Math.exp(-(bl) * scaling * rates[cat] * (1.0 + PIj[j] * (kappa - 1.0)));
			if (i == j){
				return equiFreq[j] + equiFreq[j] * ((1.0/PIj[j])-1.0) * expA +	((PIj[j]-equiFreq[j])/PIj[j]) * expB;
			}else if ((i == A && j == G) ||	(i == G && j == A) ||
					(i == C && j == T) ||	(i == T && j == C) ){
				return equiFreq[j] + equiFreq[j] * ((1.0/PIj[j])-1.0) * expA -	(equiFreq[j]/PIj[j]) * expB;
			}else{
				return equiFreq[j] * (1.0 - expA);
			}
		case GTR :
			Matrix TPM1;
			if (!Tools.isIdentity(Q)){
				Matrix temp = new Matrix(numStates,numStates);
				for (int k=0 ; k < numStates ; k++) {
					temp.set(k,k, Math.exp(bl * rates[cat] * Rev.getD().get(k,k)));
				}
				TPM1 = Rev.getV().times(temp).times(Rev.getV().inverse());			
			}else{
				TPM1 = Q;
			}			
			return TPM1.getArray()[i][j];
		default: 
			return -1;
		}		
	}
	
	public String getLambaVConditionalLikelihood(int site, int base){
		StringBuilder res = new StringBuilder();
		for (int cat = 0 ; cat < numCat ; cat++){
			res.append(Tools.doubletoString(sequences[cat][nodeIndex.get(root)][base][site], 4));
			if (cat < numCat-1) res.append(" + ");
		}		
		return res.toString();
	}
	
	public double getVariableLikelihood(int site){
		double siteLikelihoodVar = 0;
		for (int base = 0 ; base < numStates ; base++){
			for (int cat = 0 ; cat < numCat ; cat++){
				siteLikelihoodVar += sequences[cat][nodeIndex.get(root)][base][site] * equiFreq[base] / ((double)(numCat));						
			}
		}			
		if (siteLikelihoodVar < 0){
			siteLikelihoodVar = 4.9E-324;
		}
		return siteLikelihoodVar;
	}
	
	public double getLikelihood(int site){
		double siteLikelihoodVar = 0;
		for (int base = 0 ; base < numStates ; base++){
			for (int cat = 0 ; cat < numCat ; cat++){
				siteLikelihoodVar += sequences[cat][nodeIndex.get(root)][base][site] * equiFreq[base] * ((1.0-pInv)/(double)(numCat));						
			}
		}			
		if (siteLikelihoodVar < 0){
			siteLikelihoodVar = 4.9E-324;
		}
		double siteLikelihoodInv = invariantSites[site] * pInv;
		return Math.log(siteLikelihoodVar+siteLikelihoodInv);		
	}
	
	public void update(Node node) throws NullAncestorException {
		for (Node child : node.getChildren()){
			if(child.isInode()) update(child);
		}
		switch(model){
		case JC :
			computeJC(node);
			break;
		case K2P :
			computeK2P(node);
			break;
		case HKY85 :
			computeHKY85(node);
			break;
		case GTR :
			computeGTR(node);
			break;
		}
		if (root == node){
			likelihoodValue = 0;
			int n = nodeIndex.get(node);
			for (int site = 0 ; site < numCharComp ; site++){
				double siteLikelihoodVar = 0;
				for (int base = 0 ; base < numStates ; base++){
					for (int cat = 0 ; cat < numCat ; cat++){
						siteLikelihoodVar += sequences[cat][n][base][site] * equiFreq[base] * ((1.0-pInv)/(double)(numCat));						
					}
				}			
				if (siteLikelihoodVar < 0){
					siteLikelihoodVar = 4.9E-324;
				}
				double siteLikelihoodInv = invariantSites[site] * pInv;
				likelihoodValue += Math.log(siteLikelihoodVar+siteLikelihoodInv);
			}
		}
	}
	
	/**
	 * JC partial likelihood computation, using direct analytical calculation.
	 * @param node
	 * @throws NullAncestorException 
	 */
	private void computeJC(Node node) throws NullAncestorException {
		List<Node> children = node.getChildren();		
		Node node1 = children.get(0);
		Node node2 = children.get(1);
		for (int count=1 ; count < children.size() ; count++){
			double bl1,bl2;
			if (count > 1){
				node1 = children.get(count);
				node2 = node;
				bl1 = node1.getAncestorBranchLength() / (1.0-pInv);
				bl2 = 0;
			}else{
				bl1 = node1.getAncestorBranchLength() / (1.0-pInv);
				bl2 = node2.getAncestorBranchLength() / (1.0-pInv);				
			}
			int n = nodeIndex.get(node);
			int n1 = nodeIndex.get(node1);
			int n2 = nodeIndex.get(node2);
			for (int cat=0 ; cat < numCat ; cat++){
				double exp1 = Math.exp(-(bl1) * scaling * rates[cat]);
				double exp2 = Math.exp(-(bl2) * scaling * rates[cat]);
				double diag1  = 0.25 + (0.75 * exp1);
				double offdiag1 = 0.25 - (0.25 * exp1);
				double diag2  = 0.25 + (0.75 * exp2);
				double offdiag2 = 0.25 - (0.25 * exp2);
				double[][] seq1 = sequences[cat][n1];
				double[][] seq2 = sequences[cat][n2];
				double[][] seq = sequences[cat][n];
				for (int site=0 ; site < numCharComp ; site++) {
					for (int base=0 ; base < numStates ; base++) {	
						double sum1=0, sum2=0;
						switch(base) {
						case A :
							sum1 += seq1[A][site] * diag1;
							sum2 += seq2[A][site] * diag2;
							sum1 += seq1[C][site] * offdiag1;
							sum2 += seq2[C][site] * offdiag2;
							sum1 += seq1[G][site] * offdiag1;
							sum2 += seq2[G][site] * offdiag2;
							sum1 += seq1[T][site] * offdiag1;
							sum2 += seq2[T][site] * offdiag2;
							break;
						case C :
							sum1 += seq1[A][site] * offdiag1;
							sum2 += seq2[A][site] * offdiag2;
							sum1 += seq1[C][site] * diag1;
							sum2 += seq2[C][site] * diag2;
							sum1 += seq1[G][site] * offdiag1;
							sum2 += seq2[G][site] * offdiag2;
							sum1 += seq1[T][site] * offdiag1;
							sum2 += seq2[T][site] * offdiag2;
							break;
						case G :
							sum1 += seq1[A][site] * offdiag1;
							sum2 += seq2[A][site] * offdiag2;
							sum1 += seq1[C][site] * offdiag1;
							sum2 += seq2[C][site] * offdiag2;
							sum1 += seq1[G][site] * diag1;
							sum2 += seq2[G][site] * diag2;
							sum1 += seq1[T][site] * offdiag1;
							sum2 += seq2[T][site] * offdiag2;
							break;
						case T :
							sum1 += seq1[A][site] * offdiag1;
							sum2 += seq2[A][site] * offdiag2;
							sum1 += seq1[C][site] * offdiag1;
							sum2 += seq2[C][site] * offdiag2;
							sum1 += seq1[G][site] * offdiag1;
							sum2 += seq2[G][site] * offdiag2;
							sum1 += seq1[T][site] * diag1;
							sum2 += seq2[T][site] * diag2;
							break;
						}
						seq[base][site] = sum1 * sum2;
					}
				}
			}
		}
	}

	/**
	 * K2P partial likelihood computation, using direct analytical calculation.
	 * @param node
	 * @throws NullAncestorException 
	 */
	private void computeK2P(Node node) throws NullAncestorException {
		List<Node> children = node.getChildren();
		Node node1 = children.get(0);
		Node node2 = children.get(1);
		for (int count=1 ; count < children.size() ; count++){
			double bl1,bl2;
			if (count > 1){
				node1 = children.get(count);
				node2 = node;
				bl1 = node1.getAncestorBranchLength() / (1.0-pInv);
				bl2 = 0;
			}else{
				bl1 = node1.getAncestorBranchLength() / (1.0-pInv);
				bl2 = node2.getAncestorBranchLength() / (1.0-pInv);				
			}
			int n = nodeIndex.get(node);
			int n1 = nodeIndex.get(node1);
			int n2 = nodeIndex.get(node2);
			for (int cat=0 ; cat < numCat ; cat++){
				double exp1a = Math.exp(-(bl1) * scaling * rates[cat]);
				double exp2a = Math.exp(-(bl2) * scaling * rates[cat]);
				double exp1b = Math.exp(-(bl1) * scaling * rates[cat] * ((kappa + 1) / 2));
				double exp2b = Math.exp(-(bl2) * scaling * rates[cat] * ((kappa + 1) / 2));
				double diag1  = 0.25 + (0.25 * exp1a) + (0.5 * exp1b);
				double diag2  = 0.25 + (0.25 * exp2a) + (0.5 * exp2b);
				double ti1 = 0.25 + (0.25 * exp1a) - (0.5 * exp1b);
				double ti2 = 0.25 + (0.25 * exp2a) - (0.5 * exp2b);;
				double tv1 = 0.25 - (0.25 * exp1a);
				double tv2 = 0.25 - (0.25 * exp2a);	
				double[][] seq1 = sequences[cat][n1];
				double[][] seq2 = sequences[cat][n2];
				double[][] seq = sequences[cat][n];
				for (int site=0 ; site < numCharComp ; site++) {
					for (int base=0 ; base < numStates ; base++) {				
						double sum1=0, sum2=0;
						switch(base) {
						case A :
							sum1 += seq1[A][site] * diag1;
							sum2 += seq2[A][site] * diag2;
							sum1 += seq1[C][site] * tv1;
							sum2 += seq2[C][site] * tv2;
							sum1 += seq1[G][site] * ti1;
							sum2 += seq2[G][site] * ti2;
							sum1 += seq1[T][site] * tv1;
							sum2 += seq2[T][site] * tv2;
							break;
						case C :
							sum1 += seq1[A][site] * tv1;
							sum2 += seq2[A][site] * tv2;
							sum1 += seq1[C][site] * diag1;
							sum2 += seq2[C][site] * diag2;
							sum1 += seq1[G][site] * tv1;
							sum2 += seq2[G][site] * tv2;
							sum1 += seq1[T][site] * ti1;
							sum2 += seq2[T][site] * ti2;
							break;
						case G :
							sum1 += seq1[A][site] * ti1;
							sum2 += seq2[A][site] * ti2;
							sum1 += seq1[C][site] * tv1;
							sum2 += seq2[C][site] * tv2;
							sum1 += seq1[G][site] * diag1;
							sum2 += seq2[G][site] * diag2;
							sum1 += seq1[T][site] * tv1;
							sum2 += seq2[T][site] * tv2;
							break;
						case T :
							sum1 += seq1[A][site] * tv1;
							sum2 += seq2[A][site] * tv2;
							sum1 += seq1[C][site] * ti1;
							sum2 += seq2[C][site] * ti2;
							sum1 += seq1[G][site] * tv1;
							sum2 += seq2[G][site] * tv2;
							sum1 += seq1[T][site] * diag1;
							sum2 += seq2[T][site] * diag2;
							break;
						}
						seq[base][site] = sum1 * sum2;
					}
				}
			}
		}
	}
	
	/**
	 * HKY85 partial likelihood computation, using direct analytical calculation.
	 * @param node
	 * @throws NullAncestorException 
	 */
	private void computeHKY85(Node node) throws NullAncestorException {
		List<Node> children = node.getChildren();
		Node node1 = children.get(0);
		Node node2 = children.get(1);
		for (int count=1 ; count < children.size() ; count++){
			double[] bl = new double[2];
			double[] PIj = new double[numStates];
			double[][] expA = new double[2][numStates];
			double[][] expB = new double[2][numStates];
			double[][] diag = new double[2][numStates];
			double[][] ti = new double[2][numStates];
			double[][] tv = new double[2][numStates];
			//Get branch length of each sequence
			if (count > 1){
				node1 = children.get(count);
				node2 = node;
				bl[0] = node1.getAncestorBranchLength() / (1.0-pInv);
				bl[1] = 0;
			}else{
				bl[0] = node1.getAncestorBranchLength() / (1.0-pInv);
				bl[1] = node2.getAncestorBranchLength() / (1.0-pInv);				
			}
			//Define PIj : sum frequency of A & G if j is purine, C & T if j is pyrimidine
			PIj[A] = PIj[G] = equiFreq[A] + equiFreq[G];
			PIj[C] = PIj[T] = equiFreq[C] + equiFreq[T];
			int n = nodeIndex.get(node);
			int n1 = nodeIndex.get(node1);
			int n2 = nodeIndex.get(node2);
			for (int cat=0 ; cat < numCat ; cat++){
				//Define the 2 exponentials used in formulae.
				for (int seq=0 ; seq < 2 ; seq++){
					for (int base=0 ; base < numStates ; base++){
						expA[seq][base] = Math.exp(-(bl[seq]) * scaling * rates[cat]);
						expB[seq][base] = Math.exp(-(bl[seq]) * scaling * rates[cat] * (1.0 + PIj[base] * (kappa - 1.0)));
					}
				}
				//Define formulae, for diagonal, transitions and transversions
				for (int seq=0 ; seq < 2 ; seq++){
					for (int base=0 ; base < numStates ; base++){
						diag[seq][base] = equiFreq[base] + equiFreq[base] * ((1.0/PIj[base])-1.0) * expA[seq][base] +	((PIj[base]-equiFreq[base])/PIj[base]) * expB[seq][base];
						ti[seq][base] = 	equiFreq[base] + equiFreq[base] * ((1.0/PIj[base])-1.0) * expA[seq][base] -	(equiFreq[base]/PIj[base]) * expB[seq][base];
						tv[seq][base] = 	equiFreq[base] * (1.0 - expA[seq][base]);					
					}
				}
				double[][] seq1 = sequences[cat][n1];
				double[][] seq2 = sequences[cat][n2];
				double[][] seq = sequences[cat][n];
				for (int site=0 ; site < numCharComp ; site++) {
					for (int base=0 ; base < numStates ; base++) {				
						double sum1=0, sum2=0;
						switch(base) {
						case A :
							sum1 += seq1[A][site] * diag[0][A];
							sum2 += seq2[A][site] * diag[1][A];
							sum1 += seq1[C][site] * tv[0][C];
							sum2 += seq2[C][site] * tv[1][C];
							sum1 += seq1[G][site] * ti[0][G];
							sum2 += seq2[G][site] * ti[1][G];
							sum1 += seq1[T][site] * tv[0][T];
							sum2 += seq2[T][site] * tv[1][T];
							break;
						case C :
							sum1 += seq1[A][site] * tv[0][A];
							sum2 += seq2[A][site] * tv[1][A];
							sum1 += seq1[C][site] * diag[0][C];
							sum2 += seq2[C][site] * diag[1][C];
							sum1 += seq1[G][site] * tv[0][G];
							sum2 += seq2[G][site] * tv[1][G];
							sum1 += seq1[T][site] * ti[0][T];
							sum2 += seq2[T][site] * ti[1][T];
							break;
						case G :
							sum1 += seq1[A][site] * ti[0][A];
							sum2 += seq2[A][site] * ti[1][A];
							sum1 += seq1[C][site] * tv[0][C];
							sum2 += seq2[C][site] * tv[1][C];
							sum1 += seq1[G][site] * diag[0][G];
							sum2 += seq2[G][site] * diag[1][G];
							sum1 += seq1[T][site] * tv[0][T];
							sum2 += seq2[T][site] * tv[1][T];
							break;
						case T :
							sum1 += seq1[A][site] * tv[0][A];
							sum2 += seq2[A][site] * tv[1][A];
							sum1 += seq1[C][site] * ti[0][C];
							sum2 += seq2[C][site] * ti[1][C];
							sum1 += seq1[G][site] * tv[0][G];
							sum2 += seq2[G][site] * tv[1][G];
							sum1 += seq1[T][site] * diag[0][T];
							sum2 += seq2[T][site] * diag[1][T];
							break;
						}
						seq[base][site] = sum1 * sum2;
					}
				}
			}
		}
	}
	
	/**
	 * GTR partial likelihood computation, by decomposing the (scaled) instantaneous rate matrix into its eigenvalues and eigenvectors.
	 * @param node
	 * @throws NullAncestorException
	 */
	private void computeGTR(Node node) throws NullAncestorException {
		//TODO : Voir si on peut récupérer la version CPM --> test des valeurs propres négatives. A priori c'était lié au code pour 'R for Inode', en pratique pas de R du tout ...
		List<Node> children = node.getChildren();
		Node node1 = children.get(0);
		Node node2 = children.get(1);
		for (int count=1 ; count < children.size() ; count++){
			double bl1,bl2;
			if (count > 1){
				node1 = children.get(count);
				node2 = node;
				bl1 = node1.getAncestorBranchLength() / (1.0-pInv);
				bl2 = 0;
			}else{
				bl1 = node1.getAncestorBranchLength() / (1.0-pInv);
				bl2 = node2.getAncestorBranchLength() / (1.0-pInv);				
			}
			int n = nodeIndex.get(node);
			int n1 = nodeIndex.get(node1);
			int n2 = nodeIndex.get(node2);
			for (int cat=0 ; cat < numCat ; cat++){
				Matrix TPM1;
				Matrix TPM2;
				if (!Tools.isIdentity(Q)){
					Matrix temp = new Matrix(numStates,numStates);
					for (int i=0 ; i < numStates ; i++) {
						temp.set(i,i, Math.exp(bl1 * rates[cat] * Rev.getD().get(i,i)));
					}
					TPM1 = Rev.getV().times(temp).times(Rev.getV().inverse());			
					for (int i=0 ; i < numStates ; i++) {
						temp.set(i,i, Math.exp(bl2 * rates[cat] * Rev.getD().get(i,i)));
					}
					TPM2 = Rev.getV().times(temp).times(Rev.getV().inverse());			
				}else{
					TPM1 = Q;
					TPM2 = Q;
				}			
				double[][] seq1 = sequences[cat][n1];
				double[][] seq2 = sequences[cat][n2];
				double[][] seq = sequences[cat][n];
				double[][] tpm1 = TPM1.getArray();
				double[][] tpm2 = TPM2.getArray();
				for (int site=0 ; site < numCharComp ; site++) {
					for (int base=0 ; base < numStates ; base++) {				
						double sum1=0, sum2=0;
						sum1 += seq1[A][site] * tpm1[base][A];
						sum2 += seq2[A][site] * tpm2[base][A];
						sum1 += seq1[C][site] * tpm1[base][C];
						sum2 += seq2[C][site] * tpm2[base][C];
						sum1 += seq1[G][site] * tpm1[base][G];
						sum2 += seq2[G][site] * tpm2[base][G];
						sum1 += seq1[T][site] * tpm1[base][T];
						sum2 += seq2[T][site] * tpm2[base][T];
						seq[base][site] = sum1 * sum2;
					}
				}
			}
		}
	}

}
