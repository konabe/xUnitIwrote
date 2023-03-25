import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

// テストメソッドを動的に呼び出す
class TestCase {
  protected String _name;
  
  TestCase(String name) {
    _name = name;
  }

  public void setUp() {}

  public TestResult run() throws InvocationTargetException {
    TestResult result = new TestResult();
    result.testStarted();
    this.setUp();
    try {
      // getMethodはpublicを宣言しないと見つけてくれない
      Method method = this.getClass().getMethod(_name);
      method.invoke(this);
    } catch (IllegalAccessException | NoSuchMethodException ex) {
    }
    this.tearDown();
    return result;
  }

  public void tearDown() {}
}

class TestResult {
  private Integer _runCount;

  TestResult() {
    _runCount = 0;
  }

  void testStarted() {
    _runCount ++;
  }

  String summary() {
    return String.format("%d run, 0 failed", _runCount);
  }
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

  public void testResult() throws InvocationTargetException {
    WasRun test = new WasRun("testMethod");
    TestResult result = test.run();
    assert "1 run, 0 failed".equals(result.summary());
  }
}

class Main {
  public static void main(String[] args) throws InvocationTargetException {
    new TestCaseTest("testTemplateMethod").run();
    new TestCaseTest("testResult").run();
  }
}
