import { getPublicKeyAsync, signAsync } from '@noble/ed25519';
import { encodeBase58 } from 'ethers';

export async function signAndSendRequest(
  orderlyAccountId: string,
  privateKey: Uint8Array | string,
  input: URL | string,
  init?: RequestInit | undefined
): Promise<Response> {
  const timestamp = Date.now();
  const encoder = new TextEncoder();

  const url = new URL(input);
  let message = `${String(timestamp)}${init?.method ?? 'GET'}${url.pathname}${url.search}`;
  if (init?.body) {
    message += init.body;
  }
  const orderlySignature = await signAsync(encoder.encode(message), privateKey);

  return fetch(input, {
    headers: {
      'Content-Type':
        init?.method !== 'GET' && init?.method !== 'DELETE'
          ? 'application/json'
          : 'application/x-www-form-urlencoded',
      'orderly-timestamp': String(timestamp),
      'orderly-account-id': orderlyAccountId,
      'orderly-key': `ed25519:${encodeBase58(await getPublicKeyAsync(privateKey))}`,
      'orderly-signature': Buffer.from(orderlySignature).toString('base64url'),
      ...(init?.headers ?? {})
    },
    ...(init ?? {})
  });
}
