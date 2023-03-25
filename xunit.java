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
    this.tearDown();
  }

  public void tearDown() {}
}

// メソッドが起動されたかを記録する
class WasRun extends TestCase {
  String log;

  WasRun(String name) {
    super(name);
  }

  @Override
  public void setUp() {
    this.log = "setUp ";
  }

  public void testMethod() {
    this.log += "testMethod ";
  }

  @Override
  public void tearDown() {
    this.log += "tearDown ";
  }
}

class TestCaseTest extends TestCase {
  TestCaseTest(String name) {
    super(name);
  }

  @Override
  public void setUp() {
    
  }

  public void testTemplateMethod() throws InvocationTargetException {
    WasRun test = new WasRun("testMethod");
    test.run();
    // String#equals で比較すること
    assert "setUp testMethod tearDown ".equals(test.log);
  }
}

class Main {
  public static void main(String[] args) throws InvocationTargetException {
    new TestCaseTest("testTemplateMethod").run();
  }
}
