import { BASE_URL } from './config';
import { signAndSendRequest } from './signer';

type Level = { price: number; quantity: number };
export type OrderbookSnapshot = { data: { asks: Level[]; bids: Level[] } };

export async function getOrderbook(
  symbol: string,
  maxLevel: number,
  orderlyAccountId: string,
  orderlyKey: Uint8Array
): Promise<OrderbookSnapshot> {
  const res = await signAndSendRequest(
    orderlyAccountId,
    orderlyKey,
    `${BASE_URL}/v1/orderbook/${symbol}${maxLevel != null ? `?max_level=${maxLevel}` : ''}`
  );
  const json = await res.json();
  console.log('getOrderbook:', JSON.stringify(json, undefined, 2));
  return json;
}
