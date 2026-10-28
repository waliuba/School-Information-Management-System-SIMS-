import { useEffect, useState } from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar.jsx';
import Sidebar from './Sidebar.jsx';
import { readPreferences } from '../../services/preferences.js';
import { savePreferences } from '../../services/preferences.js';
import { updatePreferences } from '../../services/api/accountApi.js';
import { useToast } from '../../context/ToastContext.jsx';

export default function MainLayout() {
  const [collapsed, setCollapsed] = useState(() => readPreferences().sidebarCollapsed === 'true');
  const [mobileOpen, setMobileOpen] = useState(false);
  const { showToast } = useToast();
  useEffect(() => {
    function onPreferenceChange(event) { setCollapsed(event.detail?.sidebarCollapsed === 'true'); }
    window.addEventListener('sims:preferences-changed', onPreferenceChange);
    return () => window.removeEventListener('sims:preferences-changed', onPreferenceChange);
  }, []);
  async function toggleSidebar() {
    const next = !collapsed;
    const preferences = { ...readPreferences(), sidebarCollapsed: String(next) };
    setCollapsed(next);
    savePreferences(preferences);
    try { await updatePreferences(preferences); }
    catch (error) { showToast(error.message || 'Sidebar preference could not be saved to your account.', { tone: 'warning' }); }
  }
  return <div className={`app-shell${collapsed ? ' app-shell--collapsed' : ''}`}>
    <Sidebar collapsed={collapsed} mobileOpen={mobileOpen} onClose={() => setMobileOpen(false)} onToggle={toggleSidebar} />
    <main className="app-main"><Navbar onMenuClick={() => setMobileOpen(true)} /><Outlet /></main>
  </div>;
}
