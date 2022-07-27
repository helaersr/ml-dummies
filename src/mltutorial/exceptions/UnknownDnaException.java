package mltutorial.exceptions;

import java.util.BitSet;

public class UnknownDnaException extends Exception {

	public UnknownDnaException(BitSet bitset) {
		super("Unknown DNA token from BitSet: " + bitset);
	}

	public UnknownDnaException(BitSet bitset, Throwable cause) {
		super("Unknown DNA token from BitSet: " + bitset, cause);
	}

	public UnknownDnaException(String dna, String taxa) {
		super("Unknown DNA token: " + dna + " from taxa " + taxa);
	}
	
	public UnknownDnaException(String dna, String taxa, Throwable cause) {
		super("Unknown DNA token: " + dna + " from taxa " + taxa, cause);
	}
	
}
