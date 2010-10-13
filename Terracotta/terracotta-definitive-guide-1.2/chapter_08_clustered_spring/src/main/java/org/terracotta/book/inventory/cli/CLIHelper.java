package org.terracotta.book.inventory.cli;


public class CLIHelper {

	private final IO io;

	public CLIHelper(final IO io) {
		this.io = io;
	}

	public String ask(String question) {
		return (String) ask(question, String.class);
	}

	public Object ask(String question, Class<?> type) {
		while (true) {
			io.println(question);
			io.print("==> ");
			io.flush();
			String answer = io.readLine();
			if (Double.class.equals(type)) {
				try {
					return Double.parseDouble(answer);
				} catch (NumberFormatException e) {
					io.println("Input not parseable as a double.  Please try again.");
				}
			} else if (Integer.class.equals(type)) {
				try {
					return Integer.parseInt(answer);
				} catch (NumberFormatException e) {
					io.println("Input not parseable as an integer.  Please try again.");
				}
			} else {
				return answer;
			}
		}
	}

	public int askMenu(String[] options) {
		return askMenu("Choose an action by number:", options);
	}

	public int askMenu(String question, String[] options) {
		if (options == null)
			return -1;
		if (options.length < 1)
			return -1;
		StringBuffer q = new StringBuffer(question);
		q.append("\n");
		for (int i = 0; i < options.length; i++) {
			q.append("[ " + i + " ] " + options[i] + "\n");
		}
		int rv = -1;
		while (rv < 0 || rv > options.length - 1) {
			Integer answer = (Integer) ask(q.toString(), Integer.class);
			rv = answer.intValue();
		}
		return rv;
	}

	public void printSeparator() {
		io.println("===================================================================");
	}

	public void println(String string) {
		io.println(string);
	}

	public void print(String string) {
		io.print(string);
	}

}
