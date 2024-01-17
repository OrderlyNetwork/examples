import { Button, Container, Tabs } from '@radix-ui/themes';
import { BrowserProvider, JsonRpcSigner } from 'ethers';
import { useEffect, useState } from 'react';

import { Account } from './Account';
import { Assets } from './Assets';
import { CreateOrder } from './CreateOrder';
import { Orderbook } from './Orderbook';
import { Orders } from './Orders';
import { Positions } from './Positions';
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
    <Container style={{ margin: '2rem auto', maxWidth: '45rem' }}>
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

      <Tabs.Root defaultValue="account" style={{ marginTop: '1rem' }}>
        <Tabs.List>
          <Tabs.Trigger value="account">Account</Tabs.Trigger>
          <Tabs.Trigger value="assets">Assets</Tabs.Trigger>
          <Tabs.Trigger value="orderbook">Orderbook</Tabs.Trigger>
          <Tabs.Trigger value="create_order">Create Order</Tabs.Trigger>
          <Tabs.Trigger value="orders">Orders</Tabs.Trigger>
          <Tabs.Trigger value="positions">Positions</Tabs.Trigger>
        </Tabs.List>

        <Tabs.Content value="account">
          <Account signer={signer} />
        </Tabs.Content>
        <Tabs.Content value="assets">
          <Assets signer={signer} />
        </Tabs.Content>
        <Tabs.Content value="orderbook">
          <Orderbook />
        </Tabs.Content>
        <Tabs.Content value="create_order">
          <CreateOrder />
        </Tabs.Content>
        <Tabs.Content value="orders">
          <Orders />
        </Tabs.Content>
        <Tabs.Content value="positions">
          <Positions />
        </Tabs.Content>
      </Tabs.Root>
    </Container>
  );
}

export default App;
