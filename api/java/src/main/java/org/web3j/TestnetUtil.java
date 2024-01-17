package org.web3j;

import java.io.IOException;

import org.json.JSONObject;
import org.web3j.crypto.Credentials;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class TestnetUtil {
   public static void mintTestUSDC(OkHttpClient client, Config config, Credentials credentials) throws IOException {
      JSONObject jsonBody = new JSONObject();
      jsonBody.put("broker_id", config.brokerId);
      jsonBody.put("chain_id", "" + config.chainId);
      jsonBody.put("user_address", credentials.getAddress());
      RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json"));
      Request req = new Request.Builder()
            .url("https://testnet-operator-evm.orderly.org/v1/faucet/usdc")
            .post(body)
            .build();
      String res;
      try (Response response = client.newCall(req).execute()) {
         res = response.body().string();
      }
      System.out.println("mintTestUSDC response: " + res);
   }
}
