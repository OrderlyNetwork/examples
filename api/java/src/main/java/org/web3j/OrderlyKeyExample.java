package org.web3j;

import java.security.*;
import java.time.Instant;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import io.github.cdimascio.dotenv.Dotenv;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderlyKeyExample {
   public static JSONObject MESSAGE_TYPES = new JSONObject("""
         {
            "EIP712Domain": [
               {"name": "name", "type": "string"},
               {"name": "version", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "verifyingContract", "type": "address"},
            ],
            "AddOrderlyKey": [
               {"name": "brokerId", "type": "string"},
               {"name": "chainId", "type": "uint256"},
               {"name": "orderlyKey", "type": "string"},
               {"name": "scope", "type": "string"},
               {"name": "timestamp", "type": "uint64"},
               {"name": "expiration", "type": "uint64"},
            ],
         }""");

   public static JSONObject OFF_CHAIN_DOMAIN = new JSONObject("""
         {
            "name": "Orderly",
            "version": "1",
            "chainId": 421614,
            "verifyingContract": "0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC",
         }""");

   public static void main(String[] args) throws Exception {
      String baseUrl = "https://testnet-api-evm.orderly.org";
      String brokerId = "woofi_dex";
      int chainId = 421614;

      Dotenv dotenv = Dotenv.load();
      String pk = dotenv.get("PRIVATE_KEY");
      Credentials credentials = Credentials.create(ECKeyPair.create(Hex.decodeHex(pk)));
      OkHttpClient client = new OkHttpClient();

      KeyPairGenerator keyGen = new KeyPairGenerator();
      KeyPair keyPair = keyGen.generateKeyPair();
      String orderlyKey = Util.encodePublicKey((EdDSAPrivateKey) keyPair.getPrivate());

      JSONObject addKeyMessage = new JSONObject();
      long timestamp = Instant.now().toEpochMilli();
      addKeyMessage.put("brokerId", brokerId);
      addKeyMessage.put("chainId", chainId);
      addKeyMessage.put("scope", "read,trading");
      addKeyMessage.put("orderlyKey", orderlyKey);
      addKeyMessage.put("timestamp", timestamp);
      addKeyMessage.put("expiration", timestamp + 1_000 * 60 * 60 * 24 * 365); // 1 year

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("types", RegisterExample.MESSAGE_TYPES);
      jsonObject.put("primaryType", "AddOrderlyKey");
      jsonObject.put("domain", RegisterExample.OFF_CHAIN_DOMAIN);
      jsonObject.put("message", addKeyMessage);

      Sign.SignatureData signature = Sign.signTypedData(jsonObject.toString(), credentials.getEcKeyPair());

      JSONObject jsonBody = new JSONObject();
      jsonBody.put("message", addKeyMessage);
      jsonBody.put("signature", Util.signatureToHashString(signature));
      jsonBody.put("userAddress", credentials.getAddress());
      RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json"));
      Request addKeyReq = new Request.Builder()
            .url(baseUrl + "/v1/orderly_key")
            .post(body)
            .build();
      String addKeyRes;
      try (Response response = client.newCall(addKeyReq).execute()) {
         addKeyRes = response.body().string();
      }
      System.out.println("orderly_key response: " + addKeyRes);
   }

   public static String signatureToHashString(Sign.SignatureData signature) {
      byte[] retval = new byte[65];
      System.arraycopy(signature.getR(), 0, retval, 0, 32);
      System.arraycopy(signature.getS(), 0, retval, 32, 32);
      System.arraycopy(signature.getV(), 0, retval, 64, 1);
      return Numeric.toHexString(retval);
   }
}
