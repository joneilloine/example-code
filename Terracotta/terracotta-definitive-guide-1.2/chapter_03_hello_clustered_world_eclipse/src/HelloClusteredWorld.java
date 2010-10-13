
public class HelloClusteredWorld {
  private static final String message = "Hello Clustered World!";
  private static final int length = message.length();

  private static char[] buffer = new char [length ];
  private static int loopCounter;

  public static void main( String args[] ) throws Exception {
    while( true ) {
      synchronized( buffer ) {
        int messageIndex = loopCounter++ % length;
        if(messageIndex == 0) java.util.Arrays.fill(buffer, '\u0000');

        buffer[messageIndex] = message.charAt(messageIndex);
        System.out.println( buffer );
      }
      Thread.sleep( 100 );
    }
  }
}
