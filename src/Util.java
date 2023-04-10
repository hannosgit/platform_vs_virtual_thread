import java.text.DecimalFormat;

public class Util {

    private Util() {
    }

    private static final DecimalFormat DEC_FORMAT = new DecimalFormat("#.##");

    public static String toHumanReadableByNumOfLeadingZeros(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Invalid file size: " + size);
        }
        if (size < 1024) return size + " Bytes";
        int unitIdx = (63 - Long.numberOfLeadingZeros(size)) / 10;
        return formatSize(size, 1L << (unitIdx * 10), " KMGTPE".charAt(unitIdx) + "iB");
    }

    private static String formatSize(long size, long divider, String unitName) {
        return DEC_FORMAT.format((double) size / divider) + " " + unitName;
    }

}
