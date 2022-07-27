package mltutorial ;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import mltutorial.exceptions.NullAncestorException;
import mltutorial.exceptions.TooManyNeighborsException;
import mltutorial.exceptions.UnknownNeighborException;

public class Node {
	public static final double MINIMAL_BRANCH_LENGTH = 0.0001;
	public static final double MAXIMAL_BRANCH_LENGTH = 5; //TODO : trouver une vraie approximation de la longueur de branche maximale
	
	public static enum Neighbor{A,B,C}
	
	protected Map<Neighbor, Node> neighbors = new EnumMap<Neighbor, Node>(Neighbor.class);
	protected Map<Neighbor, Double> branchLengths = new EnumMap<Neighbor, Double>(Neighbor.class);
	protected String label ;
	protected Neighbor ancestor ;

  public Node() {
    label = null ;
  }

  public Node(String label) {
    this.label = label ;
  }


  public Node(Node ancestor, double branchLength) {
  	if (branchLength <= 0) branchLength = MINIMAL_BRANCH_LENGTH;
  	if (branchLength > MAXIMAL_BRANCH_LENGTH) branchLength = MAXIMAL_BRANCH_LENGTH;
  	neighbors.put(Neighbor.A, ancestor);
  	branchLengths.put(Neighbor.A, branchLength);
    label = null ;
    this.ancestor = Neighbor.A;
  }
  
  public String toString(){
  	return label;
  }
  
  public Neighbor addNeighbor(Node node) throws TooManyNeighborsException {
  	if (!node.neighbors.containsKey(Neighbor.A)) {
  		node.neighbors.put(Neighbor.A, this);
  	}else if (!node.neighbors.containsKey(Neighbor.B)) {
  		node.neighbors.put(Neighbor.B, this);
  	}else if (!node.neighbors.containsKey(Neighbor.C)) {
  		node.neighbors.put(Neighbor.C, this);
  	}else {
  		throw new TooManyNeighborsException(node);
 		}
  	if (!neighbors.containsKey(Neighbor.A)) {
  		neighbors.put(Neighbor.A, node);
  		return Neighbor.A;
  	}else if (!neighbors.containsKey(Neighbor.B)) {
  		neighbors.put(Neighbor.B, node);
  		return Neighbor.B;
  	}else if (!neighbors.containsKey(Neighbor.C)) {
  		neighbors.put(Neighbor.C, node);
  		return Neighbor.C;
  	}else {
  		throw new TooManyNeighborsException(this);
 		}
  }
  
  /**
   * Add a neighbor to this, having already an orphan branch length that will be kept.
   * @param node
   * @return
   * @throws TooManyNeighborsException
   */
  public Neighbor addNeighborWithBranchLength(Node node) throws TooManyNeighborsException {
  	Neighbor keyInThis = addNeighbor(node);
  	Neighbor keyInNeighbor = getNeighborKey(keyInThis);
  	branchLengths.put(keyInThis, node.getBranchLength(keyInNeighbor));
  	return keyInThis;
  }
    
  /**
   * Remove branch between this node and given node, but keep the branch length in given node 
   * @param node
   */
  public void removeNeighborButKeepBranchLength(Node node) {
  	for (Entry<Neighbor, Node> e : neighbors.entrySet()){
  		if (e.getValue() == node){
  			Neighbor keyInNode = getNeighborKey(e.getKey());
  			node.neighbors.remove(keyInNode);
  			neighbors.remove(e.getKey());
  			branchLengths.remove(e.getKey());
  		}
  	}
  }
  
  public void removeAllNeighbors(){
  	neighbors.clear();
  	branchLengths.clear();
  	ancestor = null;
  }
  
  public void setNeighbor(Neighbor neighbor, Node node){
  	neighbors.put(neighbor, node);
  }
  
