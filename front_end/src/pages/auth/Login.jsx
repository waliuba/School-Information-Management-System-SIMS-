import { useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import LoadingScreen from '../../components/common/LoadingScreen.jsx';
import { useAuth } from '../../hooks/useAuth.js';

const demoAccounts = [
  {
    role: 'Administrator',
    username: 'admin',
    password: 'Admin@123',
    description: 'Full access to school operations and settings.',
  },
  {
    role: 'Teacher',
    username: 'teacher',
    password: 'Teacher@123',
    description: 'Access teaching, attendance, and learner records.',
  },
];

function validateForm(form) {
  const errors = {};

  if (!form.username.trim()) {
    errors.username = 'Email or username is required.';
  } else if (form.username.includes('@') && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.username)) {
    errors.username = 'Enter a valid email address.';
  }

  if (!form.password) {
    errors.password = 'Password is required.';
  }

  return errors;
}

export default function Login() {
  const { isAuthenticated, isAuthLoading, login } = useAuth();
  const [form, setForm] = useState({ username: '', password: '' });
  const [errors, setErrors] = useState({});
  const [authError, setAuthError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();

  if (isAuthLoading) {
    return <LoadingScreen message="Checking current session..." />;
  }

  if (isAuthenticated) {
    return <Navigate to="/onboarding" replace />;
  }

  function updateField(event) {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
    setErrors((current) => ({ ...current, [name]: '' }));
    setAuthError('');
  }

  function useDemoAccount(account) {
    setForm({ username: account.username, password: account.password });
    setErrors({});
    setAuthError('');
  }

  async function handleSubmit(event) {
    event.preventDefault();
    const nextErrors = validateForm(form);

    if (Object.keys(nextErrors).length > 0) {
      setErrors(nextErrors);
      return;
    }

    setIsSubmitting(true);
    setAuthError('');

    try {
      await login({ username: form.username.trim(), password: form.password });
      navigate('/onboarding', { replace: true });
    } catch {
      setAuthError('Unable to sign in. Please check your credentials and try again.');
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="login-page">
      <header className="login-nav">
        <Link className="login-back" to="/">
          <span aria-hidden="true">←</span> Back to Home
        </Link>
        <span className="login-nav__current">SIMS / Login</span>
      </header>

      <div className="login-layout">
        <section className="login-card" aria-labelledby="login-title">
          <div className="login-brand" aria-label="SIMS home">
            <span className="login-brand__mark">S</span>
            <span>SIMS<span className="login-brand__dot">.</span></span>
          </div>

          <div className="login-intro">
            <p className="login-eyebrow">Secure access</p>
            <h1 id="login-title">Welcome Back</h1>
            <p>Sign in to continue to your SIMS account.</p>
          </div>

          {authError ? (
            <div className="login-alert" role="alert">
              <span aria-hidden="true">!</span>
              <div>
                <strong>Unable to sign in</strong>
                <p>{authError.replace('Unable to sign in. ', '')}</p>
              </div>
            </div>
          ) : null}

          <form className="login-form" onSubmit={handleSubmit} noValidate>
            <label className="login-field" htmlFor="username">
              <span>Email or Username</span>
              <input
                id="username"
                name="username"
                type="text"
                placeholder="Enter your email or username"
                autoComplete="username"
                value={form.username}
                onChange={updateField}
                aria-invalid={Boolean(errors.username)}
                aria-describedby={errors.username ? 'username-error' : undefined}
              />
              {errors.username ? <small id="username-error">{errors.username}</small> : null}
            </label>

            <label className="login-field" htmlFor="password">
              <span className="login-field__label">
                Password
                <Link to="/forgot-password">Forgot password?</Link>
              </span>
              <span className="password-control">
                <input
                  id="password"
                  name="password"
                  type={showPassword ? 'text' : 'password'}
                  placeholder="Enter your password"
                  autoComplete="current-password"
                  value={form.password}
                  onChange={updateField}
                  aria-invalid={Boolean(errors.password)}
                  aria-describedby={errors.password ? 'password-error' : undefined}
                />
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowPassword((current) => !current)}
                  aria-label={showPassword ? 'Hide password' : 'Show password'}
                  aria-pressed={showPassword}
                >
                  {showPassword ? 'Hide' : 'Show'}
                </button>
              </span>
              {errors.password ? <small id="password-error">{errors.password}</small> : null}
            </label>

            <button className="login-submit" type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Signing in...' : 'LOGIN'}
              {!isSubmitting ? <span aria-hidden="true">↗</span> : null}
            </button>
          </form>

          <p className="login-signup">
            Don&apos;t have an account? <Link to="/signup">Sign up</Link>
          </p>
        </section>

        <aside className="login-info" aria-labelledby="account-info-title">
          <div className="login-info__heading">
            <p className="login-eyebrow">Demo access</p>
            <h2 id="account-info-title">Your SIMS account</h2>
            <p>Use one of the demo profiles below to explore the dashboards.</p>
          </div>

          <div className="credential-list">
            {demoAccounts.map((account) => (
              <div className="credential-card" key={account.role}>
                <div className="credential-card__top">
                  <span className="credential-card__icon" aria-hidden="true">
                    {account.role === 'Administrator' ? 'A' : 'T'}
                  </span>
                  <div>
                    <h3>{account.role}</h3>
                    <p>{account.description}</p>
                  </div>
                </div>
                <dl className="credential-details">
                  <div>
                    <dt>Username</dt>
                    <dd>{account.username}</dd>
                  </div>
                  <div>
                    <dt>Password</dt>
                    <dd>{account.password}</dd>
                  </div>
                </dl>
                <button type="button" className="credential-use" onClick={() => useDemoAccount(account)}>
                  Use this account <span aria-hidden="true">→</span>
                </button>
              </div>
            ))}
          </div>

          <div className="login-info__footer">
            <span aria-hidden="true">✓</span>
            <p>Secure account access, role-based permissions, and centralized school information.</p>
          </div>
         
        </aside>
      </div>
    </main>
  );
}
