
package com.ggj.java.qiniu.speciall;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptionUtil {
    private static final String CipherMode = "AES/CBC/PKCS5Padding";
    public static final String STRING_CHARSET_NAME = "UTF-8";
    public static final String AES_IV = "5efd3f6060emaomi";
    public static final String AES_PWD = "625202f9149maomi";

    private static SecretKeySpec createKey(String key) {
        byte[] data = null;
        if (key == null) {
            key = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(key);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        try {
            data = sb.toString().getBytes(STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, "AES");
    }

    private static IvParameterSpec createIV(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(password);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        try {
            data = sb.toString().getBytes(STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }

    public static byte[] encrypt(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(1, key, createIV(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = content.getBytes(STRING_CHARSET_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byte2hex(encrypt(data, password, iv));
    }

    public static byte[] decrypt(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(2, key, createIV(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = hex2byte(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = decrypt(data, password, iv);
        if (data == null) {
            return null;
        }
        try {
            return new String(data, STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        String tmp = "";
        for (byte b2 : b) {
            tmp = Integer.toHexString(b2 & 255);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] hex2byte(String inputString) {
        if (inputString == null || inputString.length() < 2) {
            return new byte[0];
        }
        inputString = inputString.toLowerCase();
        int l = inputString.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; i++) {
            result[i] = (byte) (Integer.parseInt(inputString.substring(i * 2, (i * 2) + 2), 16)
                    & 255);
        }
        return result;
    }

    public static String decryptMain(String result) {
       return decrypt(result,AES_PWD,AES_IV);
    }
    public static String encryptMain(String result) {
       return encrypt(result,AES_PWD,AES_IV);
    }

    public static void main(String[] args) {
        System.out.println(decryptMain("A8F1D656E68A75031DF8A255B0096B48DDD3AD12D43CBDD851C8D3677602E8958525C374247AB844DBA55E6CCACA098D814E4191ED7169688312153791656E1FD1FC9D34FB7BD1F71A5BFFEBC7566B8E4970E001D0D658D65D57982B85394847134A03A961E3FA97C3D35AE83368D8BADE365E406CD05EDF9B93F36DD46B6E0C9961EEE1768D6BF71DA54E36444B08769EA43E480F950B10C5407715666289443A311DEA79E81EF87FC8CFC45E3C5D1B12D2657581DB8C9160C84D6CF01C2C2A5155E43D3BB7D6C3CFDAAE1315655A75F1FB5B822234086C653C2E0E59E8AE266AE0AD02BDC97E04DCA48AA58139F8B19A3BED7480B5800CCCC0CD9ECD98651A1DFBB75AF20269BD723E09EFAF44DAA3F4EC7912FD173CC8339B7AF186FA5CE68F9A153528D8B5D7BCB5AFC37760F7AA796C19C538F7C738A3A4A52C61E6FC68396009DBD9B5B9F510003B44B1B2DD31A6CD822C7CAA58160184BCA40C5A517179702542A8D2B7459A73E320A7319A60E0BEDBF4FD9153A8E2BFCC59CD803B27381E585752E6E586D47C450A7EEF3C2EF295E5148E3D50F6F4BD1CF5415C97BC86FCE304C60CC1B537AB4A3FFAF0FB133FE38B4265F29A1E6FFE3772830ED0299912E8EAF8A234E86BA1690821C7C60392A1F70FA30A78C2E841C848291620F6C4592B44BB1489847F3C500A989579DC4C93AF259A1CBB98DDC14C4357DAF8DE8E86E334412ABF6E5725746E2C31FDD61BB23176C7B7D8BF2E2D61E05396E517C161612C7A7EED315C888C544748B4CDC303F822110E6B54D3827829044052984BAAA3F7ECD76E1E32E156AB26A6A91F58413CB2314644AA0A9F9647537D11B79D833365050CDC749A822C2D55F48140904519730B462D2DBE384C1BB5D5C1CA9B5719F648E12F62381F33C877E8D27750DA04C1B1AE36580FAB4C2D45B540F0471819EBAC478DC865638773B1F33B0CF44326749C18B10902F767BCC030FA7DB2BDD4A9BA1EB2A970209A00DCFFEFB53FC939FBA99B4863A166006CAF4EB9A521CE6A76CAA01FCB23ED9FDFF6B97E219828604B10AF6CD477C024ED40B32FAD55CEF7D711B4617614E76F94D598AAEF6CC3B314A27CA22C2330DFD7EFC30A53FBBF385CAD294A0A064E8D8BF34A88F4BD7EA2F708EF4B0EC66830468355BEAFEC0451596ECF4A5F91157F4979DA4212EBF1E4021FCA097FEC19D9216995429A02500BF9DD2CC561176D4B8BE8DE1C8FCBF1A852AC72F59354048D1144EFFCF690CDEB5052306CAEE859DD2C0B6BE84C6219C2F2646322B2D1E72F1CCB7317DF5CCEFDCAEF95C97CA92DA91754DCF8DE7A1C9E99384A82FCFEDBBD19AD8751E4C0A9DD7EDB80D359EFAB0F1E859BA7E5590F6129702A41FCE45D13E7E85839A2FE1971D2AA1B3C7074E980A94C1BEA97249480A2084184F22AF7E543E6C8FCEC1D4AC4B20689C4D7A2A81400ACE84DD2602F3E39852560A4E93E9458D223187A85ABEFC3AA744AC111C7897A5A5F8C639EF412EDA0792B9834285B7FF46B0E180AB3EB7AB796665170DC5DBF85E6E2F5ECA51A4DB21BC7B7DA9DF6A9CA9BFD290784856052B83DFAD6ECB856BAB8372BF0DE40B4A448E644569C2E469FA59A3C07F1DA45AC793C7D96FDFCDE8C2B568DB22F15AE6AE009205F3A67A20883B799067200A629C9437B2F2188ECBC21E98D807A4181C1F85F324C6586A99082ACC2C986BA417F61899858F2062069BB4EE4AD6DEDDAD7B1DC0295E1ECC54F8204DCE37D308E6A6CAC861D2EB669833F327FD7895BB8DBA4E06B9CFD65387BDE8DF72625EB6F0080E25D77C397EB951EC336F65BE8E8027921B5D35DAD055CA66F36E1C08D9D8ECF277467754250D6D4858C91C610816C1CD30857668392FA8C022941710E4D1A93437CA41ADDA97A45C12AC6495434980FEE304FFD5686DE4137E4149692B56154D464F6DB9CAA9273F5D8CC528F327EFF6F5407D42D444DE1AA233BC17F5716A11B93CE06DD92E6664B97DEFAEE7515CF938D027FBF35DC6CFE37E830828CA8BBC8A76D59D5B068FA35B1D18B99B52CDF7097CA136A2E97699F293D09D14A6239E5EA2F99A5D149FFDBB411C0AC92C09B26D489B4A58749093DD7324E95613E10ACF5B0B741B9FDD2416BDE03600ACD8ACE5DB7E5717393B58013A6D22FF7EA38EECFA703CFC05ACF113127A84180FEC4EDB04F7AC89C8DD9C97349F90EB1BDFB556C5B0E3E8D571EAA8C76C80AC9D12CB7FAFDCE7CBE77EA259969095D9F561DDDCF4248ECE11B3C51EE9DD1E0302E17A459862D6DACA8A64838469A615AF3FE61D80A6FC3B89D9B15B068686378B61CF3BB9ED53FDB8D2609E1167E30E10443652EF0932980F80C73B235907478559DCEB3A4A416F66E58D9EFB6EDAE1FC17114466AEB32535A24A6CA3FC40903A8E7DB869F06E2760CACB2B27E3A3A788D0E4A865DF128AFE56E60A6"));
        System.out.println(decryptMain(""));
    }
}
