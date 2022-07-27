package mltutorial.exceptions;

import mltutorial.Node;

public class NullAncestorException extends Exception {

	public NullAncestorException(Node node) {
		super("Node " + node + " is the root or tree is unrooted, and ancestors are not defined");
	}

	public NullAncestorException(Node node, Throwable cause) {
		super("Node " + node + " is the root or tree is unrooted, and ancestors are not defined", cause);
	}

}
