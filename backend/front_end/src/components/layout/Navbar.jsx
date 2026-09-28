import { useAuth } from '../../hooks/useAuth.js';

import { useState } from 'react';

export default function Navbar({ onMenuClick }) {
  const { user, logout } = useAuth();
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserMenu, setShowUserMenu] = useState(false);

  return (
    <header className="navbar">
      <div className="navbar__title">
        <button className="navbar__menu" type="button" onClick={onMenuClick} aria-label="Open navigation">☰</button>
        <div><strong>Dashboard</strong><span>SIMS administration workspace</span></div>
      </div>
      <div className="navbar__actions">
        <div className="navbar__notification">
          <button className="icon-button" type="button" onClick={() => setShowNotifications((value) => !value)} aria-label="Notifications" aria-expanded={showNotifications}>♢<i /></button>
          {showNotifications ? <div className="navbar__popover"><strong>Notifications</strong><p>No new notifications</p><button type="button">View all notifications</button></div> : null}
        </div>
        <div className="navbar__user">
          <button className="navbar__user-button" type="button" onClick={() => setShowUserMenu((value) => !value)} aria-expanded={showUserMenu}>
            <span className="navbar__avatar">{(user?.name || user?.email || 'A').charAt(0).toUpperCase()}</span>
            <span><strong>{user?.name || user?.email || 'Admin User'}</strong><small>Administrator</small></span><b>⌄</b>
          </button>
          {showUserMenu ? <div className="navbar__popover navbar__popover--user"><button type="button">Profile</button><button type="button">Account settings</button><button type="button">Security</button><button type="button" onClick={logout}>Logout</button></div> : null}
        </div>
      </div>
    </header>
  );
}
