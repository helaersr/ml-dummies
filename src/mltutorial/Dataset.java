package mltutorial;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mltutorial.exceptions.UnknownDnaException;


public class Dataset {
	public final static int numStates = 4;	
	public final static int A = 0;
	public final static int C = 1;
	public final static int G = 2;
	public final static int T = 3;
	
	public enum DNA {
		A (true, false, false, false),
		C (false, true, false, false),
		G (false, false, true, false),
		T (false, false, false, true),
		R (true, false, true, false),
		Y (false, true, false, true),
		W (true, false, false, true),
		S (false, true, true, false),
		M (true, true, false, false),
		K (false, false, true, true),
		B (false, true, true, true),
		D (true, false, true, true),
		H (true, true, false, true),
		V (true, true, true, false),
		N (true, true, true, true);
		
		private final BitSet bits;
		
		DNA(boolean A, boolean C, boolean G, boolean T){
			bits = new BitSet(4);
			bits.set(0, A);
			bits.set(1, C);
			bits.set(2, G);
			bits.set(3, T);
		}
		
		public String toString() {return this.name();}
		
		final public BitSet toBits() {return bits;}		
		
		final public int numOfStates() {return bits.cardinality();}
		final public boolean isA() {return bits.get(Dataset.A);}
		final public boolean isC() {return bits.get(Dataset.C);}
		final public boolean isG() {return bits.get(Dataset.G);}
		final public boolean isT() {return bits.get(Dataset.T);}
		
		/**
		 * Convert a BitSet to a DNA.
		 * @param bitSet
		 * @return
		 * @throws UnknownDnaException
		 */
		public static DNA getDNA(BitSet bitSet) throws UnknownDnaException {
			for (DNA dna : DNA.values()){
				if (dna.bits.equals(bitSet)){
					return dna;
				}
			}
			throw new UnknownDnaException(bitSet);
		}
	}
	
	
	private final DNA[][] data;
	private final Map<String,Integer> taxas;
	private final int nchar;
	
	public Dataset(String[] sequences){
		nchar = sequences[0].length();
		data = new DNA[sequences.length][nchar];
		taxas = new TreeMap<String,Integer>();
		int i=0;
		for (String sequence : sequences){
			taxas.put((i+1)+"",i);
			int j=0;
			for (char c : sequence.toCharArray()){
				data[i][j] = DNA.valueOf((""+c).toUpperCase());
				j++;
			}
			i++;
		}		
	}
	
	public void changeNucleotide(int taxa, int site, String newDNA){
		data[taxa][site] = DNA.valueOf(newDNA);
	}
	
	public List<String> getTaxas(){
		return new ArrayList<String>(taxas.keySet());
	}
	
	public int getNTax(){
		return taxas.size();
	}

	/**
	 * The real number of characters in this partition, before the compression.
	 * To use for computing likelihood for example.
	 * @return
	 */
	public int getNChar(){
		return nchar;
	}
	
	public DNA getDNA(String taxa, int position){
		return data[taxas.get(taxa)][position];
	}
	
	public DNA getDNA(int taxaIndex, int position){
		return data[taxaIndex][position];
	}
	
	public List<DNA> getAllDNA(String taxa){
		int t = taxas.get(taxa);
		List<DNA> dna = new ArrayList<DNA>();
		for (int s=0 ; s < nchar ; s++){
			dna.add(data[t][s]);
		}
		return dna;
	}
	
	public double[] getNucleotideFrequencies(){
		double[] frequencies = new double[numStates];
		for (DNA[] sequence : data){
			for (DNA dna : sequence){
				frequencies[A] += (dna.isA()) ? 1.0 / (double)dna.numOfStates() : 0;
				frequencies[C] += (dna.isC()) ? 1.0 / (double)dna.numOfStates() : 0;
				frequencies[G] += (dna.isG()) ? 1.0 / (double)dna.numOfStates() : 0;
				frequencies[T] += (dna.isT()) ? 1.0 / (double)dna.numOfStates() : 0;
			}	
		}
		for (int i=0 ; i < frequencies.length ; i++){
			frequencies[i] /= getNChar()*getNTax();
		}
		return frequencies;
	}

	public String getNucleotideFrequenciesToString(){
		double[] freq = getNucleotideFrequencies();
		String s = "";
		s += "A[" + Tools.doubleToPercent(freq[A], 2) + "], ";
		s += "C[" + Tools.doubleToPercent(freq[C], 2) + "], ";
		s += "G[" + Tools.doubleToPercent(freq[G], 2) + "], ";
		s += "T[" + Tools.doubleToPercent(freq[T], 2) + "]";
		return s;
	}

}
