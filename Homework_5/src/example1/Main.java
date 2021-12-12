package example1;

public class Main {
    public static void main(String[] args){
        double[] input = {3.0, 5.0, 7.0, 8.0};

        System.out.println(ReciprocalArraySum.seqArraySum(input));
        System.out.println(ReciprocalArraySum.parManyTaskArraySum(input, 2));
    }
}
