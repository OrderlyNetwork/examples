package org.web3j;

import java.time.Instant;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterExample {
   public static JSONObject MESSAGE_TYPES = new JSONObject("""
         {
            "EIP712Domain": [
               {"name": "name", "type": "string"},
               {"name": "version", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "verifyingContract", "type": "address"},
            ],
            "Registration": [
               {"name": "brokerId", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "timestamp", "type": "uint64"},
               {"name": "registrationNonce", "type": "uint256"},
            ],
            "AddOrderlyKey": [
               {"name": "brokerId", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "orderlyKey", "type": "string"},
               {"name": "scope", "type": "string"},
               {"name": "timestamp", "type": "uint64"},
               {"name": "expiration", "type": "uint64"},
            ],
            "Withdraw": [
               {"name": "brokerId", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "receiver", "type": "address"},
               {"name": "token", "type": "string"},
               {"name": "amount", "type": "uint256"},
               {"name": "withdrawNonce", "type": "uint64"},
               {"name": "timestamp", "type": "uint64"},
            ],
            "SettlePnl": [
               {"name": "brokerId", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "settleNonce", "type": "uint64"},
               {"name": "timestamp", "type": "uint64"},
            ],
         }""");

   public static JSONObject OFF_CHAIN_DOMAIN = new JSONObject("""
         {
            "name": "Orderly",
            "version": "1",
            "chainId": 421613,
            "verifyingContract": "0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC",
         }""");

   public static void main(String[] args) throws Exception {
      String baseUrl = "https://testnet-api-evm.orderly.org";
      String brokerId = "woofi_dex";
      int chainId = 421613;

      Dotenv dotenv = Dotenv.load();
      String pk = dotenv.get("PRIVATE_KEY");
      Credentials credentials = Credentials.create(ECKeyPair.create(Hex.decodeHex(pk)));
      OkHttpClient client = new OkHttpClient();

      Request nonceReq = new Request.Builder()
            .url(baseUrl + "/v1/registration_nonce")
            .build();
      String nonceRes;
      try (Response response = client.newCall(nonceReq).execute()) {
         nonceRes = response.body().string();
      }
      JSONObject nonceObj = new JSONObject(nonceRes);

      String registrationNonce = nonceObj.getJSONObject("data").getString("registration_nonce");

      JSONObject registerMessage = new JSONObject();
      registerMessage.put("brokerId", brokerId);
      registerMessage.put("chainId", chainId);
      registerMessage.put("timestamp", Instant.now().toEpochMilli());
      registerMessage.put("registrationNonce", registrationNonce);

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("types", RegisterExample.MESSAGE_TYPES);
      jsonObject.put("primaryType", "Registration");
      jsonObject.put("domain", RegisterExample.OFF_CHAIN_DOMAIN);
      jsonObject.put("message", registerMessage);

      Sign.SignatureData signature = Sign.signTypedData(jsonObject.toString(), credentials.getEcKeyPair());

      JSONObject jsonBody = new JSONObject();
      jsonBody.put("message", registerMessage);
      jsonBody.put("signature", RegisterExample.signatureToHashString(signature));
      jsonBody.put("userAddress", credentials.getAddress());
      RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json"));
      Request registerReq = new Request.Builder()
            .url(baseUrl + "/v1/register_account")
            .post(body)
            .build();
      String registerRes;
      try (Response response = client.newCall(registerReq).execute()) {
         registerRes = response.body().string();
      }
      JSONObject registerObj = new JSONObject(registerRes);

      String orderlyAccountId = registerObj.getJSONObject("data").getString("account_id");
      System.out.println("orderlyAccountId: " + orderlyAccountId);
   }

   public static String signatureToHashString(Sign.SignatureData signature) {
      byte[] retval = new byte[65];
      System.arraycopy(signature.getR(), 0, retval, 0, 32);
      System.arraycopy(signature.getS(), 0, retval, 32, 32);
      System.arraycopy(signature.getV(), 0, retval, 64, 1);
      return Numeric.toHexString(retval);
   }
}
