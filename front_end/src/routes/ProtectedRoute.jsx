import { Navigate, Outlet, useLocation } from 'react-router-dom';
import LoadingScreen from '../components/common/LoadingScreen.jsx';
import { useAuth } from '../hooks/useAuth.js';

export default function ProtectedRoute() {
  const { isAuthenticated, isAuthLoading } = useAuth();
  const location = useLocation();

  if (isAuthLoading) {
    return <LoadingScreen message="Checking session with backend..." />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  return <Outlet />;
}
