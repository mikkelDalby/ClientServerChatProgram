public class Main {

    public static void main(String[] args) {
	    // write your code here
        String string = "JOIN mikkel, 127.0.0.1:1234";
        String[] array = splitInputString(string);

        System.out.println(string);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }

    public static String[] splitInputString(String input){
        String[] parameters = input.split(" |:");
        parameters[1] = parameters[1].replace(",", "");
        return parameters;
    }
}