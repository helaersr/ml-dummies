package mltutorial.exceptions;

import mltutorial.Node;

public class TooManyNeighborsException extends Exception {

	public TooManyNeighborsException(Node node) {
		super("Cannot add a new neighbor, node (" + node + ") has already 3 neighbors.");
	}

	public TooManyNeighborsException(Node node, Throwable cause) {
		super("Cannot add a new neighbor, node (" + node + ") has already 3 neighbors.", cause);
	}

}
