import { useState } from 'react';
import PageContainer from '../../components/layout/PageContainer.jsx';
import { useAuth } from '../../hooks/useAuth.js';
import { updateProfile } from '../../services/api/accountApi.js';

const fieldDefinitions = [
  ['name', 'Full name', 'text'], ['fullName', 'Full name', 'text'], ['firstName', 'First name', 'text'], ['lastName', 'Last name', 'text'],
  ['email', 'Email', 'email'], ['username', 'Username', 'text'], ['phone', 'Phone', 'tel'],
];
const labelForRole = (user) => user?.role || user?.role_name || user?.roleName;

export default function ProfilePage() {
  const { user } = useAuth();
  const fields = fieldDefinitions.filter(([key]) => user?.[key] !== undefined && user?.[key] !== null);
  const [editing, setEditing] = useState(false);
  const [values, setValues] = useState(() => Object.fromEntries(fields.map(([key]) => [key, user[key]])));
  const [notice, setNotice] = useState('');
  const [pending, setPending] = useState(false);
  const fullName = user?.name || user?.fullName || [user?.firstName, user?.lastName].filter(Boolean).join(' ');
  const role = labelForRole(user);
  const facts = [['Role', role], ['Email', user?.email], ['Username', user?.username], ['Status', typeof user?.enabled === 'boolean' ? (user.enabled ? 'Enabled' : 'Disabled') : user?.status], ['Last login', user?.lastLogin || user?.last_login]];

  async function saveProfile(event) {
    event.preventDefault();
    setPending(true);
    setNotice('');
    try { await updateProfile(values); }
    catch (error) { setNotice(error.message || 'Profile updates are not connected yet.'); }
    finally { setPending(false); }
  }

  return <PageContainer title="Profile" description="Manage your personal information and account details.">
    <section className="account-card profile-overview" aria-labelledby="profile-overview-title"><div className="profile-overview__identity"><span className="account-avatar" aria-hidden="true">{String(fullName || user?.username || user?.email || '?').slice(0, 1).toUpperCase()}</span><div><h2 id="profile-overview-title">{fullName || user?.username || user?.email || 'Profile'}</h2>{role && <p>{role}</p>}</div></div><dl className="account-facts">{facts.filter(([, value]) => value !== undefined && value !== null && value !== '').map(([label, value]) => <div key={label}><dt>{label}</dt><dd>{String(value)}</dd></div>)}</dl></section>
    <section className="account-card"><div className="account-card__heading"><div><h2>Personal information</h2><p>Review the details currently available for your account.</p></div>{fields.length > 0 && <button className="button button--secondary" type="button" onClick={() => { setEditing((value) => !value); setNotice(''); }}>{editing ? 'Cancel editing' : 'Edit profile'}</button>}</div>
      {fields.length ? <form className="account-form" onSubmit={saveProfile}>{fields.map(([key, label, type]) => <label className="field" key={key} htmlFor={`profile-${key}`}><span>{label}</span><input className="input" id={`profile-${key}`} type={type} value={values[key] ?? ''} readOnly={!editing} onChange={(event) => setValues((current) => ({ ...current, [key]: event.target.value }))} required={editing} aria-describedby={editing && notice ? 'profile-status' : undefined} /></label>)}{editing && <div className="account-form__actions"><button className="button button--primary" type="submit" disabled={pending}>{pending ? 'Saving…' : 'Save changes'}</button></div>}</form> : <p className="account-muted">Personal information fields are not available in the current user record. <span className="account-todo">TODO(api): extend the current user response with editable profile fields.</span></p>}
      {notice && <p className="account-notice" id="profile-status" role="status">{notice}</p>}
    </section>
  </PageContainer>;
}
