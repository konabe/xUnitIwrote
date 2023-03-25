import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

// テストメソッドを動的に呼び出す
class TestCase {
  protected String _name;

  TestCase(String name) {
    _name = name;
  }

   public void run() throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    // getMethodはpublicを宣言しないと見つけてくれない
    Method method = this.getClass().getMethod(_name);
    method.invoke(this);
  }
}

// メソッドが起動されたかを記録する
class WasRun extends TestCase {
  Integer wasRun;

  WasRun(String name) {
    super(name);
    wasRun = null;
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
