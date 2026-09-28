import { NavLink } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth.js';

const groups = [
  {
    label: 'Academic',
    items: [
      ['Students', '/students', '♙'],
      ['Teachers', '/teachers', '♟'],
      ['Courses', '/courses', '▤'],
      ['Departments', '/departments', '▦'],
      ['Enrollment', '/enrollments', '↗'],
      ['Attendance', '/attendance', '◷'],
      ['Examinations', '/results', '▣'],
      ['Performance', '/performance', '↗'],
    ],
  },
  {
    label: 'Management',
    items: [
      ['Users', '/users', '♙'],
      ['Roles & Permissions', '/roles', '⚿'],
    ],
  },
];

export default function Sidebar({ isCollapsed, isMobileOpen, onToggle, onClose }) {
  const { user } = useAuth();
  const role = user?.role || user?.role_name || user?.roleName;

  return (
    <aside className={`sidebar${isMobileOpen ? ' sidebar--open' : ''}`}>
      <div className="sidebar__brand">
        <span className="sidebar__brand-mark">S</span>
        {!isCollapsed ? <div>
          <strong>SIMS</strong>
          <small>Admin console</small>
        </div> : null}
        <button className="sidebar__toggle" type="button" onClick={onToggle} aria-label={isCollapsed ? 'Expand navigation' : 'Collapse navigation'} title={isCollapsed ? 'Expand navigation' : 'Collapse navigation'}>
          {isCollapsed ? '→' : '←'}
        </button>
      </div>

      <nav className="sidebar__nav" aria-label="Main navigation">
        <NavLink to="/dashboard" end className={({ isActive }) => `sidebar__link${isActive ? ' sidebar__link--active' : ''}`} onClick={onClose}>
          <span aria-hidden="true">⌂</span><span className="sidebar__link-label">Overview</span>
        </NavLink>
        {groups.map((group) => (
          <div className="sidebar__group" key={group.label}>
            {!isCollapsed ? <p className="sidebar__group-label">{group.label}</p> : null}
            {group.items.map(([label, to, icon]) => {
              if ((label === 'Users' || label === 'Roles & Permissions') && role !== 'ADMIN') return null;
              return (
                <NavLink key={`${label}-${to}`} to={to} className={({ isActive }) => `sidebar__link${isActive ? ' sidebar__link--active' : ''}`} onClick={onClose} title={isCollapsed ? label : undefined}>
                  <span aria-hidden="true">{icon}</span><span className="sidebar__link-label">{label}</span>
                </NavLink>
              );
            })}
          </div>
        ))}
        <div className="sidebar__group">
          {!isCollapsed ? <p className="sidebar__group-label">System</p> : null}
          <NavLink to="/settings" className="sidebar__link" onClick={onClose} title={isCollapsed ? 'System settings' : undefined}>
            <span aria-hidden="true">⚙</span><span className="sidebar__link-label">System settings</span>
          </NavLink>
        </div>
      </nav>
    </aside>
  );
}
