import { useState } from 'react';
import PageContainer from '../../components/layout/PageContainer.jsx';
import Avatar from '../../components/common/Avatar.jsx';
import Badge from '../../components/common/Badge.jsx';
import { useAuth } from '../../hooks/useAuth.js';
import { updateProfile } from '../../services/api/accountApi.js';

function Detail({ label, value }) {
  if (value === undefined || value === null || value === '') return null;

  return <div><dt>{label}</dt><dd>{String(value)}</dd></div>;
}

export default function ProfilePage() {
  const { user, updateCurrentUser } = useAuth();
  const [editing, setEditing] = useState(false);
  const [email, setEmail] = useState('');
  const [pending, setPending] = useState(false);
  const [notice, setNotice] = useState(null);
  const status = typeof user?.enabled === 'boolean'
    ? (user.enabled ? 'Enabled' : 'Disabled')
    : null;
  const role = user?.role;
  const displayName = user?.username || user?.email || 'Profile';

  function cancelEdit() {
    setEditing(false);
    setEmail('');
    setNotice(null);
  }

  async function saveProfile(event) {
    event.preventDefault();
    setPending(true);
    setNotice(null);
    try {
      const updatedUser = await updateProfile({ email });
      updateCurrentUser(updatedUser);
      setEditing(false);
      setEmail('');
      setNotice({ type: 'success', message: 'Email address updated.' });
    } catch (error) {
      setNotice({ type: 'error', message: error.message || 'Could not update your email address.' });
    } finally {
      setPending(false);
    }
  }

  return <PageContainer title="My Profile" description="View your profile and account information.">
    <section className="account-card profile-overview" aria-labelledby="profile-overview-title">
      <div className="profile-overview__identity">
        <Avatar name={displayName} className="account-avatar" />
        <div className="profile-overview__copy">
          <h2 id="profile-overview-title">{displayName}</h2>
          {user?.email && <p>{user.email}</p>}
          {role && <Badge tone="info" className="profile-overview__role">{role}</Badge>}
        </div>
      </div>
      {status && <Badge tone={status === 'Enabled' ? 'success' : 'neutral'}>Account {status.toLowerCase()}</Badge>}
    </section>

    <section className="account-card" aria-labelledby="profile-details-title">
      <div className="account-card__heading">
        <div>
          <h2 id="profile-details-title">Personal information</h2>
          <p>Details available for your signed-in account.</p>
        </div>
        {user && !editing && <button className="button button--secondary" type="button" onClick={() => { setEmail(user.email || ''); setNotice(null); setEditing(true); }}>Edit email</button>}
      </div>
      {user ? <dl className="account-facts profile-details">
        <Detail label="Username" value={user.username} />
        <Detail label="Email" value={user.email} />
        <Detail label="Account ID" value={user.userId} />
        <Detail label="Role" value={role} />
        <Detail label="Status" value={status} />
      </dl> : <p className="account-muted">Account details are unavailable. Refresh the page or sign in again to reload your profile.</p>}
      {editing && <form className="account-form account-form--single profile-edit-form" onSubmit={saveProfile}>
        <label className="field" htmlFor="profile-email"><span>Email</span><input className="input" id="profile-email" type="email" autoComplete="email" maxLength="100" required value={email} onChange={(event) => setEmail(event.target.value)} /></label>
        <div className="account-form__actions">
          <button className="button button--primary" type="submit" disabled={pending}>{pending ? 'Saving…' : 'Save changes'}</button>
          <button className="button button--secondary" type="button" disabled={pending} onClick={cancelEdit}>Cancel</button>
        </div>
      </form>}
      {notice && <p className={`account-notice${notice.type === 'success' ? ' account-notice--success' : ''}`} role={notice.type === 'error' ? 'alert' : 'status'}>{notice.message}</p>}
    </section>
  </PageContainer>;
}
