/**Example.java
 * 2:52:00 PM @author Arindam
 */
package ilpme.entities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ilpme.xhail.core.statements.Example;
import ilpme.xhail.core.parser.InputStates;
import ilpme.xhail.core.parser.Splitter;
import ilpme.core.Logger;
import ilpme.xhail.core.parser.Parser;
import ilpme.xhail.core.statements.Display;

/**
 * @author Arindam
 * The data structure to hold an annotated sample 
 * in the training data.
 * The training data is a collection of such samples.
 */
public class Sample {
	
	/***
	 * The properties of a Sample Observation
	 */
	private List<String> story;

	private List<Display> displays ;

	private List<String> domains ;

	private List<Example> examples ;
	
	private String name="NONAME";
	
	
	public Sample(Path source){
		/**
		 * Read from file 
		 * categorize and add to bucket
		 */
		this.story  = new LinkedList<String>() ;
		this.displays = new LinkedList<Display>() ;
		this.domains = new LinkedList<String>();
		this.examples = new LinkedList<Example>();		
		this.name = source.getFileName().toString();
		parse(source);
	}
	
	public void addBackground(String statement) {
		if (null != statement) {
			statement = statement.trim();
			if (statement.startsWith("#compute"))
				Logger.warning(false, "'#compute' statements are not supported and will be ignored");
			else if (statement.startsWith("#hide"))
				Logger.warning(false, "'#hide' statements are not supported and will be ignored");
			else if (statement.startsWith("#show"))
				Logger.warning(false, "'#show' statements are not supported and will be ignored");
			else if (statement.startsWith("#display") && statement.endsWith("."))
				addDisplay(Parser.parseDisplay(statement.substring("#display".length(), statement.length() - 1).trim()));
			else if (statement.startsWith("#example") && statement.endsWith("."))
				addExample(Parser.parseExample(statement.substring("#example".length(), statement.length() - 1).trim()));
			else if (statement.startsWith("#modeb") && statement.endsWith("."))
				Logger.warning(false, "'#modeb' statements are not supported in sample files and will be ignored");
			else if (statement.startsWith("#modeh") && statement.endsWith("."))
				Logger.warning(false, "'#modeh' statements are not supported in sample files and will be ignored");
			else if (statement.startsWith("#domain"))
				domains.add(statement);
			else
				story.add(statement);
		}
	}
	
	public void addDisplay(Display display) {
		if (null == display)
			throw new IllegalArgumentException("Illegal 'display' argument in Problem.Builder.addDisplay(Display): " + display);
		this.displays.add(display);
	}

	public void addExample(Example example) {
		if (null != example)
			examples.add(example);
	}
	
	public void parse(InputStream stream) {
		if (null == stream)
			throw new IllegalArgumentException("Illegal 'stream' argument in Problem.Builder.parse(InputStream): " + stream);
		for (String statement : new Splitter(InputStates.INITIAL).parse(stream))
			addBackground(statement);
	}

	public void parse(Path path) {
		if (null == path)
			throw new IllegalArgumentException("Illegal 'path' argument in Problem.Builder.parse(Path): " + path);
		try {
			parse(new FileInputStream(path.toFile()));
		} catch (FileNotFoundException e) {
			Logger.error("cannot find file '" + path.getFileName().toString() + "'");
		}
		
	}

	public List<String> getStory() {
		return story;
	}

	public List<Display> getDisplays() {
		return displays;
	}

	public List<String> getDomains() {
		return domains;
	}

	public List<Example> getExamples() {
		return examples;
	}

	public String toASPString() {
		String obs = "%story\n "+ StringUtils.join(this.story,"\n")+"\n%examples\n";
		for (Example example :this.getExamples())
			for (String statement : example.asClauses())
				obs+=statement+"\n";
		return obs;
	}
	
	public String toASPStringForSatisfiability() {
		String obs = "%story\n "+ StringUtils.join(this.story,"\n")+"\n%examples\n";
		for (Example example :this.getExamples())
			obs+=example.asClauses()[2]+"\n";
		return obs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Sample [ name=" + name+", story=" + story + ", displays=" + displays + ", domains=" + domains + ", examples=" + examples
				 + "]";
	}
	
}
