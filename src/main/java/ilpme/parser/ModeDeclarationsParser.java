package ilpme.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ilpme.core.Logger;
import ilpme.xhail.core.parser.InputStates;
import ilpme.xhail.core.parser.Parser;
import ilpme.xhail.core.parser.Splitter;
import ilpme.xhail.core.statements.ModeB;
import ilpme.xhail.core.statements.ModeH;

public class ModeDeclarationsParser {
	public ModeDeclarationsParser(){
		
	}
	
	public Map<ModeH,List<ModeB>> parse(Path src){
		List<ModeB> modebs = new LinkedList<ModeB>();
		List<ModeH> modehs = new LinkedList<ModeH>();
		
		Map<ModeH,List<ModeB>> declarations = new HashMap<ModeH,List<ModeB>>();
		
		if (null == src)
			throw new IllegalArgumentException("Illegal 'path' argument in Problem.Builder.parse(Path): " + src);
		try {
			for (String statement : new Splitter(InputStates.INITIAL).parse(new FileInputStream(src.toFile()))){
				if (null != statement) {
					statement = statement.trim();
					if (statement.startsWith("#modeb") && statement.endsWith("."))
						modebs.add(Parser.parseModeB(statement.substring("#modeb".length(), statement.length() - 1).trim()));
					else if (statement.startsWith("#modeh") && statement.endsWith("."))
						modehs.add(Parser.parseModeH(statement.substring("#modeh".length(), statement.length() - 1).trim()));
				}
			}
			
			for(ModeH modeh: modehs){
				declarations.put(modeh, modebs);
			}
			
			if(modehs.isEmpty()){
				Logger.warning(false, "No mode declarations are found in "+ src.getFileName());
			}
			
		} catch (FileNotFoundException e) {
			Logger.error("cannot find file '" + src.getFileName().toString() + "'");
		}
		
		return declarations;
	}
}
