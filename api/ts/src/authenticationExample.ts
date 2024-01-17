import bs58 from 'bs58';
import { config } from 'dotenv';
import { webcrypto } from 'node:crypto';

import { signAndSendRequest } from './signer';

// this is only necessary in Node.js to make `@noble/ed25519` dependency work
// eslint-disable-next-line @typescript-eslint/no-explicit-any
if (!globalThis.crypto) globalThis.crypto = webcrypto as any;

config();

async function main() {
  const baseUrl = 'https://testnet-api-evm.orderly.org';
  const orderlyAccountId = '<orderly-account-id>';

  const orderlyKey = bs58.decode(process.env.ORDERLY_SECRET!);

  const res = await signAndSendRequest(orderlyAccountId, orderlyKey, `${baseUrl}/v1/order`, {
    method: 'POST',
    body: JSON.stringify({
      symbol: 'PERP_ETH_USDC',
      order_type: 'MARKET',
      order_quantity: 0.01,
      side: 'BUY'
    })
  });
  const response = await res.json();
  console.log(response);
}

main();
