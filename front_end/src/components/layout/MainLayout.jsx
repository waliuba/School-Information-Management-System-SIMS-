import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar.jsx';
import Sidebar from './Sidebar.jsx';

export default function MainLayout() {
  const [collapsed, setCollapsed] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  return <div className={`app-shell${collapsed ? ' app-shell--collapsed' : ''}`}>
    <Sidebar collapsed={collapsed} mobileOpen={mobileOpen} onClose={() => setMobileOpen(false)} onToggle={() => setCollapsed((value) => !value)} />
    <main className="app-main"><Navbar onMenuClick={() => setMobileOpen(true)} /><Outlet /></main>
  </div>;
}
