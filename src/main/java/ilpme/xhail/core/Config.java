/**
 * 
 */
package ilpme.xhail.core;

import java.nio.file.Path;
import ilpme.core.Logger;

/**
 * @author stefano
 *
 */
public class Config {

	public static class Builder implements Buildable<Config> {

		private boolean all = false;
		private boolean blind = false;
		private Path clasp = null;
		private boolean debug = false;
		String errors = "";
		private boolean full = false;
		private Path gringo = null;
		private boolean help = false;
		private int iterations = 0;
		private long kill = 0L;
		private boolean mute = false;
		private boolean output = false;
		private boolean prettify = false;
		private boolean search = false;
		private boolean terminate = false;

		private boolean version = false;



		@Override
		public Config build() {
			if (!errors.isEmpty())
				Logger.error("errors found:\n" + errors);
			return new Config(this);
		}

		public Builder missingParameter() {
			errors += "  a parameter is missing on the command line\n";
			return this;
		}


		public Builder setAll(boolean all) {
			this.all = all;
			return this;
		}

		public Builder setBlind(boolean blind) {
			this.blind = blind;
			return this;
		}

		public Builder setClasp(Path clasp) {
			this.clasp = clasp;
			return this;
		}

		public Builder setDebug(boolean debug) {
			this.debug = debug;
			return this;
		}

		public Builder setFull(boolean full) {
			this.full = full;
			return this;
		}

		public Builder setGringo(Path gringo) {
			
			this.gringo = gringo;
			return this;
		}

		public Builder setHelp(boolean help) {
			this.help = help;
			return this;
		}

		public Builder setIterations(int iterations) {
			this.iterations = iterations;
			return this;
		}

		public Builder setKill(long kill) {
			this.kill = kill;
			return this;
		}

		public Builder setMute(boolean mute) {
			this.mute = mute;
			return this;
		}

		public Builder setOutput(boolean output) {
			this.output = output;
			return this;
		}

		public Builder setPrettify(boolean prettify) {
			this.prettify = prettify;
			return this;
		}

		public Builder setSearch(boolean search) {
			this.search = search;
			return this;
		}

		public Builder setTerminate(boolean terminate) {
			this.terminate = terminate;
			return this;
		}

		public Builder setVersion(boolean version) {
			this.version = version;
			return this;
		}

	}

	private final boolean all;

	private final boolean blind;

	private Path clasp;

	private final boolean debug;

	private final boolean full;

	private Path gringo;

	private final boolean help;

	private final int iterations;

	private final long kill;

	private final boolean mute;

	private final String name;

	private final boolean output;

	private final boolean prettify;

	private final boolean search;

	private final boolean terminate;

	private final boolean version;

	private Config(Builder builder) {
		if (null == builder)
			throw new IllegalArgumentException("Illegal 'builder' argument in Application(Application.Builder): " + builder);
		this.all = builder.all;
		this.blind = builder.blind;
		this.clasp = builder.clasp;
		this.debug = builder.debug;
		this.full = builder.full;
		this.gringo = builder.gringo;
		this.iterations = builder.iterations;
		this.help = builder.help;
		this.kill = builder.kill;
		String name = "stdin";
		this.mute = builder.mute;
		this.name = name;
		this.output = builder.output;
		this.prettify = builder.prettify;
		this.search = builder.search;
		this.terminate = builder.terminate;
		this.version = builder.version;
	}

	public Path getClasp() {
		return clasp;
	}

	public Path getGringo() {
		return gringo;
	}

	public final int getIterations() {
		return iterations;
	}

	public final long getKill() {
		return kill;
	}

	public final String getName() {
		return name;
	}
	
	public final boolean isAll() {
		return all;
	}

	public final boolean isBlind() {
		return blind;
	}

	public final boolean isDebug() {
		return debug;
	}

	public final boolean isFull() {
		return full;
	}

	public final boolean isHelp() {
		return help;
	}

	public final boolean isMute() {
		return mute;
	}

	public final boolean isOutput() {
		return output;
	}

	public final boolean isPrettify() {
		return prettify;
	}

	public final boolean isSearch() {
		return search;
	}

	public final boolean isTerminate() {
		return terminate;
	}

	public final boolean isVersion() {
		return version;
	}

	public void setClasp(Path clasp) {
		this.clasp = clasp;
	}

	public void setGringo(Path gringo) {
		this.gringo = gringo;
	}

	@Override
	public String toString() {
		String result = "";
		if (all)
			result += " -a";
		if (blind)
			result += " -b";
		if (null != clasp)
			result += " -c " + clasp.toString();
		if (debug)
			result += " -d";
		if (full)
			result += " -f";
		if (null != gringo)
			result += " -g " + gringo.toString();
		if (help)
			result += " -h";
		if (iterations > 0)
			result += " -i " + iterations;
		if (kill > 0L)
			result += " -k " + kill;
		if (mute)
			result += " -m";
		if (prettify)
			result += " -p";
		if (search)
			result += " -s";
		if (version)
			result += " -v";
		return result;
	}

}
