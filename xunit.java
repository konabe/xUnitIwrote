import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

// テストメソッドを動的に呼び出す
class TestCase {
  protected String _name;
  
  TestCase(String name) {
    _name = name;
  }

  public void run() throws InvocationTargetException {
    try {
      // getMethodはpublicを宣言しないと見つけてくれない
      Method method = this.getClass().getMethod(_name);
      method.invoke(this);
    } catch (IllegalAccessException | NoSuchMethodException ex) {
      return;
    }
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

class TestCaseTest extends TestCase {
  TestCaseTest(String name) {
    super(name);
  }

  public void testRunning() throws InvocationTargetException {
    WasRun test = new WasRun("testMethod");
    assert test.wasRun == null;
    test.run();
    assert test.wasRun == 1;
  }

  public void testSetUp() throws InvocationTargetException {
    WasRun test = new WasRun("testMethod");
    test.run();
    assert test.wasSetUp == 1;
  }
}

class Main {
  public static void main(String[] args) throws InvocationTargetException {
    new TestCaseTest("testRunning").run();
    new TestCaseTest("testSetUp").run();
  }
}
