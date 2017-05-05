package ilpme.entities;
/**
 * 
 * @author Arindam
 * Each partial hypothesis is a collection of normal clauses
 * Two partial hypothesis can be combined
 * Each partial hypothesis can be refined
 */
public class PartialHypotheis {
	private int index;  //The solution to the problems E{1}...E{index}
	private LogicProgram top; //the root of the partial hypothesis
	private LogicProgram bottom; //the root of the partial hypothesis
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public LogicProgram getTop() {
		return top;
	}
	public void setTop(LogicProgram top) {
		this.top = top;
	}
	public LogicProgram getBottom() {
		return bottom;
	}
	public void setBottom(LogicProgram bottom) {
		this.bottom = bottom;
	}
		
}
