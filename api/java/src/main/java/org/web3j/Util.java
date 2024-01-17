package org.web3j;

import org.bitcoinj.base.Base58;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;

public abstract class Util {
   public static String signatureToHashString(Sign.SignatureData signature) {
      byte[] retval = new byte[65];
      System.arraycopy(signature.getR(), 0, retval, 0, 32);
      System.arraycopy(signature.getS(), 0, retval, 32, 32);
      System.arraycopy(signature.getV(), 0, retval, 64, 1);
      return Numeric.toHexString(retval);
   }

   public static String encodePublicKey(EdDSAPrivateKey privateKey) {
      return "ed25519:" + Base58.encode(privateKey.getAbyte());
   }
}
