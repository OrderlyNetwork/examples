package org.web3j;

import io.github.cdimascio.dotenv.Dotenv;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
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
      EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
      EdDSAPrivateKeySpec encoded = new EdDSAPrivateKeySpec(Base58.decode(key), spec);
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
