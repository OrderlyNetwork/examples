package org.web3j;

import java.io.IOException;
import java.time.Instant;
import java.security.*;

import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PnL {

   public final Config config;

   private OkHttpClient client;
   private Signer signer;
   private Credentials credentials;

   public PnL(Config config, OkHttpClient client, Signer signer, Credentials credentials) {
      this.config = config;
      this.client = client;
      this.signer = signer;
      this.credentials = credentials;
   }

   public int getSettleNonce() throws InvalidKeyException, SignatureException, OrderlyClientException, IOException {
      Request req = signer.createSignedRequest("/v1/settle_nonce");
      String res;
      try (Response response = client.newCall(req).execute()) {
         res = response.body().string();
      }
      JSONObject obj = new JSONObject(res);
      return obj.getJSONObject("data").getInt("settle_nonce");
   }

   public JSONObject settlePnL() throws InvalidKeyException, SignatureException, OrderlyClientException, IOException {
      int settleNonce = getSettleNonce();

      JSONObject message = new JSONObject();
      message.put("brokerId", config.brokerId);
      message.put("chainId", config.chainId);
      message.put("timestamp", Instant.now().toEpochMilli());
      message.put("settleNonce", settleNonce);

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("types", Eip712.MESSAGE_TYPES);
      jsonObject.put("primaryType", "SettlePnl");
      jsonObject.put("domain", Eip712.ON_CHAIN_DOMAIN);
      jsonObject.put("message", message);

      Sign.SignatureData signature = Sign.signTypedData(jsonObject.toString(), credentials.getEcKeyPair());

      JSONObject json = new JSONObject();
      json.put("message", message);
      json.put("signature", Util.signatureToHashString(signature));
      json.put("userAddress", credentials.getAddress());
      json.put("verifyingContract", Eip712.ON_CHAIN_DOMAIN.getString("verifyingContract"));
      Request req = signer.createSignedRequest("/v1/settle_pnl", "POST", json);
      String res;
      try (Response response = client.newCall(req).execute()) {
         res = response.body().string();
      }
      JSONObject obj = new JSONObject(res);

      return obj;
   }
}
