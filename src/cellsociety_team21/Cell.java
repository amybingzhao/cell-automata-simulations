package cellsociety_team21;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;

import javafx.scene.Node;
// TODO: not sure if we want to extend Node or not? It makes it easier to position in the gridpane but also not sure
// what to do with the unimplemented methods..

public class Cell extends Node {
	private String myCurState;
	private String myNextState;
	private int myRow;
	private int myCol;
	
	public Cell(String initialState, int r, int c) {
		myCurState = initialState;
		myRow = r;
		myCol = c;
		myNextState = null;
	}
	
	public int getRow() {
		return myRow;
	}
	
	public void setRow(int r) {
		myRow = r;
	}
	
	public int getCol() {
		return myCol;
	}
	
	public void setCol(int c) {
		myCol = c;
	}
	
	public String getCurState() {
		return myCurState;
	}
	
	public void setCurState(String s) {
		myCurState = s;
	}
	
	public String getNextState() {
		return myNextState;
	}
	
	public void setNextState(String s) {
		myNextState = s;
	}

	@Override
	protected NGNode impl_createPeer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean impl_computeContains(double localX, double localY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

}
