package ilpme;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ilpme.app.IncrementalIterativeLearning;
import ilpme.core.Config;
import ilpme.core.Logger;
import ilpme.entities.ILPMEProblem;
import ilpme.entities.LogicProgram;
import ilpme.xhail.core.entities.Answers;

/**
 * 
 * @author Arindam
 *
 */
public class Application implements Callable<LogicProgram> {

	static {
		Answers.started();
	}

	/**
	 * The <code>PATHS</code> where <code>gringo</code> and <code>clasp</code>
	 * most likely are.
	 */
	private static final Path[] PATHS = { Paths.get("/Library/Gringo/"), Paths.get("/Library/Clasp/"), Paths.get("/usr/bin/gringo/"),
			Paths.get("/usr/bin/clasp/"), Paths.get("/usr/bin/"), Paths.get("/usr/local/gringo/"), Paths.get("/usr/local/clasp/"), Paths.get("/usr/local/"),
			Paths.get("/opt/bin/"), Paths.get("/opt/local/"), Paths.get("/opt/clasp/"), Paths.get("/opt/gringo/"), Paths.get("/opt/local/gringo/"),
			Paths.get("/opt/local/clasp/"), Paths.get("C:\\Gringo\\"), Paths.get("C:\\Clasp\\") };
	private static final Path ROOT = Paths.get(".").toAbsolutePath().getRoot().normalize();

	private static final ExecutorService service = Executors.newSingleThreadExecutor();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.Builder builder = new Config.Builder();
		if (null == args)
			builder.missingParameter();
		else
			for (int i = 0; i < args.length; i++)
				switch (args[i]) {
				case "-c":
				case "--clasp":
					if (args.length - i <= 1)
						builder.missingParameter();
					else
						builder.setClasp(args[++i]);
					break;
				case "-d":
				case "--debug":
					builder.setDebug(true);
					break;
				case "-f":
				case "--full":
					builder.setFull(true);
					break;					
				case "-g":
				case "--gringo":
					if (args.length - i <= 1)
						builder.missingParameter();
					else
						builder.setGringo(args[++i]);
					break;
				case "-h":
				case "--help":
					
					break;
				case "-i":
				case "--iter":
					if (args.length - i <= 1)
						builder.missingParameter();
					else
						builder.setIterations(args[++i]);
					break;
				case "-k":
				case "--kill":
					if (args.length - i <= 1)
						builder.missingParameter();
					else
						builder.setKill(args[++i]);
					break;
				case "-m":
				case "--mute":
					builder.setMute(true);
					break;
				case "--source":
					builder.setSource(args[++i]);
				}

		
		Config config = builder.build();
		Application application = new Application(config);
		application.execute();
	}

	private Config config = null;
	private ILPMEProblem problem = null;
	private Application(Config config) {
		if (null == config)
			throw new IllegalArgumentException("Illegal 'config' argument in Application(Config): " + config);

		this.config = config;
	}
	

	@Override
	public LogicProgram call() throws Exception {
		Logger.message("\nSolving...\n");
		
		/**
		 * create the problem object here
		 */
		IncrementalIterativeLearning iil = new IncrementalIterativeLearning();
		return iil.learn(problem);
	}

	/**
	 * 
	 */
	public void execute() {
		
		if(config.hasError()){
			Logger.error("");
			Logger.help();
			return;
		}
		
		if(config.isHelp()){
			Logger.help();
			return;
		}
		
		
		
		long kill = config.getKill();
		try {
			final Future<LogicProgram> task = service.submit(this);
			LogicProgram answer = kill > 0L ? task.get(kill, TimeUnit.SECONDS) : task.get();
			Logger.stamp(answer);
		} catch (CancellationException e) {
			Logger.message(String.format("*** Info  (%s): computation was cancelled", Logger.SIGNATURE));
		} catch (ExecutionException e) {
			Logger.message(String.format("*** Info  (%s): computation threw an exception", Logger.SIGNATURE));
		} catch (InterruptedException e) {
			Logger.message(String.format("*** Info  (%s): current thread was interrupted while waiting", Logger.SIGNATURE));
		} catch (TimeoutException e) {
			Logger.message(String.format("*** Info  (%s): solving interrupted after %d second/s", Logger.SIGNATURE, kill));
		} catch (final Exception e) {
			// If something independent by our will happens...
			String message = "unexpected runtime error:\n  " + e.getMessage();
			for (StackTraceElement element : e.getStackTrace())
				message += "\n    " + element.toString();
			Logger.error(message);
		} finally {
			service.shutdownNow();
		}

	}
}