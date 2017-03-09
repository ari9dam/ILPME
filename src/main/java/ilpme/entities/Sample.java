/**Example.java
 * 2:52:00 PM @author Arindam
 */
package ilpme.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import xhail.core.statements.Example;

/**
 * @author Arindam
 *
 */
public class Sample {
	/**
	 * The set of positive examples
	 */
	private Set<Example> positiveExamples;
	
	/**
	 * The set of positive examples
	 */
	private Set<Example> negativeExamples;
	
	/**
	 * The story (observation) from which conclusions aer drawn
	 */
	private Set<String> story;

	/**
	 * @return the positiveExamples
	 */
	public Set<Example> getPositiveExamples() {
		return positiveExamples;
	}

	/**
	 * @param positiveExamples the positiveExamples to set
	 */
	public void setPositiveExamples(Set<Example> positiveExamples) {
		this.positiveExamples = positiveExamples;
	}

	/**
	 * @return the negativeExamples
	 */
	public Set<Example> getNegativeExamples() {
		return negativeExamples;
	}

	/**
	 * @param negativeExamples the negativeExamples to set
	 */
	public void setNegativeExamples(Set<Example> negativeExamples) {
		this.negativeExamples = negativeExamples;
	}

	/**
	 * @return the story
	 */
	public Set<String> getStory() {
		return story;
	}

	/**
	 * @param story the story to set
	 */
	public void setStory(Set<String> story) {
		this.story = story;
	}

	public Sample(Set<Example> positiveExamples, Set<Example> negativeExamples, Set<String> story) {
		this.positiveExamples = positiveExamples;
		this.negativeExamples = negativeExamples;
		this.story = story;
	}

	public Sample() {
		positiveExamples = new LinkedHashSet<Example>();
		negativeExamples = new LinkedHashSet<Example>();
		story = new LinkedHashSet<String>();
	}
	
	
}
