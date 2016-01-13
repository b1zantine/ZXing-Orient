package me.sudar.zxingorient;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by sudar on 13/1/16.
 * Email : hey@sudar.me
 */
public final class Barcode {

    public static final Collection<String> DEFAULT_CODE_TYPES = null;

    public static final Collection<String> PRODUCT_CODE_TYPES =
            list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "RSS_14");

    public static final Collection<String> ONE_D_CODE_TYPES =
            list("UPC_A", "UPC_E", "UPC_EAN_EXTENSION", "EAN_8", "EAN_13", "CODABAR", "CODE_39",
                    "CODE_93", "CODE_128", "ITF", "RSS_14", "RSS_EXPANDED");

    public static final Collection<String> TWO_D_CODE_TYPES =
            list("QR_CODE", "DATA_MATRIX", "PDF_417", "AZTEC");

    /** QR Code 2D barcode format. */
    public static final Collection<String> QR_CODE = Collections.singleton("QR_CODE");

    /** Data Matrix 2D barcode format. */
    public static final Collection<String> DATA_MATRIX= Collections.singleton("DATA_MATRIX");

    /** PDF417 format. */
    public static final Collection<String> PDF_417 = Collections.singleton("PDF_417");

    /** Aztec 2D barcode format. */
    public static final Collection<String> AZTEC_CODE = Collections.singleton("AZTEC");

    /** MaxiCode 2D barcode format. **** NO DETECTOR AVAILABLE FOR MAXICODE. ONLY DECODING IS POSSIBLE ******/
    public static final Collection<String> MAXICODE = Collections.singleton("MAXICODE");



    /** UPC-A 1D format. */
    public static final Collection<String> UPC_A = Collections.singleton("UPC_A");

    /** UPC-E 1D format. */
    public static final Collection<String> UPC_E = Collections.singleton("UPC_E");

    /** UPC/EAN extension format. Not a stand-alone format. */
    public static final Collection<String> UPC_EAN_EXTENSION = Collections.singleton("UPC_EAN_EXTENSION");

    /** EAN-8 1D format. */
    public static final Collection<String> EAN_8 = Collections.singleton("EAN_8");

    /** EAN-13 1D format. */
    public static final Collection<String> EAN_13 = Collections.singleton("EAN_13");

    /** CODABAR 1D format. */
    public static final Collection<String> CODABAR = Collections.singleton("CODABAR");

    /** Code 39 1D format. */
    public static final Collection<String> CODE_39 = Collections.singleton("CODE_39");

    /** Code 93 1D format. */
    public static final Collection<String> CODE_93 = Collections.singleton("CODE_93");

    /** Code 128 1D format. */
    public static final Collection<String> CODE_128 = Collections.singleton("CODE_128");

    /** ITF (Interleaved Two of Five) 1D format. */
    public static final Collection<String> ITF = Collections.singleton("ITF");

    /** RSS 14 */
    public static final Collection<String> RSS_14 = Collections.singleton("RSS_14");

    /** RSS EXPANDED */
    public static final Collection<String> RSS_EXPANDED = Collections.singleton("RSS_EXPANDED");


    private static List<String> list(String... values) {
        return Collections.unmodifiableList(Arrays.asList(values));
    }

}
