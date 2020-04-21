public class Buffer
{
   private int buffer = -1; // shared by producer and consumer threads

   // place value into buffer
   public void blockingPut(int value) throws InterruptedException
   {
      System.out.printf(" writes data to %2d\n", value);
      buffer = value;
   } 

   // return value from buffer
   public int blockingGet() throws InterruptedException
   {
      //System.out.printf(" reads data %2d\n", buffer);
      return buffer;
   } 
}