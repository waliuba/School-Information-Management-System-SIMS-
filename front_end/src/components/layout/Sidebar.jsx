import { NavLink, useLocation } from 'react-router-dom';
import { useState } from 'react';
import { useAuth } from '../../hooks/useAuth.js';

const groups = [
  { title: 'ACADEMIC', items: [
    ['Students', '/students', 'ST'], ['Teachers', '/teachers', 'TE', true], ['Courses', '/subjects', 'CO'],
    ['Departments', '/departments', 'DE'], ['Enrollment', '/enrollments', 'EN'], ['Attendance', '/attendance', 'AT'], ['Examinations', '/results', 'EX'],
  ] },
  { title: 'MANAGEMENT', items: [['Users', '/users', 'US', true], ['Roles & permissions', '/roles', 'RP', true], ['Settings', '/settings/account', 'SE', true]] },
];

function isPrivilegedRole(role) {
  return ['ADMIN', 'SUPER_ADMIN'].includes(String(role || '').toUpperCase());
}

export default function Sidebar({ collapsed, mobileOpen, onClose, onToggle }) {
  const { user } = useAuth();
  const location = useLocation();
  const [attendanceOpen, setAttendanceOpen] = useState(location.pathname.startsWith('/attendance'));
  const role = user?.role || user?.role_name || user?.roleName;
  const visibleGroups = groups
    .map((group) => ({
      ...group,
      items: group.items.filter(([, , , adminOnly]) => !adminOnly || isPrivilegedRole(role)),
    }))
    .filter((group) => group.items.length > 0);
  return <>
    <button className={`sidebar-scrim${mobileOpen ? ' is-open' : ''}`} aria-label="Close navigation" onClick={onClose} />
    <aside className={`sidebar${collapsed ? ' sidebar--collapsed' : ''}${mobileOpen ? ' sidebar--mobile-open' : ''}`}>
      <div className="sidebar__brand"><span className="sidebar__brand-mark">S</span><div className="sidebar__brand-copy"><strong>SIMS</strong><small>School information system</small></div></div>
      <button className="sidebar__collapse" type="button" onClick={onToggle} aria-expanded={!collapsed} aria-controls="main-navigation" aria-label={collapsed ? 'Expand sidebar' : 'Collapse sidebar'} title={collapsed ? 'Expand sidebar' : 'Collapse sidebar'}><span aria-hidden="true">{collapsed ? '»' : '«'}</span><span className="sidebar__collapse-label">{collapsed ? 'Expand menu' : 'Collapse menu'}</span></button>
      <nav className="sidebar__nav" id="main-navigation" aria-label="Main navigation">
        <NavLink end to="/dashboard" title={collapsed ? 'Overview' : undefined} onClick={onClose} className={({ isActive }) => `sidebar__link${isActive ? ' sidebar__link--active' : ''}`}><span className="sidebar__icon">OV</span><span className="sidebar__link-label">Overview</span></NavLink>
        {visibleGroups.map((group) => <section className="sidebar__group" key={group.title}><h2 className="sidebar__group-title">{group.title}</h2>{group.items.map(([label, to, icon]) => label === 'Attendance' ? <div className="sidebar__attendance" key={to}>
          <button type="button" className={`sidebar__link sidebar__attendance-toggle${location.pathname.startsWith('/attendance') ? ' sidebar__link--active' : ''}`} aria-expanded={attendanceOpen} aria-controls="attendance-submenu" title={collapsed ? label : undefined} onClick={() => setAttendanceOpen((open) => !open)}>
            <span className="sidebar__icon">{icon}</span><span className="sidebar__link-label">{label}</span><span className="sidebar__attendance-chevron" aria-hidden="true">{attendanceOpen ? '⌃' : '⌄'}</span>
          </button>
          {attendanceOpen && <div className="sidebar__submenu" id="attendance-submenu">
            {role === 'TEACHER' && <NavLink to="/attendance/mark" title={collapsed ? 'Mark Attendance' : undefined} onClick={onClose} className={({ isActive }) => `sidebar__submenu-link${isActive ? ' sidebar__submenu-link--active' : ''}`}>Mark Attendance</NavLink>}
            <NavLink end to="/attendance" title={collapsed ? 'Attendance history' : undefined} onClick={onClose} className={({ isActive }) => `sidebar__submenu-link${isActive ? ' sidebar__submenu-link--active' : ''}`}>Attendance history</NavLink>
          </div>}
        </div> : <NavLink key={to} to={to} title={collapsed ? label : undefined} onClick={onClose} className={({ isActive }) => `sidebar__link${isActive ? ' sidebar__link--active' : ''}`}><span className="sidebar__icon">{icon}</span><span className="sidebar__link-label">{label}</span></NavLink>)}</section>)}
      </nav>
      
    </aside>
  </>;
}
