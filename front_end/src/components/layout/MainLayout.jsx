import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar.jsx';
import Sidebar from './Sidebar.jsx';

export default function MainLayout() {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [isMobileOpen, setIsMobileOpen] = useState(false);

  return (
    <div className={`app-shell${isCollapsed ? ' app-shell--collapsed' : ''}${isMobileOpen ? ' app-shell--mobile-open' : ''}`}>
      <Sidebar
        isCollapsed={isCollapsed}
        isMobileOpen={isMobileOpen}
        onToggle={() => setIsCollapsed((value) => !value)}
        onClose={() => setIsMobileOpen(false)}
      />
      <main className="app-main">
        <Navbar
          onMenuClick={() => setIsMobileOpen((value) => !value)}
          onNotificationsClick={() => {}}
        />
        <Outlet />
      </main>
    </div>
  );
}
