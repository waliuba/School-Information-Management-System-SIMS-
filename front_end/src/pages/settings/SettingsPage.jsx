import { useEffect, useMemo, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import PageContainer from '../../components/layout/PageContainer.jsx';
import { useAuth } from '../../hooks/useAuth.js';
import { getPreferences, updatePreferences } from '../../services/api/accountApi.js';
import {
  createSystemSetting,
  deleteSystemSetting,
  getAuditEvents,
  getSchoolProfile,
  getSystemHealth,
  getSystemSettings,
  updateSchoolProfile,
  updateSystemSetting,
} from '../../services/api/settingsApi.js';
import { applyTheme, DEFAULT_PREFERENCES, readPreferences, savePreferences } from '../../services/preferences.js';
import './settings.css';

const sections = [
  { slug: 'account', title: 'Administrator Account', phase: null },
  { slug: 'security', title: 'Security & Access', phase: null },
  { slug: 'school', title: 'School Configuration', phase: null },
  { slug: 'notifications', title: 'Notifications', phase: null, status: 'preview' },
  { slug: 'preferences', title: 'Appearance & Preferences', phase: null },
  { slug: 'data', title: 'Data Management & Backups', phase: null, status: 'preview' },
  { slug: 'system', title: 'System Health & Information', phase: null },
  { slug: 'audit', title: 'Audit & Activity History', phase: null },
  { slug: 'danger', title: 'Danger Zone', phase: null, status: 'preview' },
];

const dataCards = [
  { label: 'Backup jobs', value: 'Not connected', tone: 'muted' },
  { label: 'Export window', value: 'Not configured', tone: 'muted' },
  { label: 'Restore path', value: 'Manual recovery only', tone: 'warning' },
];

const supportedTimeZones = typeof Intl.supportedValuesOf === 'function'
  ? Intl.supportedValuesOf('timeZone')
  : ['Africa/Nairobi', 'Europe/London', 'America/New_York'];

export default function SettingsPage() {
  const location = useLocation();
  const { user, isAuthLoading } = useAuth();
  const active = useMemo(() => sections.find((item) => location.pathname.endsWith(`/${item.slug}`)) || sections[0], [location.pathname]);
  const [preferences, setPreferences] = useState(readPreferences);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [notice, setNotice] = useState(null);
  const [schoolProfile, setSchoolProfile] = useState({
    schoolName: '',
    shortName: '',
    academicYear: '',
    timezone: '',
    language: '',
  });
  const [schoolLoading, setSchoolLoading] = useState(false);
  const [schoolSaving, setSchoolSaving] = useState(false);
  const [auditEvents, setAuditEvents] = useState([]);
  const [auditLoading, setAuditLoading] = useState(false);
  const [auditError, setAuditError] = useState(null);
  const [systemHealth, setSystemHealth] = useState(null);
  const [systemHealthLoading, setSystemHealthLoading] = useState(false);
  const [systemHealthError, setSystemHealthError] = useState(null);
  const [healthRefresh, setHealthRefresh] = useState(0);
  const [systemSettings, setSystemSettings] = useState([]);
  const [systemSettingsLoading, setSystemSettingsLoading] = useState(false);
  const [systemSettingsError, setSystemSettingsError] = useState(null);
  const [systemSettingsNotice, setSystemSettingsNotice] = useState(null);
  const [systemSettingDraft, setSystemSettingDraft] = useState({ key: '', value: '' });
  const [editingSystemSetting, setEditingSystemSetting] = useState(null);
  const [savingSystemSetting, setSavingSystemSetting] = useState(false);
  const [deletingSystemSetting, setDeletingSystemSetting] = useState(null);
  const [notificationPrefs, setNotificationPrefs] = useState({
    emailAlerts: true,
    pushAlerts: false,
    securityAlerts: true,
    weeklySummary: true,
  });
  const timeZoneOptions = useMemo(
    () => [...new Set([...supportedTimeZones, 'UTC', schoolProfile.timezone].filter(Boolean))].sort(),
    [schoolProfile.timezone]
  );

  useEffect(() => {
    if (isAuthLoading || !user || active.slug !== 'preferences') return;
    let live = true;
    getPreferences().then((next) => {
      if (!live) return;
      const value = { ...DEFAULT_PREFERENCES, ...next, theme: 'light' };
      setPreferences(value); savePreferences(value); setLoading(false);
    }).catch((error) => {
      if (!live) return;
      setNotice({ type: 'error', text: `${error.message} Showing this device’s saved preferences.` });
      setLoading(false);
    });
    return () => { live = false; };
  }, [active.slug, isAuthLoading, user]);

  useEffect(() => {
    if (isAuthLoading || !user || active.slug !== 'school') return;
    let live = true;
    setSchoolLoading(true);
    setNotice(null);
    getSchoolProfile().then((profile) => {
      if (live) setSchoolProfile(profile);
    }).catch((error) => {
      if (live) setNotice({ type: 'error', text: error.message || 'School profile could not be loaded.' });
    }).finally(() => {
      if (live) setSchoolLoading(false);
    });
    return () => { live = false; };
  }, [active.slug, isAuthLoading, user]);

  useEffect(() => {
    if (isAuthLoading || !user || active.slug !== 'audit') return;
    let live = true;
    setAuditLoading(true);
    setAuditError(null);
    getAuditEvents().then((events) => {
      if (live) setAuditEvents(events);
    }).catch((error) => {
      if (live) setAuditError(error.message || 'Audit events could not be loaded.');
    }).finally(() => {
      if (live) setAuditLoading(false);
    });
    return () => { live = false; };
  }, [active.slug, isAuthLoading, user]);

  useEffect(() => {
    if (isAuthLoading || !user || active.slug !== 'system') return;
    let live = true;
    setSystemHealthLoading(true);
    setSystemHealthError(null);
    getSystemHealth().then((health) => {
      if (live) setSystemHealth(health);
    }).catch((error) => {
      if (live) setSystemHealthError(error.message || 'System health could not be loaded.');
    }).finally(() => {
      if (live) setSystemHealthLoading(false);
    });
    return () => { live = false; };
  }, [active.slug, healthRefresh, isAuthLoading, user]);

  useEffect(() => {
    if (isAuthLoading || !user || active.slug !== 'system') return;
    let live = true;
    setSystemSettingsLoading(true);
    setSystemSettingsError(null);
    getSystemSettings().then((settings) => {
      if (live) setSystemSettings(settings);
    }).catch((error) => {
      if (live) setSystemSettingsError(error.message || 'System settings could not be loaded.');
    }).finally(() => {
      if (live) setSystemSettingsLoading(false);
    });
    return () => { live = false; };
  }, [active.slug, isAuthLoading, user]);

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

  useEffect(() => { applyTheme(); }, []);

  async function saveSchoolProfile(event) {
    event.preventDefault();
    setSchoolSaving(true);
    setNotice(null);
    try {
      const saved = await updateSchoolProfile(schoolProfile);
      setSchoolProfile(saved);
      setNotice({ type: 'success', text: 'School profile saved.' });
    } catch (error) {
      setNotice({ type: 'error', text: error.message || 'School profile could not be saved.' });
    } finally {
      setSchoolSaving(false);
    }
  }

  function handleNotificationSave() {
    setNotice({ type: 'success', text: 'Preview updated in this page only. Notification preferences are not persisted, and no delivery service is connected.' });
  }

  async function saveSystemSetting(event) {
    event.preventDefault();
    setSavingSystemSetting(true);
    setSystemSettingsNotice(null);
    try {
      const saved = editingSystemSetting
        ? await updateSystemSetting(editingSystemSetting, systemSettingDraft.value)
        : await createSystemSetting(systemSettingDraft);
      setSystemSettings((current) => {
        const next = editingSystemSetting
          ? current.map((setting) => setting.key === saved.key ? saved : setting)
          : [...current, saved];
        return next.sort((left, right) => left.key.localeCompare(right.key));
      });
      setSystemSettingDraft({ key: '', value: '' });
      setEditingSystemSetting(null);
      setSystemSettingsNotice({ type: 'success', text: editingSystemSetting ? 'System setting updated.' : 'System setting created.' });
    } catch (error) {
      setSystemSettingsNotice({ type: 'error', text: error.message || 'System setting could not be saved.' });
    } finally {
      setSavingSystemSetting(false);
    }
  }

  function editSystemSetting(setting) {
    setEditingSystemSetting(setting.key);
    setSystemSettingDraft(setting);
    setSystemSettingsNotice(null);
  }

  function cancelSystemSettingEdit() {
    setEditingSystemSetting(null);
    setSystemSettingDraft({ key: '', value: '' });
    setSystemSettingsNotice(null);
  }

  async function removeSystemSetting(key) {
    if (!window.confirm(`Delete the system setting "${key}"?`)) return;
    setDeletingSystemSetting(key);
    setSystemSettingsNotice(null);
    try {
      await deleteSystemSetting(key);
      setSystemSettings((current) => current.filter((setting) => setting.key !== key));
      if (editingSystemSetting === key) cancelSystemSettingEdit();
      setSystemSettingsNotice({ type: 'success', text: 'System setting deleted.' });
    } catch (error) {
      setSystemSettingsNotice({ type: 'error', text: error.message || 'System setting could not be deleted.' });
    } finally {
      setDeletingSystemSetting(null);
    }
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
            <label className="field" htmlFor="settings-theme"><span>Theme</span><select className="input" id="settings-theme" value="light" disabled><option value="light">Light mode (always on)</option></select></label>
            <label className="settings-check"><input type="checkbox" checked={preferences.sidebarCollapsed === 'true'} onChange={(event) => change('sidebarCollapsed', String(event.target.checked))} /><span>Start with the sidebar collapsed</span></label>
            <label className="field" htmlFor="settings-page-size"><span>Default table page size</span><select className="input" id="settings-page-size" value={preferences.tablePageSize} onChange={(event) => change('tablePageSize', event.target.value)}>{['10', '25', '50', '100'].map((size) => <option key={size} value={size}>{size} rows</option>)}</select></label>
            <div className="settings-actions"><button className="button button--primary" type="submit" disabled={saving}>{saving ? 'Saving…' : 'Save preferences'}</button><button className="button button--secondary" type="button" disabled={saving} onClick={resetPreferences}>Reset to defaults</button></div>
          </>}
          {notice && <p className={`settings-notice settings-notice--${notice.type}`} role={notice.type === 'error' ? 'alert' : 'status'}>{notice.text}</p>}
        </form>}
        {active.slug === 'school' && <div className="settings-content">
          <form className="settings-card" onSubmit={saveSchoolProfile}>
            <h3>School profile</h3>
            {schoolLoading ? <p role="status">Loading school profile…</p> : <>
              <div className="settings-grid settings-grid--two">
                <label className="field"><span>School name</span><input className="input" maxLength="100" required disabled={schoolSaving} value={schoolProfile.schoolName} onChange={(event) => setSchoolProfile((value) => ({ ...value, schoolName: event.target.value }))} /></label>
                <label className="field"><span>Short name</span><input className="input" maxLength="20" required disabled={schoolSaving} value={schoolProfile.shortName} onChange={(event) => setSchoolProfile((value) => ({ ...value, shortName: event.target.value }))} /></label>
                <label className="field"><span>Academic year</span><input className="input" maxLength="20" required disabled={schoolSaving} value={schoolProfile.academicYear} onChange={(event) => setSchoolProfile((value) => ({ ...value, academicYear: event.target.value }))} /></label>
                <label className="field"><span>Timezone</span><select className="input" required disabled={schoolSaving} value={schoolProfile.timezone} onChange={(event) => setSchoolProfile((value) => ({ ...value, timezone: event.target.value }))}><option value="">Select a timezone</option>{timeZoneOptions.map((timezone) => <option key={timezone} value={timezone}>{timezone}</option>)}</select></label>
                <label className="field settings-grid__full"><span>Default language</span><select className="input" required disabled={schoolSaving} value={schoolProfile.language} onChange={(event) => setSchoolProfile((value) => ({ ...value, language: event.target.value }))}><option value="">Select a language</option><option value="English">English</option><option value="Kiswahili">Kiswahili</option><option value="French">French</option></select></label>
              </div>
              <div className="settings-actions"><button className="button button--primary" type="submit" disabled={schoolSaving}>{schoolSaving ? 'Saving…' : 'Save school profile'}</button></div>
            </>}
            {notice && <p className={`settings-notice settings-notice--${notice.type}`} role={notice.type === 'error' ? 'alert' : 'status'}>{notice.text}</p>}
          </form>
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
                  </tr>
                </thead>
                <tbody>
                  {auditLoading ? <tr><td colSpan="4" className="audit-empty">Loading audit events…</td></tr>
                    : auditError ? <tr><td colSpan="4" className="audit-empty" role="alert">{auditError}</td></tr>
                      : auditEvents.length === 0 ? <tr><td colSpan="4" className="audit-empty">No audit events have been recorded.</td></tr>
                        : auditEvents.map((event) => <tr key={event.eventId}>
                          <td>{new Date(event.occurredAt).toLocaleString()}</td>
                          <td>{event.actor}</td>
                          <td>{event.action}</td>
                          <td>{event.target}</td>
                        </tr>)}
                </tbody>
              </table>
            </div>
          </div>
          <p className="settings-muted">The audit viewer displays up to the 100 most recent records.</p>
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
          <div className="settings-actions settings-actions--between">
            <p>Live application, database, authentication, and audit-storage checks.</p>
            <button className="button button--secondary" type="button" disabled={systemHealthLoading} onClick={() => setHealthRefresh((value) => value + 1)}>
              {systemHealthLoading ? 'Checking…' : 'Refresh health'}
            </button>
          </div>
          {systemHealthError && <p className="settings-notice settings-notice--error" role="alert">{systemHealthError}</p>}
          {systemHealthLoading && !systemHealth && <p role="status">Checking system health…</p>}
          {systemHealth && <>
            <p className={`settings-health-summary settings-health-summary--${systemHealth.status.toLowerCase()}`} role="status">
              Overall status: <strong>{systemHealth.status}</strong> · Checked {new Date(systemHealth.checkedAt).toLocaleString()}
            </p>
            <div className="settings-status-grid">
              {systemHealth.checks.map((check) => <article key={check.name} className={`settings-status-card settings-status-card--${check.status.toLowerCase()}`}>
                <div className="settings-status-card__heading">
                  <span>{check.name}</span>
                  <em>{check.status}</em>
                </div>
                <small>{check.detail}</small>
              </article>)}
            </div>
          </>}
          <section className="settings-card">
            <h3>System settings</h3>
            <p>Manage non-secret system configuration values. Do not store passwords, tokens, or other credentials here.</p>
            <form className="system-setting-form" onSubmit={saveSystemSetting}>
              <label className="field">
                <span>Key</span>
                <input className="input" required maxLength="80" pattern="[A-Za-z][A-Za-z0-9._-]{0,79}" disabled={savingSystemSetting || Boolean(editingSystemSetting)} value={systemSettingDraft.key} onChange={(event) => setSystemSettingDraft((value) => ({ ...value, key: event.target.value }))} />
              </label>
              <label className="field">
                <span>Value</span>
                <input className="input" required maxLength="1000" disabled={savingSystemSetting} value={systemSettingDraft.value} onChange={(event) => setSystemSettingDraft((value) => ({ ...value, value: event.target.value }))} />
              </label>
              <div className="settings-actions">
                <button className="button button--primary" type="submit" disabled={savingSystemSetting}>{savingSystemSetting ? 'Saving…' : editingSystemSetting ? 'Update setting' : 'Create setting'}</button>
                {editingSystemSetting && <button className="button button--secondary" type="button" disabled={savingSystemSetting} onClick={cancelSystemSettingEdit}>Cancel</button>}
              </div>
            </form>
            {systemSettingsNotice && <p className={`settings-notice settings-notice--${systemSettingsNotice.type}`} role={systemSettingsNotice.type === 'error' ? 'alert' : 'status'}>{systemSettingsNotice.text}</p>}
            {systemSettingsError && <p className="settings-notice settings-notice--error" role="alert">{systemSettingsError}</p>}
            <div className="audit-table-wrap">
              <table className="audit-table">
                <thead><tr><th>Key</th><th>Value</th><th>Actions</th></tr></thead>
                <tbody>
                  {systemSettingsLoading ? <tr><td colSpan="3" className="audit-empty">Loading system settings…</td></tr>
                    : systemSettings.length === 0 ? <tr><td colSpan="3" className="audit-empty">No system settings have been created.</td></tr>
                      : systemSettings.map((setting) => <tr key={setting.key}>
                        <td>{setting.key}</td>
                        <td className="system-setting-value">{setting.value}</td>
                        <td>
                          <div className="settings-actions">
                            <button className="button button--secondary" type="button" disabled={Boolean(deletingSystemSetting) || savingSystemSetting} onClick={() => editSystemSetting(setting)}>Edit</button>
                            <button className="button button--danger" type="button" disabled={Boolean(deletingSystemSetting) || savingSystemSetting} onClick={() => removeSystemSetting(setting.key)}>{deletingSystemSetting === setting.key ? 'Deleting…' : 'Delete'}</button>
                          </div>
                        </td>
                      </tr>)}
                </tbody>
              </table>
            </div>
          </section>
          <p className="settings-muted">The public <code>/api/health</code> endpoint reports application liveness only. Detailed readiness checks require administrator authentication.</p>
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
