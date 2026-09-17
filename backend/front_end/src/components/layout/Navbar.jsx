import { useAuth } from '../../hooks/useAuth.js';

export default function Navbar() {
  const { user, logout } = useAuth();

  return (
    <header className="navbar">
      <div>
        <strong>School Information Management System</strong>
        <span>React client for backend APIs</span>
      </div>
      <div className="navbar__user">
        <span>{user?.name || user?.email || 'Authenticated user'}</span>
        <button type="button" onClick={logout}>
          Logout
        </button>
      </div>
    </header>
  );
}
