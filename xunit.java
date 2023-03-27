import java.lang.reflect.Method;
import java.util.ArrayList;

// テストメソッドを動的に呼び出す
class TestCase {
  protected String _name;
  
  TestCase(String name) {
    _name = name;
  }

  public void setUp() throws Exception  {}
  public void tearDown() {}

  public void run(TestResult result) {
    result.testStarted();
    try {
      this.setUp();
    } catch (Exception ex) {
      result.setUpFailed();
      return;
    }
    try {
      // getMethodはpublicを宣言しないと見つけてくれない
      Method method = this.getClass().getMethod(_name);
      method.invoke(this);
    } catch (Exception ex) {
      result.testFailed();
    }
    this.tearDown();
  }
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
  private boolean _setupFailed;

  TestResult() {
    _runCount = 0;
    _failedCount = 0;
    _setupFailed = false;
  }

  void setUpFailed() {
    _setupFailed = true;
  }

  void testStarted() {
    _runCount ++;
  }

  void testFailed() {
    _failedCount ++;
  }

  String summary() {
    if (_setupFailed) {
      return "setUp failed.\n"; 
    }
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
  public void setUp() throws Exception {
    this.log = "setUp ";
  }

  public void testMethod() {
    this.log += "testMethod ";
  }

  public void testBrokenMethod() throws IllegalAccessException {
    this.log += "testBrokenMethod ";
    throw new IllegalAccessException();
  }

  @Override
  public void tearDown() {
    this.log += "tearDown ";
  }
}

class SetupFailure extends TestCase {
  SetupFailure(String name) {
    super(name);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    throw new IllegalAccessException();
  }

  public void testMethod() {
    assert true;
  }
}

class TestCaseTest extends TestCase {
  private TestResult _result;

  TestCaseTest(String name) {
    super(name);
  }

  @Override
  public void setUp() {
    _result = new TestResult();
  }

  public void testTemplateMethod() {
    WasRun test = new WasRun("testMethod");
    test.run(_result);
    // String#equals で比較すること
    assert "setUp testMethod tearDown ".equals(test.log);
  }

  public void testBrokenMethod() {
    WasRun test = new WasRun("testBrokenMethod");
    test.run(_result);
    // String#equals で比較すること
    assert "setUp testBrokenMethod tearDown ".equals(test.log);
  }

  public void testResult() {
    WasRun test = new WasRun("testMethod");
    test.run(_result);
    assert "1 run, 0 failed".equals(_result.summary());
  }

  public void testFailedResult() {
    WasRun test = new WasRun("testBrokenMethod");
    test.run(_result);
    assert "1 run, 1 failed".equals(_result.summary());
  }

  public void testSetupFailureOutput() {
    WasRun test = new WasRun("testBrokenMethod");
    test.run(_result);
    assert "1 run, 1 failed".equals(_result.summary());
  }

  public void testFailedResultFormatting() {
    _result.testStarted();
    _result.testFailed();
    assert "1 run, 1 failed".equals(_result.summary());
  }

  public void testSetupFailure() {
    SetupFailure test = new SetupFailure("testMethod");
    test.run(_result);
    assert "setUp failed.\n".equals(_result.summary());
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
    suite.add(new TestCaseTest("testBrokenMethod"));
    suite.add(new TestCaseTest("testResult"));
    suite.add(new TestCaseTest("testFailedResult"));
    suite.add(new TestCaseTest("testFailedResultFormatting"));
    suite.add(new TestCaseTest("testSetupFailure"));
    suite.add(new TestCaseTest("testSuite"));
    TestResult result = new TestResult();
    suite.run(result);
    System.out.println(result.summary());
  }
}
