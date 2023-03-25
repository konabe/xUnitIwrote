import java.lang.reflect.Method;
import java.util.ArrayList;

// テストメソッドを動的に呼び出す
class TestCase {
  protected String _name;
  
  TestCase(String name) {
    _name = name;
  }

  public void setUp() {}

  public void run(TestResult result) {
    result.testStarted();
    this.setUp();
    try {
      // getMethodはpublicを宣言しないと見つけてくれない
      Method method = this.getClass().getMethod(_name);
      method.invoke(this);
    } catch (Exception ex) {
      result.testFailed();
    }
    this.tearDown();
  }

  public void tearDown() {}
}

class TestSuite {
  private ArrayList<TestCase> _tests;

  TestSuite() {
    _tests = new ArrayList<TestCase>();
  }

  void add(TestCase test) {
    _tests.add(test);
  }

  void run(TestResult result) {
    for (TestCase test : _tests) {
      test.run(result);
    }
  }
}

class TestResult {
  private Integer _runCount;
  private Integer _failedCount;

  TestResult() {
    _runCount = 0;
    _failedCount = 0;
  }

  void testStarted() {
    _runCount ++;
  }

  void testFailed() {
    _failedCount ++;
  }

  String summary() {
    return String.format("%d run, %d failed", _runCount, _failedCount);
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

  public void testBrokenMethod() throws IllegalAccessException {
    throw new IllegalAccessException();
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

  public void testTemplateMethod() {
    WasRun test = new WasRun("testMethod");
    TestResult result = new TestResult();
    test.run(result);
    // String#equals で比較すること
    assert "setUp testMethod tearDown ".equals(test.log);
  }

  public void testResult() {
    WasRun test = new WasRun("testMethod");
    TestResult result = new TestResult();
    test.run(result);
    assert "1 run, 0 failed".equals(result.summary());
  }

  public void testFailedResult() {
    WasRun test = new WasRun("testBrokenMethod");
    TestResult result = new TestResult();
    test.run(result);
    assert "1 run, 1 failed".equals(result.summary());
  }

  public void testFailedResultFormatting() {
    TestResult result = new TestResult();
    result.testStarted();
    result.testFailed();
    assert "1 run, 1 failed".equals(result.summary());
  }

  public void testSuite() {
    TestSuite suite = new TestSuite();
    suite.add(new WasRun("testMethod"));
    suite.add(new WasRun("testBrokenMethod"));
    TestResult result = new TestResult();
    suite.run(result);
    assert "2 run, 1 failed".equals(result.summary());
  }
}

class Main {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite();
    suite.add(new TestCaseTest("testTemplateMethod"));
    suite.add(new TestCaseTest("testResult"));
    suite.add(new TestCaseTest("testFailedResult"));
    suite.add(new TestCaseTest("testFailedResultFormatting"));
    suite.add(new TestCaseTest("testSuite"));
    TestResult result = new TestResult();
    suite.run(result);
    System.out.println(result.summary());
  }
}
