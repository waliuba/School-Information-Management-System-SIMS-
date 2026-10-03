import { NavLink } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth.js';

const groups = [
  { title: 'ACADEMIC', items: [
    ['Students', '/students', 'ST'], ['Teachers', '/teachers', 'TE'], ['Courses', '/subjects', 'CO'],
    ['Departments', '/departments', 'DE'], ['Enrollment', '/enrollments', 'EN'], ['Attendance', '/attendance', 'AT'], ['Examinations', '/results', 'EX'],
  ] },
  { title: 'MANAGEMENT', items: [['Users', '/users', 'US', true], ['Roles & permissions', '/roles', 'RP', true]] },
];

export default function Sidebar({ collapsed, mobileOpen, onClose, onToggle }) {
  const { user } = useAuth();
  const role = user?.role || user?.role_name || user?.roleName;
  return <>
    <button className={`sidebar-scrim${mobileOpen ? ' is-open' : ''}`} aria-label="Close navigation" onClick={onClose} />
    <aside className={`sidebar${collapsed ? ' sidebar--collapsed' : ''}${mobileOpen ? ' sidebar--mobile-open' : ''}`}>
      <div className="sidebar__brand"><span className="sidebar__brand-mark">S</span><div className="sidebar__brand-copy"><strong>SIMS</strong><small>School information system</small></div></div>
      <button className="sidebar__collapse" type="button" onClick={onToggle} aria-expanded={!collapsed} aria-controls="main-navigation" aria-label={collapsed ? 'Expand sidebar' : 'Collapse sidebar'} title={collapsed ? 'Expand sidebar' : 'Collapse sidebar'}><span aria-hidden="true">{collapsed ? '»' : '«'}</span><span className="sidebar__collapse-label">{collapsed ? 'Expand menu' : 'Collapse menu'}</span></button>
      <nav className="sidebar__nav" id="main-navigation" aria-label="Main navigation">
        <NavLink end to="/dashboard" title={collapsed ? 'Overview' : undefined} onClick={onClose} className={({ isActive }) => `sidebar__link${isActive ? ' sidebar__link--active' : ''}`}><span className="sidebar__icon">OV</span><span className="sidebar__link-label">Overview</span></NavLink>
        {groups.map((group) => <section className="sidebar__group" key={group.title}><h2 className="sidebar__group-title">{group.title}</h2>{group.items.filter(([, , , adminOnly]) => !adminOnly || role === 'ADMIN').map(([label, to, icon]) => <NavLink key={to} to={to} title={collapsed ? label : undefined} onClick={onClose} className={({ isActive }) => `sidebar__link${isActive ? ' sidebar__link--active' : ''}`}><span className="sidebar__icon">{icon}</span><span className="sidebar__link-label">{label}</span></NavLink>)}</section>)}
      </nav>
      
    </aside>
  </>;
}
