import { OrderlyConfigProvider } from '@orderly.network/hooks';
import { Theme } from '@radix-ui/themes';
import React from 'react';
import ReactDOM from 'react-dom/client';

import App from './App.tsx';

import './index.css';
import '@radix-ui/themes/styles.css';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <Theme>
      <OrderlyConfigProvider networkId="testnet" brokerId="woofi_dex">
        <App />
      </OrderlyConfigProvider>
    </Theme>
  </React.StrictMode>
);
