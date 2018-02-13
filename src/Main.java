public class Main {
    public static void main(String[] args) {
        TheftChecker theftChecker = new TheftChecker(Integer.parseInt(args[0]),args[1], args[2]);
        theftChecker.indexDocuments();
        theftChecker.check();
    }
}