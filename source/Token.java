public class MyClass implements Comparable<MyClass> {
   private int key;
   private int data;
 
   public MyClass(int key, int data) { 
      this.key = key;
      this.data = data;
   }

   public MyClass( ) {
      key = 0;
      data = -1;
    }

   public int compareTo(MyClass obj) {
      MyClass mc = (MyClass) obj;
      if (key < mc.key) {
         return -1; 
      } else if (key > mc.key) {
         return 1;
      } else {
         return 0;
      }
   }

   public String toString( ) {
      return "(" + key + ", " + data + ")";
   }
}
