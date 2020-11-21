package hedera.starter.utilities;

public class Utils {
    public static final int FILE_PART_SIZE = 5000;

    public static byte[] copyBytes(int start, int length, byte[] bytes) {
        byte[] rv = new byte[length];
        for (int i = 0; i < length; i++) {
            rv[i] = bytes[start + i];
        }
        return rv;
    }

}
