package com.example.graphics.aestest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private static SecretKeySpec secretKey ;
    private static byte[] key ;
    private static byte[] iv;
    private static String decryptedString;
    private static String encryptedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final String strToEncrypt = "My text to encrypt";
        final String strPssword = "encryptorKey";
        setKey(strPssword);

        encrypt(strToEncrypt.trim());

        TextView textView1 = (TextView) findViewById(R.id.text1);
        textView1.setText(strToEncrypt);

        TextView textView2 = (TextView) findViewById(R.id.text2);
        textView2.setText(encryptedString);


        final String strToDecrypt =  encryptedString;
        decrypt(strToDecrypt.trim());

        TextView textView3 = (TextView) findViewById(R.id.text3);
        textView3.setText(decryptedString);
    }

    public static void setKey(String myKey){


        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            System.out.println(key.length);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            secretKey = new SecretKeySpec(key, "AES");
            iv = new byte[]{11,53,63,87,11,69,63,28,0,9,18,99,95,23,45,8};

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    public static String encrypt(String strToEncrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,new IvParameterSpec(iv));
            encryptedString = (Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")),Base64.DEFAULT));

        }
        catch (Exception e)
        {

            System.out.println("Error while encrypting: "+e.toString());
        }
        return null;
    }
    public static String decrypt(String strToDecrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING","BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey,new IvParameterSpec(iv));
            String decoded = new String(cipher.doFinal(Base64.decode(strToDecrypt,Base64.DEFAULT)), Charset.forName("UTF-8"));
            decryptedString = (decoded);

        }
        catch (Exception e)
        {

            System.out.println("Error while decrypting: "+e.toString());
        }
        return null;
    }
}
