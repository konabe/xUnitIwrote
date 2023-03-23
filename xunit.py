# テストメソッドを動的に呼び出す
class TestCase:
    def __init__(self, name):
        self.name = name
    def setUp(self):
        pass
    def run(self):
        self.setUp()
        method = getattr(self, self.name)
        method()
        
# メソッドが起動されたかを記録する
class WasRun(TestCase):
    def setUp(self):
        self.wasRun = None
        self.wasSetUp = 1
    def testMethod(self):
        self.wasRun = 1

# テストコード

class TestCaseTest(TestCase):
    def testRunning(self):
        test = WasRun("testMethod")
        test.run()
        assert(test.wasRun)

    def testSetUp(self):
        test = WasRun("testMethod")
        test.run()
        assert(test.wasSetUp)
        

TestCaseTest("testRunning").run()
TestCaseTest("testSetUp").run()