import { NavLink } from 'react-router-dom';

const navItems = [
  { label: 'Dashboard', to: '/dashboard' },
  { label: 'Students', to: '/students' },
  { label: 'Teachers', to: '/teachers' },
  { label: 'Classes', to: '/classes' },
  { label: 'Departments', to: '/departments' },
  { label: 'Subjects', to: '/subjects' },
  { label: 'Enrollments', to: '/enrollments' },
  { label: 'Attendance', to: '/attendance' },
  { label: 'Exams & Results', to: '/results' },
  { label: 'Users', to: '/users' },
  { label: 'Roles', to: '/roles' },
];

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="sidebar__brand">
        <span className="sidebar__brand-mark">S</span>
        <div>
          <strong>SIMS</strong>
          <small>Backend testing client</small>
        </div>
      </div>

      <nav className="sidebar__nav" aria-label="Main navigation">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) => `sidebar__link${isActive ? ' sidebar__link--active' : ''}`}
          >
            {item.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
