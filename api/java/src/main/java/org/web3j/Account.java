package org.web3j;

import java.io.IOException;
import java.time.Instant;
import java.security.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Account {

   public final Config config;

   private OkHttpClient client;
   private Signer signer;
   private Credentials credentials;

   public Account(Config config, OkHttpClient client, Signer signer, Credentials credentials) {
      this.config = config;
      this.client = client;
      this.signer = signer;
      this.credentials = credentials;
   }

   /**
    * Get the current summary of user token holdings.
    * 
    * @throws OrderlyClientException
    * @throws InvalidKeyException
    * @throws SignatureException
    * @throws IOException
    */
   public JSONArray getClientHolding()
         throws OrderlyClientException, InvalidKeyException, SignatureException, IOException {
      Request req = signer.createSignedRequest("/v1/client/holding");
      String res;
      try (Response response = client.newCall(req).execute()) {
         res = response.body().string();
      }
      System.out.println("client holding response: " + res);
      JSONObject obj = new JSONObject(res);
      return obj.getJSONObject("data").getJSONArray("holding");
   }

   public int getWithdrawNonce() throws InvalidKeyException, SignatureException, OrderlyClientException, IOException {
      Request req = signer.createSignedRequest("/v1/withdraw_nonce");
      String res;
      try (Response response = client.newCall(req).execute()) {
         res = response.body().string();
      }
      JSONObject obj = new JSONObject(res);
      return obj.getJSONObject("data").getInt("withdraw_nonce");
   }

   public JSONObject withdraw(String token, String amount)
         throws InvalidKeyException, SignatureException, OrderlyClientException, IOException {
      int withdrawNonce = getWithdrawNonce();

      JSONObject message = new JSONObject();
      message.put("brokerId", config.brokerId);
      message.put("chainId", config.chainId);
      message.put("receiver", credentials.getAddress());
      message.put("token", token);
      message.put("amount", amount);
      message.put("withdrawNonce", withdrawNonce);
      message.put("timestamp", Instant.now().toEpochMilli());

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("types", Eip712.MESSAGE_TYPES);
      jsonObject.put("primaryType", "Withdraw");
      jsonObject.put("domain", Eip712.ON_CHAIN_DOMAIN);
      jsonObject.put("message", message);

      Sign.SignatureData signature = Sign.signTypedData(jsonObject.toString(), credentials.getEcKeyPair());

      JSONObject json = new JSONObject();
      json.put("message", message);
      json.put("signature", Util.signatureToHashString(signature));
      json.put("userAddress", credentials.getAddress());
      json.put("verifyingContract", Eip712.ON_CHAIN_DOMAIN.getString("verifyingContract"));
      System.out.println(json);
      Request req = signer.createSignedRequest("/v1/withdraw_request", "POST", json);
      String res;
      try (Response response = client.newCall(req).execute()) {
         res = response.body().string();
      }
      JSONObject obj = new JSONObject(res);

      return obj;
   }
}
