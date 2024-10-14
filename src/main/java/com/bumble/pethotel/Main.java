package com.bumble.pethotel;

import java.util.*;

import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;

public class Main {

    static String checksumKey = "142b85085f3639fab02a1f1045b0e52d3de6daf58aabcc1f2a3e857f9efca985";

    static String transaction = "{'orderCode':123,'amount':3000,'description':'VQRIO123','accountNumber':'12345678','reference':'TF230204212323','transactionDateTime':'2023-02-04 18:25:00','currency':'VND','paymentLinkId':'124c33293c43417ab7879e14c8d9eb18','code':'00','desc':'Thành công','counterAccountBankId':'','counterAccountBankName':'','counterAccountName':'','counterAccountNumber':'','virtualAccountName':'','virtualAccountNumber':''}";

    static String transactionSignature = "07b78831f904a463bdd7f39f7d5a5d60815b24dc6547cb63243d9fcf85e57406";

    public static Boolean isValidData(String transaction, String transactionSignature) {
        try {
            JSONObject jsonObject = new JSONObject(transaction);
            Iterator<String> sortedIt = sortedIterator(jsonObject.keys(), (a, b) -> a.compareTo(b));

            StringBuilder transactionStr = new StringBuilder();
            while (sortedIt.hasNext()) {
                String key = sortedIt.next();
                String value = jsonObject.get(key).toString();
                transactionStr.append(key);
                transactionStr.append('=');
                transactionStr.append(value);
                if (sortedIt.hasNext()) {
                    transactionStr.append('&');
                }
            }

            String signature = new HmacUtils("HmacSHA256", checksumKey).hmacHex(transactionStr.toString());
            return signature.equals(transactionSignature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Iterator<String> sortedIterator(Iterator<?> it, Comparator<String> comparator) {
        List<String> list = new ArrayList<String>();
        while (it.hasNext()) {
            list.add((String) it.next());
        }

        Collections.sort(list, comparator);
        return list.iterator();
    }

    public static void main(String[] args) {
        System.out.println(isValidData(transaction, transactionSignature));
    }
}
