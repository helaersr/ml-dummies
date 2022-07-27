package mltutorial.exceptions;

import mltutorial.Node;

public class UnknownNeighborException extends Exception {

	public UnknownNeighborException(Node node, Node neighbor) {
		super("Node " + neighbor + " is not a neighbor of node " + node);
	}

	public UnknownNeighborException(Node node, Node neighbor, Throwable cause) {
		super("Node " + neighbor + " is not a neighbor of node " + node, cause);
	}

}
