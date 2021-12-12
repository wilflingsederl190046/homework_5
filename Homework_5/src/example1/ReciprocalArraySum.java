package example1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Class wrapping methods for implementing reciprocal array sum in parallel.
 */
public final class ReciprocalArraySum {

    /**
     * Default constructor.
     */
    private ReciprocalArraySum() {
    }

    /**
     * Sequentially compute the sum of the reciprocal values for a given array.
     *
     * @param input Input array
     * @return The sum of the reciprocals of the array input
     */
    protected static double seqArraySum(final double[] input) {
        double sum = 0;
        // ToDo: Compute sum of reciprocals of array elements
        for (int i = 0; i < input.length; i++) {
            sum += 1 / input[i];
        }

        return sum;
    }


    /**
     * This class stub can be filled in to implement the body of each task
     * created to perform reciprocal array sum in parallel.
     */
    private static class ReciprocalArraySumTask extends RecursiveAction {
        /**
         * Starting index for traversal done by this task.
         */
        private final int startIndexInclusive;
        /**
         * Ending index for traversal done by this task.
         */
        private final int endIndexExclusive;
        /**
         * Input array to reciprocal sum.
         */
        private final double[] input;
        /**
         * Intermediate value produced by this task.
         */
        private double value;

        private static int SEQUENTIAL_THRESHOLD = 50000;

        /**
         * Constructor.
         * @param setStartIndexInclusive Set the starting index to begin
         *        parallel traversal at.
         * @param setEndIndexExclusive Set ending index for parallel traversal.
         * @param setInput Input values
         */
        ReciprocalArraySumTask(final int setStartIndexInclusive,
                               final int setEndIndexExclusive, final double[] setInput) {
            this.startIndexInclusive = setStartIndexInclusive;
            this.endIndexExclusive = setEndIndexExclusive;
            this.input = setInput;
        }

        /**
         * Getter for the value produced by this task.
         * @return Value produced by this task
         */
        public double getValue() {
            return value;
        }

        @Override
        protected void compute() {
            // TODO: Implement Thread forking on Threshold value. (If size of
            // array smaller than threshold: compute sequentially else, fork
            // 2 new threads
            if(input.length-1 < SEQUENTIAL_THRESHOLD) {
                for(int i = startIndexInclusive; i < endIndexExclusive; i++){
                    value += 1/input[i];
                }
            } else {
                assert input.length % 2 == 0;
                int midLength = input.length / 2;

                ReciprocalArraySumTask upperHalf = new ReciprocalArraySumTask(0, midLength, input);
                ReciprocalArraySumTask lowerHalf = new ReciprocalArraySumTask(midLength, input.length, input);
                invokeAll(upperHalf, lowerHalf);
                value = upperHalf.getValue() + lowerHalf.getValue();
            }
        }
    }


    /**
     * TODO: Extend the work you did to implement parArraySum to use a set
     * number of tasks to compute the reciprocal array sum.
     *
     * @param input Input array
     * @param numTasks The number of tasks to create
     * @return The sum of the reciprocals of the array input
     */
    protected static double parManyTaskArraySum(final double[] input, final int numTasks) {
        double sum = 0;
        // ToDo: Start Calculation with help of ForkJoinPool

        ReciprocalArraySumTask output [] = new ReciprocalArraySumTask[numTasks];
        for (int i=0; i<numTasks; i++){
            int chunkSize = (input.length + numTasks - 1) / numTasks;
            int chunkStart = i * chunkSize;
            int end = (i + 1) * chunkSize;
            int chunkEnd;
            if (end > input.length) {
                chunkEnd = input.length;
            } else {
                chunkEnd = end;
            }
            output[i] = new ReciprocalArraySumTask(chunkStart, chunkEnd, input);
        }
        for (int i=1; i<numTasks; i++){
            output[i].fork();
        }
        output[0].compute();
        for (int i=1; i<numTasks; i++){
            output[i].join();
        }
        for(int i=0; i<numTasks; i++){
            sum += output[i].getValue();
        }
        return sum;
    }
}

