import { useState } from 'react';
import { Navigate, useLocation, useNavigate } from 'react-router-dom';
import Button from '../../components/common/Button.jsx';
import ErrorMessage from '../../components/common/ErrorMessage.jsx';
import Input from '../../components/common/Input.jsx';
import LoadingScreen from '../../components/common/LoadingScreen.jsx';
import { useAuth } from '../../hooks/useAuth.js';

export default function Login() {
  const { isAuthenticated, isAuthLoading, login } = useAuth();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const redirectTo = location.state?.from?.pathname || '/dashboard';

  if (isAuthLoading) {
    return <LoadingScreen message="Checking current session..." />;
  }

  if (isAuthenticated) {
    return <Navigate to={redirectTo} replace />;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setIsSubmitting(true);
    setError(null);

    try {
      await login(form);
      navigate(redirectTo, { replace: true });
    } catch (apiError) {
      setError(apiError);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <div>
          <h1>SIMS Login</h1>
          <p>Authenticate through the backend before using protected pages.</p>
        </div>

        <ErrorMessage error={error} title="Login failed" />

        <Input
          id="email"
          label="Email"
          type="email"
          value={form.email}
          onChange={(event) => setForm((current) => ({ ...current, email: event.target.value }))}
          required
        />
        <Input
          id="password"
          label="Password"
          type="password"
          value={form.password}
          onChange={(event) => setForm((current) => ({ ...current, password: event.target.value }))}
          required
        />

        <Button type="submit" disabled={isSubmitting}>
          {isSubmitting ? 'Signing in...' : 'Sign in'}
        </Button>
      </form>
    </main>
  );
}
