class WasRun {
  Integer wasRun;

  WasRun(String name) {
    wasRun = null;
  }

  void run() {
    testMethod();
  }

  void testMethod() {
    wasRun = 1;
  }
}

class Main {
  public static void main(String[] args) {
    WasRun test = new WasRun("testMethod");
    System.out.println(test.wasRun);
    test.run();
    System.out.println(test.wasRun);
  }
}
