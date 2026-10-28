import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth.js';

export default function RoleRoute({ allowedRoles = [] }) {
  const { user } = useAuth();
  const roleName = String(user?.role || user?.role_name || user?.roleName || '').toUpperCase();
  const roles = allowedRoles.map((role) => String(role).toUpperCase());

  if (!roles.length || roles.includes(roleName) || roles.includes(`ROLE_${roleName}`)) {
    return <Outlet />;
  }

  return <Navigate to="/unauthorized" replace />;
}
