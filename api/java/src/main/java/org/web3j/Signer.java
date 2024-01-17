package org.web3j;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.time.Instant;
import java.util.Base64;

import org.json.JSONObject;

import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Signer {
   public final String baseUrl;

   public String accountId;
   public EdDSAPrivateKey privateKey;

   public Signer(String baseUrl) {
      this.baseUrl = baseUrl;
   }

   public Signer(String baseUrl, String accountId) {
      this.baseUrl = baseUrl;
      this.accountId = accountId;
   }

   public Signer(String baseUrl, String accountId, EdDSAPrivateKey privateKey) {
      this.baseUrl = baseUrl;
      this.accountId = accountId;
      this.privateKey = privateKey;
   }

   public Request createSignedRequest(String url)
         throws SignatureException, UnsupportedEncodingException, InvalidKeyException, OrderlyClientException {
      checkIsInitialized();
      return createSignedRequest(url, "GET", null);
   }

   public Request createSignedRequest(String url, String method, JSONObject json)
         throws OrderlyClientException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
      checkIsInitialized();
      if (method == "GET") {
         return createSignedGetRequest(url);
      } else if (method == "POST" && json != null) {
         return createSignedPostRequest(url, json);
      } else {
         throw new IllegalArgumentException();
      }
   }

   private void checkIsInitialized() throws OrderlyClientException {
      if (accountId == null) {
         throw OrderlyClientExceptionReason.ACCOUNT_UNINITIALIZED.asException();
      }
      if (privateKey == null) {
         throw OrderlyClientExceptionReason.KEYPAIR_UNINITIALIZED.asException();
      }
   }

   private Request createSignedGetRequest(String url)
         throws SignatureException, UnsupportedEncodingException, InvalidKeyException {
      long timestamp = Instant.now().toEpochMilli();
      String message = "" + timestamp + "GET" + url;

      EdDSAEngine signature = new EdDSAEngine();
      signature.initSign(privateKey);
      byte[] orderlySignature = signature.signOneShot(message.getBytes("UTF-8"));

      return new Request.Builder()
            .url(baseUrl + url)
            .addHeader("Content-Type", "x-www-form-urlencoded")
            .addHeader("orderly-timestamp", "" + timestamp)
            .addHeader("orderly-account-id", accountId)
            .addHeader("orderly-key", Util.encodePublicKey(privateKey))
            .addHeader("orderly-signature", Base64.getUrlEncoder().encodeToString(orderlySignature))
            .get()
            .build();
   }

   private Request createSignedPostRequest(String url, JSONObject json)
         throws SignatureException, UnsupportedEncodingException, InvalidKeyException {
      RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

      long timestamp = Instant.now().toEpochMilli();
      String message = "" + timestamp + "POST" + url + json.toString();

      EdDSAEngine signature = new EdDSAEngine();
      signature.initSign(privateKey);
      byte[] orderlySignature = signature.signOneShot(message.getBytes("UTF-8"));

      return new Request.Builder()
            .url(baseUrl + url)
            .addHeader("Content-Type", "application/json")
            .addHeader("orderly-timestamp", "" + timestamp)
            .addHeader("orderly-account-id", accountId)
            .addHeader("orderly-key", Util.encodePublicKey(privateKey))
            .addHeader("orderly-signature", Base64.getUrlEncoder().encodeToString(orderlySignature))
            .post(body)
            .build();
   }
}
