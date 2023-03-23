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
        self.log = "setUp "
    def testMethod(self):
        self.log = self.log + "testMethod "

# テストコード

class TestCaseTest(TestCase):
    def testTemplateMethod(self):
        self.test = WasRun("testMethod")
        self.test.run()
        assert("setUp testMethod tearDown " == self.test.log)
        

TestCaseTest("testTemplateMethod").run()