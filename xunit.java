import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

class WasRun {
  Integer wasRun;
  private String _name;

  WasRun(String name) {
    wasRun = null;
    _name = name;
  }

   void run() throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    // getMethodはpublicを宣言しないと見つけてくれない
    Method method = this.getClass().getMethod(_name);
    method.invoke(this);
  }

  public void testMethod() {
    wasRun = 1;
  }
}

class Main {
  public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    WasRun test = new WasRun("testMethod");
    System.out.println(test.wasRun);
    test.run();
    System.out.println(test.wasRun);
  }
}
