package org.web3j;

import java.security.spec.PKCS8EncodedKeySpec;

import io.github.cdimascio.dotenv.Dotenv;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.bitcoinj.base.Base58;
import org.json.JSONObject;

public class AuthenticationExample {
   public static void main(String[] args) throws Exception {
      String baseUrl = "https://testnet-api-evm.orderly.org";
      String orderlyAccountId = "<orderly-account-id>";

      Dotenv dotenv = Dotenv.load();
      OkHttpClient client = new OkHttpClient();

      String key = dotenv.get("ORDERLY_SECRET");
      PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec(Base58.decode(key));
      EdDSAPrivateKey orderlyKey = new EdDSAPrivateKey(encoded);

      Signer signer = new Signer(baseUrl, orderlyAccountId, orderlyKey);

      JSONObject json = new JSONObject();
      json.put("symbol", "PERP_ETH_USDC");
      json.put("order_type", "MARKET");
      json.put("order_quantity", 0.01);
      json.put("side", "BUY");
      Request req = signer.createSignedRequest("/v1/order", "POST", json);
      String res;
      try (Response response = client.newCall(req).execute()) {
         res = response.body().string();
      }
      JSONObject obj = new JSONObject(res);
   }
}
