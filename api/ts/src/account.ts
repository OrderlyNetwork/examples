import { API } from '@orderly.network/types';

import { BASE_URL } from './config';
import { signAndSendRequest } from './signer';

export async function getClientHolding(orderlyAccountId: string, orderlyKey: Uint8Array) {
  const res = await signAndSendRequest(
    orderlyAccountId,
    orderlyKey,
    `${BASE_URL}/v1/client/holding`
  );
  const json = await res.json();
  console.log('getClientHolding:', JSON.stringify(json, undefined, 2));
}

export async function getOpenOrders(
  orderlyAccountId: string,
  orderlyKey: Uint8Array
): Promise<API.Order[]> {
  const res = await signAndSendRequest(
    orderlyAccountId,
    orderlyKey,
    `${BASE_URL}/v1/orders?status=INCOMPLETE`
  );
  const json = await res.json();
  console.log('getOpenOrders:', JSON.stringify(json, undefined, 2));
  return json.data.rows;
}

export async function getOpenAlgoOrders(
  algoType: 'STOP' | 'TP_SL' | 'POSITIONAL_TP_SL',
  orderlyAccountId: string,
  orderlyKey: Uint8Array
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
): Promise<{ algo_order_id: string; symbol: string; algo_status: string }[]> {
  const res = await signAndSendRequest(
    orderlyAccountId,
    orderlyKey,
    `${BASE_URL}/v1/algo/orders?status=INCOMPLETE&algo_type=${algoType}`
  );
  const json = await res.json();
  console.log('getOpenAlgoOrders:', JSON.stringify(json, undefined, 2));
  return json.data.rows;
}
