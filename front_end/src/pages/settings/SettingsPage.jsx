import { useEffect, useMemo, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import PageContainer from '../../components/layout/PageContainer.jsx';
import { useAuth } from '../../hooks/useAuth.js';
import { getPreferences, updatePreferences } from '../../services/api/accountApi.js';
import { applyTheme, DEFAULT_PREFERENCES, readPreferences, savePreferences } from '../../services/preferences.js';
import './settings.css';

const sections = [
  { slug: 'account', title: 'Administrator Account', phase: null },
  { slug: 'security', title: 'Security & Access', phase: null },
  { slug: 'school', title: 'School Configuration', phase: null, status: 'preview' },
  { slug: 'notifications', title: 'Notifications', phase: null, status: 'preview' },
  { slug: 'preferences', title: 'Appearance & Preferences', phase: null },
  { slug: 'data', title: 'Data Management & Backups', phase: null, status: 'preview' },
  { slug: 'system', title: 'System Health & Information', phase: null, status: 'preview' },
  { slug: 'audit', title: 'Audit & Activity History', phase: null, status: 'preview' },
  { slug: 'danger', title: 'Danger Zone', phase: null, status: 'preview' },
];

const dataCards = [
  { label: 'Backup jobs', value: 'Not connected', tone: 'muted' },
  { label: 'Export window', value: 'Not configured', tone: 'muted' },
  { label: 'Restore path', value: 'Manual recovery only', tone: 'warning' },
];

const systemChecks = [
  { name: 'Application API', detail: 'No deployment health source is connected.', state: 'not-connected' },
  { name: 'Oracle database', detail: 'No Oracle health export or Actuator collector is configured.', state: 'not-connected' },
  { name: 'Authentication', detail: 'JWT validation is active, but no operational session health source is connected.', state: 'pending' },
  { name: 'Audit stream', detail: 'Audit writes exist in code, but no external export or retention job is configured.', state: 'pending' },
];

function formatTimestamp() {
  return new Date().toISOString().replace('T', ' ').slice(0, 19) + ' UTC';
}

export default function SettingsPage() {
  const location = useLocation();
  const { user, isAuthLoading } = useAuth();
  const active = useMemo(() => sections.find((item) => location.pathname.endsWith(`/${item.slug}`)) || sections[0], [location.pathname]);
  const [preferences, setPreferences] = useState(readPreferences);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [notice, setNotice] = useState(null);
  const [schoolProfile, setSchoolProfile] = useState(() => ({
    schoolName: 'SIMS Academy',
    shortName: 'SIMS',
    academicYear: '2026/2027',
    timezone: 'UTC',
    language: 'English',
  }));
  const [notificationPrefs, setNotificationPrefs] = useState({
    emailAlerts: true,
    pushAlerts: false,
    securityAlerts: true,
    weeklySummary: true,
  });

  useEffect(() => {
    if (isAuthLoading || !user) return;
    let live = true;
    getPreferences().then((next) => {
      if (!live) return;
      const value = { ...DEFAULT_PREFERENCES, ...next };
      setPreferences(value); savePreferences(value); setLoading(false);
    }).catch((error) => {
      if (!live) return;
      setNotice({ type: 'error', text: `${error.message} Showing this device’s saved preferences.` });
      setLoading(false);
    });
    return () => { live = false; };
  }, [isAuthLoading, user]);

  function change(key, value) {
    setPreferences((current) => ({ ...current, [key]: value }));
    setNotice(null);
  }

  async function save(event) {
    event.preventDefault(); setSaving(true); setNotice(null);
    try {
      const saved = await updatePreferences(preferences);
      setPreferences(saved); savePreferences(saved);
      setNotice({ type: 'success', text: 'Preferences saved to your account.' });
    } catch (error) { setNotice({ type: 'error', text: error.message || 'Preferences could not be saved.' }); }
    finally { setSaving(false); }
  }

  async function resetPreferences() {
    setSaving(true); setNotice(null);
    try {
      const saved = await updatePreferences(DEFAULT_PREFERENCES);
      setPreferences(saved); savePreferences(saved);
      setNotice({ type: 'success', text: 'Preferences reset to their defaults.' });
    } catch (error) { setNotice({ type: 'error', text: error.message || 'Preferences could not be reset.' }); }
    finally { setSaving(false); }
  }

  useEffect(() => { applyTheme(preferences.theme); }, [preferences.theme]);

  function handleSchoolSave() {
    setNotice({ type: 'success', text: 'Preview updated in this page only. School configuration is not persisted because its server API is not connected.' });
  }

  function handleNotificationSave() {
    setNotice({ type: 'success', text: 'Preview updated in this page only. Notification preferences are not persisted, and no delivery service is connected.' });
  }

  return <PageContainer title="Settings" description="Manage administrator account and system settings.">
    <div className="settings-layout">
      <nav className="settings-nav" aria-label="Settings destinations">
        {sections.map((item) => <Link key={item.slug} to={`/settings/${item.slug}`} aria-current={active.slug === item.slug ? 'page' : undefined} className={active.slug === item.slug ? 'settings-nav__link is-active' : 'settings-nav__link'}>
          <span>{item.title}</span>{item.status === 'preview' && <small>Preview</small>}{item.phase && <small>Planned</small>}
        </Link>)}
      </nav>
      <section className="settings-panel" aria-labelledby="settings-section-title">
        <header className="settings-panel__header"><h2 id="settings-section-title">{active.title}</h2>{active.status === 'preview' && <span className="settings-preview">Preview only</span>}{active.phase && <span className="settings-planned">Planned for Phase {active.phase}</span>}</header>
        {active.slug === 'account' && <div className="settings-content">
          <p>Manage the signed-in administrator profile and password.</p>
          <dl className="account-facts">{[['Username', user?.username], ['Email', user?.email], ['Role', user?.role], ['Status', user?.enabled === false ? 'Disabled' : user?.enabled === true ? 'Enabled' : null]].filter(([, value]) => value).map(([label, value]) => <div key={label}><dt>{label}</dt><dd>{String(value)}</dd></div>)}</dl>
          <div className="settings-actions"><Link className="button button--secondary" to="/profile">Edit account email</Link><Link className="button button--secondary" to="/security">Change password</Link></div>
        </div>}
        {active.slug === 'security' && <div className="settings-content"><p>Password changes invalidate all previously issued access tokens, including this session. Sign in again with the new password.</p><Link className="button button--primary" to="/security">Open password change</Link><p className="account-muted">Active session listing and sign-out-other-sessions are planned after session revocation storage is available.</p></div>}
        {active.slug === 'preferences' && <form className="settings-form" onSubmit={save}>
          {loading ? <p role="status">Loading preferences…</p> : <>
            <label className="field" htmlFor="settings-theme"><span>Theme</span><select className="input" id="settings-theme" value={preferences.theme} onChange={(event) => change('theme', event.target.value)}><option value="system">Use device setting</option><option value="light">Light</option><option value="dark">Dark</option></select></label>
            <label className="settings-check"><input type="checkbox" checked={preferences.sidebarCollapsed === 'true'} onChange={(event) => change('sidebarCollapsed', String(event.target.checked))} /><span>Start with the sidebar collapsed</span></label>
            <label className="field" htmlFor="settings-page-size"><span>Default table page size</span><select className="input" id="settings-page-size" value={preferences.tablePageSize} onChange={(event) => change('tablePageSize', event.target.value)}>{['10', '25', '50', '100'].map((size) => <option key={size} value={size}>{size} rows</option>)}</select></label>
            <div className="settings-actions"><button className="button button--primary" type="submit" disabled={saving}>{saving ? 'Saving…' : 'Save preferences'}</button><button className="button button--secondary" type="button" disabled={saving} onClick={resetPreferences}>Reset to defaults</button></div>
          </>}
          {notice && <p className={`settings-notice settings-notice--${notice.type}`} role={notice.type === 'error' ? 'alert' : 'status'}>{notice.text}</p>}
        </form>}
        {active.slug === 'school' && <div className="settings-content">
          <div className="settings-card">
            <h3>School profile</h3>
            <div className="settings-grid settings-grid--two">
              <label className="field"><span>School name</span><input className="input" value={schoolProfile.schoolName} onChange={(event) => setSchoolProfile((value) => ({ ...value, schoolName: event.target.value }))} /></label>
              <label className="field"><span>Short name</span><input className="input" value={schoolProfile.shortName} onChange={(event) => setSchoolProfile((value) => ({ ...value, shortName: event.target.value }))} /></label>
              <label className="field"><span>Academic year</span><input className="input" value={schoolProfile.academicYear} onChange={(event) => setSchoolProfile((value) => ({ ...value, academicYear: event.target.value }))} /></label>
              <label className="field"><span>Timezone</span><select className="input" value={schoolProfile.timezone} onChange={(event) => setSchoolProfile((value) => ({ ...value, timezone: event.target.value }))}><option>UTC</option><option>Africa/Nairobi</option><option>Europe/London</option><option>America/New_York</option></select></label>
              <label className="field settings-grid__full"><span>Default language</span><select className="input" value={schoolProfile.language} onChange={(event) => setSchoolProfile((value) => ({ ...value, language: event.target.value }))}><option>English</option><option>Kiswahili</option><option>French</option></select></label>
            </div>
            <div className="settings-actions"><button className="button button--primary" type="button" onClick={handleSchoolSave}>Save school profile</button></div>
          </div>
        </div>}
        {active.slug === 'notifications' && <div className="settings-content">
          <div className="settings-card">
            <h3>Notification policy</h3>
            <div className="settings-check-list">
              <label className="settings-check"><input type="checkbox" checked={notificationPrefs.emailAlerts} onChange={(event) => setNotificationPrefs((value) => ({ ...value, emailAlerts: event.target.checked }))} /><span>Email alerts</span></label>
              <label className="settings-check"><input type="checkbox" checked={notificationPrefs.pushAlerts} onChange={(event) => setNotificationPrefs((value) => ({ ...value, pushAlerts: event.target.checked }))} /><span>Push alerts</span></label>
              <label className="settings-check"><input type="checkbox" checked={notificationPrefs.securityAlerts} onChange={(event) => setNotificationPrefs((value) => ({ ...value, securityAlerts: event.target.checked }))} /><span>Security alerts</span></label>
              <label className="settings-check"><input type="checkbox" checked={notificationPrefs.weeklySummary} onChange={(event) => setNotificationPrefs((value) => ({ ...value, weeklySummary: event.target.checked }))} /><span>Weekly summary</span></label>
            </div>
            <div className="settings-actions"><button className="button button--primary" type="button" onClick={handleNotificationSave}>Save notification preferences</button></div>
          </div>
        </div>}
        {active.slug === 'audit' && <div className="settings-content">
          <div className="settings-card">
            <div className="audit-table-wrap">
              <table className="audit-table">
                <thead>
                  <tr>
                    <th>Timestamp</th>
                    <th>Actor</th>
                    <th>Action</th>
                    <th>Target</th>
                    <th>Outcome</th>
                  </tr>
                </thead>
                <tbody>
                  <tr><td colSpan="5" className="audit-empty">No live audit records are available. The read-only audit API is not connected.</td></tr>
                </tbody>
              </table>
            </div>
          </div>
          <p className="settings-muted">This is a preview; it does not display fabricated or sample audit events.</p>
        </div>}
        {active.slug === 'data' && <div className="settings-content">
          <div className="settings-status-grid">
            {dataCards.map((item) => <article key={item.label} className={`settings-status-card settings-status-card--${item.tone}`}>
              <span>{item.label}</span>
              <strong>{item.value}</strong>
            </article>)}
          </div>
          <div className="settings-card">
            <h3>Operational backup controls</h3>
            <p>Backup jobs, export windows, and restore orchestration are intentionally unavailable until a deployment-managed backup source is linked to this environment.</p>
            <div className="settings-actions">
              <button className="button button--secondary" type="button" disabled>Export audit snapshot</button>
              <button className="button button--secondary" type="button" disabled>Run backup check</button>
            </div>
          </div>
          <p className="settings-muted">Recovery runbook, retention window, and authorized export limits must be verified by the host environment before the UI exposes any destructive backup or restore action.</p>
        </div>}
        {active.slug === 'system' && <div className="settings-content">
          <div className="settings-status-grid">
            {systemChecks.map((item) => <article key={item.name} className={`settings-status-card settings-status-card--${item.state}`}>
              <div className="settings-status-card__heading">
                <span>{item.name}</span>
                <em>{item.state === 'not-connected' ? 'Not connected' : 'Pending'}</em>
              </div>
              <strong>{formatTimestamp()}</strong>
              <small>{item.detail}</small>
            </article>)}
          </div>
          <div className="settings-card settings-card--warning">
            <h3>System status policy</h3>
            <p>Stale, unavailable, or partially connected systems must never display as healthy. Secure deployment health and backup sources must be attached before this screen can claim operational status.</p>
          </div>
        </div>}
        {active.slug === 'danger' && <div className="settings-content">
          <div className="settings-card settings-card--warning">
            <h3>Danger zone</h3>
            <p>Destructive operations are blocked until the backend exposes an auditable server-side action with step-up re-authentication, idempotency protection, and recovery guidance.</p>
          </div>
          <div className="settings-actions">
            <button className="button button--danger" type="button" disabled>No destructive action available</button>
          </div>
          <p className="settings-muted">No destructive action is wired to this preview. A live action requires a reviewed server endpoint, audit trail, and recovery runbook.</p>
        </div>}
        {active.phase && <div className="settings-content"><p>This destination does not have a connected backend capability yet.</p><p>It will become available in Phase {active.phase} after its service, authorization, and audit behavior are implemented.</p></div>}
      </section>
    </div>
  </PageContainer>;
}
