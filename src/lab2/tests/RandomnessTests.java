package lab2.tests;

public class RandomnessTests {
    public static final double[][] QUANTILES = {{0.01D, 2.326347874D}, {0.05D, 1.644853627D}, {0.1D, 1.281551566D}};

    public static void isEquiprobableTest(int[] sequence) {
        double chi_square_statistics = 0.0D;
        int[] bytes = new int[256];

        int i;
        for (i = 0; i < bytes.length; ++i) {
            bytes[i] = 0;
        }

        for (i = 0; i < sequence.length; ++i) {
            ++bytes[sequence[i]];
        }

        for (i = 0; i < bytes.length; ++i) {
            chi_square_statistics += Math.pow((double) (bytes[i] - sequence.length / 256), 2.0D) / (double) (sequence.length / 256);
        }

        System.out.println("Sign equal probability test with sequence length: " + sequence.length);

        for (i = 0; i < 3; ++i) {
            System.out.println("  level of significance: " + QUANTILES[i][0]);
            double chi_square_limit_value = 22.58317958127243D * QUANTILES[i][1] + 255.0D;
            System.out.println("  chi square statistics: " + chi_square_statistics);
            System.out.println("  chi square limit value: " + chi_square_limit_value);
            if (chi_square_statistics <= chi_square_limit_value) {
                System.out.println("    Sign equal probability test completed successfully.");
            } else {
                System.out.println("   Sign equal probability test failed.");
            }
        }

    }

    public static void isIndependentTest(int[] sequence) {
        double chi_square_statistics = 0.0D;
        int n = sequence.length / 2;
        int[][] bytes = new int[256][256];

        int first_byte_number;
        int second_byte_number;
        for (first_byte_number = 0; first_byte_number < bytes.length; ++first_byte_number) {
            for (second_byte_number = 0; second_byte_number < bytes.length; ++second_byte_number) {
                bytes[first_byte_number][second_byte_number] = 0;
            }
        }

        for (first_byte_number = 1; first_byte_number < sequence.length; first_byte_number += 2) {
            ++bytes[sequence[first_byte_number - 1]][sequence[first_byte_number]];
        }

        first_byte_number = 0;
        second_byte_number = 0;

        int i;
        for (i = 0; i < bytes.length; ++i) {
            int j;
            for (j = 0; j < bytes.length; ++j) {
                first_byte_number += bytes[i][j];
            }

            for (j = 0; j < bytes.length; ++j) {
                for (int k = 0; k < bytes.length; ++k) {
                    second_byte_number += bytes[k][j];
                }

                if (first_byte_number != 0 && second_byte_number != 0) {
                    chi_square_statistics += Math.pow((double) bytes[i][j], 2.0D) / (double) (first_byte_number * second_byte_number);
                    second_byte_number = 0;
                }
            }

            first_byte_number = 0;
        }

        chi_square_statistics = (chi_square_statistics - 1.0D) * (double) n;
        System.out.println("Sign independence test with sequence length: " + sequence.length);

        for (i = 0; i < 3; ++i) {
            System.out.println("  level of significance: " + QUANTILES[i][0]);
            double chi_square_limit_value = 360.62445840513D * QUANTILES[i][1] + 65025.0D;
            System.out.println("  chi square statistics: " + chi_square_statistics);
            System.out.println("  chi square limit value: " + chi_square_limit_value);
            if (chi_square_statistics <= chi_square_limit_value) {
                System.out.println("    Sign independence test completed successfully.");
            } else {
                System.out.println("   Sign independence test failed.");
            }
        }

    }

    public static void isHomogeneityTest(int[] sequence) {
        double chi_square_statistics = 0.0D;
        boolean intervals_number = true;
        int interval_length = sequence.length / 20;
        int[][] bytes = new int[20][256];

        int iterator;
        int sequence_part_index;
        for (iterator = 0; iterator < bytes.length; ++iterator) {
            for (sequence_part_index = 0; sequence_part_index < bytes[iterator].length; ++sequence_part_index) {
                bytes[iterator][sequence_part_index] = 0;
            }
        }

        iterator = 0;
        sequence_part_index = 0;

        int byte_in_raw_number;
        for (byte_in_raw_number = 0; byte_in_raw_number < 20 * interval_length; ++byte_in_raw_number) {
            if (iterator == interval_length) {
                iterator = 0;
                ++sequence_part_index;
            }

            ++bytes[sequence_part_index][sequence[byte_in_raw_number]];
            ++iterator;
        }

        byte_in_raw_number = 0;

        int i;
        for (i = 0; i < 256; ++i) {
            int j;
            for (j = 0; j < 20; ++j) {
                byte_in_raw_number += bytes[j][i];
            }

            for (j = 0; j < 20; ++j) {
                if (byte_in_raw_number != 0) {
                    chi_square_statistics += Math.pow((double) bytes[j][i], 2.0D) / (double) (byte_in_raw_number * interval_length);
                }
            }

            byte_in_raw_number = 0;
        }

        chi_square_statistics = (chi_square_statistics - 1.0D) * 20.0D * (double) interval_length;
        System.out.println("Sign homogeneity test with sequence length: " + sequence.length);

        for (i = 0; i < 3; ++i) {
            System.out.println("  level of significance: " + QUANTILES[i][0]);
            double chi_square_limit_value = Math.sqrt(9690.0D) * QUANTILES[i][1] + 4845.0D;
            System.out.println("  chi square statistics: " + chi_square_statistics);
            System.out.println("  chi square limit value: " + chi_square_limit_value);
            if (chi_square_statistics <= chi_square_limit_value) {
                System.out.println("    Sign homogeneity test completed successfully.");
            } else {
                System.out.println("   Sign homogeneity test failed.");
            }
        }

    }
}
