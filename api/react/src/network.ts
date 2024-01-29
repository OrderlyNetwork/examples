import { BrowserProvider } from 'ethers';

// Arbitrum Sepolia
export const testnetChainId = '421614';
export const testnetChainIdHex = '0x66eee';

export async function checkValidNetwork(provider: BrowserProvider) {
  const signer = await provider.getSigner();
  const network = await signer.provider.getNetwork();
  const chainId = network.chainId.toString();

  if (chainId !== testnetChainId) {
    await addArbitrumGoerliNetwork();
  }
}

export function addArbitrumGoerliNetwork(): Promise<unknown> {
  if (!window.ethereum) return Promise.resolve();
  return window.ethereum.request({
    method: 'wallet_addEthereumChain',
    params: [
      {
        chainId: testnetChainIdHex,
        rpcUrls: ['https://arbitrum-goerli.publicnode.com'],
        chainName: 'Arbitrum Goerli',
        nativeCurrency: {
          name: 'ETH',
          symbol: 'ETH',
          decimals: 18
        },
        blockExplorerUrls: ['https://goerli.arbiscan.io/']
      }
    ]
  });
}
