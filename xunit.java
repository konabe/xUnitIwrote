import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

// テストメソッドを動的に呼び出す
class TestCase {
  protected String _name;
  
  TestCase(String name) {
    _name = name;
  }

  public void setUp() {}

  public void run() throws InvocationTargetException {
    this.setUp();
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
  String log;

  WasRun(String name) {
    super(name);
  }

  public void setUp() {
    wasRun = null;
    this.log = "setUp ";
  }

  public void testMethod() {
    wasRun = 1;
    this.log += "testMethod ";
  }
}

class TestCaseTest extends TestCase {
  private WasRun _test;

  TestCaseTest(String name) {
    super(name);
  }

  @Override
  public void setUp() {
    _test = new WasRun("testMethod");
    
  }

  public void testRunning() throws InvocationTargetException {
    _test.run();
    assert "setUp testMethod ".equals(_test.log);
  }

  public void testSetUp() throws InvocationTargetException {
    _test.run();
    assert _test.wasRun == 1;
  }
}

class Main {
  public static void main(String[] args) throws InvocationTargetException {
    new TestCaseTest("testRunning").run();
    new TestCaseTest("testSetUp").run();
  }
}
