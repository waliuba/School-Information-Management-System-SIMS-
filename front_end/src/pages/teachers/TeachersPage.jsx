import { useEffect, useMemo, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import PageContainer from '../../components/layout/PageContainer.jsx';
import { useAuth } from '../../hooks/useAuth.js';
import { getTeachers } from '../../services/api/resourcesApi.js';

const statusLabel = (teacher) => teacher.enabled ? 'Active' : 'Inactive';
const safeText = (value) => value == null || value === '' ? '—' : String(value);

export default function TeachersPage() {
  const [params, setParams] = useSearchParams();
  const { user } = useAuth();
  const canAdd = (user?.role || user?.role_name || user?.roleName) === 'ADMIN';
  const search = params.get('search') || '';
  const status = params.get('status') || 'all';
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [error, setError] = useState(false);

  useEffect(() => {
    let active = true;
    setLoading((current) => rows.length === 0);
    setRefreshing(rows.length > 0);
    setError(false);
    getTeachers().then((data) => { if (active) setRows(data); })
      .catch(() => { if (active) setError(true); })
      .finally(() => { if (active) { setLoading(false); setRefreshing(false); } });
    return () => { active = false; };
  // The list is fetched once; filters are applied locally because the API has no query support.
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function updateParam(key, value) {
    const next = new URLSearchParams(params);
    if (value) next.set(key, value); else next.delete(key);
    setParams(next, { replace: true });
  }
  function clearFilters() { setParams({}, { replace: true }); }

  const filtered = useMemo(() => rows.filter((teacher) => {
    const query = search.trim().toLowerCase();
    const matchesSearch = !query || [teacher.username, teacher.email].some((value) => String(value || '').toLowerCase().includes(query));
    const matchesStatus = status === 'all' || (status === 'active' ? teacher.enabled : !teacher.enabled);
    return matchesSearch && matchesStatus;
  }).sort((a, b) => String(a.username || '').localeCompare(String(b.username || ''))), [rows, search, status]);
  const activeCount = rows.filter((teacher) => teacher.enabled).length;
  const inactiveCount = rows.length - activeCount;
  const hasFilters = !!search || status !== 'all';

  const addLink = canAdd ? <Link className="primary-button" to="/teachers/new">+ Add Teacher</Link> : null;
  return <PageContainer title="Teachers" description="Manage and monitor the school's teaching staff." actions={addLink}>
    <section className="teacher-stats" aria-label="Teacher summary">
      {[['Total teachers', rows.length, 'All teacher accounts', 'TT'], ['Active', activeCount, 'Enabled accounts', 'AC'], ['Inactive', inactiveCount, 'Disabled accounts', 'IN']].map(([label, value, detail, icon]) => <article className="teacher-stat" key={label}><span className="teacher-stat__icon" aria-hidden="true">{icon}</span><div><span>{label}</span><strong>{loading ? '—' : value}</strong><small>{detail}</small></div></article>)}
      {/* TODO(api): Add the Departments card when teacher records expose a department. */}
    </section>

    <section className="teacher-toolbar" aria-label="Teacher filters">
      <label className="teacher-search">Search teachers<input type="search" value={search} onChange={(event) => updateParam('search', event.target.value)} placeholder="Name or email" /></label>
      <label>Status<select value={status} onChange={(event) => updateParam('status', event.target.value)}><option value="all">All statuses</option><option value="active">Active</option><option value="inactive">Inactive</option></select></label>
      {hasFilters && <button className="teacher-clear" type="button" onClick={clearFilters}>Clear filters</button>}
      {/* TODO(api): Department, employee number, phone, and department search are unavailable in the current teacher DTO. */}
    </section>

    <div className="teacher-table-heading"><h2>Teacher records</h2><span aria-live="polite">{loading ? 'Loading teachers…' : `${filtered.length} ${filtered.length === 1 ? 'teacher' : 'teachers'}`}</span>{refreshing && <span className="teacher-refresh" role="status">Updating…</span>}</div>
    {error ? <div className="teacher-error" role="alert"><div><strong>Unable to load teachers</strong><p>We couldn't retrieve the teacher records right now.</p></div><button type="button" onClick={() => {setLoading(true);setError(false);getTeachers().then(setRows).catch(()=>setError(true)).finally(()=>setLoading(false));}}>Retry</button></div> : loading ? <div className="teacher-skeleton" aria-label="Loading teachers">{Array.from({length:5},(_,i)=><div key={i}><span/><span/><span/></div>)}</div> : filtered.length === 0 ? <div className="teacher-empty"><strong>{rows.length ? 'No teachers match your filters' : 'No teachers found'}</strong><p>{rows.length ? 'Try changing your search or status filter.' : 'Teacher records will appear here when they are added.'}</p>{rows.length ? <button type="button" onClick={clearFilters}>Clear filters</button> : addLink}</div> : <>
      <div className="teacher-table-wrap"><table className="teacher-table"><caption className="sr-only">Teacher records</caption><thead><tr><th scope="col">Teacher</th><th scope="col">Email</th><th scope="col">Department</th><th scope="col">Phone</th><th scope="col">Status</th><th scope="col">Actions</th></tr></thead><tbody>{filtered.map((teacher) => <tr key={teacher.userId}><td><Link className="teacher-identity" to={`/teachers/${teacher.userId}`}><span className="teacher-avatar" aria-hidden="true">{String(teacher.username || '?').slice(0,1).toUpperCase()}</span><span><strong title={safeText(teacher.username)}>{safeText(teacher.username)}</strong><small>#{safeText(teacher.userId)}</small></span></Link></td><td title={safeText(teacher.email)}>{safeText(teacher.email)}</td><td>—</td><td>—</td><td><span className={`teacher-status ${teacher.enabled ? 'is-active' : 'is-inactive'}`}>{statusLabel(teacher)}</span></td><td><span className="teacher-actions"><Link to={`/teachers/${teacher.userId}`}>View</Link><Link to={`/teachers/${teacher.userId}/edit`}>Edit</Link></span></td></tr>)}</tbody></table></div>
      <div className="teacher-cards">{filtered.map((teacher) => <article className="teacher-mobile-card" key={teacher.userId}><div className="teacher-mobile-card__heading"><Link to={`/teachers/${teacher.userId}`}><strong>{safeText(teacher.username)}</strong><small>#{safeText(teacher.userId)}</small></Link><span className={`teacher-status ${teacher.enabled ? 'is-active' : 'is-inactive'}`}>{statusLabel(teacher)}</span></div><p>{safeText(teacher.email)}</p><small>Department —</small><div className="teacher-actions"><Link to={`/teachers/${teacher.userId}`}>View</Link><Link to={`/teachers/${teacher.userId}/edit`}>Edit</Link></div></article>)}</div>
    </>}
  </PageContainer>;
}
