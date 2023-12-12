import { Button, Container } from '@radix-ui/themes';
import { BrowserProvider, JsonRpcSigner } from 'ethers';
import { useEffect, useState } from 'react';

import { Account } from './Account';
import { Assets } from './Assets';
import { checkValidNetwork } from './network';

function App() {
  const [provider, setProvider] = useState<BrowserProvider | undefined>();
  const [signer, setSigner] = useState<JsonRpcSigner | undefined>();

  useEffect(() => {
    async function run() {
      if (!window.ethereum) return;
      const p = new BrowserProvider(window.ethereum);
      setProvider(p);
      const s = await p.getSigner();
      setSigner(s);
    }
    run();
  }, []);

  useEffect(() => {
    if (!provider) return;
    checkValidNetwork(provider);
  }, [provider]);

  return (
    <Container style={{ margin: '2rem' }}>
      <Button
        disabled={provider == null}
        onClick={async () => {
          if (!provider || !!signer) return;
          const s = await provider.getSigner();
          setSigner(s);
        }}
      >
        {signer
          ? `${signer.address.substring(0, 6)}...${signer.address.substr(-4)}`
          : 'Connect wallet'}
      </Button>

      <Account signer={signer} />
      <Assets signer={signer} />
    </Container>
  );
}

export default App;
