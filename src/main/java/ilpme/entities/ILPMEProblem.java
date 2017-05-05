package ilpme.entities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FileUtils;

import ilpme.core.Config;
import ilpme.core.Logger;
import ilpme.parser.ModeDeclarationsParser;

public class ILPMEProblem {
	private  List<Sample> trainingData;
	private String background;
	private ModeDeclarations modeDeclarations;
	private int maxAddition;
	private Config config;
	
	public ILPMEProblem(Config config) throws IOException {
		this.config = config;
		this.modeDeclarations = new ModeDeclarations();
		
		ModeDeclarationsParser modeParser = new ModeDeclarationsParser();
		
		Path src = config.getSource();
		for(File file: src.toFile().listFiles()){
			String extension = file.getName().substring(file.getName().lastIndexOf('.')+1);
			
			if("bk".equalsIgnoreCase(extension)){
				//read the background knowledge and store it
				String bk = FileUtils.readFileToString(file, Charset.defaultCharset());
				this.background+=bk;
			}else if("m".equalsIgnoreCase(extension)){
				//read the mode declarations and store it
				this.modeDeclarations.mergeAll(modeParser.parse(file.toPath()));
			}else if("sample".equalsIgnoreCase(extension)){
				//read the training samples and store it
				Sample sample = new Sample(file.toPath());
				this.trainingData.add(sample);
			}else{
				Logger.warning(false, "Ignoring file with unknown extension "+ file.getName());
			}
		}	
	}
	
	public List<Sample> getTrainingData() {
		return trainingData;
	}
	public String getBackground() {
		return background;
	}
	public ModeDeclarations getModeDeclarations() {
		return modeDeclarations;
	}
	public int getMaxAddition() {
		return maxAddition;
	}
	public void setBlind(boolean b) {
		// TODO Auto-generated method stub
		
	}
	public void setClasp(String string) {
		// TODO Auto-generated method stub
		
	}
	public void setDebug(boolean b) {
		// TODO Auto-generated method stub
		
	}
	public void setFull(boolean b) {
		// TODO Auto-generated method stub
		
	}
	public void setGringo(String string) {
		// TODO Auto-generated method stub
		
	}
	public void setIterations(String string) {
		// TODO Auto-generated method stub
		
	}
	public void setKill(String string) {
		// TODO Auto-generated method stub
		
	}
	public void setMute(boolean b) {
		// TODO Auto-generated method stub
		
	}
	public void setTerminate(boolean b) {
		// TODO Auto-generated method stub
		
	}
	public void setSource(String string) {
		// TODO Auto-generated method stub
		
	}
	public long getKill() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
