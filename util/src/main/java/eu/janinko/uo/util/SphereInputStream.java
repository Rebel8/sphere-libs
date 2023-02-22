package eu.janinko.uo.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Honza Brázdil &lt;jbrazdil@redhat.com&gt;
 */
public class SphereInputStream extends InputStream {
    private final BufferedInputStream is;
    private int zn = -1;

    private byte[] buffer = new byte[10];

    public SphereInputStream(BufferedInputStream is) {
        this.is = is;
    }

    public void convertANSI(int c) throws IOException {
        cacheHead = 0;
        cacheLength = 2;
        switch (c) {
            case 0x8A: //Š
            {
                cache[0]=0xC5;
                cache[1]=0xA0;
                break;
            }
            case 0x8E: //Ž
            {
                cache[0]=0xC5;
                cache[1]=0xBD;
                break;
            }
            case 0x8D: //Ť
            {
                cache[0]=0xC5;
                cache[1]=0xA4;
                break;
            }

            case 0x93: //“
            {
                cache[0]=0xE2;
                cache[1]=0x80;
                cache[2]=0x9C;
                cacheLength = 3;
                break;
            }
            case 0x96: //–
            {
                cache[0]=0xE2;
                cache[1]=0x80;
                cache[2]=0x93;
                cacheLength = 3;
                break;
            }
            case 0x9A: //š
            {
                cache[0]=0xC5;
                cache[1]=0xA1;
                break;
            }
            case 0x9E: //ž
            {
                cache[0]=0xC5;
                cache[1]=0xBE;
                break;
            }
            case 0x9D: //ť
            {
                cache[0]=0xC5;
                cache[1]=0xA5;
                break;
            }

            case 0xA0: // NBSP
            {
                cache[0]=0xC2;
                cache[1]=0xA0;
                break;
            }
            case 0xA9: //©
            {
                cache[0]=0xC2;
                cache[1]=0xA9;
                break;
            }

            case 0xBC: //Ľ
            {
                cache[0]=0xC4;
                cache[1]=0xbd;
                break;
            }
            case 0xB4: //´
            {
                cache[0]=0xC2;
                cache[1]=0xB4;
                break;
            }
            case 0xBE: //ľ
            {
                cache[0]=0xC4;
                cache[1]=0xBE;
                break;
            }

            case 0xC1: //Á
            {
                cache[0]=0xC3;
                cache[1]=0x81;
                break;
            }
            case 0xC4: //Ä
            {
                cache[0]=0xC3;
                cache[1]=0x84;
                break;
            }
            case 0xC5: //Å
            {
                cache[0]=0xC3;
                cache[1]=0x85;
                break;
            }
            case 0xC8: //Č
            {
                cache[0]=0xC4;
                cache[1]=0x8C;
                break;
            }
            case 0xC9: //É
            {
                cache[0]=0xC3;
                cache[1]=0x89;
                break;
            }
            case 0xCC: //Ě
            {
                cache[0]=0xC4;
                cache[1]=0x9A;
                break;
            }
            case 0xCD: //Í
            {
                cache[0]=0xC3;
                cache[1]=0x8D;
                break;
            }
            case 0xCF: //Ď
            {
                cache[0]=0xC4;
                cache[1]=0x8E;
                break;
            }

            case 0xD2: //Ň
            {
                cache[0]=0xC5;
                cache[1]=0x87;
                break;
            }
            case 0xD3: //Ó
            {
                cache[0]=0xC3;
                cache[1]=0x93;
                break;
            }
            case 0xD6: //Ö
            {
                cache[0]=0xC3;
                cache[1]=0x96;
                break;
            }
            case 0xD8: //Ř
            {
                cache[0]=0xC5;
                cache[1]=0x98;
                break;
            }
            case 0xD9: //Ů
            {
                cache[0]=0xC5;
                cache[1]=0xAE;
                break;
            }
            case 0xDA: //Ú
            {
                cache[0]=0xC3;
                cache[1]=0x9A;
                break;
            }
            case 0xDD: //Ý
            {
                cache[0]=0xC3;
                cache[1]=0x9D;
                break;
            }
            case 0xE0: //ŕ
            {
                cache[0]=0xC5;
                cache[1]=0x95;
                break;
            }
            case 0xE1: //á
            {
                cache[0]=0xC3;
                cache[1]=0xA1;
                break;
            }
            case 0xE4: //á
            {
                cache[0]=0xC3;
                cache[1]=0xA4;
                break;
            }
            case 0xE5: //ĺ
            {
                cache[0]=0xC4;
                cache[1]=0xBA;
                break;
            }
            case 0xE6: //ć
            {
                cache[0]=0xC4;
                cache[1]=0x87;
                break;
            }
            case 0xE8: //č
            {
                cache[0]=0xC4;
                cache[1]=0x8D;
                break;
            }
            case 0xE9: //é
            {
                cache[0]=0xC3;
                cache[1]=0xA9;
                break;
            }
            case 0xEB: //ë
            {
                cache[0]=0xC3;
                cache[1]=0xab;
                break;
            }
            case 0xEC: //ě
            {
                cache[0]=0xC4;
                cache[1]=0x9B;
                break;
            }
            case 0xED: //í
            {
                cache[0]=0xC3;
                cache[1]=0xAD;
                break;
            }
            case 0xEF: //ď
            {
                cache[0]=0xC4;
                cache[1]=0x8F;
                break;
            }

            case 0xF1: //ń
            {
                cache[0]=0xC5;
                cache[1]=0x84;
                break;
            }
            case 0xF2: //ň
            {
                cache[0]=0xC5;
                cache[1]=0x88;
                break;
            }
            case 0xF3: //ó
            {
                cache[0]=0xC3;
                cache[1]=0xB3;
                break;
            }
            case 0xF6: //Ö
            {
                cache[0]=0xC3;
                cache[1]=0xb6;
                break;
            }
            case 0xF8: //ř
            {
                cache[0]=0xC5;
                cache[1]=0x99;
                break;
            }
            case 0xF9: //ů
            {
                cache[0]=0xC5;
                cache[1]=0xAF;
                break;
            }
            case 0xFA: //ú
            {
                cache[0]=0xC3;
                cache[1]=0xBA;
                break;
            }
            case 0xFD: //ý
            {
                cache[0]=0xC3;
                cache[1]=0xBD;
                break;
            }

            default: {
                cache[0]=0xEF;
                cache[1]=0xBF;
                cache[2]=0xBD;
                cacheLength = 3;
                System.err.printf("Unknown char to parse: %d %x %c%n", c, c, ((char)c));
                break;
                //throw new IllegalStateException("Unknown char to parse: " + c + "(" + ((char)c) + ")");
            }
        }
    }

/*
    private int[] __convert() throws IOException {
        switch (zn) {
            case 0x8A: //Š
            {
                cache[0]=0xC5, 0xA0};
            case 0x8E: //Ž
            {
                cache[0]=0xC5, 0xBD};
            case 0x8D: //Ť
            {
                cache[0]=0xC5, 0xA4};

            case 0x9A: //š
            {
                cache[0]=0xC5, 0xA1};
            case 0x9E: //ž
            {
                cache[0]=0xC5, 0xBE};
            case 0x9D: //ť
            {
                cache[0]=0xC5, 0xA5};

            case 0xBC: //Ľ
            {
                cache[0]=0xC4, 0xbd};

            case 0xC1: //Á
            {
                cache[0]=0xC3, 0x81};
            case 0xC8: //Č
            {
                cache[0]=0xC4, 0x8C};
            case 0xC9: //É
            {
                cache[0]=0xC3, 0x89};
            case 0xCC: //Ě
            {
                cache[0]=0xC4, 0x9A};
            case 0xCD: //Í
            {
                cache[0]=0xC3, 0x8D};
            case 0xCF: //Ď
            {
                cache[0]=0xC4, 0x8E};

            case 0xD2: //Ň
            {
                cache[0]=0xC5, 0x87};
            case 0xD3: //Ó
            {
                cache[0]=0xC3, 0x93};
            case 0xD8: //Ř
            {
                cache[0]=0xC5, 0x98};
            case 0xD9: //Ů
            {
                cache[0]=0xC5, 0xAE};
            case 0xDA: //Ú
            {
                cache[0]=0xC3, 0x9A};
            case 0xDD: //Ý
            {
                cache[0]=0xC3, 0x9D};

            case 0xE1: //á
            {
                cache[0]=0xC3, 0xA1};
            case 0xE8: //č
            {
                cache[0]=0xC4, 0x8D};
            case 0xE9: //é
            {
                cache[0]=0xC3, 0xA9};
            case 0xEB: //ë
            {
                cache[0]=0xC3, 0xab};
            case 0xEC: //ě
            {
                cache[0]=0xC4, 0x9B};
            case 0xED: //í
            {
                cache[0]=0xC3, 0xAD};
            case 0xEF: //ď
            {
                cache[0]=0xC4, 0x8F};


            case 0xF2: //ň
            {
                cache[0]=0xC5, 0x88};
            case 0xF3: //ó
            {
                cache[0]=0xC3, 0xB3};
            case 0xF8: //ř
            {
                cache[0]=0xC5, 0x99};
            case 0xF9: //ů
            {
                cache[0]=0xC5, 0xAF};
            case 0xFA: //ú
            {
                cache[0]=0xC3, 0xBA};
            case 0xFD: //ý
            {
                cache[0]=0xC3, 0xBD};

            case 0xB4: //´
            {
                if (pzn != 0xC2) {
                {
                cache[0]=0xC2, 0xB4};
                }
                break;
            }
            case 0xBE: //ľ
            {
                if (pzn != 0xC5 && pzn != 0xC4) {
                {
                cache[0]=0xC4, 0xBE};
                }
                break;
            }
            case 0x93: //“
            {
                if (pzn != 0xC3 && pzn != 0x80) {
                {
                cache[0]=0xE2, 0x80, 0x9C};
                }
                break;
            }
            case 0x96: //–
            {
            {
                cache[0]=0xE2, 0x80, 0x93};
            }
            case 0xA9: //©
            {
                if (pzn != 0xC3 && pzn != 0xC2) {
                {
                cache[0]=0xC2, 0xA9};
                }
                break;
            }

            case 0xC2: // konflikt
            case 0xC3: // konflikt
            case 0xC4: // konflikt
            case 0xC5: // konflikt
            {
                int nzn = is.read();
                __readNext();
                if (zn < 0xC0)// konflikt
                {
                {
                cache[0]=pzn, zn};
                } else {
                    int[] converted = __convert();
                    int[] ret = new int[converted.length + 1];
                    ret[0] = pzn;
                    System.arraycopy(converted, 0, ret, 1, converted.length);
                    return ret;
                }
            }

            default: {
            {
                cache[0]=zn};
            }
        }
    }
*/

