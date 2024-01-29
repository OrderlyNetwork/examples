import { ethers } from 'ethers';
import { FC, PropsWithChildren, createContext, useContext, useEffect, useState } from 'react';

export type OrderlyContextType = {
  provider?: ethers.Provider;
  signer?: ethers.JsonRpcSigner;
  accountId?: string;
  address?: string;
  userId?: string;
};

export const OrderlyContext = createContext<OrderlyContextType | null>(null);

export const OrderlyProvider: FC<PropsWithChildren> = ({ children }) => {
  const [orderly, setOrderly] = useState<OrderlyContextType>({});

  useEffect(() => {
    async function run() {
      if (!window.ethereum) return;
      const provider = new ethers.BrowserProvider(window.ethereum);
      const signer = await provider.getSigner();
      setOrderly((orderly) => ({
        provider,
        signer,
        ...(orderly ?? {})
      }));
    }
    run();
  }, []);

  return <OrderlyContext.Provider value={orderly}>{children}</OrderlyContext.Provider>;
};

export function useOrderly(): OrderlyContextType {
  const ctx = useContext(OrderlyContext);
  if (!ctx) {
    throw new Error('OrderlyContext uninitialized');
  }
  return ctx;
}
