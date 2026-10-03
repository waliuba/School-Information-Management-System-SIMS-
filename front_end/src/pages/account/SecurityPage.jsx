import { useMemo, useState } from 'react';
import PageContainer from '../../components/layout/PageContainer.jsx';
import { changePassword } from '../../services/api/accountApi.js';

const emptyForm = { currentPassword: '', newPassword: '', confirmPassword: '' };

export default function SecurityPage() {
  const [values, setValues] = useState(emptyForm);
  const [visible, setVisible] = useState({});
  const [touched, setTouched] = useState({});
  const [notice, setNotice] = useState(null);
  const [pending, setPending] = useState(false);
  const errors = useMemo(() => ({
    currentPassword: values.currentPassword ? '' : 'Enter your current password.',
    newPassword: !values.newPassword ? 'Enter a new password.' : values.newPassword.length < 8 ? 'Use at least 8 characters.' : values.newPassword.length > 100 ? 'Use no more than 100 characters.' : values.newPassword === values.currentPassword ? 'Your new password must differ from your current password.' : '',
    confirmPassword: !values.confirmPassword ? 'Confirm your new password.' : values.confirmPassword !== values.newPassword ? 'Passwords do not match.' : '',
  }), [values]);
  const valid = Object.values(errors).every((error) => !error);

  async function submit(event) {
    event.preventDefault();
    setNotice('');
    setPending(true);
    try {
      await changePassword(values);
      setValues(emptyForm);
      setTouched({});
      setVisible({});
      setNotice({ type: 'success', message: 'Password updated successfully.' });
    }
    catch (error) { setNotice({ type: 'error', message: error.message || 'Could not update your password.' }); }
    finally { setPending(false); }
  }

  const passwordFields = [
    ['currentPassword', 'Current password', 'current-password'], ['newPassword', 'New password', 'new-password'], ['confirmPassword', 'Confirm new password', 'new-password'],
  ];
  return <PageContainer title="Security" description="Manage your password and account security.">
    <section className="account-card"><div className="account-card__heading"><div><h2>Change password</h2><p>Use 8 to 100 characters. Your current password is required to confirm this change.</p></div></div>
      <form className="account-form account-form--single" onSubmit={submit} noValidate>{passwordFields.map(([key, label, autocomplete]) => <div className="field" key={key}><label htmlFor={key}><span>{label}</span></label><div className="password-field"><input className="input" id={key} name={key} type={visible[key] ? 'text' : 'password'} autoComplete={autocomplete} value={values[key]} onBlur={() => setTouched((current) => ({ ...current, [key]: true }))} onChange={(event) => { setValues((current) => ({ ...current, [key]: event.target.value })); setTouched((current) => ({ ...current, [key]: true })); }} aria-invalid={Boolean(touched[key] && errors[key])} aria-describedby={`${key}-error`} /><button className="password-field__toggle" type="button" aria-label={`${visible[key] ? 'Hide' : 'Show'} ${label.toLowerCase()}`} onClick={() => setVisible((current) => ({ ...current, [key]: !current[key] }))}>{visible[key] ? 'Hide' : 'Show'}</button></div><small className="field__error" id={`${key}-error`} aria-live="polite">{touched[key] ? errors[key] : ''}</small></div>)}
        <button className="button button--primary" type="submit" disabled={!valid || pending}>{pending ? 'Saving…' : 'Update password'}</button>
      </form>{notice && <p className={`account-notice${notice.type === 'success' ? ' account-notice--success' : ''}`} role="status">{notice.message}</p>}
    </section>
    <p className="account-muted">Last password change, last login, and active sessions are not provided by the current authentication data.</p>
  </PageContainer>;
}