    private final int[] cache = new int[10];
    private int cacheHead = 0;
    private int cacheLength = 0;

    @Override
    public int read() throws IOException {
        if(cacheHead < cacheLength){
            return cache[cacheHead++];
        }

        int znak = is.read();
        int length = utfLength(znak);

        if(length == 0){
            convertANSI(znak);
            return cache[cacheHead++];
        }else if(length == 1){
            return znak;
        }else{
            is.mark(length);
            int read = is.read(buffer, 1, length-1);
            if(read != length-1){
                is.reset();
                convertANSI(znak);
                return cache[cacheHead++];
            }
            if(checkUtf(buffer, length)){
                cache[0] = znak;
                for(int i=0; i<length; i++){
                    cache[i] = buffer[i];
                }
                cacheHead = 0;
                cacheLength = length;
                return cache[cacheHead++];
            }else{
                is.reset();
                convertANSI(znak);
                return cache[cacheHead++];
            }
        }
    }


    private int utfLength(int zn){
        if(zn <= 0x7F) // ASCII
            return 1;
        if((zn & 0b1110_0000) == 0b1100_0000)
            return 2;
        if((zn & 0b1111_0000) == 0b1110_0000)
            return 3;
        if((zn & 0b1111_1000) == 0b1111_0000)
            return 4;
        if((zn & 0b1111_1100) == 0b1111_1000)
            return 5;
        if((zn & 0b1111_1110) == 0b1111_1100)
            return 6;
        return 0;
    }

    private boolean checkUtf(byte[] buffer, int length) throws IOException{
        for(int i=1; i<length; i++){
            if((buffer[i] & 0b1100_0000) != 0b1000_0000)
                return false;
        }
        return true;
    }

}
