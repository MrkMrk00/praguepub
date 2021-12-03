package cz.vse.praguePub.db;


import static cz.vse.praguePub.db.AesUtil.fillTo16Chars;

public class DecryptionExample {

    public static void main(String[] args) {

        var au = new AesUtil(128, 1000);
        var res = au.decrypt(
                "fbcfcdb5bae8eedf3fa4702bcd1d7d89::5bb737fe5c00850297cd20ef22a72920::yh8TxrKmBUN8mCoPkyNPeg==",
                fillTo16Chars("1234abc"));
        System.out.println(res);
    }
}
