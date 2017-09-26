package ilpme.utils;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import ilpme.core.ILPMEConfig;
import ilpme.core.Logger;
import ilpme.entities.PartialHypotheis;
import ilpme.entities.Sample;
import ilpme.xhail.core.entities.Values;
import ilpme.xhail.core.parser.Acquirer;

public class SatisfiabilityUtils {

	private final Path errors;
	private final Path middle;
	private final Path target;
	private String gringoPath;
	private String claspPath;
	private final String UNSATISFIABLE = "UNSATISFIABLE";

	public SatisfiabilityUtils(String gringoPath, String claspPath) {
		errors = Paths.get("satisfiability_utills_error.asp");
		middle  = Paths.get("satisfiability_utills_grounded.asp");
		target  = Paths.get("satisfiability_utills_clasp.asp");
		this.gringoPath = gringoPath;
		this.claspPath = claspPath;
	}

	public boolean doesEntail(PartialHypotheis ph, Sample exm, List<String> bk){

		String program = ph.getHypothesis().toString() + "\n#hide.\n";
		String obs = exm.toASPStringForSatisfiability();

		Path path = Paths.get("ilpme_satisfiability.lp");
		Path folder = Paths.get(".", "temp").toAbsolutePath().normalize();
		try {
			if (!Files.exists(folder)){
				Files.createDirectory(folder);
			}

			Path file = folder.resolve(path.getFileName());
			FileUtils.writeStringToFile(file.toFile(), program+"\n"+obs +"\n"
					+ StringUtils.join(bk,'\n'), Charset.defaultCharset() );

			/***
			 * call clasp/gringo
			 * process the output
			 */
			try {
				List<String> gringoCmd  = new LinkedList<String>();
				gringoCmd.add(gringoPath);
				gringoCmd.add(file.toAbsolutePath().toString());
				Process gringo = new ProcessBuilder(gringoCmd) //
						.redirectError(Redirect.to(errors.toFile())).
						redirectOutput(Redirect.to(middle.toFile())).start();

				gringo.waitFor();
				List<String> claspCmd = new LinkedList<String>();
				claspCmd.add(this.claspPath);
				claspCmd.add(this.middle.toAbsolutePath().toString());
				Process clasp = new ProcessBuilder(claspCmd).
						redirectOutput(Redirect.to(target.toFile())).start();
				clasp.waitFor();
				String data = FileUtils.readFileToString(target.toFile(), Charset.defaultCharset());


				if(data.contains(this.UNSATISFIABLE))
					return false;
				if(data.contains("SATISFIABLE"))
					return true;
				return false;

			} catch (InterruptedException e) {
				Logger.warning(false, "gringo/clasp failed");
				System.err.println(e);
			}
		}catch (IOException e) {
			Logger.warning(false, "cannot create 'temp' folder (do we have rights?)");
			System.err.println(e);
		}

		return false;
	}
	
	public boolean doesEntail(String ph, Sample exm, List<String> bk){

		String program = ph;
		String obs = exm.toASPStringForSatisfiability();

		Path path = Paths.get("ilpme_satisfiability.lp");
		Path folder = Paths.get(".", "temp").toAbsolutePath().normalize();
		try {
			if (!Files.exists(folder)){
				Files.createDirectory(folder);
			}

			Path file = folder.resolve(path.getFileName());
			FileUtils.writeStringToFile(file.toFile(), program+"\n"+obs +"\n"
					+ StringUtils.join(bk,'\n'), Charset.defaultCharset() );

			/***
			 * call clasp/gringo
			 * process the output
			 */
			try {
				List<String> gringoCmd  = new LinkedList<String>();
				gringoCmd.add(gringoPath);
				gringoCmd.add(file.toAbsolutePath().toString());
				Process gringo = new ProcessBuilder(gringoCmd) //
						.redirectError(Redirect.to(errors.toFile())).
						redirectOutput(Redirect.to(middle.toFile())).start();

				gringo.waitFor();
				List<String> claspCmd = new LinkedList<String>();
				claspCmd.add(this.claspPath);
				claspCmd.add(this.middle.toAbsolutePath().toString());
				Process clasp = new ProcessBuilder(claspCmd).
						redirectOutput(Redirect.to(target.toFile())).start();
				clasp.waitFor();
				String data = FileUtils.readFileToString(target.toFile(), Charset.defaultCharset());


				if(data.contains(this.UNSATISFIABLE))
					return false;
				if(data.contains("SATISFIABLE"))
					return true;
				return false;

			} catch (InterruptedException e) {
				Logger.warning(false, "gringo/clasp failed");
				System.err.println(e);
			}
		}catch (IOException e) {
			Logger.warning(false, "cannot create 'temp' folder (do we have rights?)");
			System.err.println(e);
		}

		return false;
	}

	public Map.Entry<Values, Collection<Collection<String>>> execute(String source, ILPMEConfig config) {
		try {
			Path path = Paths.get("ilpme_satisfiability.lp");
			Path folder = Paths.get(".", "temp").toAbsolutePath().normalize();
			if (!Files.exists(folder)){
				Files.createDirectory(folder);
			}				
			Path file = folder.resolve(path.getFileName());
			FileUtils.writeStringToFile(file.toFile(), source,Charset.defaultCharset());

			if (config.isDebug())
				Logger.message(String.format("*** Info  (%s): calling '%s'", Logger.SIGNATURE, String.join("", this.gringoPath)));

			List<String> gringoCmd  = new LinkedList<String>();
			gringoCmd.add(gringoPath);
			gringoCmd.add(file.toAbsolutePath().toString());

			Process gringo = new ProcessBuilder(gringoCmd) //
					.redirectError(Redirect.to(errors.toFile())).
					redirectOutput(Redirect.to(middle.toFile())).start();
			gringo.waitFor();

			try {
				if (config.isDebug())
					Logger.message(String.format("*** Info  (%s): calling '%s'", Logger.SIGNATURE, String.join(" ", this.claspPath)));
				List<String> claspCmd = new LinkedList<String>();
				claspCmd.add(this.claspPath);
				claspCmd.add(this.middle.toAbsolutePath().toString());
				claspCmd.add("--verbose=0");
				claspCmd.add("--opt-mode=optN");
				
				Process clasp = new ProcessBuilder(claspCmd).
						redirectOutput(Redirect.to(target.toFile())).start();
				clasp.waitFor();
				try {
					System.out.println("Parsing input");
					return Acquirer.from(Files.newInputStream(target)).parse();
				} catch (IOException e) {
					if (!config.isOutput())
						Logger.error("cannot read from 'clasp' process");
				}
			} catch (IOException e) {
				if (!config.isOutput())
					Logger.error("cannot launch 'clasp' process");
			} catch (InterruptedException e) {
				if (!config.isOutput())
					Logger.error("'clasp' process was interrupted");
			}

		} catch (IOException e) {
			if (!config.isOutput())
				Logger.error("cannot launch 'gringo' process");
		} catch (InterruptedException e) {
			if (!config.isOutput())
				Logger.error("'gringo' process was interrupted");
		}
		return new SimpleEntry<Values, Collection<Collection<String>>>(null, Collections.emptySet());
	}
}
