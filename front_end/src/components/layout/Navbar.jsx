import { useEffect, useMemo, useRef, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth.js';
import Icon from '../common/Icon.jsx';
import Avatar from '../common/Avatar.jsx';
import Tooltip from '../common/Tooltip.jsx';

const routeDetails = [
  ['/dashboard', 'Dashboard', 'School administration'], ['/profile', 'Profile', 'Manage your personal information and account details.'],
  ['/account-settings', 'Account settings', 'Manage your account and session.'], ['/security', 'Security', 'Manage your password and account security.'],
  ['/notifications', 'Notifications', 'Review your notifications.'], ['/students', 'Students', 'Manage student records.'], ['/teachers', 'Teachers', 'Manage and monitor the school\'s teaching staff.'],
  ['/courses', 'Courses', 'Manage courses and academic units.'], ['/subjects', 'Subjects', 'Manage academic units.'], ['/departments', 'Departments', 'Manage academic departments.'],
  ['/enrollments', 'Enrollments', 'Manage student enrollments.'], ['/attendance/mark', 'Mark Attendance', 'Record student attendance for your class.'], ['/attendance', 'Attendance', 'Review attendance records.'], ['/results', 'Results', 'Manage examination results.'],
  ['/users', 'Users', 'Manage user accounts.'], ['/roles', 'Roles & permissions', 'Manage roles and permissions.'], ['/reports', 'Reports', 'Review school reports.'], ['/classes', 'Classes', 'Manage classes.'],
];

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
  const showBreadcrumbs = location.pathname !== '/dashboard';

  function closePanel(returnFocus = false) {
    setPanel('');
    if (returnFocus) requestAnimationFrame(() => triggerRef.current?.focus());
  }

  useEffect(() => {
    function onPointerDown(event) {
      if (panel && wrapperRef.current && !wrapperRef.current.contains(event.target)) closePanel();
    }
    function onKeyDown(event) {
      if (event.key === 'Escape' && panel) { event.preventDefault(); closePanel(true); return; }
      if (panel !== 'profile') return;
      if (event.key === 'Tab') {
        requestAnimationFrame(() => {
          if (!wrapperRef.current?.contains(document.activeElement)) closePanel();
        });
        return;
      }

      const items = [...(wrapperRef.current?.querySelectorAll('#account-menu [role="menuitem"]') || [])];
      if (!items.length || !['ArrowDown', 'ArrowUp', 'Home', 'End'].includes(event.key)) return;
      event.preventDefault();
      const currentIndex = items.indexOf(document.activeElement);
      const nextIndex = event.key === 'Home' ? 0
        : event.key === 'End' ? items.length - 1
          : event.key === 'ArrowDown' ? (currentIndex + 1 + items.length) % items.length
            : (currentIndex <= 0 ? items.length - 1 : currentIndex - 1);
      items[nextIndex].focus();
    }
    document.addEventListener('pointerdown', onPointerDown);
    document.addEventListener('keydown', onKeyDown);
    return () => { document.removeEventListener('pointerdown', onPointerDown); document.removeEventListener('keydown', onKeyDown); };
  }, [panel]);

  return <header className="navbar">
    <div className="navbar__left"><button className="navbar__menu" type="button" aria-label="Open navigation" onClick={onMenuClick}><span aria-hidden="true">☰</span></button><div className="navbar__heading">{showBreadcrumbs && <nav className="navbar__breadcrumbs" aria-label="Breadcrumb"><Link to="/dashboard">Dashboard</Link><span aria-hidden="true">/</span><span aria-current="page">{title}</span></nav>}<strong>{title}</strong><span>{subtitle}</span></div></div>
    <div className="navbar__flex-space" />
    <div className="navbar__actions" ref={wrapperRef}>
      <div className="navbar__popover-wrap"><Tooltip content="Notifications"><button ref={triggerRef} className={`navbar__icon-button${panel === 'notifications' ? ' is-open' : ''}`} type="button" aria-label={unread ? `Notifications, ${unread} unread` : 'Notifications'} aria-expanded={panel === 'notifications'} aria-haspopup="dialog" onClick={() => { const opening = panel !== 'notifications'; setPanel(opening ? 'notifications' : ''); if (opening) requestAnimationFrame(() => wrapperRef.current?.querySelector('.notification-popover a[href], .notification-popover button:not([disabled])')?.focus()); }}><Icon name="bell" />{unread > 0 && <span className="navbar__badge" aria-label={`${unread} unread`} />}</button></Tooltip>
        {panel === 'notifications' && <section className="navbar-popover notification-popover" role="dialog" aria-label="Notifications"><header><strong>Notifications</strong><button type="button" disabled={!unread} onClick={() => setNotifications((items) => items.map((item) => ({ ...item, unread: false })))}>Mark all read</button></header>
          <div className="notification-list">{notifications.length ? notifications.map((item) => <article className={`notification-item${item.unread ? ' is-unread' : ''}`} key={item.id}><button type="button" aria-label={`Mark ${item.title} as read`} disabled={!item.unread} onClick={() => setNotifications((items) => items.map((n) => n.id === item.id ? { ...n, unread: false } : n))}><span className="notification-item__dot" aria-hidden="true" /></button><div><strong>{item.title}</strong><p>{item.detail}</p>{item.time && <small>{item.time}</small>}</div></article>) : <p className="notification-empty">You’re all caught up. New notifications will appear here.</p>}</div>
          <Link to="/notifications" onClick={() => closePanel()}>View all notifications</Link></section>}</div>
      <div className="navbar__popover-wrap"><button className="navbar__profile" type="button" aria-expanded={panel === 'profile'} aria-haspopup="menu" aria-controls="account-menu" onClick={(event) => { triggerRef.current = event.currentTarget; const opening = panel !== 'profile'; setPanel(opening ? 'profile' : ''); if (opening) requestAnimationFrame(() => wrapperRef.current?.querySelector('#account-menu [role="menuitem"]')?.focus()); }}><Avatar name={name} className="navbar__avatar" /><span className="navbar__profile-copy"><strong>{name}</strong>{role && <small>{role}</small>}</span><span className="navbar__chevron" aria-hidden="true">⌄</span></button>
        {panel === 'profile' && <div className="navbar-popover profile-popover" id="account-menu" role="menu" aria-label="Account"><span className="profile-popover__label">Account</span><Link role="menuitem" to="/profile" onClick={() => closePanel()}><Icon name="user" />Profile</Link><Link role="menuitem" to="/account-settings" onClick={() => closePanel()}><Icon name="settings" />Account settings</Link><Link role="menuitem" to="/security" onClick={() => closePanel()}><Icon name="shield" />Security</Link><div className="profile-popover__separator" /><button className="profile-popover__logout" role="menuitem" type="button" onClick={logout}><Icon name="logout" />Log out</button></div>}</div>
    </div>
  </header>;
}
