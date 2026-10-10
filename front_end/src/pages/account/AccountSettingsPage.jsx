import PageContainer from '../../components/layout/PageContainer.jsx';
import { useAuth } from '../../hooks/useAuth.js';
import { useNavigate } from 'react-router-dom';

export default function AccountSettingsPage() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const status = typeof user?.enabled === 'boolean' ? (user.enabled ? 'Enabled' : 'Disabled') : user?.status;
  return <PageContainer title="Account settings" description="Manage your account and session.">
    <section className="account-card"><div className="account-card__heading"><div><h2>Account</h2><p>Account details from your signed-in user record.</p></div></div><dl className="account-facts">{[['Username', user?.username], ['Email', user?.email], ['Status', status]].filter(([, value]) => value !== undefined && value !== null && value !== '').map(([label, value]) => <div key={label}><dt>{label}</dt><dd>{String(value)}</dd></div>)}</dl>{!user?.username && !user?.email && status == null && <p className="account-muted">No account details are available.</p>}</section>
    <section className="account-card"><div className="account-card__heading"><div><h2>Session</h2><p>Sign out of this SIMS session.</p></div></div><button className="button button--secondary" type="button" onClick={async () => { await logout(); navigate('/login', { replace: true }); }}>Log out</button></section>
  </PageContainer>;
}
