import React from 'react';
import ReactDOM from 'react-dom/client';
import { Auth0Provider } from '@auth0/auth0-react';
import './index.css';
import App from './App';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
    <Auth0Provider
      domain="dev-6a8gx4jqe8ymcodi.us.auth0.com"
      clientId="LeECmGtmibebqZVG80hUoUUl7ZefIr7a"
      authorizationParams={{
        redirect_uri: window.location.origin,
        scope: 'openid profile email',
      }}
    >
      <App />
    </Auth0Provider>
  </React.StrictMode>
);