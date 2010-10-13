package zipwire.exception;

public class ZipwireException extends RuntimeException {
	private static final long serialVersionUID = 1435577350774329858L;
	public ZipwireException(String string) {super(string);}
	public ZipwireException(Throwable e) {super(e);}
	public ZipwireException(String string, Exception e) {super(string, e);}

}