import { useEffect, useMemo, useRef, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth.js';

const routeDetails = [
  ['/dashboard', 'Dashboard', 'School administration'], ['/profile', 'Profile', 'Manage your personal information and account details.'],
  ['/account-settings', 'Account settings', 'Manage your account and session.'], ['/security', 'Security', 'Manage your password and account security.'],
  ['/notifications', 'Notifications', 'Review your notifications.'], ['/students', 'Students', 'Manage student records.'], ['/teachers', 'Teachers', 'Manage and monitor the school\'s teaching staff.'],
  ['/courses', 'Courses', 'Manage courses and academic units.'], ['/subjects', 'Subjects', 'Manage academic units.'], ['/departments', 'Departments', 'Manage academic departments.'],
  ['/enrollments', 'Enrollments', 'Manage student enrollments.'], ['/attendance/mark', 'Mark Attendance', 'Record student attendance for your class.'], ['/attendance', 'Attendance', 'Review attendance records.'], ['/results', 'Results', 'Manage examination results.'],
  ['/users', 'Users', 'Manage user accounts.'], ['/roles', 'Roles & permissions', 'Manage roles and permissions.'], ['/reports', 'Reports', 'Review school reports.'], ['/classes', 'Classes', 'Manage classes.'],
];

function Icon({ name }) {
  const common = { width: 18, height: 18, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', strokeWidth: 1.8, strokeLinecap: 'round', strokeLinejoin: 'round', 'aria-hidden': true };
  if (name === 'bell') return <svg {...common}><path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9"/><path d="M10 21h4"/></svg>;
  if (name === 'user') return <svg {...common}><circle cx="12" cy="8" r="4"/><path d="M5 21a7 7 0 0 1 14 0"/></svg>;
  if (name === 'settings') return <svg {...common}><circle cx="12" cy="12" r="3"/><path d="m19.4 15 .1.1a1.7 1.7 0 1 1-2.4 2.4l-.1-.1a1.7 1.7 0 0 0-2.9 1.2v.2a1.7 1.7 0 1 1-3.4 0v-.2a1.7 1.7 0 0 0-2.9-1.2l-.1.1a1.7 1.7 0 1 1-2.4-2.4l.1-.1a1.7 1.7 0 0 0-1.2-2.9H4a1.7 1.7 0 1 1 0-3.4h.2a1.7 1.7 0 0 0 1.2-2.9l-.1-.1a1.7 1.7 0 1 1 2.4-2.4l.1.1a1.7 1.7 0 0 0 2.9-1.2V2a1.7 1.7 0 1 1 3.4 0v.2a1.7 1.7 0 0 0 2.9 1.2l.1-.1a1.7 1.7 0 1 1 2.4 2.4l-.1.1a1.7 1.7 0 0 0 1.2 2.9h.2a1.7 1.7 0 1 1 0 3.4h-.2a1.7 1.7 0 0 0-1.2 2.9Z"/></svg>;
  if (name === 'shield') return <svg {...common}><path d="M12 22s8-4 8-11V5l-8-3-8 3v6c0 7 8 11 8 11Z"/><path d="m9 12 2 2 4-4"/></svg>;
  return <svg {...common}><path d="M10 17l5-5-5-5M15 12H3"/><path d="M12 3h6a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-6"/></svg>;
}

function getUserName(user) {
  return user?.name || user?.fullName || [user?.firstName, user?.lastName].filter(Boolean).join(' ') || user?.username || user?.email || 'Administrator';
}

export default function Navbar({ onMenuClick }) {
  const { user, logout } = useAuth();
  const location = useLocation();
  const [panel, setPanel] = useState('');
  const [notifications, setNotifications] = useState([]); // TODO(api): load real notification records.
  const wrapperRef = useRef(null);
  const triggerRef = useRef(null);
  const unread = notifications.filter((item) => item.unread).length;
  const name = getUserName(user);
  const role = user?.role || user?.role_name || user?.roleName;
  const route = useMemo(() => routeDetails.find(([path]) => location.pathname === path || location.pathname.startsWith(`${path}/`)), [location.pathname]);
  const title = route?.[1] || 'SIMS';
  const subtitle = route?.[2] || 'School information system';

  function closePanel(returnFocus = false) {
    setPanel('');
    if (returnFocus) requestAnimationFrame(() => triggerRef.current?.focus());
  }

  useEffect(() => {
    function onPointerDown(event) {
      if (panel && wrapperRef.current && !wrapperRef.current.contains(event.target)) closePanel();
    }
    function onKeyDown(event) {
      if (event.key === 'Escape' && panel) { event.preventDefault(); closePanel(true); }
    }
    document.addEventListener('pointerdown', onPointerDown);
    document.addEventListener('keydown', onKeyDown);
    return () => { document.removeEventListener('pointerdown', onPointerDown); document.removeEventListener('keydown', onKeyDown); };
  }, [panel]);

  return <header className="navbar">
    <div className="navbar__left"><button className="navbar__menu" type="button" aria-label="Open navigation" onClick={onMenuClick}><span aria-hidden="true">☰</span></button><div className="navbar__heading"><strong>{title}</strong><span>{subtitle}</span></div></div>
    <div className="navbar__flex-space" />
    <div className="navbar__actions" ref={wrapperRef}>
      <div className="navbar__popover-wrap"><button ref={triggerRef} className={`navbar__icon-button${panel === 'notifications' ? ' is-open' : ''}`} type="button" aria-label={unread ? `Notifications, ${unread} unread` : 'Notifications'} aria-expanded={panel === 'notifications'} aria-haspopup="dialog" onClick={() => setPanel(panel === 'notifications' ? '' : 'notifications')}><Icon name="bell" />{unread > 0 && <span className="navbar__badge" aria-label={`${unread} unread`} />}</button>
        {panel === 'notifications' && <section className="navbar-popover notification-popover" role="dialog" aria-label="Notifications"><header><strong>Notifications</strong><button type="button" disabled={!unread} onClick={() => setNotifications((items) => items.map((item) => ({ ...item, unread: false })))}>Mark all read</button></header>
          <div className="notification-list">{notifications.length ? notifications.map((item) => <article className={`notification-item${item.unread ? ' is-unread' : ''}`} key={item.id}><button type="button" aria-label={`Mark ${item.title} as read`} disabled={!item.unread} onClick={() => setNotifications((items) => items.map((n) => n.id === item.id ? { ...n, unread: false } : n))}><span className="notification-item__dot" aria-hidden="true" /></button><div><strong>{item.title}</strong><p>{item.detail}</p>{item.time && <small>{item.time}</small>}</div></article>) : <p className="notification-empty">You’re all caught up. New notifications will appear here.</p>}</div>
          <Link to="/notifications" onClick={() => closePanel()}>View all notifications</Link></section>}</div>
      <div className="navbar__popover-wrap"><button className="navbar__profile" type="button" aria-expanded={panel === 'profile'} aria-haspopup="menu" aria-controls="account-menu" onClick={(event) => { triggerRef.current = event.currentTarget; setPanel(panel === 'profile' ? '' : 'profile'); }}><span className="navbar__avatar">{String(name).slice(0, 1).toUpperCase()}</span><span className="navbar__profile-copy"><strong>{name}</strong>{role && <small>{role}</small>}</span><span className="navbar__chevron" aria-hidden="true">⌄</span></button>
        {panel === 'profile' && <div className="navbar-popover profile-popover" id="account-menu" role="menu" aria-label="Account"><span className="profile-popover__label">Account</span><Link role="menuitem" to="/profile" onClick={() => closePanel()}><Icon name="user" />Profile</Link><Link role="menuitem" to="/account-settings" onClick={() => closePanel()}><Icon name="settings" />Account settings</Link><Link role="menuitem" to="/security" onClick={() => closePanel()}><Icon name="shield" />Security</Link><div className="profile-popover__separator" /><button className="profile-popover__logout" role="menuitem" type="button" onClick={logout}><Icon name="logout" />Log out</button></div>}</div>
    </div>
  </header>;
}
