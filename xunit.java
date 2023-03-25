class Main {
  public static void main(String[] args) {
    WasRun test = new WasRun("testMethod");
    System.out.println(test.wasRun);
    test.testMethod();
    System.out.println(test.wasRun);
  }
}
