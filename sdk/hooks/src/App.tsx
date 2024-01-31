import { Button, Container, Flex, Switch, Tabs } from '@radix-ui/themes';
import { BrowserProvider, JsonRpcSigner } from 'ethers';
import { useEffect, useState } from 'react';

import { Account } from './Account';
import { Assets } from './Assets';
import { CreateOrder } from './CreateOrder';
import { Orderbook } from './Orderbook';
import { Orders } from './Orders';
import { Positions } from './Positions';
import { Trades } from './Trades';
import { DelegateSignerResponse } from './helpers/delegateSigner';
import { checkValidNetwork } from './network';

function App() {
  const [provider, setProvider] = useState<BrowserProvider | undefined>();
  const [signer, setSigner] = useState<JsonRpcSigner | undefined>();
  const [delegateSignerEnabled, setDelegateSignerEnabled] = useState<boolean>(false);
  const [delegateSigner, setDelegateSigner] = useState<DelegateSignerResponse>();
  const [_delegateOrderlyKey, setDelegateOrderlyKey] = useState<string>();

  console.log('delegateSigner', delegateSigner);

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
      <Flex gap="4" align="center" wrap="wrap">
        <Button
          onClick={async () => {
            if (!window.ethereum) return;
            let p = provider;
            if (!provider) {
              p = new BrowserProvider(window.ethereum);
              setProvider(p);
            }
            if (!p) return;
            if (!signer) {
              const s = await p.getSigner();
              setSigner(s);
            }
          }}
        >
          {signer
            ? `${signer.address.substring(0, 6)}...${signer.address.substr(-4)}`
            : 'Connect wallet'}
        </Button>
        <Flex gap="1">
          <Switch
            onCheckedChange={(checked) => {
              setDelegateSignerEnabled(checked);
            }}
          />
          Delegate Signer mode
        </Flex>
      </Flex>

      <Tabs.Root defaultValue="account" style={{ marginTop: '1rem' }}>
        <Tabs.List>
          <Tabs.Trigger value="account">Account</Tabs.Trigger>
          <Tabs.Trigger value="assets">Assets</Tabs.Trigger>
          <Tabs.Trigger value="orderbook">Orderbook</Tabs.Trigger>
          <Tabs.Trigger value="create_order">Create Order</Tabs.Trigger>
          <Tabs.Trigger value="orders">Orders</Tabs.Trigger>
          <Tabs.Trigger value="positions">Positions</Tabs.Trigger>
          <Tabs.Trigger value="trades">Trades</Tabs.Trigger>
        </Tabs.List>

        <Tabs.Content value="account">
          <Account
            signer={signer}
            delegateSignerEnabled={delegateSignerEnabled}
            delegateSigner={delegateSigner}
            setDelegateSigner={setDelegateSigner}
            setDelegateOrderlyKey={setDelegateOrderlyKey}
          />
        </Tabs.Content>
        <Tabs.Content value="assets">
          <Assets
            signer={signer}
            delegateSignerEnabled={delegateSignerEnabled}
            delegateSigner={delegateSigner}
          />
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
        <Tabs.Content value="trades">
          <Trades />
        </Tabs.Content>
      </Tabs.Root>
    </Container>
  );
}

export default App;