  /**
   * Replace a neighbor of this node by a new one. 
   * This method should only be used when you want to remove a node from the tree, 
   * because oldNeighbor will loose a neighbor that won't be replaced, creating an incoherence in the tree.
   * Attention : don't forget to check the branch length (for example, if you remove an abstract binary root, sum the 2 branch length).
   * @param oldNeighbor
   * @param newNeighbor
   */
  public Neighbor replaceNeighbor(Node oldNeighbor, Node newNeighbor) throws UnknownNeighborException {
  	for (Entry<Neighbor, Node> e : neighbors.entrySet()){
  		if (e.getValue() == oldNeighbor){
  			neighbors.put(e.getKey(), newNeighbor);
  			return e.getKey();
  		}
  	}
  	throw new UnknownNeighborException(this, oldNeighbor);
  }
  
  public boolean hasNeighbor(Neighbor neighbor) {
    return (neighbors.containsKey(neighbor));
  }

  public Node getNeighbor(Neighbor neighbor) {
  	return neighbors.get(neighbor);
  }
  
  /**
   * Return the key of this node in the given neighbor
   * @param neighbor the neighbor key in this node
   * @return the key of this node in the given neighbor
   */
  public Neighbor getNeighborKey(Neighbor neighbor) {
  	for (Entry<Neighbor, Node> e : neighbors.get(neighbor).neighbors.entrySet()){
  		if (e.getValue() == this) return e.getKey();
  	}
  	return null;
  }
  
  public Set<Node> getNeighborNodes(){
  	return new HashSet<Node>(neighbors.values());
  }
  
  public Set<Neighbor> getNeighborKeys(){
  	return new HashSet<Neighbor>(neighbors.keySet());
  }
  
  public void setToRoot(){
  	ancestor = null;
  	for (Node n : getNeighborNodes()) n.setAncestor(this);
  }
  
  public void setAncestor(Node node) {  	
  	for (Entry<Neighbor, Node> e : neighbors.entrySet()) 
  		if (e.getValue() != node) {
  			e.getValue().setAncestor(this);
  		}else{
  			ancestor = e.getKey();
  		}
  }
  
  public void removeAncestor() {
  	for (Node n : neighbors.values()) 
  		if (n != neighbors.get(ancestor)) n.removeAncestor();
  	ancestor = null;
  }
  
  public List<Node> getChildren() {
  	if (ancestor == null) return new ArrayList<Node>(neighbors.values());
  	List<Node> list = new ArrayList<Node>(neighbors.values());
  	list.remove(neighbors.get(ancestor));
  	return list;
  }
    
  public Neighbor getAncestorKey() throws NullAncestorException {
  	if (ancestor == null) 
  		throw new NullAncestorException(this);
  	return ancestor;
  }
  
  public Node getAncestorNode() throws NullAncestorException {
  	if (ancestor == null) throw new NullAncestorException(this); 
  	return neighbors.get(ancestor);
  }
  
  public double getAncestorBranchLength() throws NullAncestorException {
  	if (ancestor == null) throw new NullAncestorException(this);
  	if (branchLengths.containsKey(ancestor)) return branchLengths.get(ancestor);  	
  	return 0;
  }
  
  public void setBranchLength(Neighbor neighbor, double branchLength) {
  	if (branchLength < MINIMAL_BRANCH_LENGTH) branchLength = MINIMAL_BRANCH_LENGTH;
  	if (branchLength > MAXIMAL_BRANCH_LENGTH) branchLength = MAXIMAL_BRANCH_LENGTH;  	
  	branchLengths.put(neighbor, branchLength);
  	neighbors.get(neighbor).branchLengths.put(getNeighborKey(neighbor), branchLength);
  }
    
  public double getBranchLength(Neighbor neighbor){
  	if (branchLengths.containsKey(neighbor))
  		return branchLengths.get(neighbor);
  	else
  		return 0;
  }
  
  public void setLabel(String label){
  	this.label = label;
  }
  
  public String getLabel(){
  	return label;
  }
    
  public boolean isLeaf(){
  	return (neighbors.size() == 1);
  }
  
  public boolean isInode(){
  	return (neighbors.size() > 1);
  }
  
}

