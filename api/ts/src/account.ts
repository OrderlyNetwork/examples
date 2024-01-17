import { getPublicKeyAsync, signAsync } from '@noble/ed25519';
import { encodeBase58 } from 'ethers';

import { BASE_URL } from './config';

export async function getClientHolding(orderlyAccountId: string, privateKey: Uint8Array) {
  const timestamp = Date.now();
  const encoder = new TextEncoder();

  const message = `${String(timestamp)}GET/v1/client/holding`;
  const orderlySignature = await signAsync(encoder.encode(message), privateKey);

  const res = await fetch(`${BASE_URL}/v1/client/holding`, {
    headers: {
      'orderly-timestamp': String(timestamp),
      'orderly-account-id': orderlyAccountId,
      'orderly-key': `ed25519:${encodeBase58(await getPublicKeyAsync(privateKey))}`,
      'orderly-signature': Buffer.from(orderlySignature).toString('base64url')
    }
  });
  const json = await res.json();
  console.log('getClientHolding:', json);
}
