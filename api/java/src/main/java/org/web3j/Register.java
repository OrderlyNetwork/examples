package org.web3j;

import java.io.IOException;
import java.time.Instant;
import java.security.*;

import org.json.JSONObject;
import org.web3j.crypto.*;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register {

   public final Config config;

   private OkHttpClient client;
   private Credentials credentials;

   public Register(Config config, OkHttpClient client, Credentials credentials) {
      this.config = config;
      this.client = client;
      this.credentials = credentials;
   }

   public String registerAccount() throws IOException {
      Request nonceReq = new Request.Builder()
            .url(config.baseUrl + "/v1/registration_nonce")
            .build();

      String nonceRes;
      try (Response response = client.newCall(nonceReq).execute()) {
         nonceRes = response.body().string();
      }
      JSONObject nonceObj = new JSONObject(nonceRes);

      String registrationNonce = nonceObj.getJSONObject("data").getString("registration_nonce");

      JSONObject registerMessage = new JSONObject();
      registerMessage.put("brokerId", config.brokerId);
      registerMessage.put("chainId", config.chainId);
      registerMessage.put("timestamp", Instant.now().toEpochMilli());
      registerMessage.put("registrationNonce", registrationNonce);

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("types", Eip712.MESSAGE_TYPES);
      jsonObject.put("primaryType", "Registration");
      jsonObject.put("domain", Eip712.OFF_CHAIN_DOMAIN);
      jsonObject.put("message", registerMessage);

      Sign.SignatureData signature = Sign.signTypedData(jsonObject.toString(), credentials.getEcKeyPair());

      JSONObject jsonBody = new JSONObject();
      jsonBody.put("message", registerMessage);
      jsonBody.put("signature", Util.signatureToHashString(signature));
      jsonBody.put("userAddress", credentials.getAddress());
      RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json"));
      Request registerReq = new Request.Builder()
            .url(config.baseUrl + "/v1/register_account")
            .post(body)
            .build();
      String registerRes;
      try (Response response = client.newCall(registerReq).execute()) {
         registerRes = response.body().string();
      }
      System.out.println("register_account response: " + registerRes);
      JSONObject registerObj = new JSONObject(registerRes);

      return registerObj.getJSONObject("data").getString("account_id");
   }

   public KeyPair addAccessKey()
         throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
      KeyPairGenerator keyGen = new KeyPairGenerator();
      KeyPair keyPair = keyGen.generateKeyPair();
      String orderlyKey = Util.encodePublicKey((EdDSAPrivateKey) keyPair.getPrivate());

      JSONObject addKeyMessage = new JSONObject();
      long timestamp = Instant.now().toEpochMilli();
      addKeyMessage.put("brokerId", config.brokerId);
      addKeyMessage.put("chainId", config.chainId);
      addKeyMessage.put("scope", "read,trading");
      addKeyMessage.put("orderlyKey", orderlyKey);
      addKeyMessage.put("timestamp", timestamp);
      addKeyMessage.put("expiration", timestamp + 1_000 * 60 * 60 * 24 * 365); // 1 year

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("types", Eip712.MESSAGE_TYPES);
      jsonObject.put("primaryType", "AddOrderlyKey");
      jsonObject.put("domain", Eip712.OFF_CHAIN_DOMAIN);
      jsonObject.put("message", addKeyMessage);

      Sign.SignatureData signature = Sign.signTypedData(jsonObject.toString(), credentials.getEcKeyPair());

      JSONObject jsonBody = new JSONObject();
      jsonBody.put("message", addKeyMessage);
      jsonBody.put("signature", Util.signatureToHashString(signature));
      jsonBody.put("userAddress", credentials.getAddress());
      RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json"));
      Request addKeyReq = new Request.Builder()
            .url(config.baseUrl + "/v1/orderly_key")
            .post(body)
            .build();
      String addKeyRes;
      try (Response response = client.newCall(addKeyReq).execute()) {
         addKeyRes = response.body().string();
      }
      System.out.println("orderly_key response: " + addKeyRes);

      return keyPair;
   }
}
